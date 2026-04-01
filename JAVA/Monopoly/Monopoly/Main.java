package Monopoly;

import controller.GameController;
import model.Jugador;
import ui.MonopolyFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        if (args != null && args.length > 0 && "consola".equalsIgnoreCase(args[0])) {
            GameController gameController = new GameController();

            Jugador j1 = new Jugador(1, "Juan");
            Jugador j2 = new Jugador(2, "María");
            Jugador j3 = new Jugador(3, "Carlos");

            gameController.agregarJugador(j1);
            gameController.agregarJugador(j2);
            gameController.agregarJugador(j3);

            gameController.iniciarJuego();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            MonopolyFrame frame = new MonopolyFrame();
            frame.setVisible(true);
        });
    }
}
