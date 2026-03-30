package EstacionEspacial;

public interface TDAPila<T> {
    void apilar(T elemento);
    T desapilar() ;
    boolean estaVacia();
    
}
