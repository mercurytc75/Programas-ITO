# Monopoly - Proyecto Java

## Descripción
Implementación completa del juego de Monopoly en Java siguiendo estándares de la industria con una arquitectura limpia y bien organizada.

## Requisitos
- **Java**: 8 o superior
- **IDE**: NetBeans, Eclipse, IntelliJ IDEA (recomendado)
- Sin dependencias externas

## Estructura del Proyecto

```
Monopoly/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/monopoly/
│   │   │       ├── Main.java                    # Punto de entrada
│   │   │       ├── model/
│   │   │       │   ├── Jugador.java            # Entidad de jugador
│   │   │       │   ├── Propiedad.java          # Entidad de propiedad
│   │   │       │   ├── Juego.java              # Lógica principal del juego
│   │   │       │   ├── Tablero.java            # Gestión del tablero
│   │   │       │   └── casillas/               # Casillas del tablero
│   │   │       │       ├── Casilla.java        # Clase abstracta base
│   │   │       │       ├── Salida.java         # Casilla de salida
│   │   │       │       ├── Carcel.java         # Casilla de cárcel
│   │   │       │       ├── EstacionamientoLibre.java
│   │   │       │       ├── ImpuestoDeIngresos.java
│   │   │       │       ├── ImpuestoDeLujo.java
│   │   │       │       └── PropiedadCasilla.java
│   │   │       ├── tda/                        # Estructuras de datos
│   │   │       │   ├── Cola.java               # Cola (FIFO)
│   │   │       │   ├── Lista.java              # Lista enlazada
│   │   │       │   └── Nodo.java               # Nodo genérico
│   │   │       ├── service/                    # Servicios (futuro)
│   │   │       ├── ui/                         # Interfaz gráfica (futuro)
│   │   │       └── util/                       # Utilidades (futuro)
│   │   └── resources/                          # Recursos del proyecto
│   └── test/
│       └── java/com/monopoly/                  # Tests (futuro)
├── bin/                                        # Compilados (.class)
├── doc/                                        # Documentación
│   └── MONOPOLY_EXPLICACION.md
├── .gitignore
└── README.md

```

## Paquetes Principales

### `com.monopoly`
- **Main.java**: Punto de entrada de la aplicación

### `com.monopoly.model`
- **Jugador.java**: Representa a un jugador con dinero, posición y propiedades
- **Propiedad.java**: Representa una propiedad comprable del tablero
- **Juego.java**: Controla la lógica principal del juego (turnos, ganador)
- **Tablero.java**: Gestiona las 40 casillas y sus efectos

### `com.monopoly.model.casillas`
- **Casilla.java**: Clase abstracta para todas las casillas
- **Salida.java**: Da $200 al pasar
- **Carcel.java**: Encierra al jugador
- **EstacionamientoLibre.java**: Casilla segura sin efecto
- **ImpuestoDeIngresos.java**: Cobra $200
- **ImpuestoDeLujo.java**: Cobra $75
- **PropiedadCasilla.java**: Propiedad comprable con renta

### `com.monopoly.tda`
- **Nodo<T>**: Nodo genérico para estructuras
- **Cola<T>**: Estructura FIFO para gestionar turnos
- **Lista<T>**: Lista enlazada para almacenar propiedades

### `com.monopoly.service` (Futuro)
Servicios de negocio para lógica más compleja

### `com.monopoly.ui` (Futuro)
Interfaz gráfica cuando esté disponible

### `com.monopoly.util` (Futuro)
Funciones de utilidad generales

## Cómo Compilar

### En NetBeans
1. File → Open Project → Seleccionar carpeta Monopoly
2. Run → Clean and Build Project
3. Run → Run Main Project

### Desde terminal
```bash
cd src/main/java
javac -d ../../../../bin com/monopoly/*.java com/monopoly/model/*.java \
  com/monopoly/model/casillas/*.java com/monopoly/tda/*.java
```

## Cómo Ejecutar

### En NetBeans
- Run → Run Main Project (o presiona F6)

### Desde terminal
```bash
cd bin
java com.monopoly.Main
```

## Flujo del Juego

1. **Crear jugadores** (3 por defecto: Juan, María, Carlos)
2. **Iniciar partida** con $1500 cada uno
3. **Turnos:**
   - Lanzar dados (2-12)
   - Mover según resultado
   - Procesar casilla actual
4. **Casillas especiales:**
   - Salida: +$200
   - Impuestos: -$200 o -$75
   - Cárcel: Encarcelación
   - Propiedades: Compra o pago de renta
5. **Fin del juego:** Último jugador vivo es ganador

## Historial de Cambios

### v1.0 (29/03/2026)
- ✅ Estructura base completa
- ✅ Clases de casillas especiales
- ✅ Sistema de turnos con Cola
- ✅ Lógica de juego básica
- ✅ Documentación completa

## TODO / Mejoras Futuras

- [ ] Permitir compra/venta de propiedades
- [ ] Sistema de cárcel avanzado (3 turnos)
- [ ] Casillas de Oportunidad y Cofre
- [ ] Construcción de casas y hoteles
- [ ] Interfaz gráfica (GUI con Swing/JavaFX)
- [ ] Sistema de guardado/carga
- [ ] Modo multiplayer en red
- [ ] Tests unitarios

## Conceptos de Java Utilizados

| Concepto | Uso |
|----------|-----|
| **POO** | Clases, objetos, encapsulación |
| **Herencia** | Casilla como clase base |
| **Polimorfismo** | Método `efecto()` overridden |
| **Genéricos** | `Cola<T>`, `Lista<T>` |
| **Estructuras de datos** | Cola, Lista enlazada |
| **Control de flujo** | while, if/else, for |

## Autor
- **Copilot** - Estructura y desarrollo inicial

## Licencia
Proyecto educativo - Uso libre

---

**Última actualización:** 29 de marzo de 2026  
**Estado del proyecto:** En desarrollo
