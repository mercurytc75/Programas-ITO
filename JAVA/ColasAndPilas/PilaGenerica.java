import java.util.ArrayList;
import java.util.List;

public class PilaGenerica<T> implements PilaTDA <T> {
    private List<T> elementos;
    public PilaGenerica (){
        elementos = new ArrayList<>();
    }
    @Override
    public void aplilar(T elemento) {
        elementos.add(elemento);
    }

    @Override
    public T desapilar() {


        if(estaVacia()){
            return null;
        }
        return elementos.remove(elementos.size() -1);
    }

    @Override
    public T clima() {
      
        if(estaVacia()){
            return null;
        }
        return elementos.get(elementos.size()-1);
        
    }

    @Override
    public boolean estaVacia() {
        return elementos.isEmpty();
    }

    @Override
    public int tamanio() {

        return elementos.size();

    }
}
