package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;

public class Carcel extends Casilla {
    public Carcel() {
        super("Cárcel", 10);
    }

    @Override
    public void efecto(Jugador jugador) {
        jugador.encarcelar();
        System.out.println(jugador.getNombre() + " cayó en la cárcel!");
    }

    public void salirDeCarcel(Jugador jugador) {
        jugador.salirDeCarcel();
        System.out.println(jugador.getNombre() + " salió de la cárcel!");
    }
}
