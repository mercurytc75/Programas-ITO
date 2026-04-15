package Mineria;

import java.util.List;
import java.util.Random;

/**
 * Clase GestorEventos: genera eventos aleatorios en tiempo real
 * Implementa eventos como derrumbes, daños, vetas ricas, etc.
 */
public class GestorEventos {
    private List<Robot> robots;
    private List<ZonaMina> zonas;
    private Fundidora fundidora;
    private Random random;
    private boolean activo;
    
    private static final int INTERVALO_EVENTOS_MIN = 20000;  // 20 segundos
    private static final int INTERVALO_EVENTOS_MAX = 60000;  // 60 segundos
    
    public enum TipoEvento {
        FALLO_ELECTRICO("⚡ Fallo eléctrico"),
        DERRUMBE_ZONA("🌋 Derrumbe en zona"),
        ROBOT_DAÑADO("🤖 Robot dañado"),
        VETA_RICA("💎 Veta rica");
        
        final String descripcion;
        
        TipoEvento(String descripcion) {
            this.descripcion = descripcion;
        }
    }
    
    public GestorEventos(List<Robot> robots, List<ZonaMina> zonas, Fundidora fundidora) {
        this.robots = robots;
        this.zonas = zonas;
        this.fundidora = fundidora;
        this.random = new Random();
        this.activo = false;
    }
    
    /**
     * Inicia el generador de eventos aleatorios
     */
    public void iniciar() {
        if (activo) {
            System.out.println("[GestorEventos] Ya está activo");
            return;
        }
        
        activo = true;
        new Thread(this::bucleEventos, "GestorEventos").start();
    }
    
    /**
     * Bucle principal de generación de eventos
     */
    private void bucleEventos() {
        System.out.println("[GestorEventos] Sistema de eventos iniciado");
        
        while (activo) {
            try {
                // Esperar tiempo aleatorio entre eventos
                int tiempoEspera = INTERVALO_EVENTOS_MIN + 
                        random.nextInt(INTERVALO_EVENTOS_MAX - INTERVALO_EVENTOS_MIN);
                Thread.sleep(tiempoEspera);
                
                if (!activo) break;
                
                // Generar evento aleatorio
                TipoEvento evento = TipoEvento.values()[random.nextInt(TipoEvento.values().length)];
                generarEvento(evento);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    /**
     * Genera un evento específico
     */
    private void generarEvento(TipoEvento tipo) {
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("⚠️  EVENTO: %s%n", tipo.descripcion);
        System.out.println("=".repeat(60));
        
        switch (tipo) {
            case FALLO_ELECTRICO:
                eventoFalloElectrico();
                break;
            case DERRUMBE_ZONA:
                eventoDerrumbe();
                break;
            case ROBOT_DAÑADO:
                eventoRobotDañado();
                break;
            case VETA_RICA:
                eventoVetaRica();
                break;
        }
    }
    
    /**
     * EVENTO: Fallo eléctrico - la fundidora se detiene 10 segundos
     */
    private void eventoFalloElectrico() {
        System.out.println("La fundidora sufre un fallo eléctrico y se detiene por 10 segundos");
        System.out.println("💡 Acción recomendada: Presiona 'Reset Fundidora' para acelerar recuperación");
        
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                System.out.println("[EVENTO] La fundidora se ha recuperado del fallo eléctrico");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * EVENTO: Derrumbe en zona - bloquea una zona aleatoria por 20 segundos
     */
    private void eventoDerrumbe() {
        if (zonas.isEmpty()) return;
        
        ZonaMina zonaAfectada = zonas.get(random.nextInt(zonas.size()));
        System.out.printf("¡¡¡Derrumbe en Zona %s!!! Bloqueada por 20 segundos%n", zonaAfectada.getNombre());
        System.out.println("💡 Acción recomendada: Reasigna los robots de esa zona a otras");
        
        zonaAfectada.bloquear(20);
    }
    
    /**
     * EVENTO: Robot dañado - un robot pierde 50% de energía
     */
    private void eventoRobotDañado() {
        if (robots.isEmpty()) return;
        
        Robot robotAfectado = robots.get(random.nextInt(robots.size()));
        System.out.printf("Robot #%d ha sufrido una avería y pierde 50%% de energía%n", robotAfectado.getRobotId());
        System.out.println("💡 Acción recomendada: Recarga el robot o espera que se repare solo");
        
        // Simular daño (reducir energía)
        robotAfectado.setEnergia(Math.max(0, robotAfectado.getEnergia() - 50));
    }
    
    /**
     * EVENTO: Veta rica - una zona produce minerales x2 por 15 segundos
     */
    private void eventoVetaRica() {
        if (zonas.isEmpty()) return;
        
        ZonaMina zonaAfortunada = zonas.get(random.nextInt(zonas.size()));
        System.out.printf("¡¡¡Veta rica descubierta en Zona %s!!! Los minerales se duplican por 15 segundos%n", 
                zonaAfortunada.getNombre());
        System.out.println("💡 Acción recomendada: Envía más robots a esa zona para aprovechar el bonus");
        
        // Reponer la zona con bonus
        zonaAfortunada.reponer(zonaAfortunada.getReservaMaxima() / 2);
        
        new Thread(() -> {
            try {
                Thread.sleep(15000);
                System.out.printf("[EVENTO] La veta rica en Zona %s se agotó%n", zonaAfortunada.getNombre());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Detiene el generador de eventos
     */
    public void detener() {
        activo = false;
        System.out.println("[GestorEventos] Sistema de eventos detenido");
    }
    
    public boolean isActivo() {
        return activo;
    }
}
