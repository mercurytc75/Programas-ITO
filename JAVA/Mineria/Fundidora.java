package Mineria;

import java.util.List;

/**
 * Clase Fundidora: procesa los minerales de forma sincronizada
 * Solo puede atender un robot a la vez
 */
public class Fundidora {
    private int mineralRefinado;
    private int ciclosCompletados;
    private boolean ocupada;
    private int robotActual;
    
    public Fundidora() {
        this.mineralRefinado = 0;
        this.ciclosCompletados = 0;
        this.ocupada = false;
        this.robotActual = -1;
    }
    
    /**
     * Procesa los minerales de forma sincronizada
     * @param idRobot ID del robot que hace la solicitud
     * @param minerales Lista de minerales a procesar
     */
    public synchronized void procesarMinerales(int idRobot, List<Mineral> minerales) {
        try {
            ocupada = true;
            robotActual = idRobot;
            
            System.out.println("🔥 [FUNDIDORA] Iniciando procesamiento para Robot #" + idRobot);
            
            // Simular tiempo de refino (1-2 segundos por lote)
            int tiempoRefino = 1000 + (int)(Math.random() * 1000);
            Thread.sleep(tiempoRefino);
            
            // Contar los minerales procesados
            int cantidadProcesada = minerales.size();
            mineralRefinado += cantidadProcesada;
            ciclosCompletados++;
            
            System.out.println("✅ [FUNDIDORA] Procesamiento completado. " + cantidadProcesada + 
                             " minerales refinados. Total: " + mineralRefinado);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            ocupada = false;
            robotActual = -1;
        }
    }
    
    public synchronized int getMineralRefinado() {
        return mineralRefinado;
    }
    
    public synchronized int getCiclosCompletados() {
        return ciclosCompletados;
    }
    
    public synchronized boolean isOcupada() {
        return ocupada;
    }
    
    public synchronized int getRobotActual() {
        return robotActual;
    }
}
