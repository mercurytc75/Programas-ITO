import java.util.Random;

public class HiloWhatsApp implements Runnable {
    private final int totalNotificaciones = 5;
    private final Random random = new Random();

    @Override
    public void run() {
        for (int i = 1; i <= totalNotificaciones; i++) {
            try {
               
                Thread.sleep(500 + random.nextInt(2000));
                System.out.println("[WhatsApp] - Notificación #" + i + " recibida.");
            } catch (InterruptedException e) {
                System.out.println("Hilo de WhatsApp interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }
}
