package TexEditor;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class EditorTexto {

    private static ArrayList<String> documento = new ArrayList<>();
    private static Stack<Command> undoStack = new Stack<>();
    private static Stack<Command> redoStack = new Stack<>();


    // Ejecutar comando
    private static void ejecutarComando(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear(); // importante
    }

    // Deshacer
    private static void deshacer() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        } else {
            System.out.println("No hay acciones para deshacer.");
        }
    }

    // Rehacer
    private static void rehacer() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        } else {
            System.out.println("No hay acciones para rehacer.");
        }
    }

    // Mostrar documento
    private static void mostrarDocumento() {
        System.out.println("\n--- Documento ---");

        if (documento.isEmpty()) {
            System.out.println("(Vacío)");
        } else {
            for (int i = 0; i < documento.size(); i++) {
                System.out.println(i + ": " + documento.get(i));
            }
        }

        System.out.println("-----------------\n");
    }

  
    private static void mostrarMenu() {
        System.out.println("====== EDITOR DE TEXTO ======");
        System.out.println("1. Agregar línea");
        System.out.println("2. Eliminar línea");
        System.out.println("3. Deshacer");
        System.out.println("4. Rehacer");
        System.out.println("5. Mostrar documento");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            int opcion;

            do {
                mostrarMenu();
                while (!sc.hasNextInt()) {
                    System.out.println("Entrada inválida. Intente nuevamente.");
                    sc.next();
                }

                opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                switch (opcion) {

                    case 1:
                        System.out.print("Ingrese texto: ");
                        String texto = sc.nextLine();
                        ejecutarComando(new AgregarlineaCommand(documento, texto));
                        break;

                    case 2:
                        if (documento.isEmpty()) {
                            System.out.println("El documento está vacío.");
                            break;
                        }

                        System.out.print("Ingrese índice a eliminar: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Entrada inválida.");
                            sc.next();
                            break;
                        }

                        int index = sc.nextInt();

                        if (index >= 0 && index < documento.size()) {
                            ejecutarComando(new EliminarlineaCommand(documento, index));
                        } else {
                            System.out.println("Índice fuera de rango.");
                        }
                        break;

                    case 3:
                        deshacer();
                        break;

                    case 4:
                        rehacer();
                        break;

                    case 5:
                        mostrarDocumento();
                        break;

                    case 6:
                        System.out.println("Saliendo del editor...");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }

            } while (opcion != 6);
        }
    }
}
