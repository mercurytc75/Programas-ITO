package Monopoly;

public class Lista<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public Lista() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }

        tamanio++;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            return null;
        }

        Nodo<T> actual = cabeza;
        int contador = 0;

        while (contador < indice) {
            actual = actual.getSiguiente();
            contador++;
        }

        return actual.getDato();
    }

    public int tamanio() {
        return tamanio;
    }

    public void limpiar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }
}
