package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;

public class EstacionamientoLibre extends Casilla {
    public EstacionamientoLibre() {
        super("Estacionamiento Libre", 20);
    }

    @Override
    public void efecto(Jugador jugador) {
        System.out.println(jugador.getNombre() + " está en Estacionamiento Libre (seguro)");
    }
}
