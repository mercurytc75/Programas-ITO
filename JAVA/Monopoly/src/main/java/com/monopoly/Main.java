package main.java.com.monopoly;

import main.java.com.monopoly.model.Jugador;
import com.monopoly.model.Juego;

public class Main {

    public static void main(String[] args) {
        // Crear el juego
        Juego juego = new Juego();

        // Crear jugadores
        Jugador j1 = new Jugador(1, "Juan");
        Jugador j2 = new Jugador(2, "María");
        Jugador j3 = new Jugador(3, "Carlos");

        // Agregar jugadores al juego
        juego.agregarJugador(j1);
        juego.agregarJugador(j2);
        juego.agregarJugador(j3);

        // Iniciar el juego
        juego.iniciarJuego();
    }
}
