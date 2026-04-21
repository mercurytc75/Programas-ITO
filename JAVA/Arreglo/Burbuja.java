package Arreglo;

import java.util.LinkedList;
import java.util.Random;

public class Burbuja {

    public static void main(String[] args) {
        int n = 50000;

        LinkedList<Integer> lista = new LinkedList<>();

        Random rm = new Random();

        for (int i = 0; i < n; i++) {
            lista.add(rm.nextInt(1000));
        }

        long inicio = System.nanoTime();

        bubbleSort(lista);

        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;
        System.out.println("Tiempo con LinkedList: " + tiempoMs + " ms");
    }

    public static void bubbleSort(LinkedList<Integer> lista) {
        int n = lista.size();
        boolean intercambio;

        for (int i = 0; i < n - 1; i++) {
            intercambio = false;

            for (int j = 0; j < n - i - 1; j++) {
                int actual = lista.get(j);
                int siguiente = lista.get(j + 1);

                if (actual > siguiente) {
                    lista.set(j, siguiente);
                    lista.set(j + 1, actual);
                    intercambio = true;
                }
            }

            if (!intercambio) break;
        }
    }
}
