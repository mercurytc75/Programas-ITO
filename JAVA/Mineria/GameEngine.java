package Mineria;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private List<Robot> robots;
    private List<ZonaMina> zonas;
    private Fundidora fundidora;
    private Bodega bodega;
    private GestorEventos gestorEventos; // FASE 2: Sistema de eventos
    private GestorContratos gestorContratos; // FASE 3: Sistema de contratos
    private Tienda tienda; // FASE 4: Sistema de tienda
    private boolean juegoActivo;
    private int dinero;
    private int rondaActual;
    private int rondasTotales;
    private long tiempoRondaMs;
    private long tiempoInicioRonda;

    private boolean resumenMostrado;
    private Thread threadRondas;
    private Thread threadEstadisticas;
    private Thread threadContratos;

    private static final int DINERO_INICIAL = 5000;
    private static final int RONDAS_TOTALES = 10;
    private static final long TIEMPO_RONDA_MS = 120000;

    private static final int PRECIO_ROBOT_NIVEL_1 = 500;
    private static final int PRECIO_ROBOT_NIVEL_2 = 1200;
    private static final int PRECIO_ROBOT_NIVEL_3 = 2500;
    private static final int PRECIO_RECARGA = 50;
    private static final int PRECIO_REPARACION = 200;

    public GameEngine(int cantidadRobots) {
        this.robots = new ArrayList<>();
        this.zonas = new ArrayList<>();
        this.fundidora = new Fundidora();
        this.bodega = new Bodega();
        this.juegoActivo = false;
        this.dinero = DINERO_INICIAL;
        this.rondaActual = 0;
        this.rondasTotales = RONDAS_TOTALES;
        this.tiempoRondaMs = TIEMPO_RONDA_MS;

        this.resumenMostrado = false;

        crearZonas();
        inicializarRobotsIniciales(cantidadRobots);

        // FASE 2: Inicializar gestor de eventos
        this.gestorEventos = new GestorEventos(robots, zonas, fundidora);

        // FASE 3: Inicializar gestor de contratos
        this.gestorContratos = new GestorContratos(bodega);

        // FASE 4: Inicializar tienda
        this.tienda = new Tienda(this);
    }

    private void inicializarRobotsIniciales(int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            ZonaMina zonaInicial = zonas.get(0);
            Robot robot = new Robot(i, 1, fundidora, bodega, zonaInicial);
            robots.add(robot);
            System.out.printf(
                "[INIT] Robot #%d listo — esperando inicio de partida%n",
                i
            );
        }
    }

    private void crearZonas() {
        zonas.add(new ZonaMina("A", "Cobre/Hierro", 1000));
        zonas.add(new ZonaMina("B", "Plata/Oro", 800));
        zonas.add(new ZonaMina("C", "Diamante", 500));
    }

    public synchronized boolean crearRobot(int id, int nivel) {
        int costo = obtenerCostoRobot(nivel);
        if (dinero < costo) {
            System.out.printf(
                "[ALERTA] Dinero insuficiente para comprar Robot Nivel %d (costo: $%d)%n",
                nivel,
                costo
            );
            return false;
        }
        dinero -= costo;
        ZonaMina zonaInicial = zonas.get(0);
        Robot robot = new Robot(id, nivel, fundidora, bodega, zonaInicial);
        robots.add(robot);
        robot.start();
        System.out.printf(
            "[OK] Robot #%d (Nivel %d) comprado y asignado a Zona %s%n",
            id,
            nivel,
            zonaInicial.getNombre()
        );
        return true;
    }

    private int obtenerCostoRobot(int nivel) {
        return switch (nivel) {
            case 1 -> PRECIO_ROBOT_NIVEL_1;
            case 2 -> PRECIO_ROBOT_NIVEL_2;
            case 3 -> PRECIO_ROBOT_NIVEL_3;
            default -> PRECIO_ROBOT_NIVEL_1;
        };
    }

    public void iniciar() {
        if (juegoActivo) {
            System.out.println("[ALERTA] El juego ya esta en curso");
            return;
        }
        juegoActivo = true;
        resumenMostrado = false;
        System.out.println("\n" + "=".repeat(70));
        System.out.println(
            "INICIANDO MINETRUSH - JUEGO DE MINERIA EN TIEMPO REAL"
        );
        System.out.println("=".repeat(70));
        System.out.printf("Dinero inicial: $%d%n", dinero);
        System.out.printf("Robots activos: %d%n", robots.size());
        System.out.printf(
            "Rondas: %d (2 min cada una = 20 min totales)%n",
            rondasTotales
        );
        System.out.println(
            "Zonas disponibles: Zona A (Cobre/Hierro), Zona B (Plata/Oro), Zona C (Diamante)"
        );
        System.out.println("=".repeat(70) + "\n");

        for (Robot robot : robots) {
            if (!robot.isAlive()) {
                robot.start();
            }
        }

        // FASE 2: Iniciar gestor de eventos
        gestorEventos.iniciar();

        // FASE 3: Iniciar gestor de contratos
        gestorContratos.iniciar();

        threadRondas = new Thread(this::manejarRondas, "Rondas");
        threadRondas.start();
        threadEstadisticas = new Thread(
            this::mostrarEstadisticas,
            "Estadisticas"
        );
        threadEstadisticas.start();
        threadContratos = new Thread(
            this::bucleEvaluacionContratos,
            "EvaluadorContratos"
        );
        threadContratos.start();
    }

    private void manejarRondas() {
        for (int r = 1; r <= rondasTotales; r++) {
            if (!juegoActivo) break;
            rondaActual = r;
            tiempoInicioRonda = System.currentTimeMillis();
            System.out.printf(
                "%n[NOTICIA] RONDA %d/%d INICIADA%n",
                r,
                rondasTotales
            );
            System.out.printf(
                "Dinero: $%d | Robots activos: %d%n",
                dinero,
                contarRobotsActivos()
            );
            try {
                Thread.sleep(tiempoRondaMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            System.out.printf("[NOTICIA] RONDA %d FINALIZADA%n", r);
            if (
                dinero <= 0 ||
                (contarRobotsActivos() == 0 && dinero < PRECIO_ROBOT_NIVEL_1)
            ) {
                finalizarJuego(
                    "DERROTA",
                    "Te quedaste sin dinero y sin robots para continuar"
                );
                return;
            }
        }
        if (dinero > 0) {
            finalizarJuego(
                "VICTORIA",
                "Completaste todas las rondas con ganancia"
            );
        } else {
            finalizarJuego("DERROTA", "Terminaste sin dinero");
        }
    }

    public synchronized boolean recargarRobot(int robotId) {
        Robot robot = robots
            .stream()
            .filter(r -> r.getRobotId() == robotId)
            .findFirst()
            .orElse(null);
        if (robot == null) {
            System.out.println("[ALERTA] Robot no encontrado");
            return false;
        }
        if (dinero < PRECIO_RECARGA) {
            System.out.printf(
                "[ALERTA] Dinero insuficiente para recargar Robot #%d (costo: $%d)%n",
                robotId,
                PRECIO_RECARGA
            );
            return false;
        }
        dinero -= PRECIO_RECARGA;
        robot.recargarEnergia(100);
        System.out.printf(
            "[OK] Robot #%d recargado. Dinero restante: $%d%n",
            robotId,
            dinero
        );
        return true;
    }

    public synchronized boolean repararRobot(int robotId) {
        Robot robot = robots
            .stream()
            .filter(r -> r.getRobotId() == robotId)
            .findFirst()
            .orElse(null);
        if (robot == null) {
            System.out.println("[ALERTA] Robot no encontrado");
            return false;
        }
        if (robot.getEstado() != Robot.Estado.ROTO) {
            System.out.printf(
                "[ALERTA] Robot #%d no está ROTO (estado: %s) — reparación cancelada%n",
                robotId,
                robot.getEstado()
            );
            return false;
        }
        if (dinero < PRECIO_REPARACION) {
            System.out.printf(
                "[ALERTA] Dinero insuficiente para reparar Robot #%d (costo: $%d)%n",
                robotId,
                PRECIO_REPARACION
            );
            return false;
        }
        dinero -= PRECIO_REPARACION;
        robot.repararRobot();
        System.out.printf(
            "[OK] Robot #%d reparado. Dinero restante: $%d%n",
            robotId,
            dinero
        );
        return true;
    }

    public synchronized void asignarRobotAZona(int robotId, String nombreZona) {
        Robot robot = robots
            .stream()
            .filter(r -> r.getRobotId() == robotId)
            .findFirst()
            .orElse(null);
        ZonaMina zona = zonas
            .stream()
            .filter(z -> z.getNombre().equals(nombreZona))
            .findFirst()
            .orElse(null);
        if (robot != null && zona != null) {
            robot.setZona(zona);
            System.out.printf(
                "[OK] Robot #%d asignado a Zona %s%n",
                robotId,
                nombreZona
            );
        }
    }

    public synchronized void enfriarFundidora() {
        // FASE 2: Usar el nuevo método enfriar() que maneja el dinero en Fundidora
        if (fundidora.enfriar(dinero)) {
            dinero -= Fundidora.COSTO_ENFRIAR;
            System.out.printf(
                "[OK] Fundidora enfriada. Dinero restante: $%d%n",
                dinero
            );
        } else {
            System.out.println(
                "[ALERTA] Dinero insuficiente para enfriar fundidora (costo: $" +
                    Fundidora.COSTO_ENFRIAR +
                    ")"
            );
        }
    }

    public synchronized int venderMinerales() {
        // FASE 2: Usar los nuevos métodos de venta con precios reales
        int ingresoTotal = 0;

        if (bodega.isCompradorEspecialActivo()) {
            ingresoTotal = bodega.venderConCompradorEspecial();
        } else {
            ingresoTotal = bodega.venderAhora();
        }

        dinero += ingresoTotal;
        System.out.printf("[VENTA] Dinero total ahora: $%d%n", dinero);
        return ingresoTotal;
    }

    private void bucleEvaluacionContratos() {
        while (juegoActivo) {
            try {
                Thread.sleep(5000);
                if (!juegoActivo) break;
                int delta = gestorContratos.evaluarContratos(dinero);
                if (delta != 0) {
                    synchronized (this) {
                        dinero += delta;
                    }
                    if (delta > 0) System.out.printf(
                        "[CONTRATOS] +$%d ganados por contrato completado%n",
                        delta
                    );
                    else System.out.printf(
                        "[CONTRATOS] -$%d penalización por contrato vencido%n",
                        -delta
                    );
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void mostrarEstadisticas() {
        while (juegoActivo) {
            try {
                Thread.sleep(5000);
                if (!juegoActivo) break;
                long tiempoTranscurrido =
                    System.currentTimeMillis() - tiempoInicioRonda;
                long tiempoRestante = Math.max(
                    0,
                    tiempoRondaMs - tiempoTranscurrido
                );
                int segundosRestantes = (int) (tiempoRestante / 1000);
                System.out.println("\n========== ESTADO DEL JUEGO ==========");
                System.out.printf(
                    "[DINERO] $%-6d | [RONDA] %d/%d | [TIEMPO] %02d:%02d seg%n",
                    dinero,
                    rondaActual,
                    rondasTotales,
                    segundosRestantes / 60,
                    segundosRestantes % 60
                );
                System.out.printf(
                    "[FUNDIDORA] %d refinados | Calor: %d%% | [BODEGA] %d/%d minerales%n",
                    fundidora.getMineralRefinado(),
                    fundidora.getNivelCalor(),
                    bodega.getCantidadAlmacenada(),
                    bodega.getCapacidadMaxima()
                );
                System.out.println("--- ROBOTS ACTIVOS ---");
                for (Robot robot : robots) {
                    System.out.printf(
                        "[Robot #%-2d] Nivel: %d | Energia: %-3d%% | Zona: %s | Estado: %s%n",
                        robot.getRobotId(),
                        robot.getNivel(),
                        robot.getEnergia(),
                        robot.getZonaAsignada(),
                        robot.getEstado()
                    );
                }
                System.out.println("-- Zonas disponibles --");
                for (ZonaMina zona : zonas) {
                    String estado = zona.isBloqueada()
                        ? "BLOQUEADA"
                        : "Disponible";
                    System.out.printf(
                        "Zona %s: %s - Reserva: %d/%d (%d%%)%n",
                        zona.getNombre(),
                        estado,
                        zona.getReserva(),
                        zona.getReservaMaxima(),
                        zona.getPorcentajeReserva()
                    );
                }

                // FASE 3: Mostrar contratos activos
                List<Contrato> contratos =
                    gestorContratos.getContratosActivos();
                if (!contratos.isEmpty()) {
                    System.out.println("-- Contratos activos --");
                    for (Contrato c : contratos) {
                        String aceptado = c.isAceptado()
                            ? "ACEPTADO"
                            : "PENDIENTE";
                        System.out.printf(
                            "[Contrato #%d] %s - %s %s%n",
                            c.getId(),
                            aceptado,
                            c.getProgressBar(),
                            c.getTipoMineral()
                        );
                    }
                }

                System.out.println("====================================\n");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int contarRobotsActivos() {
        return (int) robots
            .stream()
            .filter(r -> r.isActivo() && r.getEstado() == Robot.Estado.ACTIVO)
            .count();
    }

    private synchronized void finalizarJuego(String resultado, String motivo) {
        if (resumenMostrado || !juegoActivo) return;
        juegoActivo = false;
        resumenMostrado = true;

        // FASE 2: Detener gestor de eventos
        gestorEventos.detener();

        // FASE 3: Detener gestor de contratos
        gestorContratos.detener();

        for (Robot robot : robots) {
            robot.detener();
            robot.interrupt();
        }
        esperarTerminacion();
        System.out.println("\n" + "=".repeat(50));
        System.out.printf("[%s] %s%n", resultado, motivo);
        System.out.println("=".repeat(50));
        System.out.printf("Dinero final: $%d%n", dinero);
        System.out.printf(
            "Ronda completada: %d/%d%n",
            rondaActual,
            rondasTotales
        );
        System.out.printf("Robots activos: %d%n", contarRobotsActivos());
        System.out.printf(
            "Minerales totales refinados: %d%n",
            fundidora.getMineralRefinado()
        );
        System.out.printf(
            "Contratos completados: %d | Total ganado: $%d | Total perdido: -$%d%n",
            gestorContratos.getContratosCompletados().size(),
            gestorContratos.getTotalGanado(),
            gestorContratos.getTotalPerdido()
        );
        System.out.println("=".repeat(50) + "\n");
    }

    private void esperarTerminacion() {
        for (Robot robot : robots) {
            try {
                robot.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void detener() {
        juegoActivo = false;
        gestorEventos.detener();
        gestorContratos.detener();
        for (Robot robot : robots) {
            robot.detener();
            robot.interrupt();
        }
        if (threadRondas != null) threadRondas.interrupt();
        if (threadEstadisticas != null) threadEstadisticas.interrupt();
        if (threadContratos != null) threadContratos.interrupt();
        System.out.println("[GameEngine] Juego detenido completamente.");
    }
    
    public void reanudarJuego() {
        juegoActivo = true;
        gestorEventos.reanudar();
        gestorContratos.reanudar();
        for (Robot robot : robots) {
            robot.reanudar();
        }
        if (threadRondas != null) threadRondas.interrupt();
        if (threadEstadisticas != null) threadEstadisticas.interrupt();
        if (threadContratos != null) threadContratos.interrupt();
        System.out.println("[GameEngine] Juego reanudado completamente.");
    }

    // FASE 3: Métodos para interacción con contratos
    public synchronized boolean aceptarContrato(int idContrato) {
        return gestorContratos.aceptarContrato(idContrato);
    }

    public synchronized void rechazarContrato(int idContrato) {
        gestorContratos.rechazarContrato(idContrato);
    }

    public synchronized List<Contrato> getContratosActivos() {
        return gestorContratos.getContratosActivos();
    }

    // FASE 4: Métodos para interacción con tienda
    public synchronized void descuentoDinero(int cantidad) {
        this.dinero -= cantidad;
        if (this.dinero < 0) {
            this.dinero = 0;
        }
    }

    public synchronized void agregarDinero(int cantidad) {
        this.dinero += cantidad;
    }

    public synchronized boolean crearRobotPlus(int id, int nivel) {
        ZonaMina zonaInicial = zonas.get(0);
        Robot robot = new Robot(id, nivel, fundidora, bodega, zonaInicial);
        robots.add(robot);
        robot.start();
        System.out.printf(
            "[OK] Robot #%d (Nivel %d) agregado a la flota%n",
            id,
            nivel
        );
        return true;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public synchronized int getDinero() {
        return dinero;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public int getRondasTotales() {
        return rondasTotales;
    }

    public List<Robot> getRobots() {
        return new ArrayList<>(robots);
    }

    public List<ZonaMina> getZonas() {
        return new ArrayList<>(zonas);
    }

    public Fundidora getFundidora() {
        return fundidora;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public boolean isJuegoActivo() {
        return juegoActivo;
    }
}
