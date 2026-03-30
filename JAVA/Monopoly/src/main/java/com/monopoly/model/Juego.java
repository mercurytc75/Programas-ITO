package com.monopoly.model;

import com.monopoly.tda.Cola;

public class Juego {
    private Cola<Jugador> jugadores;
    private Tablero tablero;
    private boolean juegoActivo;
    private int turnoActual;

    public Juego() {
        jugadores = new Cola<>();
        tablero = new Tablero();
        juegoActivo = true;
        turnoActual = 0;
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.encolar(jugador);
    }

    public void iniciarJuego() {
        System.out.println("========== MONOPOLY ==========");
        System.out.println("Iniciando juego con " + contarJugadores() + " jugadores\n");

        while (juegoActivo && contarJugadoresActivos() > 1) {
            turnoActual++;
            ejecutarTurno();
        }

        finalizarJuego();
    }

    private void ejecutarTurno() {
        Jugador jugadorActual = jugadores.descolar();
        System.out.println("\n--- TURNO " + turnoActual + " --- " + jugadorActual.getNombre() + " (Dinero: $" + jugadorActual.getDinero() + ")");

        if (!jugadorActual.estaBancarro()) {
            // Lanzar dados
            int dados = tablero.lanzarDados();
            System.out.println(jugadorActual.getNombre() + " lanzó: " + dados);

            // Mover jugador
            if (jugadorActual.estaEnCarcel()) {
                System.out.println(jugadorActual.getNombre() + " está en cárcel. Necesita 3 turnos para salir o pagar $50.");
                jugadorActual.salirDeCarcel();
            } else {
                jugadorActual.mover(dados);
                System.out.println(jugadorActual.getNombre() + " se movió a la posición " + jugadorActual.getPosicion());

                // Procesar la casilla
                tablero.procesarCasilla(jugadorActual);
            }
        } else {
            System.out.println(jugadorActual.getNombre() + " está en bancarrota y es eliminado!");
        }

        // Volver a encolar si no está en bancarrota
        if (!jugadorActual.estaBancarro()) {
            jugadores.encolar(jugadorActual);
        }
    }

    private int contarJugadores() {
        Cola<Jugador> temp = new Cola<>();
        int count = 0;

        while (!jugadores.estaVacia()) {
            Jugador j = jugadores.descolar();
            temp.encolar(j);
            count++;
        }

        while (!temp.estaVacia()) {
            jugadores.encolar(temp.descolar());
        }

        return count;
    }

    private int contarJugadoresActivos() {
        Cola<Jugador> temp = new Cola<>();
        int count = 0;

        while (!jugadores.estaVacia()) {
            Jugador j = jugadores.descolar();
            if (!j.estaBancarro()) {
                count++;
            }
            temp.encolar(j);
        }

        while (!temp.estaVacia()) {
            jugadores.encolar(temp.descolar());
        }

        return count;
    }

    private void finalizarJuego() {
        System.out.println("\n========== FIN DEL JUEGO ==========");
        Jugador ganador = jugadores.descolar();
        System.out.println("¡" + ganador.getNombre() + " gana con $" + ganador.getDinero() + "!");
    }
}
