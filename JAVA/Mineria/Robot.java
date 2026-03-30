package Mineria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase Robot que extiende Thread para ejecutar minería en paralelo
 * Cada robot recolecta entre 1 y 10 minerales de forma aleatoria
 */
public class Robot extends Thread {
    private int id;
    private List<Mineral> mineralRecolectado;
    private Fundidora fundidora;
    private Bodega bodega;
    private Random random;
    private boolean activo;
    private int totalMineralRecolectado;  // contador de ciclos
    
    public Robot(int id, Fundidora fundidora, Bodega bodega) {
        this.id = id;
        this.fundidora = fundidora;
        this.bodega = bodega;
        this.mineralRecolectado = new ArrayList<>();
        this.random = new Random();
        this.activo = true;
        this.totalMineralRecolectado = 0;
    }
    
    @Override
    public void run() {
        System.out.printf("Robot #%d inicia operaciones%n", id);
        
        while (activo) {
            try {
                // Simular búsqueda en el mapa (1-3 segundos)
                int tiempoMinerando = random.nextInt(2000) + 1000;
                System.out.printf("Robot #%d está buscando minerales...%n", id);
                Thread.sleep(tiempoMinerando);
                
                // Recolectar cantidad ALEATORIA de 1 a 10 minerales
                int cantidadMinerales = random.nextInt(10) + 1;  // 1-10
                mineralRecolectado.clear();
                
                for (int i = 0; i < cantidadMinerales; i++) {
                    String[] tiposMineral = {"Oro", "Plata", "Cobre", "Hierro", "Diamante"};
                    String tipoAleatorio = tiposMineral[random.nextInt(tiposMineral.length)];
                    double pesoAleatorio = random.nextDouble() * 5 + 1;  // 1-6 kg
                    
                    Mineral m = new Mineral(tipoAleatorio, pesoAleatorio);
                    mineralRecolectado.add(m);
                }
                
                System.out.printf("Robot #%d recolectó %d minerales: %s%n", id, cantidadMinerales, mineralRecolectado);
                totalMineralRecolectado += cantidadMinerales;
                
                // Transportar a la fundidora y luego almacenar en bodega
                enviarAFundidoraYGuardarEnBodega();
                
                // Esperar un poco antes de volver a minar
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
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
    
   
    
    public List<Mineral> getMineralRecolectado() {
        return mineralRecolectado;
    }
    
    public int getTotalMineralRecolectado() {
        return totalMineralRecolectado;
    }
    public int getRobotId() {
        return id;
    }
    public long getBodega(){
        return bodega.getEspacioDisponible();
    }
    public boolean isActivo() {
        return activo && this.isAlive();
    }
}
