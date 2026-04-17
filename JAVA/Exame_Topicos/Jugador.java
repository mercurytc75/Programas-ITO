public class Jugador extends Thread{
    private final int id;
    private final Pelota pelota;

    public Jugador (int id, Pelota pelota){
        this.id = id;
        this.pelota = pelota;
    }

    @Override
    public void run(){
        try {
            while(pelota.getIntercambios() < pelota.getMaxIntercambios()){
                pelota.golpear(id);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
