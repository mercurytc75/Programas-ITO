package Mineria;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada del juego de minería.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaJuego ventana = new VentanaJuego();
            ventana.setVisible(true);
        });
    }
}
