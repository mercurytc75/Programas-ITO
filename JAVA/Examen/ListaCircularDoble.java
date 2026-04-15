package Examen;

import java.util.Random;

public class ListaCircularDoble {

    Nodo cabeza;

    public void agregar(String nombre) {
        Nodo nuevo = new Nodo(nombre);

        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
            cabeza.anterior = cabeza;
        } else {
            Nodo ultimo = cabeza.anterior;
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
        }
    }

    public void Jugar() {
        if (cabeza == null) return;

        Random random = new Random();
        Nodo actual = cabeza;

        while (actual.siguiente != actual) {
            int pasos = random.nextInt(10) + 5; // gira varias veces
            boolean derecha = random.nextBoolean();

            System.out.println("\n la botella empieza en " + actual.nombre);
            System.out.println(
                "Dirección: " + (derecha ? "Derecha ->" : "Izquierda <-")
            );
            System.out.println("pasos: " + pasos);

            for (int i = 0; i < pasos; i++) {
                if (derecha) {
                    actual = actual.siguiente;
                    System.out.println(
                        "avanca a la derecha -> : " + actual.nombre
                    );
                } else {
                    actual = actual.anterior;
                    System.out.println(
                        "avanca a la izquierda <- : " + actual.nombre
                    );
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("La botella apunta a: " + actual.nombre);

            // eliminar nodo
            actual.anterior.siguiente = actual.siguiente;
            actual.siguiente.anterior = actual.anterior;

            // mover al siguiente
            actual = actual.siguiente;
        }

        System.out.println("Ganador: " + actual.nombre);
    }
}
