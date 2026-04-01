package engine;

import java.util.ArrayList;
import java.util.List;

import model.Jugador;
import tda.Cola;

public class TurnManager {
    private final Cola<Jugador> colaTurnos;

    public TurnManager() {
        this.colaTurnos = new Cola<>();
    }

    public void agregar(Jugador jugador) {
        colaTurnos.encolar(jugador);
    }

    public Jugador siguiente() {
        return colaTurnos.descolar();
    }

    public void reinsertar(Jugador jugador) {
        colaTurnos.encolar(jugador);
    }

    public Jugador actual() {
        return colaTurnos.verFrente();
    }

    public List<Jugador> obtenerOrdenActual() {
        List<Jugador> jugadores = new ArrayList<>();
        Cola<Jugador> temporal = new Cola<>();

        while (!colaTurnos.estaVacia()) {
            Jugador jugador = colaTurnos.descolar();
            jugadores.add(jugador);
            temporal.encolar(jugador);
        }

        while (!temporal.estaVacia()) {
            colaTurnos.encolar(temporal.descolar());
        }

        return jugadores;
    }
}
