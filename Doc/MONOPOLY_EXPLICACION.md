# 🎲 Documentación - Juego de Monopoly en Java

## 📋 Tabla de Contenidos
1. [Introducción](#introducción)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Clases Principales](#clases-principales)
4. [Clases de Casillas](#clases-de-casillas)
5. [Estructuras de Datos](#estructuras-de-datos)
6. [Flujo del Juego](#flujo-del-juego)
7. [Cómo Ejecutar](#cómo-ejecutar)

---

## Introducción

Este proyecto implementa un **juego de Monopoly** completo en Java. El juego sigue las reglas clásicas donde los jugadores:
- Lanzan dados para moverse por el tablero
- Compran propiedades
- Pagan renta cuando caen en propiedades ajenas
- Tratan de no quebrantar (bancarrota)

---

## Estructura del Proyecto

```
Monopoly/
├── src/
│   ├── main/
│   │   └── Main.java                 (Punto de entrada del juego)
│   ├── model/
│   │   ├── Jugador.java              (Representa cada jugador)
│   │   ├── Propiedad.java            (Información de propiedades)
│   │   ├── Juego.java                (Lógica principal del juego)
│   │   ├── Tablero.java              (Gestión del tablero)
│   │   └── casillas/
│   │       ├── Casilla.java          (Clase abstracta de casillas)
│   │       ├── Salida.java           (Casilla de Salida - da dinero)
│   │       ├── Carcel.java           (Casilla de Cárcel)
│   │       ├── EstacionamientoLibre.java  (Casilla segura)
│   │       ├── ImpuestoDeIngresos.java    (Impuesto de $200)
│   │       ├── ImpuestoDeLujo.java       (Impuesto de $75)
│   │       └── PropiedadCasilla.java     (Casilla con propiedad)
│   ├── service/
│   ├── tda/
│   │   ├── Cola.java                 (Estructura de datos - Cola)
│   │   ├── Lista.java                (Estructura de datos - Lista)
│   │   └── Nodo.java                 (Nodo para estructuras)
│   └── ui/
```

---

## Clases Principales

### 1. **Jugador.java**
Representa a cada jugador en el juego.

**Atributos:**
```java
- id: int                    // Identificador único
- nombre: String             // Nombre del jugador
- dinero: int               // Dinero disponible (comienza con $1500)
- posicion: int             // Posición actual en el tablero (0-39)
- propiedades: Lista        // Lista de propiedades que posee
- enCarcel: boolean         // Estado de encarcelamiento
```

**Métodos Principales:**
```java
- mover(int pasos)          // Mueve el jugador pasos casillas
- pagar(int cantidad)       // Reduce el dinero del jugador
- recibir(int cantidad)     // Aumenta el dinero del jugador
- encarcelar()              // Encarcela al jugador
- salirDeCarcel()           // Libera al jugador de la cárcel
- estaBancarro()            // Verifica si el jugador quebró (dinero ≤ 0)
- agregarPropiedad()        // Agrega una propiedad a su lista
```

**Ejemplo de Uso:**
```java
Jugador j1 = new Jugador(1, "Juan");
j1.mover(5);                        // Se mueve 5 casillas
j1.pagar(200);                      // Paga $200
j1.recibir(100);                    // Recibe $100
System.out.println(j1.getDinero()); // Imprime: 1400
```

---

### 2. **Propiedad.java**
Representa una propiedad que puede ser poseída.

**Atributos:**
```java
- nombre: String        // Nombre de la propiedad (ej: "Avenida A")
- precio: int          // Precio de compra
- renta: int           // Renta que otros deben pagar
- duenio: Jugador      // Propietario actual (null si sin dueño)
```

**Métodos Principales:**
```java
- tieneDuenio()        // Verifica si la propiedad tiene dueño
- setDuenio(Jugador)   // Asigna un dueño
- getDuenio()          // Obtiene el dueño actual
- getPrecio()          // Devuelve el precio
- getRenta()           // Devuelve la renta
```

**Ejemplo:**
```java
Propiedad avenidaA = new Propiedad("Avenida A", 100, 20);
avenidaA.setDuenio(j1);  // Juan ahora es dueño
System.out.println(avenidaA.tieneDuenio()); // true
```

---

### 3. **Tablero.java**
Gestiona todas las casillas del juego.

**Características:**
- **40 casillas** (como el Monopoly real)
- Casillas especiales en posiciones fijas:
  - **Posición 0**: Salida (recibe $200)
  - **Posición 4**: Impuesto de Ingresos (-$200)
  - **Posición 10**: Cárcel
  - **Posición 20**: Estacionamiento Libre (seguro)
  - **Posición 38**: Impuesto de Lujo (-$75)
  - **Posición 39**: Vaya a la Cárcel

**Métodos Principales:**
```java
- lanzarDados()            // Simula lanzar dos dados (2-12)
- getCasilla(int pos)      // Obtiene la casilla en esa posición
- procesarCasilla()        // Ejecuta el efecto de la casilla
- crearPropiedad()         // Crea una nueva propiedad en una posición
```

**Ejemplo:**
```java
Tablero tablero = new Tablero();
int dados = tablero.lanzarDados();  // Lanza dados
System.out.println("Sacaste: " + dados);
```

---

### 4. **Juego.java**
Controla la lógica principal y turnos del juego.

**Flujo Principal:**
1. Crear jugadores
2. Agregarlos al juego
3. Ejecutar turnos hasta que quede 1 jugador
4. Declarar ganador

**Métodos Principales:**
```java
- agregarJugador(Jugador)   // Agrega un jugador a la cola
- iniciarJuego()            // Comienza la simulación de turnos
- ejecutarTurno()           // Ejecuta un turno de un jugador
- finalizarJuego()          // Declara al ganador
```

**Lógica del Turno:**
```
1. Obtener el jugador actual de la cola
2. Si está en bancarrota → eliminar
3. Si está en cárcel → simplemente sale al siguiente turno
4. Si no está en cárcel:
   - Lanzar dados
   - Mover ese número de casillas
   - Procesar el efecto de la casilla
5. Volver a encolar si sigue vivo
```

---

## Clases de Casillas

Todas las casillas heredan de la clase abstracta **Casilla.java**:

```java
public abstract class Casilla {
    protected String nombre;
    protected int posicion;
    
    public abstract void efecto(Jugador jugador);
}
```

### Tipos de Casillas:

#### **Salida.java** ✅
- Posición: 0
- Efecto: El jugador recibe $200

```java
public void efecto(Jugador jugador) {
    jugador.recibir(200);
    System.out.println("¡"+jugador.getNombre()+" recibió $200!");
}
```

#### **Cárcel.java** 🔒
- Posición: 10
- Efecto: Encierra al jugador

#### **EstacionamientoLibre.java** 🅿️
- Posición: 20
- Efecto: Ninguno (es seguro)

#### **ImpuestoDeIngresos.java** 💰
- Posición: 4
- Efecto: El jugador paga $200

#### **ImpuestoDeLujo.java** 💎
- Posición: 38
- Efecto: El jugador paga $75

#### **PropiedadCasilla.java** 🏠
- Una propiedad normal
- Efecto: 
  - Si no tiene dueño: mostrar oferta
  - Si tiene dueño: pagar renta

---

## Estructuras de Datos

### **Cola<T>**
Implementación de una cola FIFO (First In, First Out) para gestionar turnos.

**Operaciones:**
```java
- encolar(T dato)    // Agregar elemento al final
- descolar()         // Eliminar y devolver el primer elemento
- estaVacia()        // Verifica si la cola está vacía
- verFrente()        // Ver el primer elemento sin eliminar
```

**Uso en Monopoly:**
```java
Cola<Jugador> jugadores = new Cola<>();
jugadores.encolar(juan);
jugadores.encolar(maria);

Jugador actual = jugadores.descolar(); // Juan
jugadores.encolar(actual);              // Al final de la cola
```

### **Lista<T>**
Implementación de una lista dinámica para almacenar propiedades.

---

## Flujo del Juego

```
┌─────────────────────────────┐
│  Crear instancia de Juego   │
└──────────────┬──────────────┘
               │
       ┌───────▼────────┐
       │ Agregar 3 jugadores
       │ (Juan, María, Carlos)
       └───────┬────────┘
               │
       ┌───────▼──────────────┐
       │ iniciarJuego()       │
       │ Comienza bucle       │
       └───────┬──────────────┘
               │
    ┌──────────▼───────────┐
    │ Mientras hay >1 jugador
    │ y juego activo:
    └──────────┬──────────┘
               │
    ┌──────────▼────────────────┐
    │ Turno del jugador actual  │
    │ 1. Lanzar dados           │
    │ 2. Mover                  │
    │ 3. Procesar casilla       │
    │ 4. Volver a encolar       │
    └──────────┬─────────────┘
               │
    ┌──────────▼──────────────┐
    │ ¿Más de 1 jugador vivo?│
    └──────────┬──────────┬──┘
         Sí───►│          │◄───No
              │          │
         ┌────┘          └────────────┐
         │                            │
    Siguiente turno           Mostrar ganador
```

---

## Cómo Ejecutar

### **Opción 1: En NetBeans**

1. **Abrir el proyecto:**
   - File → Open Project
   - Seleccionar la carpeta `Monopoly`

2. **Compilar:**
   - Click derecho en el proyecto
   - Clean and Build

3. **Ejecutar:**
   - Click derecho en `Main.java`
   - Run File

### **Opción 2: Desde terminal**

```bash
# Navegar al directorio del proyecto
cd c:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly

# Compilar
javac -d bin src/main/*.java src/model/*.java src/model/casillas/*.java src/tda/*.java

# Ejecutar
java -cp bin main.Main
```

---

## Ejemplo de Salida

```
========== MONOPOLY ==========
Iniciando juego con 3 jugadores

--- TURNO 1 --- Juan (Dinero: $1500)
Juan lanzó: 7
Juan se movió a la posición 7
Juan está en una propiedad sin dueño en la posición 7

--- TURNO 2 --- María (Dinero: $1500)
María lanzó: 5
María se movió a la posición 5
María pagó Impuesto de Ingresos: $200

--- TURNO 3 --- Carlos (Dinero: $1500)
Carlos lanzó: 8
Carlos se movió a la posición 8
Carlos está en una propiedad sin dueño en la posición 8

--- TURNO 4 --- Juan (Dinero: $1500)
...
```

---

## Mejoras Futuras

1. ✅ **Sistema de compra de propiedades** - Permitir que los jugadores compren
2. ✅ **Cálculo de renta automático** - Pagar renta basado en propiedades
3. ✅ **Sistema de cárcel avanzado** - 3 turnos para salir o pagar $50
4. ❌ **Casillas de Oportunidad y Cofre** - Tarjetas aleatorias
5. ❌ **Construcción de casas y hoteles** - Aumentar renta
6. ❌ **Interfaz Gráfica (GUI)** - Visualizar el tablero
7. ❌ **Guardado y carga de partidas** - Persistencia de datos

---

## Conceptos de Programación Utilizados

| Concepto | Implementación |
|----------|---|
| **POO** | Clases: Jugador, Propiedad, Juego, Tablero |
| **Herencia** | Todas las casillas heredan de `Casilla` |
| **Polimorfismo** | Método `efecto()` en cada subclase de Casilla |
| **Encapsulación** | Atributos privados con getters/setters |
| **TDA** | Cola y Lista para estructuras dinámicas |
| **Control de flujo** | While, if/else para la lógica del juego |
| **Genéricos** | `Cola<T>`, `Lista<T>` |

---

**Autor:** Copilot  
**Fecha:** 29 de marzo de 2026  
**Versión:** 1.0

