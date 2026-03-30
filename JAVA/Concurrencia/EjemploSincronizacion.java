package Concurrencia;

/**
 * Ejemplo detallado de manejo de hilos y sincronización en Java.
 * Este código demuestra el uso de:
 * 1. Runnable
 * 2. start() y join()
 * 3. synchronized (bloques de bloqueo)
 * 4. wait() y notify()
 */
public class EjemploSincronizacion {

    public static void main(String[] args) {
        System.out.println("--- Inicio del Ejemplo de Concurrencia ---");

        // 1. Recurso compartido (una cuenta bancaria simple)
        CuentaBancaria miCuenta = new CuentaBancaria(100);

        // 2. Definir tareas usando Runnable (preferido)
        Runnable tareaDeposito = () -> {
            for (int i = 0; i < 3; i++) {
                miCuenta.depositar(50);
                try {
                    Thread.sleep(500); // Uso de sleep()
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable tareaRetiro = () -> {
            for (int i = 0; i < 3; i++) {
                miCuenta.retirar(70);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 3. Crear hilos y usar start()
        Thread hilo1 = new Thread(tareaDeposito, "Hilo-Deposito");
        Thread hilo2 = new Thread(tareaRetiro, "Hilo-Retiro");

        hilo1.start();
        hilo2.start();

        // 4. Usar join() para esperar a que terminen antes de imprimir el saldo final
        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Saldo final: " + miCuenta.getSaldo());
        System.out.println("--- Fin del Ejemplo ---");
    }
}

/**
 * Clase que gestiona un recurso compartido con sincronización.
 */
class CuentaBancaria {
    private double saldo;
    private final Object lock = new Object(); // Objeto final para bloqueo

    public CuentaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    // Uso de bloque sincronizado para controlar el acceso al saldo
    public void depositar(double cantidad) {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " depositando: " + cantidad);
            saldo += cantidad;
            System.out.println("Nuevo saldo tras depósito: " + saldo);
            
            // Notificamos a otros hilos que el saldo ha cambiado (por si alguien esperaba para retirar)
            lock.notifyAll(); 
        }
    }

    public void retirar(double cantidad) {
        synchronized (lock) {
            // Uso de wait() mientras no haya saldo suficiente
            while (saldo < cantidad) {
                System.out.println(Thread.currentThread().getName() + " esperando... Saldo insuficiente (" + saldo + ")");
                try {
                    lock.wait(); // El hilo suelta el bloqueo y espera
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName() + " retirando: " + cantidad);
            saldo -= cantidad;
            System.out.println("Nuevo saldo tras retiro: " + saldo);
        }
    }

    public double getSaldo() {
        synchronized (lock) {
            return saldo;
        }
    }
}
