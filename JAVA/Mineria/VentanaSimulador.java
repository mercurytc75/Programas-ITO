package Mineria;

/**
 * Compatibilidad con el nombre anterior de la ventana.
 * Ahora reutiliza la interfaz nueva de juego.
 */
public class VentanaSimulador extends VentanaJuego {
    public VentanaSimulador() {
        super();
    }

    public VentanaSimulador(int robotsIniciales) {
        super(robotsIniciales);
    }
}
