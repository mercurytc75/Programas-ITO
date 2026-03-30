# ESTRUCTURA DE CARPETAS - Monopoly

## Árbol de Directorios Completo

```
Monopoly/
├── 📄 README.md                          # Documentación principal
├── 📄 .gitignore                         # Archivos a ignorar en Git
│
├── 📁 src/                               # Código fuente
│   ├── main/
│   │   ├── java/
│   │   │   └── com/monopoly/             # Paquete raíz (com.monopoly)
│   │   │       ├── 📄 Main.java          # PUNTO DE ENTRADA
│   │   │       │
│   │   │       ├── 📁 model/             # Modelo de datos
│   │   │       │   ├── 📄 Jugador.java
│   │   │       │   ├── 📄 Propiedad.java
│   │   │       │   ├── 📄 Juego.java
│   │   │       │   ├── 📄 Tablero.java
│   │   │       │   └── 📁 casillas/      # Sub-paquete
│   │   │       │       ├── 📄 Casilla.java         (abstracta)
│   │   │       │       ├── 📄 Salida.java
│   │   │       │       ├── 📄 Carcel.java
│   │   │       │       ├── 📄 EstacionamientoLibre.java
│   │   │       │       ├── 📄 ImpuestoDeIngresos.java
│   │   │       │       ├── 📄 ImpuestoDeLujo.java
│   │   │       │       └── 📄 PropiedadCasilla.java
│   │   │       │
│   │   │       ├── 📁 tda/               # Estructuras de datos
│   │   │       │   ├── 📄 Nodo.java      # Nodo genérico
│   │   │       │   ├── 📄 Cola.java      # FIFO Queue
│   │   │       │   └── 📄 Lista.java     # Linked List
│   │   │       │
│   │   │       ├── 📁 service/           # Servicios (futuro)
│   │   │       │   └── (vacío)
│   │   │       │
│   │   │       ├── 📁 ui/                # Interfaz gráfica (futuro)
│   │   │       │   └── (vacío)
│   │   │       │
│   │   │       └── 📁 util/              # Utilidades (futuro)
│   │   │           └── (vacío)
│   │   │
│   │   └── resources/                    # Recursos (imágenes, configs)
│   │       └── (vacío)
│   │
│   └── test/                             # Tests (futuro)
│       └── java/com/monopoly/
│           └── (vacío)
│
├── 📁 bin/                               # Archivos compilados (.class)
│   └── (generado automáticamente)
│
└── 📁 doc/                               # Documentación
    └── 📄 MONOPOLY_EXPLICACION.md        # Guía detallada del código
```

---

## Explicación de Estructura

### 🎯 Estándares Utilizados

✅ **Maven/Gradle Compatible**
- Estructura estándar `src/main/java`
- Separación clara de código y pruebas
- Paquetes con nomenclatura `com.empresa.proyecto`

✅ **Principios SOLID**
- **S**ingle Responsibility: Cada clase tiene un propósito
- **O**pen/Closed: Casilla es extensible
- **L**iskov: Todas las casillas implementan `efecto()`
- **I**nterface Segregation: Clases especializadas
- **D**ependency Inversion: Uso de abstracciones

### 📦 Paquetes

| Paquete | Propósito | Estado |
|---------|-----------|--------|
| `com.monopoly` | Entrada principal | ✅ Activo |
| `com.monopoly.model` | Entidades del juego | ✅ Activo |
| `com.monopoly.model.casillas` | Tipos de casillas | ✅ Activo |
| `com.monopoly.tda` | Estructuras de datos | ✅ Activo |
| `com.monopoly.service` | Servicios de negocio | 🔄 Futuro |
| `com.monopoly.ui` | Interfaz gráfica | 🔄 Futuro |
| `com.monopoly.util` | Funciones auxiliares | 🔄 Futuro |

### 📂 Directorios Especiales

- **`bin/`**: No incluir en Git (generado por compilación)
- **`target/`**: Para Maven (si se usa)
- **`build/`**: Para Gradle (si se usa)
- **`doc/`**: Documentación del proyecto

---

## Cómo Importar en NetBeans

1. **File** → **Open Project**
2. Seleccionar carpeta `Monopoly`
3. NetBeans automáticamente detecta:
   - Source packages en `src/main/java`
   - Test packages en `src/test/java`
   - Classpath correcto

---

## Cómo Compilar Desde Terminal

```bash
# Navegar al proyecto
cd c:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly

# Compilar (crea archivos en bin/)
javac -d bin -sourcepath src/main/java src/main/java/com/monopoly/*.java \
  src/main/java/com/monopoly/model/*.java \
  src/main/java/com/monopoly/model/casillas/*.java \
  src/main/java/com/monopoly/tda/*.java

# Ejecutar
java -cp bin com.monopoly.Main
```

---

## Ventajas de Esta Estructura

✅ **Escalabilidad**: Fácil agregar nuevos paquetes  
✅ **Mantenibilidad**: Claro dónde va cada cosa  
✅ **Profesionalismo**: Sigue estándares de la industria  
✅ **IDE-Friendly**: Todos los IDEs lo entienden  
✅ **Git-Ready**: .gitignore ya configurado  
✅ **Testing**: Preparado para tests unitarios  

---

## Próximos Pasos

1. **Service**: Crear clase `CompraService` para manejar compras
2. **UI**: Crear interfaz gráfica con Swing o JavaFX
3. **Util**: Agregadores, validadores, conversores
4. **Test**: Crear tests en `src/test/java`

---

**Versión**: 1.0  
**Fecha**: 29/03/2026  
**Autor**: Copilot
