import java.util.LinkedList;
import java.util.Queue;

public class ColaGenerica<T> implements ColaTDA<T> {
    private  Queue <T>  elementos;

    public ColaGenerica(){
        elementos = new LinkedList<>();
    }


    @Override
    public void encolar(T elemento) {
        elementos.offer(elemento);
    }

    @Override
    public T desencolar() {
        if(!estaVacia()){
            return elementos.poll();
        }
        return null;
    }

    @Override
    public T frente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'frente'");
    }

    @Override
    public boolean estaVacia() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estaVacia'");
    }

    @Override
    public int tamanio() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tamanio'");
    }
    
}
