package ProductoConsumidor;

import java.util.ArrayList;
import java.util.List;

public class RestaurantePc {
    public static void main(String[] args) {

        // Capacidad de la cola aumentada para manejar más tráfico
        ColaPedidos cola = new ColaPedidos(10);
        CorteDeCaja corte = new CorteDeCaja();

        // Menú con 10 platillos (5 originales + 5 adicionales con propiedades completas)
        Platillo[] menu = {
            new Platillo("Hamburguesa", "Comida Rápida", 150.0),
            new Platillo("Pizza", "Comida Rápida", 200.0),
            new Platillo("Tacos", "Mexicana", 80.0),
            new Platillo("Ensalada", "Saludable", 120.0),
            new Platillo("Sopa", "Entrada", 60.0),
            new Platillo("Sushi", "Japonesa", 250.0),
            new Platillo("Pasta Alfredo", "Italiana", 180.0),
            new Platillo("Burrito", "Mexicana", 110.0),
            new Platillo("Ceviche", "Mariscos", 220.0),
            new Platillo("Pollo Asado", "General", 160.0)
        }; 

        int numMeseros = 10;
        int numCocineros = 3;
        List<Thread> hilos = new ArrayList<>();

        System.out.println("--- Restaurante abierto con 10 meseros, 3 cocineros y 10 platillos en menú ---\n");

        
        String[] estaciones = {"Parrilla", "Fríos", "Repostería"};
        for (int i = 0; i < numCocineros; i++) {
            Cocinero cocinero = new Cocinero(cola, estaciones[i], corte);
            cocinero.setName("Cocinero-" + (i + 1));
            cocinero.start();
            hilos.add(cocinero);
        }

        // --- CREACIÓN DINÁMICA DE MESEROS ---
        for (int i = 1; i <= numMeseros; i++) {
            Mesero mesero = new Mesero(cola, menu, numMeseros, numCocineros);
            mesero.setName("Mesero-" + i); // Identificador único dinámico
            mesero.start();
            hilos.add(mesero);
        }

        // Esperamos a que todos los hilos terminen antes de imprimir el corte
        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Error esperando hilos");
            }
        }

        // --- IMPRESIÓN DEL REPORTE FINAL ---
        corte.imprimirReporte();
    }
}