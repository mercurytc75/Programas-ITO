package EstacionEspacial;

public interface TDACola <T > {
    void encolar(T elemento);
    T desencolar();
    T verSiguiente();
    boolean estaVacia();
    void mostrarCola();
    
}
