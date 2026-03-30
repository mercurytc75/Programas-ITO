package EstacionEspacial;

import java.util.LinkedList;

public class ColaRobots implements TDACola<Robot >{

    private LinkedList<Robot> cola = new LinkedList<>();



    @Override
    public void encolar(Robot elemento) {
        cola.addLast(elemento);
   }

    @Override
    public Robot desencolar() {
        return cola.pollFirst();
   }

    @Override
    public Robot verSiguiente() {
        return cola.peekFirst();
    }

    @Override
    public boolean estaVacia() {
        return cola.isEmpty();
    }

    @Override
    public void mostrarCola() {
        for(Robot r : cola) {
            System.out.println(" - >" + r);
        }
    }

    
}
