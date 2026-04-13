package model.casillas;

import model.Jugador;

public class Salida extends Casilla {
    private static final int DINERO_SALIDA = 200;

    public Salida() {
        super("Salida", 0);
    }

    @Override
    public String efecto(Jugador jugador) {
        jugador.recibir(DINERO_SALIDA);
        return jugador.getNombre() + " pasó por Salida y recibió $" + DINERO_SALIDA;
    }
}
