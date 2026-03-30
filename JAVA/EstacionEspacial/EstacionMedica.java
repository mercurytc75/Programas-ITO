package EstacionEspacial;

import java.util.Random;
import java.util.Scanner;

public class EstacionMedica {
    private TDACola<Robot> filaPacientes;
    private TDAPila<String> heramientas;
    private final Random ran = new Random();

    String herrmientasDisp[] = {"taladro" , "destornillador"," tornillos"};

    public EstacionMedica(){
        this.filaPacientes = new ColaRobots();
        this.heramientas = new PilaHerramiente();
    }
    public void registrarRobot(String nombre, String serie, String falla) {
        Robot nuevo = new Robot(nombre, serie, falla);
        filaPacientes.encolar(nuevo);
        System.out.println("[REGISTRO ] Robot " + nombre + " añadido a la fila.");
    }

    public void atenderSiguiente() {
        
        if(!filaPacientes.estaVacia()){
            Robot atendido = filaPacientes.desencolar();
            if(heramientas.estaVacia()){
                System.out.println("No hay herramientas disponibles.");
                System.out.println("usando las herrramientas disponibles.");
                String her = herrmientasDisp[ran.nextInt(herrmientasDisp.length)];
                System.out.println(" Reparando a: " + atendido +" con la herramienta " + her);
            }else{
                String herramienta = heramientas.desapilar();
                System.out.println(" Reparando a: " + atendido +" con la herramienta " + herramienta);
            }
        }
         else {
            System.out.println("No hay robots esperando.");
        }
    }

    public void mostrarProximo() {
        if (!filaPacientes.estaVacia()) {
            System.out.println("Próximo en fila: " + filaPacientes.verSiguiente().getNombre());
        }
    }

    public void verListado() {
        System.out.println("\n--- ROBOTS EN ESPERA ---");
        filaPacientes.mostrarCola();
    }

    
    public void guardarHerramienta(String h) {
        heramientas.apilar(h);
        System.out.println("[HERRAMIENTA] Guardada: " + h);
    }

    public void usarHerramienta() {
        if (!heramientas.estaVacia()) {
            String h = heramientas.desapilar();
            System.out.println("[HERRAMIENTA] Sacada para usar: " + h);
        } else {
            System.out.println("El compartimiento está vacío.");
        }
    }

    public static void main(String[]  args){
         EstacionMedica estacion = new EstacionMedica();
        try (Scanner sc = new Scanner(System.in)) {
            int opcion = 0;

            do {
                System.out.println("\n========== MENÚ ESTACIÓN ESPACIAL MÉDICA ==========");
                System.out.println("1. Registrar paciente ");
                System.out.println("2. Atender paciente ");
                System.out.println("3. Mostrar siguiente paciente ");
                System.out.println("4. Mostrar listado de espera ");
                System.out.println("5. Guardar herramienta ");
                System.out.println("6. Sacar herramienta ");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                
                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    opcion = -1;
                }

                switch (opcion) {
                    case 1:
                        System.out.print("Nombre del Robot: ");
                        String nombre = sc.nextLine();
                        System.out.print("Número de serie: ");
                        String serie = sc.nextLine();
                        System.out.print("Falla reportada: ");
                        String falla = sc.nextLine();
                        estacion.registrarRobot(nombre, serie, falla);
                        break;
                    case 2:
                        estacion.atenderSiguiente();
                        break;
                    case 3:
                        estacion.mostrarProximo();
                        break;
                    case 4:
                        estacion.verListado();
                        break;
                    case 5:
                        System.out.print("Nombre de la herramienta a guardar: ");
                        String h = sc.nextLine();
                        estacion.guardarHerramienta(h);
                        break;
                    case 6:
                        estacion.usarHerramienta();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema médico...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (opcion != 0);
        }

    }
   
}
