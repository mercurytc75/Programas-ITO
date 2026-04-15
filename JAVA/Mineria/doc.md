# 🎮 PLAN DE JUEGO - Minería (de Simulador a Juego)

## 📌 Contexto del Proyecto

Actualmente el proyecto es un **simulador automático** donde los robots minan solos,
sin intervención del jugador, sin objetivos, sin puntuación y sin condiciones de victoria/derrota.

**Objetivo:** Convertirlo en un **juego interactivo en tiempo real** donde el jugador tome decisiones
constantemente, reaccione a eventos, administre recursos y tenga metas claras con presión de tiempo.

---

## 🎯 Concepto del Juego

**Nombre provisional:** `MineRush`

**Género:** Gestión de recursos en tiempo real

**Descripción:**
El jugador es el director de una empresa minera. Mientras los robots trabajan automáticamente,
el jugador debe **intervenir en tiempo real**: mover robots entre zonas, enfriar la fundidora,
vender minerales a tiempo, aceptar contratos urgentes y evitar que la bodega se llene.
No hay turnos — todo ocurre a la vez y el jugador debe reaccionar.

---

## ⚡ Modalidad: Tiempo Real (Opción Elegida)

El juego corre **continuamente**. Los robots minan solos en sus hilos (como ya hacen),
pero el jugador tiene **acciones disponibles en todo momento** que cambian el resultado.

### ¿Qué hace el jugador mientras los robots trabajan?

| Situación en el juego                  | Acción del jugador                              |
|----------------------------------------|-------------------------------------------------|
| La Zona B se está agotando             | Arrastra/reasigna robots a otra zona            |
| La fundidora está al 80% de calor      | Pulsa "Enfriar" antes de que se sobrecaliente   |
| La bodega está al 90% de capacidad     | Pulsa "Vender ahora" para vaciarla              |
| Llega un contrato urgente (30 seg)     | Decide aceptarlo o ignorarlo                    |
| Un robot se queda sin energía          | Pulsa "Recargar" (cuesta dinero)                |
| Un robot se rompe                      | Decide si repararlo o desactivarlo              |
| Llega un comprador especial (bonus)    | Pulsa "Vender" en los próximos 10 segundos      |

> 👉 Si el jugador **no hace nada**, el juego continúa pero con penalizaciones acumuladas.
> Si el jugador **reacciona a tiempo**, obtiene bonificaciones y el juego fluye mejor.

---

## 🧩 Mecánicas Principales

### 1. Dinero / Presupuesto
- El jugador **empieza con $5,000**
- Comprar robots cuesta dinero
- Reparar y recargar robots cuesta dinero
- Mejorar la fundidora y bodega cuesta dinero
- Vender minerales refinados **genera ingresos**
- Cumplir contratos da **bonificaciones extra**

### 2. Robots (Semi-automáticos)
- Minan solos en su zona asignada (hilo en background)
- El jugador puede **reasignarlos a otra zona en cualquier momento**
- Cada robot tiene:
  - `nivel` (1, 2, 3) — afecta velocidad y cantidad recolectada
  - `energia` (0–100) — baja con cada viaje, se recarga manualmente
  - `estado` — Activo / Sin energía / Roto / En reparación
- Si la energía llega a 0 → el robot **se detiene solo** hasta que el jugador lo recargue
- Si un robot trabaja en Zona C con energía < 20% → puede **romperse**

### 3. Zonas de Minería
- **3 zonas** disponibles, cada una con reserva limitada:
  - `Zona A — Cobre/Hierro` → común, bajo valor, robots duran más
  - `Zona B — Plata/Oro` → intermedio, valor medio, desgaste normal
  - `Zona C — Diamantes` → raro, alto valor, robots se desgastan rápido
- Cuando una zona **se agota**, avisa visualmente (parpadeo rojo en el mapa)
- El jugador debe **mover sus robots** o perderán ciclos sin recolectar nada

### 4. Fundidora (Elemento Reactivo)
- Procesa minerales en cola (FIFO) automáticamente
- Tiene una **barra de calor** (0–100%)
- Sube ~10% por cada lote procesado
- Baja sola muy lento (–2% por segundo)
- Si llega al 100% → **se sobrecalienta**, para 15 segundos
- El jugador puede pulsar **"Enfriar"** (–40% de calor, costo: $100) para evitarlo
- Con mejoras de nivel, la barra sube más lento y baja más rápido

### 5. Bodega (Elemento de Presión)
- Capacidad inicial: **50 minerales**
- Barra de ocupación visible siempre
- Si se llena → robots **no pueden descargar** → pierden tiempo (penalización pasiva)
- El jugador puede pulsar **"Vender ahora"** en cualquier momento para vaciarla
  - Venta normal: precio estándar por tipo de mineral
  - Si espera al **camión comprador** (evento cada ~90 seg): precio x1.5
- Mejorable con dinero (+50 de capacidad por mejora)

### 6. Contratos (Misiones con Presión de Tiempo)
- Aparecen **aleatoriamente** durante el juego (no al inicio de ronda)
- Cada contrato tiene:
  - Tipo de mineral requerido (ej. "25 unidades de Oro")
  - **Contador visual regresivo** (ej. 60 segundos)
  - Recompensa si se cumple
  - Penalización en dinero si se ignora o vence
- El jugador decide **aceptar o rechazar** al momento que aparece
- Solo pueden estar **2 contratos activos** al mismo tiempo
- Ejemplo de contrato urgente: "⚠️ URGENTE: 10 Diamantes en 30 seg → +$2,000"

### 7. Eventos Aleatorios en Tiempo Real
Cada cierto tiempo (aleatorio) ocurre un evento que requiere reacción del jugador:

| Evento                        | Descripción                                                  |
|-------------------------------|--------------------------------------------------------------|
| ⚡ Fallo eléctrico             | La fundidora para 10 seg salvo que el jugador pulse "Reset"  |
| 🚛 Comprador especial         | Paga x2 por cualquier mineral, disponible solo 10 segundos   |
| 🌋 Derrumbe en zona           | Una zona queda bloqueada 20 seg, robots deben moverse        |
| 🤖 Robot dañado               | Un robot aleatorio pierde 50% energía de golpe               |
| 💎 Veta rica                  | Una zona produce x2 minerales por 15 segundos                |

---

## 🏆 Condiciones de Victoria y Derrota

### Victoria
- Sobrevivir **10 rondas de 2 minutos** (20 min totales) con dinero positivo
- Meta de puntuación: acumular **$30,000** en ventas totales

### Derrota
- **Quedarse en $0 o negativo**
- **Todos los robots inactivos** (rotos o sin energía) sin dinero para repararlos
- Deuda acumulada por contratos fallidos supera el presupuesto

---

## 💰 Tabla de Precios (Referencia Inicial)

| Item                      | Costo / Ingreso        |
|---------------------------|------------------------|
| Robot Nivel 1             | $500                   |
| Robot Nivel 2             | $1,200                 |
| Robot Nivel 3             | $2,500                 |
| Recarga energía robot     | $50 por robot          |
| Reparación robot roto     | $200                   |
| Enfriar fundidora         | $100                   |
| Mejora Fundidora (nivel)  | $800                   |
| Mejora Bodega (+50 cap)   | $600                   |
| Venta Cobre/Hierro        | $10 por unidad         |
| Venta Plata               | $25 por unidad         |
| Venta Oro                 | $50 por unidad         |
| Venta Diamante            | $150 por unidad        |
| Penalización contrato     | -$300 a -$1,000        |

---

## 🖥️ Layout de la Interfaz (VentanaJuego)

```
┌──────────────────────────────────────────────────────────────────┐
│  💰 Dinero: $5,000   │  ⏱ Ronda: 1/10  │  Tiempo: 01:45         │
├────────────────┬─────────────────────────┬───────────────────────┤
│                │                         │  📋 CONTRATOS         │
│   🗺️ MAPA     │   🤖 ROBOTS             │  ┌──────────────────┐ │
│                │                         │  │ 25x Oro  ⏳45s   │ │
│  [Zona A 🟡]  │  Robot#1 ████░░ 70%⚡   │  │ Recomp: $1,500   │ │
│  [Zona B 🔵]  │  Zona: B  Estado: ✅    │  │ [Aceptar][Ignorar]│ │
│  [Zona C 💎]  │                         │  └──────────────────┘ │
│                │  Robot#2 ██░░░░ 30%⚡   │  ┌──────────────────┐ │
│                │  Zona: A  Estado: ✅    │  │ 10x Diamante ⏳20s│ │
│                │  [Recargar $50]         │  │ Recomp: $2,000   │ │
│                │                         │  └──────────────────┘ │
├────────────────┴─────────────────────────┴───────────────────────┤
│  🔥 FUNDIDORA  [████████░░] 80%  [⬇️ Enfriar $100]              │
│  📦 BODEGA     [███████░░░] 35/50        [💵 Vender Ahora]       │
├──────────────────────────────────────────────────────────────────┤
│  📜 LOG: [12:03] Robot#2 recolectó 8x Plata en Zona A           │
│          [12:04] ⚠️ Fundidora al 80% — ¡considera enfriarla!    │
└──────────────────────────────────────────────────────────────────┘
```

### Descripción de Paneles

| Panel           | Descripción                                                       |
|-----------------|-------------------------------------------------------------------|
| **Header**      | Dinero actual, ronda actual y tiempo restante de ronda            |
| **Mapa**        | Las 3 zonas con indicador visual de reserva restante              |
| **Robots**      | Estado de cada robot: barra de energía, zona, botones de acción   |
| **Contratos**   | Tarjetas de contratos activos con cuenta regresiva y botones      |
| **Fundidora**   | Barra de calor + botón de enfriar (aparece en rojo si > 70%)      |
| **Bodega**      | Barra de ocupación + botón de vender                              |
| **Log**         | Registro de eventos en tiempo real con timestamps                 |

---

## 📂 Clases a Modificar / Crear

### Modificar
| Clase              | Cambio Principal                                                |
|--------------------|-----------------------------------------------------------------|
| `Robot.java`       | Agregar: nivel, energía, zona asignada, estado (roto/activo)    |
| `Fundidora.java`   | Agregar: barra de calor, sobrecalentamiento, método enfriar()   |
| `Bodega.java`      | Agregar: evento venta inmediata, evento camión comprador        |
| `Simulador.java`   | Convertir en `GameEngine.java` con rondas, dinero y eventos     |
| `VentanaSimulador` | Convertir en `VentanaJuego.java` con el nuevo layout            |
| `Main.java`        | Apuntar a `VentanaJuego`                                        |

### Crear
| Clase nueva           | Descripción                                                   |
|-----------------------|---------------------------------------------------------------|
| `GameEngine.java`     | Orquestador: rondas, dinero, eventos aleatorios, victoria     |
| `Contrato.java`       | Representa un contrato: tipo, cantidad, tiempo, recompensa    |
| `GestorContratos.java`| Genera contratos aleatorios y evalúa cumplimiento             |
| `GestorEventos.java`  | Lanza eventos aleatorios en tiempo real (derrumbe, comprador) |
| `ZonaMina.java`       | Zona con tipo de mineral, reserva y estado (bloqueada/libre)  |
| `Tienda.java`         | Lógica de compra y mejora de robots/edificios                 |
| `VentanaJuego.java`   | Ventana principal del juego con todos los paneles             |
| `PanelMapa.java`      | Panel visual del mapa con zonas y robots asignados            |
| `PanelRobots.java`    | Panel de estado de robots con botones de acción               |
| `PanelContratos.java` | Panel de contratos activos con cuenta regresiva               |
| `PanelFundidora.java` | Panel de calor de fundidora con botón enfriar                 |
| `PanelBodega.java`    | Panel de bodega con botón vender                              |
| `PanelTienda.java`    | Ventana emergente de la tienda                                |

---

## 🔢 Orden de Implementación

1. **Fase 1 — Base del juego**
   - Crear `GameEngine.java` (rondas, dinero, estado general)
   - Modificar `Robot.java` (energía, nivel, zona, estado)
   - Crear `ZonaMina.java` (reservas, estado bloqueado)

2. **Fase 2 — Interactividad del jugador**
   - Modificar `Fundidora.java` (barra de calor, enfriar())
   - Modificar `Bodega.java` (venta inmediata, camión comprador)
   - Crear `GestorEventos.java` (eventos aleatorios)

3. **Fase 3 — Contratos**
   - Crear `Contrato.java` y `GestorContratos.java`
   - Integrar evaluación de contratos en `GameEngine`

4. **Fase 4 — Tienda**
   - Crear `Tienda.java` con compra y mejoras
   - Conectar tienda con `GameEngine` (descuento de dinero)

5. **Fase 5 — UI completa**
   - Crear `VentanaJuego.java` con el layout del diagrama
   - Crear todos los paneles (Mapa, Robots, Contratos, Fundidora, Bodega)
   - Crear `PanelTienda.java` como ventana emergente (JDialog)

6. **Fase 6 — Pulido final**
   - Pantalla de inicio (Nueva Partida / Dificultad)
   - Pantalla de fin (victoria/derrota con resumen)
   - Ajuste de balanceo (precios, tiempos, eventos)
   - Efectos visuales: parpadeos, colores de alerta

---

## 📝 Notas Técnicas

- Los **robots siguen siendo hilos** (Thread) — no cambia esa lógica base
- Los **eventos reactivos** se lanzan desde un hilo del `GestorEventos` que notifica a la UI via `SwingUtilities.invokeLater`
- Los **botones de acción** (enfriar, recargar, vender) simplemente llaman métodos de `GameEngine` que aplican el efecto y descuentan dinero
- La **cuenta regresiva de contratos** corre en un timer de Swing (javax.swing.Timer) para no bloquear la UI
- El **mapa** no necesita ser gráfico avanzado — con JPanels de colores y JLabels de zona es suficiente para la primera versión
- Guardar una referencia de `GameEngine` en `VentanaJuego` y pasarla a todos los paneles para que puedan leer el estado