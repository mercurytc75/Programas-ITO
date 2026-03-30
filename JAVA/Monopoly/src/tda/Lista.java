package tda;

public class Lista<T> {
    private Nodo<T>  cabeza;

    public Lista() {
        cabeza = null;
    }

    public void agregar(T dato){
        Nodo<T> nuevo = new Nodo<>(dato);
        
        if(cabeza == null){
            cabeza = nuevo;
        }else{
            Nodo<T> actual = cabeza;
            while(actual.getSiguiente() != null){
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
    }

    public boolean estaVacia(){
        return cabeza == null;
    }

    public void mostrar(){
        Nodo<T> actual = cabeza;
        while(actual != null){
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }
}