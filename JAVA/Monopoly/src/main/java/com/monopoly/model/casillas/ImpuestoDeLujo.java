package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;

public class ImpuestoDeLujo extends Casilla {
    private static final int MONTO_IMPUESTO = 75;

    public ImpuestoDeLujo() {
        super("Impuesto de Lujo", 38);
    }

    @Override
    public String efecto(Jugador jugador) {
        jugador.pagar(MONTO_IMPUESTO);
        return jugador.getNombre() + " pagó Impuesto de Lujo: $" + MONTO_IMPUESTO;
    }
}
