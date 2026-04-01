package com.monopoly;

import com.monopoly.model.Jugador;
import com.monopoly.model.Juego;
import com.monopoly.ui.MonopolyFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        if (args != null && args.length > 0 && "consola".equalsIgnoreCase(args[0])) {
            Juego juego = new Juego();

            Jugador j1 = new Jugador(1, "Juan");
            Jugador j2 = new Jugador(2, "María");
            Jugador j3 = new Jugador(3, "Carlos");

            juego.agregarJugador(j1);
            juego.agregarJugador(j2);
            juego.agregarJugador(j3);

            juego.iniciarJuego();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            MonopolyFrame frame = new MonopolyFrame();
            frame.setVisible(true);
        });
    }
}
