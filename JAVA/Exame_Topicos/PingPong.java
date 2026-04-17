import Monopoly.Jugador;

public class PingPong {
    public static void main(String[] args) {
        int TotalDeIntercambios = 10; //lo podemos mejorarer si queremos mas o menos golpes 

        Pelota pelota = new Pelota(TotalDeIntercambios);

        Jugador j1 = new Jugador(1, pelota);
        Jugador j2 = new Jugador(2, pelota);
        
        

        System.out.println("=== Inicio del juego de Ping Pong ===");
        j1.start();
        j2.start();
        

        try{    
            j1.join();
            j2.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Juego terminado después de " + TotalDeIntercambios + " intercambios ===");
    }
}