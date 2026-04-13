package Monopoly;

public class Jugador {
    private int id;
    private String nombre;
    private int dinero;
    private int posicion;
    private Lista<Propiedad> propiedades;
    private boolean enCarcel;
    private int turnosEnCarcel;

    public Jugador(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.dinero = 1500; // Dinero inicial
        this.posicion = 0; // Posición inicial en el tablero
        this.propiedades = new Lista<>(); // Lista de propiedades del jugador
        this.enCarcel = false; // Estado inicial del jugador
        this.turnosEnCarcel = 0;
    }

    public void mover(int pasos) {
        posicion = (posicion + pasos) % 40; // Asumiendo un tablero de 40 casillas
    }

    public void pagar(int cantidad) {
        dinero -= cantidad;
        if (dinero < 0) dinero = 0;
    }

    public void recibir(int cantidad) {
        dinero += cantidad;
    }

    public void agregarPropiedad(Propiedad propiedad) {
        propiedades.agregar(propiedad);
    }

    public void encarcelar() {
        enCarcel = true;
        turnosEnCarcel = 0;
    }

    public void salirDeCarcel() {
        enCarcel = false;
        turnosEnCarcel = 0;
    }

    public void incrementarTurnoEnCarcel() {
        turnosEnCarcel++;
    }

    public int getTurnosEnCarcel() {
        return turnosEnCarcel;
    }

    public boolean estaEnCarcel() {
        return enCarcel;
    }

    public boolean estaBancarro() {
        return dinero <= 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDinero() {
        return dinero;
    }

    public int getPosicion() {
        return posicion;
    }

    public int getId() {
        return id;
    }

    public Lista<Propiedad> getPropiedades() {
        return propiedades;
    }
}
