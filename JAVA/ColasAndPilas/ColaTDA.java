public interface ColaTDA <T> {
    void encolar(T elemento);
    T desencolar();
    T frente();
    boolean estaVacia();
    int tamanio();
}
