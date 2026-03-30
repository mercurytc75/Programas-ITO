package ProductoConsumidor;

class Mesero extends Thread {
    private ColaPedidos cola;
    private Platillo[] menu;
    private static int meserosTerminados = 0;
    private static final Object lockStatic = new Object();
    private int totalMeseros;
    private int totalCocineros;

    public Mesero(ColaPedidos cola, Platillo[] menu, int totalMeseros, int totalCocineros) {
        this.cola = cola;
        this.menu = menu;
        this.totalMeseros = totalMeseros;
        this.totalCocineros = totalCocineros;
    }

    @Override
    public void run() {
        for (int i = 0; i < menu.length; i++) {
            Pedido pedido = new Pedido(i + 1, menu[i]);
            System.out.println("[" + this.getName() + "] Tomando orden: " + menu[i].getNombre());
            cola.agregarPedido(pedido);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error en el mesero " + this.getName());
            }
        }

        // --- MANEJO DE CIERRE (Punto 6) ---
        synchronized (lockStatic) {
            meserosTerminados++;
            if (meserosTerminados == totalMeseros) {
                System.out.println("\n*** TODOS LOS MESEROS HAN TERMINADO. ENVIANDO ÓRDENES DE CIERRE... ***\n");
                for (int i = 0; i < totalCocineros; i++) {
                    Pedido cierre = new Pedido(0, null);
                    cierre.setEstado(EstadoPedido.FINALIZADO);
                    cola.agregarPedido(cierre);
                }
            }
        }
    }
}