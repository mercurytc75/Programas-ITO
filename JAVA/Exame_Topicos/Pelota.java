public class Pelota {
    private int turno = 1;
    private int intercambio = 0;
    private final int maxIntercambios;
    
    public Pelota(int maxIntercambios){
        this.maxIntercambios = maxIntercambios;
    }

    public synchronized void golpear(int jugador) throws InterruptedException {
        while (turno != jugador && intercambio < maxIntercambios){
            wait(); // espera su turno
        }
        if(intercambio >= maxIntercambios) return;

        intercambio++;

        System.out.println("Golpe: " + intercambio + " - Jugador " + jugador + " golpea la pelota");

        // cambia el turno al otro jugador
        turno = (jugador == 1) ? 2 : 1;

        // le avisamos al otro jugador
        notifyAll();
    }

    public int getMaxIntercambios(){
        return maxIntercambios;
    }

    public int getIntercambios( ){
        return intercambio;
    }
}
