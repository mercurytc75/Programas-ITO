package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;

public class Salida extends Casilla {
    private static final int DINERO_SALIDA = 200;

    public Salida() {
        super("Salida", 0);
    }

    @Override
    public void efecto(Jugador jugador) {
        jugador.recibir(DINERO_SALIDA);
        System.out.println(jugador.getNombre() + " pasó por Salida y recibió $" + DINERO_SALIDA);
    }
}
