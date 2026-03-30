package Mineria;

/**
 * Clase que representa un mineral recolectado por los robots
 */
public class Mineral {
    private String tipo;
    private double peso;
    private long tiempoRecoleccion;
    
    public Mineral(String tipo, double peso) {
        this.tipo = tipo;
        this.peso = peso;
        this.tiempoRecoleccion = System.currentTimeMillis();
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public double getPeso() {
        return peso;
    }
    
    public long getTiempoRecoleccion() {
        return tiempoRecoleccion;
    }
    
    @Override
    public String toString() {
        return "Mineral{" + tipo + ", " + peso + "kg}";
    }
}
