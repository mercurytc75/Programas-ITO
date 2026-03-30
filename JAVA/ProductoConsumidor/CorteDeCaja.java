package ProductoConsumidor;

import java.util.HashMap;
import java.util.Map;

public class CorteDeCaja {
    private double totalGanado = 0;
    private Map<String, Integer> conteoPlatillos = new HashMap<>();

    public synchronized void registrarVenta(Platillo platillo) {
        totalGanado += platillo.getPrecio();
        conteoPlatillos.put(platillo.getNombre(), 
            conteoPlatillos.getOrDefault(platillo.getNombre(), 0) + 1);
    }

    public synchronized void imprimirReporte() {
        System.out.println("\n==========================================");
        System.out.println("      CORTE DE CAJA - FINAL DE JORNADA    ");
        System.out.println("==========================================");
        System.out.printf("Total Ganado: $%.2f\n", totalGanado);
        System.out.println("\nResumen de Platillos Vendidos:");
        conteoPlatillos.forEach((nombre, cantidad) -> {
            System.out.println("- " + nombre + ": " + cantidad);
        });
        System.out.println("==========================================\n");
    }
}
