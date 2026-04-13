package Monopoly;

public class Tablero {

    private Casilla[] casillas;
    public static final int TOTAL_CASILLAS = 40;

    public Tablero() {
        casillas = new Casilla[TOTAL_CASILLAS];
        inicializarTablero();
    }

    private void inicializarTablero() {
        // Casillas especiales
        casillas[0] = new Salida();
        casillas[4] = new Impuesto("Impuesto de Ingresos", 4, 200);
        casillas[10] = new Carcel();
        casillas[20] = new EstacionamientoLibre();
        casillas[30] = new Carcel(); // Vaya a la Carcel
        casillas[38] = new Impuesto("Impuesto de Lujo", 38, 75);

        inicializarPropiedadesMonopoly();

        // Fallback para posiciones no modeladas todavía.
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            if (casillas[i] == null) {
                int precio = 100 + (i % 10) * 20;
                int renta = Math.max(10, precio / 10);
                casillas[i] = new PropiedadCasilla(
                    "Lote " + i,
                    i,
                    precio,
                    renta
                );
            }
        }
    }

    private void inicializarPropiedadesMonopoly() {
        crearPropiedad(1, "Avenida Mediterranea", 60, 2);
        crearPropiedad(3, "Avenida Baltica", 60, 4);
        crearPropiedad(5, "Ferrocarril Reading", 200, 25);
        crearPropiedad(6, "Avenida Oriental", 100, 6);
        crearPropiedad(8, "Avenida Vermont", 100, 6);
        crearPropiedad(9, "Avenida Connecticut", 120, 8);
        crearPropiedad(11, "Plaza San Carlos", 140, 10);
        crearPropiedad(12, "Compania Electrica", 150, 12);
        crearPropiedad(13, "Avenida States", 140, 10);
        crearPropiedad(14, "Avenida Virginia", 160, 12);
        crearPropiedad(15, "Ferrocarril Pennsylvania", 200, 25);
        crearPropiedad(16, "Plaza St. James", 180, 14);
        crearPropiedad(18, "Avenida Tennessee", 180, 14);
        crearPropiedad(19, "Avenida Nueva York", 200, 16);
        crearPropiedad(21, "Avenida Kentucky", 220, 18);
        crearPropiedad(23, "Avenida Indiana", 220, 18);
        crearPropiedad(24, "Avenida Illinois", 240, 20);
        crearPropiedad(25, "Ferrocarril B&O", 200, 25);
        crearPropiedad(26, "Avenida Atlantico", 260, 22);
        crearPropiedad(27, "Avenida Ventnor", 260, 22);
        crearPropiedad(28, "Compania de Agua", 150, 12);
        crearPropiedad(29, "Jardines Marvin", 280, 24);
        crearPropiedad(31, "Avenida Pacifico", 300, 26);
        crearPropiedad(32, "Avenida Carolina del Norte", 300, 26);
        crearPropiedad(34, "Avenida Pennsylvania", 320, 28);
        crearPropiedad(35, "Ferrocarril Short Line", 200, 25);
        crearPropiedad(37, "Parque Place", 350, 35);
        crearPropiedad(39, "Paseo Tablado", 400, 50);
    }

    public int lanzarDados() {
        return DiceUtils.lanzarTirada().getTotal();
    }

    public Casilla getCasilla(int posicion) {
        return casillas[posicion];
    }

    public String procesarCasilla(Jugador jugador) {
        return procesarCasilla(jugador, false);
    }

    public String procesarCasilla(Jugador jugador, boolean comprarPropiedad) {
        int posicion = jugador.getPosicion();
        Casilla casilla = casillas[posicion];

        if (casilla != null) {
            if (casilla instanceof PropiedadCasilla propiedadCasilla) {
                return propiedadCasilla.efecto(jugador, comprarPropiedad);
            }

            return casilla.efecto(jugador);
        }

        return (
            jugador.getNombre() +
            " está en una propiedad sin dueño en la posición " +
            posicion
        );
    }

    public int getTotalCasillas() {
        return TOTAL_CASILLAS;
    }

    public String getNombreCasilla(int posicion) {
        Casilla casilla = casillas[posicion];

        if (casilla != null) {
            return casilla.getNombre();
        }

        return "Propiedad";
    }

    public void crearPropiedad(
        int posicion,
        String nombre,
        int precio,
        int renta
    ) {
        if (casillas[posicion] == null) {
            casillas[posicion] = new PropiedadCasilla(
                nombre,
                posicion,
                precio,
                renta
            );
        }
    }
}
