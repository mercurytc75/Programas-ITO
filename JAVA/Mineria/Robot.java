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
    
    private Random random;
    private boolean activo;
    private int totalMineralRecolectado;  // contador de ciclos
    
    public Robot(int id, Fundidora fundidora, Bodega bodega) {
        this.id = id;
        this.fundidora = fundidora;
        
        this.mineralRecolectado = new ArrayList<>();
        this.random = new Random();
        this.activo = true;
        this.totalMineralRecolectado = 0;
    }
    
    @Override
    public void run() {
        System.out.println("🤖 Robot #" + id + " inicia operaciones");
        
        while (activo) {
            try {
                // Simular búsqueda en el mapa (1-3 segundos)
                int tiempoMinerando = random.nextInt(2000) + 1000;
                System.out.println("🔍 Robot #" + id + " está buscando minerales...");
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
                
                System.out.println("✅ Robot #" + id + " recolectó " + cantidadMinerales + 
                                 " minerales: " + mineralRecolectado);
                totalMineralRecolectado += cantidadMinerales;
                
                // Transportar a la fundidora
                enviarAFundidora();
                
                // Esperar un poco antes de volver a minar
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
                System.out.println("⚠️ Robot #" + id + " fue interrumpido");
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("🛑 Robot #" + id + " termina operaciones. Total recolectado: " + totalMineralRecolectado);
    }
    
    /**
     * Envía los minerales a la fundidora de forma sincronizada
     */
    private void enviarAFundidora() {
        try {
            // Esperar turno en la fundidora
            synchronized (fundidora) {
                System.out.println("⏳ Robot #" + id + " espera turno en fundidora...");
                fundidora.wait();  // Esperar a que la fundidora esté disponible
                
                // Procesar en la fundidora
                System.out.println("🔥 Robot #" + id + " PROCESANDO en fundidora...");
                fundidora.procesarMinerales(id, mineralRecolectado);
                
                // Notificar al siguiente robot
                fundidora.notifyAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
    
    public boolean isActivo() {
        return activo && this.isAlive();
    }
}
