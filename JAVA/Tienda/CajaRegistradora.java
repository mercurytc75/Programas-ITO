package Tienda;

import java.util.concurrent.locks.ReentrantLock;

public class CajaRegistradora {
    private final ReentrantLock lock = new ReentrantLock(true);

    public void cobrar(String empleado, Cliente cliente){
        lock.lock(); 
        try {
            System.out.println(">>>>> la caja esta ocupada por : " + empleado + " atendiendo a " + cliente.getNombre());
          

            int [] productos = cliente.getProductos();
            
            for(int i = 0 ; i < productos.length; i++){
               this.esperarXsegundos(productos[i]);
               System.out.println(empleado + " procesando producto " + (i + 1) + " de " + cliente.getNombre() + " | "+ productos[i] + " seg " );
            }

            
            System.out.println("<<< la caja se libero " + empleado + "termino con " + cliente.getNombre());
        } finally {
            lock.unlock(); 
        }
    }

    private void esperarXsegundos(int segundos){
        try{
            Thread.sleep(segundos * 1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
