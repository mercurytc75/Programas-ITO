package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;

public abstract class Casilla {
    protected String nombre;
    protected int posicion;

    public Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
    }

    public abstract void efecto(Jugador jugador);

    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }
}
