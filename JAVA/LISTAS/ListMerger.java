package LISTAS;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ListMerger {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();

    public static void main(String[] args) {
        ListMerger program = new ListMerger();
        program.runMenu();
    }

    public void runMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- MENÚ DE LISTAS ---");
            System.out.println("1. Agregar elementos a Lista 1 (" + list1.size() + " elementos)");
            System.out.println("2. Agregar elementos a Lista 2 (" + list2.size() + " elementos)");
            System.out.println("3. Fusionar y ordenar listas");
            System.out.println("4. Ver listas actuales");
            System.out.println("5. Limpiar listas");
            System.out.println("6. ver listas ordenadas.");
            System.out.println("0. Salir");
            System.out.print("Ingrese su opción: ");

            try {
                if (!scanner.hasNextInt()) {
                    if (scanner.hasNext()) {
                        String invalid = scanner.next();
                        System.out.println("Error: '" + invalid + "' no es una opción válida.");
                        continue;
                    } else {
                        break; 
                    }
                }
                choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        addElementsToList(list1, "Lista 1");
                        break;
                    case 2:
                        addElementsToList(list2, "Lista 2");
                        break;
                    case 3:
                        mergeAndSortLists();
                        break;
                    case 4:
                        displayLists();
                        break;
                    case 5:
                        list1.clear();
                        list2.clear();
                        System.out.println("Listas limpiadas.");
                        break;
                    case 6: 
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor, ingrese un número válido.");
                scanner.nextLine(); 
                choice = -1;
            }
        } while (choice != 0);
    }

    private void addElementsToList(List<Integer> list, String listName) {
        System.out.println("\n--- Agregando a " + listName + " ---");
        System.out.println("Ingrese números enteros (escriba 'fin' para terminar):");
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("fin")) {
                break;
            }

            try {
                int value = Integer.parseInt(input);
                list.add(value);
            } catch (NumberFormatException e) {
                System.out.println("Error: '" + input + "' no es un número entero válido.");
            }
        }
    }

    private void mergeAndSortLists() {
        if (list1.isEmpty() && list2.isEmpty()) {
            System.out.println("Error: Ambas listas están vacías. No hay nada que fusionar.");
            return;
        }

       
        List<Integer> mergedList = new ArrayList<>(list1);
        mergedList.addAll(list2);

        System.out.println("\nLista fusionada (antes de ordenar): " + mergedList);

       
        bubbleSort(mergedList);

        System.out.println("Lista fusionada y ordenada: " + mergedList);
    }

    private void bubbleSort(List<Integer> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    private void displayLists() {
        System.out.println("\n--- Listas Actuales ---");
        System.out.println("Lista 1: " + (list1.isEmpty() ? "[Vacía]" : list1));
        System.out.println("Lista 2: " + (list2.isEmpty() ? "[Vacía]" : list2));
    }
}
