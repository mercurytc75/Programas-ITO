package Tienda;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Tienda{
    public static void main(String[] args) {
        

        CajaRegistradora  caja  = new CajaRegistradora();
        Queue<Cliente> filaClientes = new LinkedList<>();
        
        Random rand = new Random();

        int nClientes = 4;
        
        for(int i = 0; i < nClientes ; i++){
            int numProductos = rand.nextInt(5) + 1;
            filaClientes.add(new Cliente("Cliente-" + (i + 1), numProductos));
        }

        System.out.println("############## TIENDA MERCURY ##############\n");
        Empleado e1 = new Empleado("empleado 1" , filaClientes, caja);
        Empleado e2 = new Empleado("empleado 2" , filaClientes, caja);
        Empleado e3 = new Empleado("empleado 3" , filaClientes, caja);
        

        e1.start();
        e2.start();
        e3.start();
   
    }
}