package Mineria;

import javax.swing.SwingUtilities;

/**
 * Clase Main: Punto de entrada único para el Simulador de Minería
 * Lanza la interfaz gráfica (VentanaSimulador) de forma segura en el Event Dispatch Thread
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaSimulador ventana = new VentanaSimulador();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
