package engine;

import java.util.ArrayList;
import java.util.List;

import model.Jugador;

public class GameEngine {
    private TurnManager turnManager;
    private Tablero tablero;
    private boolean juegoActivo;
    private int turnoActual;
    private Jugador ganador;
    private String ultimoEvento;

    public GameEngine() {
        turnManager = new TurnManager();
        tablero = new Tablero();
        juegoActivo = false;
        turnoActual = 0;
        ganador = null;
        ultimoEvento = "";
    }

    public void agregarJugador(Jugador jugador) {
        turnManager.agregar(jugador);
        juegoActivo = true;
    }

    public void iniciarJuego() {
        if (contarJugadores() == 0) {
            ultimoEvento = "No hay jugadores para iniciar la partida.";
            System.out.println(ultimoEvento);
            return;
        }

        juegoActivo = true;
        System.out.println("========== MONOPOLY ==========");
        System.out.println("Iniciando juego con " + contarJugadores() + " jugadores\n");

        while (juegoActivo) {
            String evento = avanzarTurno();
            if (evento != null && !evento.isBlank()) {
                System.out.println(evento);
            }
        }

        if (ganador != null) {
            System.out.println("\n========== FIN DEL JUEGO ==========");
            System.out.println("¡" + ganador.getNombre() + " gana con $" + ganador.getDinero() + "!");
        }
    }

    public String avanzarTurno() {
        if (!juegoActivo) {
            if (ganador != null) {
                return "La partida ya finalizó. Ganador: " + ganador.getNombre() + " con $" + ganador.getDinero();
            }

            return "La partida no ha iniciado.";
        }

        if (contarJugadoresActivos() <= 1) {
            finalizarJuego();
            return ultimoEvento;
        }

        Jugador jugadorActual = turnManager.siguiente();
        if (jugadorActual == null) {
            finalizarJuegoSinGanador("No hay jugadores disponibles para continuar.");
            return ultimoEvento;
        }

        turnoActual++;
        StringBuilder evento = new StringBuilder();
        evento.append("--- TURNO ").append(turnoActual).append(" --- ")
                .append(jugadorActual.getNombre())
                .append(" (Dinero: $").append(jugadorActual.getDinero()).append(")");

        if (!jugadorActual.estaBancarro()) {
            int dados = tablero.lanzarDados();
            evento.append("\n").append(jugadorActual.getNombre()).append(" lanzó: ").append(dados);

            if (jugadorActual.estaEnCarcel()) {
                evento.append("\n").append(jugadorActual.getNombre()).append(" está en cárcel. Pierde este turno y sale de la cárcel.");
                jugadorActual.salirDeCarcel();
            } else {
                int posicionAnterior = jugadorActual.getPosicion();
                jugadorActual.mover(dados);
                evento.append("\n").append(jugadorActual.getNombre()).append(" se movió de ")
                        .append(posicionAnterior).append(" a ").append(jugadorActual.getPosicion());

                String efecto = tablero.procesarCasilla(jugadorActual);
                if (efecto != null && !efecto.isBlank()) {
                    evento.append("\n").append(efecto);
                }
            }
        } else {
            evento.append("\n").append(jugadorActual.getNombre()).append(" está en bancarrota y es eliminado!");
        }

        if (!jugadorActual.estaBancarro()) {
            turnManager.reinsertar(jugadorActual);
        }

        if (contarJugadoresActivos() <= 1) {
            finalizarJuego();
            if (ganador != null) {
                evento.append("\n").append("Ganador: ").append(ganador.getNombre()).append(" con $").append(ganador.getDinero());
            }
        }

        ultimoEvento = evento.toString();
        return ultimoEvento;
    }

    public List<Jugador> obtenerJugadores() {
        return new ArrayList<>(turnManager.obtenerOrdenActual());
    }

    public boolean estaTerminado() {
        return !juegoActivo;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public String getUltimoEvento() {
        return ultimoEvento;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public Jugador getJugadorEnTurno() {
        return turnManager.actual();
    }

    private int contarJugadores() {
        return obtenerJugadores().size();
    }

    private int contarJugadoresActivos() {
        int count = 0;

        for (Jugador j : obtenerJugadores()) {
            if (!j.estaBancarro()) {
                count++;
            }
        }

        return count;
    }

    private void finalizarJuego() {
        ganador = buscarGanador();
        juegoActivo = false;

        if (ganador != null) {
            ultimoEvento = "La partida terminó. Ganador: " + ganador.getNombre() + " con $" + ganador.getDinero();
        } else {
            ultimoEvento = "La partida terminó sin un ganador definido.";
        }
    }

    private void finalizarJuegoSinGanador(String mensaje) {
        ganador = null;
        juegoActivo = false;
        ultimoEvento = mensaje;
    }

    private Jugador buscarGanador() {
        for (Jugador jugador : obtenerJugadores()) {
            if (!jugador.estaBancarro()) {
                return jugador;
            }
        }

        return null;
    }
}
