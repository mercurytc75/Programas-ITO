package model;

import tda.Lista;
import model.Propiedad;

public class Jugador {
    private int id;
    private String nombre;
    private int dinero;
    private int posicion;
    private Lista propiedades;
    private boolean enCarcel;

    public Jugador(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
        this.dinero = 1500; // Dinero inicial
        this.posicion = 0; // Posición inicial en el tablero
        this.propiedades = new Lista<>(); // Lista de propiedades del jugador
        this.enCarcel = false; // Estado inicial del jugador
    }

    public void mover(int pasos){
        posicion = (posicion + pasos) % 40; // Asumiendo un tablero de 40 casillas
    }

    private void pagar(int cantidad){
        dinero -= cantidad;
    }
    private void recibir(int cantidad){
        dinero += cantidad;
    }
    private void agregarPropiedad(Propiedad propiedad){
        propiedades.agregar(propiedad);
    }

    public String getNombre(){
        return nombre;
    }
    public int getDinero(){
        return dinero;
    }
    public int getPosicion(){
        return posicion;
    }
}
