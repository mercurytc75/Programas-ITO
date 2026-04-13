package Monopoly;

public class PropiedadCasilla extends Casilla {

    private Propiedad propiedad;

    public PropiedadCasilla(
        String nombre,
        int posicion,
        int precio,
        int renta
    ) {
        super(nombre, posicion);
        this.propiedad = new Propiedad(nombre, precio, renta);
    }

    @Override
    public String efecto(Jugador jugador) {
        return efecto(jugador, false);
    }

    public String efecto(Jugador jugador, boolean comprarSiDisponible) {
        if (!propiedad.tieneDuenio()) {
            return procesarPropiedadSinDuenio(jugador, comprarSiDisponible);
        }

        return procesarPropiedadConDuenio(jugador);
    }

    private String procesarPropiedadSinDuenio(
        Jugador jugador,
        boolean comprarSiDisponible
    ) {
        if (!comprarSiDisponible) {
            return String.format(
                "%s llegó a %s (Precio: $%d) y decidió no comprar",
                jugador.getNombre(),
                nombre,
                propiedad.getPrecio()
            );
        }

        return intentarComprarPropiedad(jugador);
    }

    private String intentarComprarPropiedad(Jugador jugador) {
        int precio = propiedad.getPrecio();

        if (jugador.getDinero() < precio) {
            return String.format(
                "%s no tiene dinero suficiente para comprar %s (Precio: $%d)",
                jugador.getNombre(),
                nombre,
                precio
            );
        }

        jugador.pagar(precio);
        propiedad.setDuenio(jugador);
        jugador.agregarPropiedad(propiedad);

        return String.format(
            "%s compró %s por $%d",
            jugador.getNombre(),
            nombre,
            precio
        );
    }

    private String procesarPropiedadConDuenio(Jugador jugador) {
        Jugador duenio = propiedad.getDuenio();

        if (duenio.equals(jugador)) {
            return String.format(
                "%s cayó en su propia propiedad: %s",
                jugador.getNombre(),
                nombre
            );
        }

        return procesarPagoDeRenta(jugador, duenio);
    }

    private String procesarPagoDeRenta(Jugador jugador, Jugador duenio) {
        int renta = propiedad.getRenta();
        jugador.pagar(renta);
        duenio.recibir(renta);

        return String.format(
            "%s debe pagar renta de $%d a %s",
            jugador.getNombre(),
            renta,
            duenio.getNombre()
        );
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }
}
