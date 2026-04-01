package com.monopoly.model.casillas;

import com.monopoly.model.Jugador;
import com.monopoly.model.Propiedad;

public class PropiedadCasilla extends Casilla {
    private Propiedad propiedad;

    public PropiedadCasilla(String nombre, int posicion, int precio, int renta) {
        super(nombre, posicion);
        this.propiedad = new Propiedad(nombre, precio, renta);
    }

    @Override
    public String efecto(Jugador jugador) {
        if (!propiedad.tieneDuenio()) {
            return jugador.getNombre() + " llegó a " + nombre + " (Precio: $" + propiedad.getPrecio() + ")";
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
