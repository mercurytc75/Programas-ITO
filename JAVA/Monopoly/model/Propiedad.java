package model;

public class Propiedad {
    private String nombre;
    private int precio;
    private int renta;
    private Jugador duenio;

    public Propiedad(String nombre, int precio, int renta) {
        this.nombre = nombre;
        this.precio = precio;
        this.renta = renta;
        this.duenio = null; // sin dueño inicial
    }

    public boolean tieneDuenio() {
        return duenio != null;
    }

    public void setDuenio(Jugador duenio) {
        this.duenio = duenio;
    }

    public Jugador getDuenio() {
        return duenio;
    }

    public int getPrecio() {
        return precio;
    }

    public int getRenta() {
        return renta;
    }

    public String getNombre() {
        return nombre;
    }
}
