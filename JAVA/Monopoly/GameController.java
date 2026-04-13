package controller;

import java.util.List;

import engine.GameEngine;
import engine.PurchaseDecisionStrategy;
import engine.Tablero;
import model.Jugador;

public class GameController {
    private final GameEngine gameEngine;

    public GameController() {
        this.gameEngine = new GameEngine();
    }

    public void agregarJugador(Jugador jugador) {
        gameEngine.agregarJugador(jugador);
    }

    public void iniciarJuego() {
        gameEngine.iniciarJuego();
    }

    public String avanzarTurno() {
        return gameEngine.avanzarTurno();
    }

    public List<Jugador> obtenerJugadores() {
        return gameEngine.obtenerJugadores();
    }

    public boolean estaTerminado() {
        return gameEngine.estaTerminado();
    }

    public int getTurnoActual() {
        return gameEngine.getTurnoActual();
    }

    public Jugador getGanador() {
        return gameEngine.getGanador();
    }

    public Jugador getJugadorEnTurno() {
        return gameEngine.getJugadorEnTurno();
    }

    public Tablero getTablero() {
        return gameEngine.getTablero();
    }

    public void setModoAutomatico(boolean modoAutomatico) {
        gameEngine.setModoAutomatico(modoAutomatico);
    }

    public void setPurchaseDecisionStrategy(PurchaseDecisionStrategy purchaseDecisionStrategy) {
        gameEngine.setPurchaseDecisionStrategy(purchaseDecisionStrategy);
    }
}
