package Examen;

public class JuegoBotella {

    public static void main(String[] args) {
        ListaCircularDoble lista = new ListaCircularDoble();

        lista.agregar("Juan");
        lista.agregar("Ana");
        lista.agregar("Luis");
        lista.agregar("Carlos");
        lista.agregar("Sofia");

        lista.Jugar();
    }
}
