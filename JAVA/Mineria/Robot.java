package Mineria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase Robot que extiende Thread para ejecutar minería en paralelo
 * Cada robot recolecta entre 1 y 10 minerales de forma aleatoria
 * Ahora con nivel, energía, zona asignada y estado
 */
public class Robot extends Thread {
    public enum Estado {
        ACTIVO, SIN_ENERGIA, ROTO, EN_REPARACION
    }
    
    private int id;
    private int nivel;              // 1, 2, 3
    private int energia;            // 0-100
    private String zonaAsignada;    // Zona de minería asignada
    private Estado estado;          // Estado del robot
    private List<Mineral> mineralRecolectado;
    private Fundidora fundidora;
    private Bodega bodega;
    private ZonaMina zona;          // Referencia a la zona actual
    private Random random;
    private boolean activo;
    private int totalMineralRecolectado;  // contador de ciclos
    
    public Robot(int id, Fundidora fundidora, Bodega bodega) {
        this.id = id;
        this.nivel = 1;  // Nivel inicial
        this.energia = 100;  // Energía al máximo
        this.zonaAsignada = "A";  // Zona inicial
        this.estado = Estado.ACTIVO;
        this.fundidora = fundidora;
        this.bodega = bodega;
        this.mineralRecolectado = new ArrayList<>();
        this.random = new Random();
        this.activo = true;
        this.totalMineralRecolectado = 0;
        this.zona = null;  // Se asignará desde GameEngine
    }
    
    /**
     * Constructor con parámetros para crear robot con propiedades específicas
     */
    public Robot(int id, int nivel, Fundidora fundidora, Bodega bodega, ZonaMina zona) {
        this.id = id;
        this.nivel = Math.max(1, Math.min(3, nivel));  // 1-3
        this.energia = 100;
        this.zonaAsignada = zona.getNombre();
        this.estado = Estado.ACTIVO;
        this.fundidora = fundidora;
        this.bodega = bodega;
        this.zona = zona;
        this.mineralRecolectado = new ArrayList<>();
        this.random = new Random();
        this.activo = true;
        this.totalMineralRecolectado = 0;
    }
    
    @Override
    public void run() {
        System.out.printf("Robot #%d inicia operaciones (Nivel: %d, Zona: %s)%n", id, nivel, zonaAsignada);
        
        while (activo) {
            try {
                // Si está sin energía, el robot no puede hacer nada hasta que
                // el jugador presione "Recargar" (cuesta $50)
                if (estado == Estado.SIN_ENERGIA) {
                    System.out.printf("Robot #%d está SIN ENERGÍA - esperando recarga%n", id);
                    Thread.sleep(1000);
                    continue;
                }
                
                // Si está roto, tampoco puede minar hasta que el jugador presione "Reparar" ($200)
                if (estado == Estado.ROTO || estado == Estado.EN_REPARACION) {
                    System.out.printf("Robot #%d está ROTO/EN REPARACIÓN%n", id);
                    Thread.sleep(2000);
                    continue;
                }
                
                // Si hay zona asignada, verificar disponibilidad
                if (zona != null && zona.isBloqueada()) {
                    System.out.printf("Robot #%d espera a que Zona %s se desbloquee%n", id, zona.getNombre());
                    Thread.sleep(1000);
                    continue;
                }
                
                // Simular búsqueda en el mapa (ajustado por nivel)
                int tiempoMinerando = 3000 - (nivel * 500);  // Nivel 1: 2500ms, Nivel 3: 1500ms
                System.out.printf("Robot #%d está buscando minerales en Zona %s...%n", id, zonaAsignada);
                Thread.sleep(tiempoMinerando);
                
                // Recolectar cantidad basada en nivel
                int cantidadBase = 5 + (nivel * 2);  // Nivel 1: 7, Nivel 3: 11
                int cantidadMinerales = random.nextInt(cantidadBase) + 1;
                mineralRecolectado.clear();
                
                // Si tenemos zona, extraer de ella
                if (zona != null) {
                    int extraido = zona.extraer(cantidadMinerales);
                    for (int i = 0; i < extraido; i++) {
                        Mineral m = new Mineral(zona.getTipoMineral(), random.nextDouble() * 5 + 1);
                        mineralRecolectado.add(m);
                    }
                    cantidadMinerales = extraido;
                } else {
                    // Minería aleatoria (comportamiento anterior)
                    for (int i = 0; i < cantidadMinerales; i++) {
                        String[] tiposMineral = {"Oro", "Plata", "Cobre", "Hierro", "Diamante"};
                        String tipoAleatorio = tiposMineral[random.nextInt(tiposMineral.length)];
                        double pesoAleatorio = random.nextDouble() * 5 + 1;
                        Mineral m = new Mineral(tipoAleatorio, pesoAleatorio);
                        mineralRecolectado.add(m);
                    }
                }
                
                System.out.printf("Robot #%d recolectó %d minerales (Energía: %d%%)%n", id, cantidadMinerales, energia);
                totalMineralRecolectado += cantidadMinerales;
                
                // La Zona C (Diamante) es la más exigente: consume 5 de energía extra por ciclo
                int desgaste = cantidadMinerales + (zona != null && zona.getNombre().equals("C") ? 5 : 0);
                energia -= desgaste;
                
                // Verificar si queda sin energía
                if (energia <= 0) {
                    energia = 0;
                    estado = Estado.SIN_ENERGIA;
                    System.out.printf("Robot #%d QUEDÓ SIN ENERGÍA%n", id);
                    continue;
                }
                
                // Transportar a la fundidora y bodega
                enviarAFundidoraYGuardarEnBodega();
                
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
                // interrupt() se llama desde GameEngine.detener() para matar el hilo.
                // Después de interrumpir, restauramos la bandera y salimos del bucle.
                System.out.printf("Robot #%d fue interrumpido%n", id);
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.printf("Robot #%d termina operaciones. Total recolectado: %d%n", id, totalMineralRecolectado);
    }
    
    /**
     * Envía los minerales a la fundidora de forma sincronizada
     */
    private void enviarAFundidoraYGuardarEnBodega() {
        List<Mineral> loteProcesado = new ArrayList<>(mineralRecolectado);

        // La sincronización la controla la propia fundidora con su método synchronized
        System.out.printf("Robot #%d espera turno en fundidora...%n", id);
        System.out.printf("Robot #%d PROCESANDO en fundidora...%n", id);
        fundidora.procesarMinerales(id, loteProcesado);

        boolean almacenado = bodega.almacenar(loteProcesado);
        if (almacenado) {
            System.out.printf("Robot #%d almacenó %d minerales en bodega%n", id, loteProcesado.size());
        } else {
            System.out.printf("Robot #%d no pudo almacenar %d minerales por falta de espacio%n", id, loteProcesado.size());
        }
    }
    
    public void detener() {
        activo = false;
    }

    public void reanudar() {
        activo = true;
    }
    
    // Getters y Setters para nuevas propiedades
    
    public int getNivel() {
        return nivel;
    }
    
    public void setNivel(int nivel) {
        this.nivel = Math.max(1, Math.min(3, nivel));
    }
    
    public synchronized int getEnergia() {
        return energia;
    }
    
    public synchronized void setEnergia(int energia) {
        this.energia = Math.max(0, Math.min(100, energia));
        if (this.energia == 0) {
            estado = Estado.SIN_ENERGIA;
        } else if (this.energia > 0 && estado == Estado.SIN_ENERGIA) {
            estado = Estado.ACTIVO;
        }
    }
    
    public synchronized void recargarEnergia(int cantidad) {
        this.energia = Math.min(100, this.energia + cantidad);
        if (this.energia > 0 && estado == Estado.SIN_ENERGIA) {
            estado = Estado.ACTIVO;
            System.out.printf("Robot #%d ha sido RECARGADO (Energía: %d%%)%n", id, energia);
        }
    }
    
    public String getZonaAsignada() {
        return zonaAsignada;
    }
    
    public void setZonaAsignada(String zona) {
        this.zonaAsignada = zona;
    }
    
    public void setZona(ZonaMina zona) {
        this.zona = zona;
        if (zona != null) {
            this.zonaAsignada = zona.getNombre();
        }
    }
    
    public ZonaMina getZona() {
        return zona;
    }
    
    public synchronized Estado getEstado() {
        return estado;
    }
    
    public synchronized void setEstado(Estado estado) {
        this.estado = estado;
        System.out.printf("Robot #%d cambió estado a: %s%n", id, estado);
    }
    
    public synchronized void repararRobot() {
        if (estado == Estado.ROTO) {
            estado = Estado.ACTIVO;
            energia = Math.max(50, energia);
            System.out.printf("Robot #%d ha sido REPARADO%n", id);
        }
    }
    
    public List<Mineral> getMineralRecolectado() {
        return mineralRecolectado;
    }
    
    public int getTotalMineralRecolectado() {
        return totalMineralRecolectado;
    }
    
    public int getRobotId() {
        return id;
    }
    
    public int getBodega() {
        return bodega.getEspacioDisponible();
    }
    
    public boolean isActivo() {
        return activo && this.isAlive();
    }
}
