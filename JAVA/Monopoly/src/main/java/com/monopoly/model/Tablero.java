package com.monopoly.model;

import com.monopoly.model.casillas.*;
import java.util.Random;

public class Tablero {
    private Casilla[] casillas;
    private static final int TOTAL_CASILLAS = 40;
    private Random dado;

    public Tablero() {
        casillas = new Casilla[TOTAL_CASILLAS];
        dado = new Random();
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
        return dado.nextInt(6) + 1 + dado.nextInt(6) + 1; // Suma de dos dados (2-12)
    }

    public Casilla getCasilla(int posicion) {
        return casillas[posicion];
    }

    public void procesarCasilla(Jugador jugador) {
        int posicion = jugador.getPosicion();
        Casilla casilla = casillas[posicion];

        if (casilla != null) {
            casilla.efecto(jugador);
        } else {
            System.out.println(jugador.getNombre() + " está en una propiedad sin dueño en la posición " + posicion);
        }
    }

    public void crearPropiedad(int posicion, String nombre, int precio, int renta) {
        if (casillas[posicion] == null) {
            casillas[posicion] = new PropiedadCasilla(nombre, posicion, precio, renta);
        }
    }
}
