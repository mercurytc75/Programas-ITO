package Mineria;

/**
 * Clase ZonaMina: representa una zona de minería con tipo de mineral y reservas
 * Control de reservas, estado de bloqueo y monitoreo de agotamiento
 */
public class ZonaMina {
    private String nombre;
    private String tipoMineral;
    private int reserva;
    private int reservaMaxima;
    private boolean bloqueada;
    private int nivelDesgaste;  // 0-100, afecta velocidad de agotamiento
    
    public ZonaMina(String nombre, String tipoMineral, int reservaInicial) {
        this.nombre = nombre;
        this.tipoMineral = tipoMineral;
        this.reserva = reservaInicial;
        this.reservaMaxima = reservaInicial;
        this.bloqueada = false;
        this.nivelDesgaste = 0;
    }
    
    /**
     * Intenta extraer minerales de la zona
     * @param cantidad Cantidad a extraer
     * @return Cantidad real extraída (puede ser menos si la zona está agotada)
     */
    public synchronized int extraer(int cantidad) {
        if (bloqueada) {
            return 0;
        }
        
        int extraido = Math.min(cantidad, reserva);
        reserva -= extraido;
        nivelDesgaste = Math.min(100, nivelDesgaste + (extraido / 5));
        
        if (reserva <= 0) {
            System.out.printf("[ZONA] %s AGOTADA - No hay más minerales%n", nombre);
        }
        
        return extraido;
    }
    
    /**
     * Repone la reserva de la zona (para regeneración o reinicio)
     */
    public synchronized void reponer(int cantidad) {
        reserva = Math.min(reserva + cantidad, reservaMaxima);
    }
    
    /**
     * Bloquea la zona (simulando derrumbes)
     */
    public synchronized void bloquear(int segundos) {
        bloqueada = true;
        System.out.printf("[ZONA] %s BLOQUEADA por %d segundos%n", nombre, segundos);
        
        new Thread(() -> {
            try {
                Thread.sleep(segundos * 1000L);
                bloqueada = false;
                System.out.printf("[ZONA] %s DESBLOQUEADA%n", nombre);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    // Getters
    public String getNombre() {
        return nombre;
    }
    
    public String getTipoMineral() {
        return tipoMineral;
    }
    
    public synchronized int getReserva() {
        return reserva;
    }
    
    public int getReservaMaxima() {
        return reservaMaxima;
    }
    
    public synchronized boolean isBloqueada() {
        return bloqueada;
    }
    
    public int getNivelDesgaste() {
        return nivelDesgaste;
    }
    
    public synchronized boolean estaAgotada() {
        return reserva <= 0;
    }
    
    public synchronized int getPorcentajeReserva() {
        return (reserva * 100) / reservaMaxima;
    }
}
