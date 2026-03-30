package main;

import model.Jugador;
import tda.Cola;

public class Main {

    public static void main(String[] args) {

        Cola<Jugador> turnos = new Cola<>();

        Jugador j1 = new Jugador(1, "Juan");
        Jugador j2 = new Jugador(2, "Maria");

        turnos.encolar(j1);
        turnos.encolar(j2);

        // Simular turnos
        for (int i = 0; i < 4; i++) {
            Jugador actual = turnos.desencolar();

            System.out.println("Turno de: " + actual.getNombre());

            turnos.encolar(actual);
        }
    }
}