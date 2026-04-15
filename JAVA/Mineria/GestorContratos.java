package Mineria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Clase GestorContratos: genera contratos aleatorios durante el juego
 * Administra contratos activos y evalúa cumplimiento
 */
public class GestorContratos {

    private List<Contrato> contratosActivos;
    private List<Contrato> contratosCompletados;
    private Random random;
    private boolean activo;
    private int contadorId;
    private Bodega bodega;

    // Configuración de contratos
    private static final int MAX_CONTRATOS_ACTIVOS = 2;
    private static final int INTERVALO_GENERACION_MIN = 30000; // 30 segundos
    private static final int INTERVALO_GENERACION_MAX = 90000; // 90 segundos

    // Tipos de contratos disponibles
    private static final String[] TIPOS_MINERALES = {
        "Cobre",
        "Hierro",
        "Plata",
        "Oro",
        "Diamante",
    };
    private static final int[] CANTIDADES = { 5, 10, 15, 20, 25 };
    private static final int[] TIEMPOS = { 30, 45, 60, 75, 90 };
    private static final int[] RECOMPENSAS = { 500, 750, 1000, 1500, 2000 };
    private static final int PENALIZACION_BASE = 300;

    public GestorContratos(Bodega bodega) {
        this.contratosActivos = new ArrayList<>();
        this.contratosCompletados = new ArrayList<>();
        this.random = new Random();
        this.bodega = bodega;
        this.activo = false;
        this.contadorId = 1;
    }

    /**
     * Inicia el generador de contratos aleatorios
     */
    public void iniciar() {
        if (activo) {
            System.out.println("[GestorContratos] Ya está activo");
            return;
        }

        activo = true;
        new Thread(this::bucleGeneracion, "GestorContratos").start();
    }

    /**
     * Bucle principal de generación de contratos
     */
    private void bucleGeneracion() {
        System.out.println("[GestorContratos] Sistema de contratos iniciado");

        while (activo) {
            try {
                // Esperar tiempo aleatorio entre génesis
                int tiempoEspera =
                    INTERVALO_GENERACION_MIN +
                    random.nextInt(
                        INTERVALO_GENERACION_MAX - INTERVALO_GENERACION_MIN
                    );
                Thread.sleep(tiempoEspera);

                if (!activo) break;

                // Generar contrato si hay espacio
                if (contratosActivos.size() < MAX_CONTRATOS_ACTIVOS) {
                    generarContratoAleatorio();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Genera un contrato aleatorio
     */
    private synchronized void generarContratoAleatorio() {
        String tipoMineral = TIPOS_MINERALES[random.nextInt(
            TIPOS_MINERALES.length
        )];
        int cantidad = CANTIDADES[random.nextInt(CANTIDADES.length)];
        int tiempo = TIEMPOS[random.nextInt(TIEMPOS.length)];
        int recompensa = RECOMPENSAS[random.nextInt(RECOMPENSAS.length)];
        int penalizacion = PENALIZACION_BASE + (recompensa / 2);

        Contrato contrato = new Contrato(
            contadorId++,
            tipoMineral,
            cantidad,
            tiempo,
            recompensa,
            penalizacion
        );
        contratosActivos.add(contrato);

        System.out.println("\n" + "=".repeat(60));
        System.out.printf(
            "📋 NUEVO CONTRATO #%d: %s%n",
            contrato.getId(),
            contrato.getDescripcion()
        );
        System.out.printf("⏱️  Tiempo límite: %d segundos%n", tiempo);
        System.out.printf(
            "Penalización por incumplimiento: -$%d%n",
            penalizacion
        );
        System.out.println("[Aceptar/Rechazar desde la interfaz]");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Jugador acepta un contrato
     */
    public synchronized boolean aceptarContrato(int idContrato) {
        Contrato contrato = buscarContrato(idContrato);
        if (contrato != null && !contrato.isAceptado()) {
            contrato.setAceptado(true);
            return true;
        }
        return false;
    }

    /**
     * Jugador rechaza/ignora un contrato
     */
    public synchronized void rechazarContrato(int idContrato) {
        Contrato contrato = buscarContrato(idContrato);
        if (contrato != null) {
            contratosActivos.remove(contrato);
            contratosCompletados.add(contrato);
            System.out.printf("👎 [CONTRATO #%d] RECHAZADO%n", idContrato);
        }
    }


    

    /**
     * Evalúa si el jugador ha completado contratos aceptados
     * con los minerales disponibles en la bodega
     * @return Dinero ganado/perdido en esta evaluación
     */
    public synchronized int evaluarContratos(int dineroActual) {
        int dineroGanado = 0;
        int dineroPerdido = 0;
        List<Contrato> aRemover = new ArrayList<>();

        // Iteramos sobre una copia de la lista para poder modificar la original
        // dentro del mismo bucle sin lanzar ConcurrentModificationException
        for (Contrato contrato : new ArrayList<>(contratosActivos)) {
            // Solo los contratos ACEPTADOS generan penalización al vencerse.
            // Si el jugador nunca lo aceptó, simplemente se descarta sin costo.
            if (contrato.isVencido()) {
                if (contrato.isAceptado()) {
                    dineroPerdido += contrato.getPenalizacion();
                    System.out.printf(
                        "💸 [CONTRATO #%d] VENCIDO — penalización: -$%d%n",
                        contrato.getId(),
                        contrato.getPenalizacion()
                    );
                } else {
                    System.out.printf(
                        "⏰ [CONTRATO #%d] VENCIDO sin aceptar — ignorado%n",
                        contrato.getId()
                    );
                }
                aRemover.add(contrato);
                continue;
            }

            if (contrato.isAceptado() && !contrato.isCompletado()) {
                // Consultamos la bodega para ver cuántos minerales del tipo
                // requerido hay disponibles en ese momento
                Map<String, Integer> minerales =
                    bodega.getMineralesClasificados();
                int disponible = minerales.getOrDefault(
                    contrato.getTipoMineral(),
                    0
                );
                if (disponible >= contrato.getCantidadRequerida()) {
                    bodega.retirarPorTipo(
                        contrato.getTipoMineral(),
                        contrato.getCantidadRequerida()
                    );
                    contrato.agregarMinerales(contrato.getCantidadRequerida());
                    dineroGanado += contrato.getRecompensa();
                    System.out.printf(
                        "🎉 [CONTRATO #%d] COMPLETADO — recompensa: +$%d%n",
                        contrato.getId(),
                        contrato.getRecompensa()
                    );
                    aRemover.add(contrato);
                }
            }
        }

        for (Contrato c : aRemover) {
            contratosActivos.remove(c);
            contratosCompletados.add(c);
        }

        return dineroGanado - dineroPerdido;
    }

    /**
     * Busca un contrato por ID
     */
    private Contrato buscarContrato(int id) {
        for (Contrato c : contratosActivos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Obtiene lista de contratos activos
     */
    public synchronized List<Contrato> getContratosActivos() {
        return new ArrayList<>(contratosActivos);
    }

    /**
     * Obtiene lista de contratos completados
     */
    public synchronized List<Contrato> getContratosCompletados() {
        return new ArrayList<>(contratosCompletados);
    }

    /**
     * Obtiene total de dinero ganado en contratos
     */
    public synchronized int getTotalGanado() {
        int total = 0;
        for (Contrato c : contratosCompletados) {
            if (c.isCompletado() && c.isAceptado()) {
                total += c.getRecompensa();
            }
        }
        return total;
    }

    /**
     * Obtiene total de dinero perdido por penalizaciones
     */
    public synchronized int getTotalPerdido() {
        int total = 0;
        for (Contrato c : contratosCompletados) {
            if (c.isVencido() && c.isAceptado()) {
                total += c.getPenalizacion();
            }
        }
        return total;
    }

    /**
     * Detiene el gestor de contratos
     */
    public void detener() {
        activo = false;
        System.out.println("[GestorContratos] Sistema de contratos detenido");
    }

    public void reanudar() {
        activo = true;
        System.out.println("[GestorContratos] Sistema de contratos reanudado");
    }

    public boolean isActivo() {
        return activo;
    }
}
