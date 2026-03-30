package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;

public class ImpuestoDeIngresos extends Casilla {
    private static final int MONTO_IMPUESTO = 200;

    public ImpuestoDeIngresos() {
        super("Impuesto de Ingresos", 4);
    }

    @Override
    public void efecto(Jugador jugador) {
        jugador.pagar(MONTO_IMPUESTO);
        System.out.println(jugador.getNombre() + " pagó Impuesto de Ingresos: $" + MONTO_IMPUESTO);
    }
}
