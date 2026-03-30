package Tienda;

import java.util.Random;

public class Cliente {
    private String nombre;
    private int [] productos;

    public Cliente(String nombre, int numProductos){
        this.nombre = nombre;
        this.productos = new int[numProductos];
        Random rand = new Random();

        for(int i = 0; i < numProductos; i++){
            this.productos[i] = rand.nextInt(3) + 1;
        }
    }
    public String getNombre(){return this.nombre;}
    public int[] getProductos(){return this.productos;}
}
