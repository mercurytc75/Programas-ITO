package Mineria;

import javax.swing.SwingUtilities;

/**
 * Clase Main: Punto de entrada único para el Simulador de Minería
 * Lanza la interfaz gráfica (VentanaSimulador) de forma segura en el Event Dispatch Thread
 */
public class Main {
    public static void main(String[] args) {

        /**
         * damos inicio  a nuestro programa parece que ya no tiene erorres y pues vemos 
         * mas adelante :v
         */
        SwingUtilities.invokeLater(() -> {
            VentanaSimulador ventana = new VentanaSimulador();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
