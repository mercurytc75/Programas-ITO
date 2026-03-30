package ProductoConsumidor;

class Cocinero extends Thread {
    private ColaPedidos cola;
    private String estacion;
    private CorteDeCaja corte;

    public Cocinero(ColaPedidos cola, String estacion, CorteDeCaja corte) {
        this.cola = cola;
        this.estacion = estacion;
        this.corte = corte;
    }

    @Override
    public void run() {
        while (true) {
            Pedido pedido = cola.tomarPedido();

            // --- DETECTAR CIERRE (Punto 6) ---
            if (pedido.getEstado() == EstadoPedido.FINALIZADO) {
                System.out.println("[" + this.getName() + " - " + estacion + "] RECIBIDA ORDEN DE CIERRE. Apagando fogones...");
                break;
            }

            // --- GESTIÓN DE ESTADOS (Punto 5) ---
            pedido.setEstado(EstadoPedido.EN_PREPARACION);
            System.out.println("[" + this.getName() + " - Estación: " + estacion + "] Cocinando " + pedido + "...");

            try {
                // Simulación de tiempo variable (Opcional, pero mejora el realismo)
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Error en el cocinero " + this.getName());
            }

            // --- REGISTRAR GANANCIAS (Punto 4) ---
            pedido.setEstado(EstadoPedido.LISTO);
            corte.registrarVenta(pedido.getPlatillo());
            System.out.println("[" + this.getName() + "] Pedido listo y cobrado: " + pedido);
        }
    }
}