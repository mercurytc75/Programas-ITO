package EstacionEspacial;

import java.util.Stack;

public class PilaHerramiente implements TDAPila<String>  {
    private Stack<String> pila = new Stack<>();

    @Override
    public void apilar(String elemento) {
        pila.push(elemento);
    }

    @Override
    public String desapilar() {
        return pila.pop();
    }

    @Override
    public boolean estaVacia() {
        return pila.isEmpty();
    }
    
}
