package Monopoly;

public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;

    public Cola() {
        frente = null;
        fin = null;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<T>(dato);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
    }

    public T descolar() {
        if (estaVacia()) {
            return null;
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();

        if (frente == null) {
            fin = null;
        }
        return dato;
    }

    public T verFrente() {
        return estaVacia() ? null : frente.getDato();
    }
}
