package Mineria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Clase Bodega: almacena temporalmente los minerales antes de ser refinados
 * Con capacidad máxima limitada y eventos de venta
 */
public class Bodega {
    private List<Mineral> mineralesAlmacenados;
    private int capacidadMaxima;
    private int espacioDisponible;
    private Map<String, Integer> mineralesClasificados;
    
    // Fase 2: Sistema de precios y venta
    private static final Map<String, Integer> PRECIOS_BASE = Map.of(
        "Cobre", 10,
        "Hierro", 10,
        "Plata", 25,
        "Oro", 50,
        "Diamante", 150
    );
    
    private int dineroGenerado;
    private Random random;
    private boolean comprador_especial_activo;
    private static final int MULTIPLICADOR_COMPRADOR = 2;
    private static final int TIEMPO_COMPRADOR_MS = 10000;
    
    public Bodega() {
        this.capacidadMaxima = 50;  // Capacidad por defecto
        this.espacioDisponible = capacidadMaxima;
        this.mineralesAlmacenados = new ArrayList<>();
        this.mineralesClasificados = new LinkedHashMap<>();
        this.dineroGenerado = 0;
        this.random = new Random();
        this.comprador_especial_activo = false;
        
        iniciarEventosCompradores();
    }
    
    /**
     * Inicia hilos de eventos de compradores especiales
     */
    private void iniciarEventosCompradores() {
        new Thread(() -> {
            while (true) {
                try {
                    // Cada 90 segundos, activar comprador especial
                    Thread.sleep(90000);
                    activarCompradorEspecial();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "EventoCompradores").start();
    }
    
    /**
     * Activa un comprador especial temporalmente
     */
    private synchronized void activarCompradorEspecial() {
        comprador_especial_activo = true;
        System.out.println("🚛 [BODEGA] ¡¡¡COMPRADOR ESPECIAL!!! Pagará x2 en los próximos 10 segundos");
        
        new Thread(() -> {
            try {
                Thread.sleep(TIEMPO_COMPRADOR_MS);
                synchronized (Bodega.this) {
                    comprador_especial_activo = false;
                    System.out.println("[BODEGA] El comprador especial se ha ido");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * Intenta almacenar minerales en la bodega
     * @param minerales Minerales a almacenar
     * @return true si se almacenaron, false si no hay espacio
     */
    public synchronized boolean almacenar(List<Mineral> minerales) {
        if (espacioDisponible >= minerales.size()) {
            mineralesAlmacenados.addAll(minerales);
            for (Mineral mineral : minerales) {
                mineralesClasificados.merge(mineral.getTipo(), 1, Integer::sum);
            }
            espacioDisponible -= minerales.size();
            
            System.out.printf("[BODEGA] %d minerales almacenados. Espacio disponible: %d/%d%n",
                    minerales.size(), espacioDisponible, capacidadMaxima);
            return true;
        } else {
            System.out.printf("[BODEGA] No hay espacio suficiente. Disponible: %d, solicitado: %d%n",
                    espacioDisponible, minerales.size());
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
        System.out.printf("[BODEGA] %d minerales retirados. Espacio disponible: %d/%d%n",
                aRetirar, espacioDisponible, capacidadMaxima);
        
        return retirados;
    }
    
    /**
     * FASE 2: Vender minerales ahora (a precio estándar)
     * @return Dinero obtenido de la venta
     */
    public synchronized int venderAhora() {
        if (mineralesAlmacenados.isEmpty()) {
            System.out.println("❌ [BODEGA] No hay minerales para vender");
            return 0;
        }
        
        int totalVenta = 0;
        StringBuilder detalles = new StringBuilder("💵 [BODEGA] Venta inmediata:\n");
        
        for (Mineral mineral : mineralesAlmacenados) {
            int precio = PRECIOS_BASE.getOrDefault(mineral.getTipo(), 10);
            totalVenta += precio;
            detalles.append(String.format("  - %s: $%d\n", mineral.getTipo(), precio));
        }
        
        dineroGenerado += totalVenta;
        int cantidadVendida = mineralesAlmacenados.size();
        mineralesAlmacenados.clear();
        mineralesClasificados.clear();
        espacioDisponible = capacidadMaxima;
        
        System.out.println(detalles.toString());
        System.out.printf("💰 Total obtenido: $%d | Dinero acumulado: $%d%n", totalVenta, dineroGenerado);
        
        return totalVenta;
    }
    
    /**
     * FASE 2: Vender con comprador especial (x2 multiplicador si está activo)
     * @return Dinero obtenido de la venta
     */
    public synchronized int venderConCompradorEspecial() {
        if (mineralesAlmacenados.isEmpty()) {
            System.out.println("❌ [BODEGA] No hay minerales para vender");
            return 0;
        }
        
        if (!comprador_especial_activo) {
            System.out.println("⚠️  [BODEGA] El comprador especial no está activo. Venta normal...");
            return venderAhora();
        }
        
        int totalVenta = 0;
        StringBuilder detalles = new StringBuilder("✨ [BODEGA] Venta especial (x2):\n");
        
        for (Mineral mineral : mineralesAlmacenados) {
            int precio = PRECIOS_BASE.getOrDefault(mineral.getTipo(), 10);
            int precioMultiplicado = precio * MULTIPLICADOR_COMPRADOR;
            totalVenta += precioMultiplicado;
            detalles.append(String.format("  - %s: $%d (era $%d)\n", 
                    mineral.getTipo(), precioMultiplicado, precio));
        }
        
        dineroGenerado += totalVenta;
        int cantidadVendida = mineralesAlmacenados.size();
        mineralesAlmacenados.clear();
        mineralesClasificados.clear();
        espacioDisponible = capacidadMaxima;
        comprador_especial_activo = false;
        
        System.out.println(detalles.toString());
        System.out.printf("💰 Total obtenido (BONUS): $%d | Dinero acumulado: $%d%n", 
                totalVenta, dineroGenerado);
        
        return totalVenta;
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

    public synchronized boolean isLlena() {
        return espacioDisponible <= 0;
    }

    public synchronized Map<String, Integer> getMineralesClasificados() {
        return new LinkedHashMap<>(mineralesClasificados);
    }
    
    public synchronized void vaciar() {
        mineralesAlmacenados.clear();
        mineralesClasificados.clear();
        espacioDisponible = capacidadMaxima;
        System.out.printf("[BODEGA] Bodega vaciada. Espacio disponible: %d/%d%n", espacioDisponible, capacidadMaxima);
    }
    
    // Getters para dinero
    public synchronized int getDineroGenerado() {
        return dineroGenerado;
    }
    
    public synchronized boolean isCompradorEspecialActivo() {
        return comprador_especial_activo;
    }
    
    public synchronized void setCapacidadMaxima(int nueva) {
        this.capacidadMaxima = nueva;
        this.espacioDisponible = Math.min(espacioDisponible, nueva);
        System.out.printf("[BODEGA] Capacidad mejorada a %d unidades%n", nueva);
    }
}
