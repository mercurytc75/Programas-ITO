package engine;

import model.casillas.*;
import model.Jugador;
import util.DiceUtils;

public class Tablero {
    private Casilla[] casillas;
    public static final int TOTAL_CASILLAS = 40;

    public Tablero() {
        casillas = new Casilla[TOTAL_CASILLAS];
        inicializarTablero();
    }

    private void inicializarTablero() {
        // Casillas especiales
        casillas[0] = new Salida();
        casillas[4] = new ImpuestoDeIngresos();
        casillas[10] = new Carcel();
        casillas[20] = new EstacionamientoLibre();
        casillas[38] = new ImpuestoDeLujo();
        casillas[39] = new Carcel(); // Vaya a la Cárcel

        // Las demás casillas son propiedades (se crean dinámicamente según sea necesario)
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            if (casillas[i] == null) {
                // Será una propiedad (inicializada cuando sea necesario)
            }
        }
    }

    public int lanzarDados() {
        return DiceUtils.lanzarDosDados();
    }

    public Casilla getCasilla(int posicion) {
        return casillas[posicion];
    }

    public String procesarCasilla(Jugador jugador) {
        int posicion = jugador.getPosicion();
        Casilla casilla = casillas[posicion];

        if (casilla != null) {
            return casilla.efecto(jugador);
        }

        return jugador.getNombre() + " está en una propiedad sin dueño en la posición " + posicion;
    }

    public int getTotalCasillas() {
        return TOTAL_CASILLAS;
    }

    public String getNombreCasilla(int posicion) {
        Casilla casilla = casillas[posicion];

        if (casilla != null) {
            return casilla.getNombre();
        }

        return "Propiedad";
    }

    public void crearPropiedad(int posicion, String nombre, int precio, int renta) {
        if (casillas[posicion] == null) {
            casillas[posicion] = new PropiedadCasilla(nombre, posicion, precio, renta);
        }
    }
}
