package Monopoly;

public class Impuesto extends Casilla {
    private final int monto;

    public Impuesto(String nombre, int posicion, int monto) {
        super(nombre, posicion);
        this.monto = monto;
    }

    @Override
    public String efecto(Jugador jugador) {
        jugador.pagar(monto);
        return jugador.getNombre() + " pagó " + nombre + ": $" + monto;
    }

    public int getMonto() {
        return monto;
    }
}
