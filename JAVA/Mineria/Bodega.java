package Mineria;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Bodega: almacena temporalmente los minerales antes de ser refinados
 * Tiene capacidad máxima limitada
 */
public class Bodega {
    private List<Mineral> mineralesAlmacenados;
    private int capacidadMaxima;
    private int espacioDisponible;
    
    public Bodega(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.espacioDisponible = capacidadMaxima;
        this.mineralesAlmacenados = new ArrayList<>();
    }
    
    /**
     * Intenta almacenar minerales en la bodega
     * @param minerales Minerales a almacenar
     * @return true si se almacenaron, false si no hay espacio
     */
    public synchronized boolean almacenar(List<Mineral> minerales) {
        if (espacioDisponible >= minerales.size()) {
            mineralesAlmacenados.addAll(minerales);
            espacioDisponible -= minerales.size();
            
            System.out.println("📦 [BODEGA] " + minerales.size() + " minerales almacenados. " +
                             "Espacio disponible: " + espacioDisponible + "/" + capacidadMaxima);
            return true;
        } else {
            System.out.println("❌ [BODEGA] No hay espacio suficiente. Disponible: " + 
                             espacioDisponible + ", solicitado: " + minerales.size());
            return false;
        }
    }
    
    /**
     * Retira minerales de la bodega
     * @param cantidad Cantidad de minerales a retirar
     * @return Lista de minerales retirados
     */
    public synchronized List<Mineral> retirar(int cantidad) {
        List<Mineral> retirados = new ArrayList<>();
        int aRetirar = Math.min(cantidad, mineralesAlmacenados.size());
        
        for (int i = 0; i < aRetirar; i++) {
            retirados.add(mineralesAlmacenados.remove(0));
        }
        
        espacioDisponible += aRetirar;
        System.out.println("📤 [BODEGA] " + aRetirar + " minerales retirados. " +
                         "Espacio disponible: " + espacioDisponible + "/" + capacidadMaxima);
        
        return retirados;
    }
    
    public synchronized int getEspacioDisponible() {
        return espacioDisponible;
    }
    
    public synchronized int getCantidadAlmacenada() {
        return mineralesAlmacenados.size();
    }
    
    public synchronized int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}
