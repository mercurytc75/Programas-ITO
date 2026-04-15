package Mineria;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Tienda: gestiona compra de robots y mejoras de edificios
 * Integrada con GameEngine para descuento de dinero
 */
public class Tienda {

    private GameEngine gameEngine;
    private List<ItemCompra> historialCompras;

    // Precios de robots
    public static final int PRECIO_ROBOT_NIVEL_1 = 500;
    public static final int PRECIO_ROBOT_NIVEL_2 = 1200;
    public static final int PRECIO_ROBOT_NIVEL_3 = 2500;

    // Precios de mejoras
    public static final int PRECIO_MEJORA_FUNDIDORA = 800;
    public static final int PRECIO_MEJORA_BODEGA = 600;
    public static final int PRECIO_RECARGA = 50;
    public static final int PRECIO_REPARACION = 200;

    // Mejoras acumuladas
    private int nivelFundidora;
    private int nivelBodega;
    private int totalRobotComprados;
    private int totalDineroGastado;

    public Tienda(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.historialCompras = new ArrayList<>();
        this.nivelFundidora = 1;
        this.nivelBodega = 1;
        this.totalRobotComprados = 0;
        this.totalDineroGastado = 0;
    }

    /**
     * FASE 4: Comprar un robot de nivel específico
     * @param nivel 1, 2 o 3
     * @return true si se compró exitosamente
     */
    public synchronized boolean comprarRobot(int nivel) {
        if (nivel < 1 || nivel > 3) {
            System.out.println(
                "❌ [TIENDA] Nivel de robot inválido. Disponibles: 1, 2, 3"
            );
            return false;
        }

        int costo = obtenerCostoRobot(nivel);
        int dineroDisponible = gameEngine.getDinero();

        if (dineroDisponible < costo) {
            int falta = costo - dineroDisponible;
            System.out.printf(
                "❌ [TIENDA] Dinero insuficiente. Tienes $%d, falta $%d%n",
                dineroDisponible,
                falta
            );
            return false;
        }

        // Descontar dinero
        gameEngine.descuentoDinero(costo);

        // Crear robot en GameEngine
        int idRobot = gameEngine.getRobots().size() + 1;
        gameEngine.crearRobotPlus(idRobot, nivel);

        totalRobotComprados++;
        totalDineroGastado += costo;

        String descripcion = String.format(
            "Robot Nivel %d - $%d",
            nivel,
            costo
        );
        historialCompras.add(new ItemCompra("Robot", descripcion, costo));

        System.out.printf(
            "✅ [TIENDA] Robot #%d (Nivel %d) comprado por $%d%n",
            idRobot,
            nivel,
            costo
        );
        System.out.printf("💰 Dinero restante: $%d%n", gameEngine.getDinero());

        return true;
    }

    /**
     * FASE 4: Mejorar la fundidora (aumenta velocidad, reduce calor)
     * @return true si se mejoró exitosamente
     */
    public synchronized boolean mejorarFundidora() {
        int costo = PRECIO_MEJORA_FUNDIDORA;
        int dineroDisponible = gameEngine.getDinero();

        if (dineroDisponible < costo) {
            int falta = costo - dineroDisponible;
            System.out.printf(
                "❌ [TIENDA] Dinero insuficiente para mejora de fundidora. Falta $%d%n",
                falta
            );
            return false;
        }

        // Descontar dinero
        gameEngine.descuentoDinero(costo);

        // Aplicar mejora en fundidora
        Fundidora fundidora = gameEngine.getFundidora();
        // Reducir velocidad de subida de calor (más lento en subir)
        fundidora.reducirVelocidadCalor();

        nivelFundidora++;
        totalDineroGastado += costo;

        String descripcion = String.format(
            "Mejora Fundidora Nivel %d - $%d",
            nivelFundidora,
            costo
        );
        historialCompras.add(
            new ItemCompra("Mejora Fundidora", descripcion, costo)
        );

        System.out.printf(
            "✅ [TIENDA] Fundidora mejorada a Nivel %d por $%d%n",
            nivelFundidora,
            costo
        );
        System.out.printf("   → Procesamiento más eficiente%n");
        System.out.printf("💰 Dinero restante: $%d%n", gameEngine.getDinero());

        return true;
    }

    /**
     * FASE 4: Mejorar la bodega (aumenta capacidad)
     * @return true si se mejoró exitosamente
     */
    public synchronized boolean mejorarBodega() {
        int costo = PRECIO_MEJORA_BODEGA;
        int dineroDisponible = gameEngine.getDinero();

        if (dineroDisponible < costo) {
            int falta = costo - dineroDisponible;
            System.out.printf(
                "❌ [TIENDA] Dinero insuficiente para mejora de bodega. Falta $%d%n",
                falta
            );
            return false;
        }

        // Descontar dinero
        gameEngine.descuentoDinero(costo);

        // Aplicar mejora en bodega (aumentar capacidad en 50 unidades)
        Bodega bodega = gameEngine.getBodega();
        int capacidadActual = bodega.getCapacidadMaxima();
        bodega.setCapacidadMaxima(capacidadActual + 50);

        nivelBodega++;
        totalDineroGastado += costo;

        String descripcion = String.format(
            "Mejora Bodega Nivel %d +50 cap - $%d",
            nivelBodega,
            costo
        );
        historialCompras.add(
            new ItemCompra("Mejora Bodega", descripcion, costo)
        );

        System.out.printf(
            "✅ [TIENDA] Bodega mejorada a Nivel %d por $%d%n",
            nivelBodega,
            costo
        );
        System.out.printf(
            "   → Capacidad: %d unidades%n",
            capacidadActual + 50
        );
        System.out.printf("💰 Dinero restante: $%d%n", gameEngine.getDinero());

        return true;
    }

    /**
     * Obtiene el costo de un robot según nivel
     */
    public static int obtenerCostoRobot(int nivel) {
        return switch (nivel) {
            case 1 -> PRECIO_ROBOT_NIVEL_1;
            case 2 -> PRECIO_ROBOT_NIVEL_2;
            case 3 -> PRECIO_ROBOT_NIVEL_3;
            default -> PRECIO_ROBOT_NIVEL_1;
        };
    }

    /**
     * Obtiene descripción de ítem para compra
     */
    public static String getDescripcionItem(String tipo) {
        return switch (tipo) {
            case "ROBOT_1" -> String.format(
                "Robot Nivel 1 - $%d (lento, barato)",
                PRECIO_ROBOT_NIVEL_1
            );
            case "ROBOT_2" -> String.format(
                "Robot Nivel 2 - $%d (normal)",
                PRECIO_ROBOT_NIVEL_2
            );
            case "ROBOT_3" -> String.format(
                "Robot Nivel 3 - $%d (rápido, caro)",
                PRECIO_ROBOT_NIVEL_3
            );
            case "FUNDIDORA" -> String.format(
                "Mejora Fundidora - $%d (procesa más rápido)",
                PRECIO_MEJORA_FUNDIDORA
            );
            case "BODEGA" -> String.format(
                "Mejora Bodega - $%d (+50 capacidad)",
                PRECIO_MEJORA_BODEGA
            );
            default -> "Artículo desconocido";
        };
    }

    /**
     * Obtiene menú disponible en la tienda
     */
    public String obtenerMenuTienda() {
        StringBuilder menu = new StringBuilder();
        menu.append("\n╔════════════════════════════════════════╗\n");
        menu.append("║          TIENDA DE MINETRUSH           ║\n");
        menu.append("╚════════════════════════════════════════╝\n");
        menu.append(
            String.format(
                "💰 Dinero disponible: $%d\n\n",
                gameEngine.getDinero()
            )
        );

        menu.append("┌─── ROBOTS ───┐\n");
        menu.append(
            String.format(
                "1. Robot Nivel 1 - $%d (Lento, barato)\n",
                PRECIO_ROBOT_NIVEL_1
            )
        );
        menu.append(
            String.format(
                "2. Robot Nivel 2 - $%d (Normal)\n",
                PRECIO_ROBOT_NIVEL_2
            )
        );
        menu.append(
            String.format(
                "3. Robot Nivel 3 - $%d (Rápido, caro)\n",
                PRECIO_ROBOT_NIVEL_3
            )
        );

        menu.append("\n┌─── MEJORAS ───┐\n");
        menu.append(
            String.format(
                "4. Mejorar Fundidora (Nivel %d) - $%d\n",
                nivelFundidora,
                PRECIO_MEJORA_FUNDIDORA
            )
        );
        menu.append(
            String.format(
                "5. Mejorar Bodega (Nivel %d) - $%d\n",
                nivelBodega,
                PRECIO_MEJORA_BODEGA
            )
        );

        menu.append("\n┌─── INFO ───┐\n");
        menu.append(
            String.format("Robots comprados: %d\n", totalRobotComprados)
        );
        menu.append(String.format("Dinero gastado: $%d\n", totalDineroGastado));
        menu.append("│ 0. Cerrar tienda\n");

        return menu.toString();
    }

    /**
     * Clase interna para registrar compras
     */
    public static class ItemCompra {

        String categoria;
        String descripcion;
        int costo;
        long timestamp;

        public ItemCompra(String categoria, String descripcion, int costo) {
            this.categoria = categoria;
            this.descripcion = descripcion;
            this.costo = costo;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return String.format("[%s] %s", categoria, descripcion);
        }
    }

    // Getters
    public int getNivelFundidora() {
        return nivelFundidora;
    }

    public int getNivelBodega() {
        return nivelBodega;
    }

    public int getTotalRobotComprados() {
        return totalRobotComprados;
    }

    public int getTotalDineroGastado() {
        return totalDineroGastado;
    }

    public List<ItemCompra> getHistorialCompras() {
        return new ArrayList<>(historialCompras);
    }
}
