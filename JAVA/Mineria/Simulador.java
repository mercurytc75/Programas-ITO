package Mineria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase Simulador: orquestador principal de la minería
 * Crea los robots, la fundidora y la bodega, y maneja la simulación
 */
public class Simulador {
    private List<Robot> robots;
    private Fundidora fundidora;
    private Bodega bodega;
    private boolean simulacionActiva;
    private int cantidadRobots;
    private boolean resumenMostrado;
    
   
    public Simulador(int cantidadRobots) {
        this.cantidadRobots = cantidadRobots;
        this.robots = new ArrayList<>();
        this.fundidora = new Fundidora();
        this.bodega = new Bodega();  // Capacidad por defecto
        this.simulacionActiva = false;
    }
    
    /**
     * Inicia la simulación creando y ejecutando todos los robots
     */
    public void iniciar() {
        if (simulacionActiva) {
            System.out.println("⚠️ La simulación ya está en curso");
            return;
        }
        
        robots = new ArrayList<>();
        fundidora = new Fundidora();
        bodega = new Bodega();
        resumenMostrado = false;
        simulacionActiva = true;
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 INICIANDO SIMULACIÓN DE MINERÍA");
        System.out.println("=".repeat(60));
        System.out.println("Robots: " + cantidadRobots);
        System.out.println("Capacidad bodega: " + bodega.getCapacidadMaxima() + " minerales");
        System.out.println("=".repeat(60) + "\n");
        
        // Crear y iniciar robots
        for (int i = 1; i <= cantidadRobots; i++) {
            Robot robot = new Robot(i, fundidora, bodega);
            robots.add(robot);
            robot.start();
        }
        
        // Thread para mostrar estadísticas
        new Thread(this::mostrarEstadisticas).start();
    }
    
    /**
     * Detiene todos los robots
     */
    public void detener() {
        finalizarSimulacion("Detención manual");
    }
    
    /**
     * Muestra estadísticas en tiempo real cada 5 segundos
     */
    private void mostrarEstadisticas() {
        while (simulacionActiva) {
            try {
                Thread.sleep(1000);

                if (bodega.isLlena()) {
                    finalizarSimulacion("La bodega alcanzó su capacidad máxima de 5000 minerales");
                    break;
                }
                
                System.out.println("\n ========== ESTADÍSTICAS ACTUALES ==========");
                System.out.println("Fundidora - Mineral refinado: " + fundidora.getMineralRefinado() + 
                                 " | Ciclos: " + fundidora.getCiclosCompletados());
                System.out.println("Bodega - Almacenado: " + bodega.getCantidadAlmacenada() + 
                                 " | Disponible: " + bodega.getEspacioDisponible() + "/" + bodega.getCapacidadMaxima());
                
                System.out.println("\nRobots activos: " + contarRobotsActivos());
                for (Robot robot : robots) {
                    System.out.println("  Robot #" + robot.getRobotId() + " - Recolectado: " + 
                                     robot.getTotalMineralRecolectado() + " | Activo: " + robot.isActivo());
                }
                System.out.println("=".repeat(45) + "\n");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private int contarRobotsActivos() {
        return (int) robots.stream().filter(Robot::isActivo).count();
    }

    private synchronized void finalizarSimulacion(String motivo) {
        if (resumenMostrado || !simulacionActiva) {
            return;
        }

        simulacionActiva = false;
        System.out.println("\n⛔ Finalizando simulación: " + motivo);

        for (Robot robot : robots) {
            robot.detener();
            robot.interrupt();
        }

        esperarTerminacion();
        resumenMostrado = true;
    }
    
    /**
     * Espera a que todos los robots terminen
     */
    public void esperarTerminacion() {
        for (Robot robot : robots) {
            try {
                robot.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        mostrarEstadisticasFinal();
    }
    
    /**
     * Muestra estadísticas finales de la simulación
     */
    private void mostrarEstadisticasFinal() {
        int totalRecolectado = robots.stream().mapToInt(Robot::getTotalMineralRecolectado).sum();
        int totalRefinado = fundidora.getMineralRefinado();
        int pendientePorFundir = Math.max(0, totalRecolectado - totalRefinado);
        Map<String, Integer> clasificacion = bodega.getMineralesClasificados();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📈 ESTADÍSTICAS FINALES");
        System.out.println("=".repeat(60));
        System.out.println("Total mineral refinado: " + totalRefinado);
        System.out.println("Ciclos completados: " + fundidora.getCiclosCompletados());
        System.out.println("Minerales en bodega: " + bodega.getCantidadAlmacenada());
        System.out.println("Clasificación en bodega:");
        if (clasificacion.isEmpty()) {
            System.out.println("  Sin minerales almacenados");
        } else {
            for (Map.Entry<String, Integer> entry : clasificacion.entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("Total recolectado por todos los robots: " + totalRecolectado);
        System.out.println("Lo que faltó por fundir: " + pendientePorFundir);
        System.out.println("Capacidad usada de bodega: " + bodega.getCantidadAlmacenada() + "/" + bodega.getCapacidadMaxima());
        System.out.println("\nDetalle por robot:");
        for (Robot robot : robots) {
            System.out.println("  Robot #" + robot.getRobotId() + ": " + robot.getTotalMineralRecolectado() + " minerales");
        }
        System.out.println("=".repeat(60) + "\n");
        finalizarSimulacion("Simulación completada");
    } 
    public Fundidora getFundidora(){
        return fundidora;
    }
    public Bodega getBodega(){
        return bodega;
    }
    public int getRobotsActivos(){
        return (int) robots.stream().filter(Robot::isActivo).count();
    }

}
