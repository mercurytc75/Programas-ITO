package Monopoly;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private TurnManager turnManager;
    private Tablero tablero;
    private boolean juegoActivo;
    private int turnoActual;
    private Jugador ganador;
    private String ultimoEvento;
    private boolean modoAutomatico;
    private PurchaseDecisionStrategy purchaseDecisionStrategy;

    public GameEngine() {
        turnManager = new TurnManager();
        tablero = new Tablero();
        juegoActivo = false;
        turnoActual = 0;
        ganador = null;
        ultimoEvento = "";
        modoAutomatico = true;
        purchaseDecisionStrategy = (jugador, propiedadCasilla) -> true;
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
        System.out.println(
            "Iniciando juego con " + contarJugadores() + " jugadores\n"
        );

        while (juegoActivo) {
            String evento = avanzarTurno();
            if (evento != null && !evento.isBlank()) {
                System.out.println(evento);
            }
        }

        if (ganador != null) {
            System.out.println("\n========== FIN DEL JUEGO ==========");
            System.out.println(
                "¡" +
                    ganador.getNombre() +
                    " gana con $" +
                    ganador.getDinero() +
                    "!"
            );
        }
    }

    public String avanzarTurno() {
        if (!juegoActivo) {
            return construirMensajeJuegoInactivo();
        }

        if (contarJugadoresActivos() <= 1) {
            finalizarJuego();
            return ultimoEvento;
        }

        Jugador jugadorActual = turnManager.siguiente();
        if (jugadorActual == null) {
            finalizarJuegoSinGanador(
                "No hay jugadores disponibles para continuar."
            );
            return ultimoEvento;
        }

        turnoActual++;
        DiceRoll tirada = DiceUtils.lanzarTirada();
        StringBuilder evento = construirEventoTurno(jugadorActual, tirada);

        if (!jugadorActual.estaBancarro()) {
            procesarTurnoJugador(jugadorActual, tirada, evento);
            manejarReinserccionJugador(jugadorActual, tirada);
        } else {
            evento
                .append("\n")
                .append(jugadorActual.getNombre())
                .append(" está en bancarrota y es eliminado!");
        }

        verificarFinDeJuego(evento);

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

    public void setModoAutomatico(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
    }

    public void setPurchaseDecisionStrategy(
        PurchaseDecisionStrategy purchaseDecisionStrategy
    ) {
        this.purchaseDecisionStrategy = purchaseDecisionStrategy;
    }

    private String construirMensajeJuegoInactivo() {
        if (ganador != null) {
            return (
                "La partida ya finalizó. Ganador: " +
                ganador.getNombre() +
                " con $" +
                ganador.getDinero()
            );
        }
        return "La partida no ha iniciado.";
    }

    private StringBuilder construirEventoTurno(
        Jugador jugador,
        DiceRoll tirada
    ) {
        StringBuilder evento = new StringBuilder();
        evento
            .append("--- TURNO ")
            .append(turnoActual)
            .append(" --- ")
            .append(jugador.getNombre())
            .append(" (Dinero: $")
            .append(jugador.getDinero())
            .append(")");
        evento
            .append("\n")
            .append(jugador.getNombre())
            .append(" lanzó: ")
            .append(tirada.getDado1())
            .append(" + ")
            .append(tirada.getDado2())
            .append(" = ")
            .append(tirada.getTotal());
        return evento;
    }

    private void procesarTurnoJugador(
        Jugador jugador,
        DiceRoll tirada,
        StringBuilder evento
    ) {
        if (jugador.estaEnCarcel()) {
            procesarTurnoEnCarcel(jugador, tirada, evento);
        } else {
            moverYProcesarCasilla(jugador, tirada.getTotal(), evento);
        }
    }

    private void procesarTurnoEnCarcel(
        Jugador jugador,
        DiceRoll tirada,
        StringBuilder evento
    ) {
        jugador.incrementarTurnoEnCarcel();

        if (tirada.esDoble()) {
            jugador.salirDeCarcel();
            evento
                .append("\n")
                .append(jugador.getNombre())
                .append(" sacó dobles y salió de la cárcel.");
            moverYProcesarCasilla(jugador, tirada.getTotal(), evento);
        } else {
            evento
                .append("\n")
                .append(jugador.getNombre())
                .append(" no sacó dobles y pierde el turno en cárcel.");

            if (jugador.getTurnosEnCarcel() >= 2) {
                jugador.salirDeCarcel();
                evento
                    .append("\n")
                    .append(jugador.getNombre())
                    .append(
                        " cumple turnos mínimos y sale de la cárcel para el próximo turno."
                    );
            }
        }
    }

    private void moverYProcesarCasilla(
        Jugador jugador,
        int dados,
        StringBuilder evento
    ) {
        int posicionAnterior = jugador.getPosicion();
        jugador.mover(dados);
        evento
            .append("\n")
            .append(jugador.getNombre())
            .append(" se movió de ")
            .append(posicionAnterior)
            .append(" a ")
            .append(jugador.getPosicion());

        Casilla casilla = tablero.getCasilla(jugador.getPosicion());
        boolean comprar = shouldBuyProperty(jugador, casilla);
        String efecto = tablero.procesarCasilla(jugador, comprar);

        if (efecto != null && !efecto.isBlank()) {
            evento.append("\n").append(efecto);
        }
    }

    private void manejarReinserccionJugador(Jugador jugador, DiceRoll tirada) {
        if (!jugador.estaEnCarcel() && tirada.esDoble()) {
            turnManager.reinsertarAlFrente(jugador);
        } else {
            turnManager.reinsertar(jugador);
        }
    }

    private void verificarFinDeJuego(StringBuilder evento) {
        if (contarJugadoresActivos() <= 1) {
            finalizarJuego();
            if (ganador != null) {
                evento
                    .append("\n")
                    .append("Ganador: ")
                    .append(ganador.getNombre())
                    .append(" con $")
                    .append(ganador.getDinero());
            }
        }
    }

    private boolean shouldBuyProperty(Jugador jugador, Casilla casilla) {
        if (!(casilla instanceof PropiedadCasilla propiedadCasilla)) {
            return false;
        }

        if (propiedadCasilla.getPropiedad().tieneDuenio()) {
            return false;
        }

        if (modoAutomatico) {
            return true;
        }

        return (
            purchaseDecisionStrategy != null &&
            purchaseDecisionStrategy.shouldBuy(jugador, propiedadCasilla)
        );
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

        ultimoEvento =
            ganador != null
                ? "La partida terminó. Ganador: " +
                  ganador.getNombre() +
                  " con $" +
                  ganador.getDinero()
                : "La partida terminó sin un ganador definido.";
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
