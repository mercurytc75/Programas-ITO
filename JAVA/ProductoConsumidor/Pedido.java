package ProductoConsumidor;

class Pedido {
    private int numero;
    private Platillo platillo;
    private EstadoPedido estado;

    public Pedido(int numero, Platillo platillo) {
        this.numero = numero;
        this.platillo = platillo;
        this.estado = EstadoPedido.RECIBIDO;
    }

    public int getNumero() {
        return numero;
    }

    public Platillo getPlatillo() {
        return platillo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        if (estado == EstadoPedido.FINALIZADO) {
            return "--- ORDEN DE CIERRE ---";
        }
        return "Pedido #" + numero + " [" + estado + "] - " + platillo.toString();
    }
}