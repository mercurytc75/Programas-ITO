package tda;

public class Lista<T> {
    private Nodo<T> cabeza;

    public Lista() {
        cabeza = null;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<T>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
    }

    public T obtener(int indice) {
        Nodo<T> actual = cabeza;
        int contador = 0;

        while (actual != null && contador < indice) {
            actual = actual.getSiguiente();
            contador++;
        }

        return actual != null ? actual.getDato() : null;
    }

    public int tamanio() {
        Nodo<T> actual = cabeza;
        int contador = 0;

        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }

        return contador;
    }
}
