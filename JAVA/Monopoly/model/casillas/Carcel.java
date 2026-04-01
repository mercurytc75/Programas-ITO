package model.casillas;

import model.Jugador;

public class Carcel extends Casilla {
    public Carcel() {
        super("Cárcel", 10);
    }

    @Override
    public String efecto(Jugador jugador) {
        jugador.encarcelar();
        return jugador.getNombre() + " cayó en la cárcel!";
    }

    public void salirDeCarcel(Jugador jugador) {
        jugador.salirDeCarcel();
    }
}
