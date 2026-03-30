package ProductoConsumidor;

import java.util.LinkedList;
import java.util.Queue;

class ColaPedidos {
    private Queue<Pedido> cola = new LinkedList<>();
    private int capacidad;

    public ColaPedidos(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void agregarPedido(Pedido pedido) {
        while (cola.size() == capacidad) {
            try {
                System.out.println("La cola esta llena. El mesero espera...");
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error al esperar en agregarPedido");
            }
        }

        cola.add(pedido);
        System.out.println("Mesero agregó: " + pedido);

        notifyAll();
    }

    public synchronized Pedido tomarPedido() {
        while (cola.isEmpty()) {
            try {
                System.out.println("No hay pedidos. El cocinero espera...");
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error al esperar en tomarPedido");
            }
        }

        Pedido pedido = cola.poll();
        System.out.println("Cocinero tomó: " + pedido);

        notifyAll();

        return pedido;
    }
}