package Mineria;

import java.util.List;

/**
 * Clase Fundidora: procesa los minerales de forma sincronizada
 * Con barra de calor que se sobrecalienta si no se controla
 */
public class Fundidora {
    private int mineralRefinado;
    private int ciclosCompletados;
    private boolean ocupada;
    private int robotActual;
    
    // Fase 2: Barra de calor
    private int nivelCalor;              // 0-100%
    private int velocidadSubida;         // +10% por lote procesado
    private int velocidadBajada;         // -2% por segundo
    private boolean sobrecalentada;      // Flag cuando llega a 100%
    private long tiempoSobrecalentamiento; // 15 segundos si se sobrecalienta
    
    private static final int INCREMENTO_CALOR = 10;
    private static final int DECREMENTO_CALOR = 2;
    private static final int UMBRAL_SOBRECALENTAMIENTO = 100;
    private static final int TIEMPO_PAUSA_SOBRECALENTAMIENTO = 15000;
    private static final int COSTO_ENFRIAR = 100;
    private static final int DESCENSO_ENFRIAR = 40;
    
    public Fundidora() {
        this.mineralRefinado = 0;
        this.ciclosCompletados = 0;
        this.ocupada = false;
        this.robotActual = -1;
        
        // Inicializar calor
        this.nivelCalor = 0;
        this.velocidadSubida = INCREMENTO_CALOR;
        this.velocidadBajada = DECREMENTO_CALOR;
        this.sobrecalentada = false;
        this.tiempoSobrecalentamiento = TIEMPO_PAUSA_SOBRECALENTAMIENTO;
        
        // Hilo para bajar el calor naturalmente
        iniciarRefrigeración();
    }
    
    /**
     * Inicia el Thread que baja el calor naturalmente cada segundo
     */
    private void iniciarRefrigeración() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    synchronized (this) {
                        if (nivelCalor > 0 && !sobrecalentada) {
                            nivelCalor = Math.max(0, nivelCalor - velocidadBajada);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "RefrigeraciónFundidora").start();
    }
    
    /**
     * Procesa los minerales de forma sincronizada
     * @param idRobot ID del robot que hace la solicitud
     * @param minerales Lista de minerales a procesar
     */
    public synchronized void procesarMinerales(int idRobot, List<Mineral> minerales) {
        try {
            // Si está sobrecalentada, rechazar
            if (sobrecalentada) {
                System.out.printf("[FUNDIDORA] ¡SOBRECALENTADA! Robot #%d debe esperar...%n", idRobot);
                return;
            }
            
            ocupada = true;
            robotActual = idRobot;
            
            System.out.printf("[FUNDIDORA] Iniciando procesamiento para Robot #%d%n", idRobot);
            
            // Simular tiempo de refino (1-2 segundos por lote)
            int tiempoRefino = 1000 + (int)(Math.random() * 1000);
            Thread.sleep(tiempoRefino);
            
            // Contar los minerales procesados
            int cantidadProcesada = minerales.size();
            mineralRefinado += cantidadProcesada;
            ciclosCompletados++;
            
            // Aumentar calor
            nivelCalor = Math.min(UMBRAL_SOBRECALENTAMIENTO, nivelCalor + velocidadSubida);
            
            System.out.printf("[FUNDIDORA] Procesamiento completado. %d minerales refinados. Total: %d | Calor: %d%%%n",
                    cantidadProcesada, mineralRefinado, nivelCalor);
            
            // Verificar sobrecalentamiento
            if (nivelCalor >= UMBRAL_SOBRECALENTAMIENTO) {
                activarSobrecalentamiento();
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            ocupada = false;
            robotActual = -1;
        }
    }
    
    /**
     * Pausa la fundidora por sobrecalentamiento
     */
    private void activarSobrecalentamiento() {
        sobrecalentada = true;
        System.out.println("⚠️  [FUNDIDORA] ¡¡¡SOBRECALENTAMIENTO!!! Pausada por 15 segundos");
        
        new Thread(() -> {
            try {
                Thread.sleep(tiempoSobrecalentamiento);
                synchronized (Fundidora.this) {
                    sobrecalentada = false;
                    nivelCalor = 50;  // Reiniciar a 50% tras recuperarse
                    System.out.println("[FUNDIDORA] Recuperada del sobrecalentamiento");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    /**
     * FASE 2: Enfriar la fundidora manualmente
     * @param dineroDisponible Dinero del jugador
     * @return true si se enfrió exitosamente, false si no hay dinero
     */
    public synchronized boolean enfriar(int dineroDisponible) {
        if (dineroDisponible < COSTO_ENFRIAR) {
            System.out.printf("❌ [FUNDIDORA] Dinero insuficiente para enfriar. Falta: $%d%n", 
                    COSTO_ENFRIAR - dineroDisponible);
            return false;
        }
        
        nivelCalor = Math.max(0, nivelCalor - DESCENSO_ENFRIAR);
        System.out.printf("❄️  [FUNDIDORA] Enfriada manualmente. Nuevo nivel: %d%% (costo: -$%d)%n", 
                nivelCalor, COSTO_ENFRIAR);
        
        if (sobrecalentada && nivelCalor < 50) {
            sobrecalentada = false;
            System.out.println("[FUNDIDORA] Sobrecalentamiento cancelado por enfriamiento manual");
        }
        
        return true;
    }
    
    public synchronized int getMineralRefinado() {
        return mineralRefinado;
    }
    
    public synchronized int getCiclosCompletados() {
        return ciclosCompletados;
    }
    
    public synchronized boolean isOcupada() {
        return ocupada;
    }
    
    public synchronized int getRobotActual() {
        return robotActual;
    }
    
    // Getters para la barra de calor
    public synchronized int getNivelCalor() {
        return nivelCalor;
    }
    
    public synchronized boolean isSobrecalentada() {
        return sobrecalentada;
    }
    
    public synchronized void setNivelCalor(int calor) {
        this.nivelCalor = Math.max(0, Math.min(UMBRAL_SOBRECALENTAMIENTO, calor));
    }
    
    /**
     * FASE 4: Reduce la velocidad de subida de calor (mejora)
     */
    public synchronized void reducirVelocidadCalor() {
        // Reducir velocidad de subida en 20% (de 10 a 8, etc)
        if (velocidadSubida > 1) {
            velocidadSubida = (velocidadSubida * 8) / 10;
        }
        // Aumentar velocidad de bajada en 50% (de 2 a 3)
        velocidadBajada = (velocidadBajada * 3) / 2;
        System.out.printf("[FUNDIDORA] Velocidad de calor optimizada: +%d%% por lote, -%d%% por segundo%n", 
                velocidadSubida, velocidadBajada);
    }
}
