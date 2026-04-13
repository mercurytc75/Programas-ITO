package model.casillas;

import model.Jugador;
import model.Propiedad;

public class PropiedadCasilla extends Casilla {
    private Propiedad propiedad;

    public PropiedadCasilla(String nombre, int posicion, int precio, int renta) {
        super(nombre, posicion);
        this.propiedad = new Propiedad(nombre, precio, renta);
    }

    @Override
    public String efecto(Jugador jugador) {
        return efecto(jugador, false);
    }

    public String efecto(Jugador jugador, boolean comprarSiDisponible) {
        if (!propiedad.tieneDuenio()) {
            if (comprarSiDisponible) {
                if (jugador.getDinero() >= propiedad.getPrecio()) {
                    jugador.pagar(propiedad.getPrecio());
                    propiedad.setDuenio(jugador);
                    jugador.agregarPropiedad(propiedad);
                    return jugador.getNombre() + " compró " + nombre + " por $" + propiedad.getPrecio();
                }

                return jugador.getNombre() + " no tiene dinero suficiente para comprar " + nombre
                        + " (Precio: $" + propiedad.getPrecio() + ")";
            }

            return jugador.getNombre() + " llegó a " + nombre + " (Precio: $" + propiedad.getPrecio()
                    + ") y decidió no comprar";
        }

        Jugador duenio = propiedad.getDuenio();
        int renta = propiedad.getRenta();

        if (!duenio.equals(jugador)) {
            jugador.pagar(renta);
            duenio.recibir(renta);
            return jugador.getNombre() + " debe pagar renta de $" + renta + " a " + duenio.getNombre();
        }

        return jugador.getNombre() + " cayó en su propia propiedad: " + nombre;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }
}
