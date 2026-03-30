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
    public void efecto(Jugador jugador) {
        if (!propiedad.tieneDuenio()) {
            System.out.println(jugador.getNombre() + " llegó a " + nombre + " (Precio: $" + propiedad.getPrecio() + ")");
        } else {
            Jugador duenio = propiedad.getDuenio();
            int renta = propiedad.getRenta();
            if (!duenio.equals(jugador)) {
                System.out.println(jugador.getNombre() + " debe pagar renta de $" + renta + " a " + duenio.getNombre());
                jugador.pagar(renta);
                duenio.recibir(renta);
            }
        }
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }
}
