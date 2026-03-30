package Tienda;

import java.util.Queue;

public class Empleado extends Thread {
    private String nombre ;
    private Queue<Cliente> fila;
    private CajaRegistradora caja ;
    public Empleado(String nombre, Queue<Cliente> fila, CajaRegistradora caja) {
        this.nombre = nombre;
        this.fila = fila;
        this.caja = caja;
    }

    @Override
    public void run() {
        while(true){

            Cliente proximoCliente;
            synchronized(fila){
                if(fila.isEmpty()){
                    break;
                }else{
                    proximoCliente = fila.poll();
                    System.out.println("[" + nombre + "] Ha tomado de la fila a: " + proximoCliente.getNombre());
                }
            }
            if(proximoCliente != null) {
                caja.cobrar(this.nombre, proximoCliente);
            }
        }

        System.out.println("El empleado " + nombre + " ha finalizado su turno");
    }
}
