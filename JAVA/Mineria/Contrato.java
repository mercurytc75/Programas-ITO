package Mineria;

/**
 * Clase Contrato: representa una misión con plazo y recompensa
 * Los contratos aparecen durante el juego y el jugador decide aceptarlos
 */
public class Contrato {

    private int id;
    private String tipoMineral; // Tipo requerido: Cobre, Plata, Oro, Diamante, etc.
    private int cantidadRequerida; // Cantidad de minerales
    private int tiempoLimiteSegundos; // Segundos para completar
    private long tiempoCreacion; // Timestamp de creación
    private int recompensa; // Dinero si se cumple
    private int penalizacion; // Dinero si se incumple
    private boolean aceptado; // ¿Jugador aceptó?
    private boolean completado; // ¿Ya se cumplió?
    private boolean vencido; // ¿Se venció el tiempo?
    private int cantidadEntregada; // Cantidad recolectada hasta ahora

    public Contrato(
        int id,
        String tipoMineral,
        int cantidadRequerida,
        int tiempoSegundos,
        int recompensa,
        int penalizacion
    ) {
        this.id = id;
        this.tipoMineral = tipoMineral;
        this.cantidadRequerida = cantidadRequerida;
        this.tiempoLimiteSegundos = tiempoSegundos;
        this.recompensa = recompensa;
        this.penalizacion = penalizacion;
        this.tiempoCreacion = System.currentTimeMillis();
        this.aceptado = false;
        this.completado = false;
        this.vencido = false;
        this.cantidadEntregada = 0;
    }

    /**
     * Devuelve el tiempo restante en segundos
     */
    public int getTiempoRestante() {
        if (vencido || completado) {
            return 0;
        }
        long tiempoTranscurrido =
            (System.currentTimeMillis() - tiempoCreacion) / 1000;
        return Math.max(0, (int) (tiempoLimiteSegundos - tiempoTranscurrido));
    }

    /**
     * Verifica si el contrato está vencido
     */
    public synchronized boolean isVencido() {
        if (completado) return false;
        if (getTiempoRestante() <= 0) {
            vencido = true;
            return true;
        }
        return false;
    }

    /**
     * Intenta cumplir el contrato con minerales entregados
     * @param cantidadARestar Cantidad de minerales a restar de bodega
     * @return true si se cumplió el contrato
     */
    public synchronized boolean agregarMinerales(int cantidadARestar) {
        if (completado || vencido) {
            return false;
        }

        cantidadEntregada += cantidadARestar;

        if (cantidadEntregada >= cantidadRequerida) {
            completado = true;
            System.out.printf(
                "✅ [CONTRATO #%d] CUMPLIDO: %s %d/%d | Recompensa: $%d%n",
                id,
                tipoMineral,
                cantidadEntregada,
                cantidadRequerida,
                recompensa
            );
            return true;
        }

        System.out.printf(
            "📋 [CONTRATO #%d] Progreso: %s %d/%d%n",
            id,
            tipoMineral,
            cantidadEntregada,
            cantidadRequerida
        );
        return false;
    }

    /**
     * Obtiene descripción legible del contrato
     */
    public String getDescripcion() {
        return String.format(
            "⚠️  %s x%d en %ds → Recomp: $%d",
            tipoMineral,
            cantidadRequerida,
            tiempoLimiteSegundos,
            recompensa
        );
    }

    /**
     * Obtiene descriptor corto con barra de progreso visual
     */
    public String getProgressBar() {
        int tiempoRestante = getTiempoRestante();
        int barLength = 10;
        int filled = (int) (barLength *
            (1.0 - (tiempoRestante / (double) tiempoLimiteSegundos)));
        filled = Math.max(0, Math.min(barLength, filled));

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        bar.append("]");

        return String.format("%s %ds", bar.toString(), tiempoRestante);
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getTipoMineral() {
        return tipoMineral;
    }

    public int getCantidadRequerida() {
        return cantidadRequerida;
    }

    public int getRecompensa() {
        return recompensa;
    }

    public int getPenalizacion() {
        return penalizacion;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
        if (aceptado) {
            System.out.printf(
                "🤝 [CONTRATO #%d] ACEPTADO: %s x%d%n",
                id,
                tipoMineral,
                cantidadRequerida
            );
        }
    }

    public boolean isCompletado() {
        return completado;
    }

    public int getCantidadEntregada() {
        return cantidadEntregada;
    }

    public int getTiempoLimiteSegundos() {
        return tiempoLimiteSegundos;
    }
}
