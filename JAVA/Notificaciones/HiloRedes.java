import java.util.Random;

public class HiloRedes implements Runnable {
    private final int totalNotificaciones = 5;
    private final Random random = new Random();
    private final String[] apps = {"Facebook", "Instagram", "TikTok"};

    @Override
    public void run() {
        for (int i = 1; i <= totalNotificaciones; i++) {
            try {
                
                String app = apps[random.nextInt(apps.length)];
               
                Thread.sleep(500 + random.nextInt(2000));
                System.out.println("[Redes Sociales: " + app + "] - Notificación #" + i + " recibida.");
            } catch (InterruptedException e) {
                System.out.println("Hilo de Redes Sociales interrumpido.");
                Thread.currentThread().interrupt();
            
            }
        }
    }
}
