package service;

import java.util.List;

import model.Jugador;

public class TurnoService {
    private List<Jugador> jugadores;
    private int turnoActual;

    public TurnoService(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.turnoActual = 0;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    public void siguienteTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
    }

}
