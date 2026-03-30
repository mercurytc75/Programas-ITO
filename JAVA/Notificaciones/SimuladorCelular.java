public class SimuladorCelular {
    public static void main(String[] args) {
        System.out.println("--- Simulador de Celular Iniciado ---");
        System.out.println("Esperando notificaciones...\n");

        
        Thread hiloWhatsApp = new Thread(new HiloWhatsApp());
        Thread hiloCorreo = new Thread(new HiloCorreo());
        Thread hiloRedes = new Thread(new HiloRedes());

        
        hiloWhatsApp.start();
        hiloCorreo.start();
        hiloRedes.start();

        
        try {
            hiloWhatsApp.join();
            hiloCorreo.join();
            hiloRedes.join();
        } catch (InterruptedException e) {
            System.out.println("El simulador fue interrumpido.");
        }

        System.out.println("\n--- Todas las notificaciones han sido recibidas ---");
    }
}
