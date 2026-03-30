package tda;

public class Nodo<T> {
    private T dato;
    private Nodo<T> sigiente;
    
    public Nodo(T dato){
        this.dato = dato;
        this.sigiente = null;
    }

    public T getDato(){
        return dato;
    }

    public void setDato(T dato){
        this.dato = dato;
    }

    public Nodo<T> getSiguiente(){
        return sigiente;
    }

    public void setSiguiente(Nodo<T> siguiente){
        this.sigiente = siguiente;
    }
}