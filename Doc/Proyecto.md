# Sistema Inteligente de Gestión de Estacionamiento

## Visión General

Sistema compuesto por **dos aplicaciones**:
- **App Móvil** (Android/iOS): para Visitantes y Pensionados.
- **App de Escritorio** (Java Swing): para Empleados y Administradores.

Ambas conectadas en tiempo real mediante **Firebase**.

---

## Stack Tecnológico

| Capa | Tecnología |
|---|---|
| App Móvil | React Native |
| App Escritorio | Java Swing |
| Autenticación | Firebase Authentication |
| Base de Datos | Firebase Firestore (NoSQL) |
| Tiempo Real | Firestore Listeners / Realtime Database |
| Almacenamiento | Firebase Storage (fotos de placas, etc.) |

---

## Roles y Funcionalidades

### Visitante (App Móvil)
- Registro e inicio de sesión con Firebase Auth
- Ver espacios disponibles en tiempo real
- Reservar espacio anticipado (pago por adelantado)
  - El espacio se bloquea 15 min antes de la hora reservada
  - Si no llega en los primeros 20 min → espacio liberado, sin reembolso
- Billetera virtual: recargar saldo
- Sistema de puntos de fidelidad: cada $100 gastados = 1 punto → 10 puntos = 1 hora gratis
- Historial de visitas y tickets

### Pensionado (App Móvil — Suscripción Mensual)
- Todo lo del Visitante
- Pago de mensualidad → desbloquea beneficios automáticamente
- No paga por hora mientras la suscripción esté activa
- Plan Familiar: registrar un segundo vehículo (+40% sobre mensualidad base)
  - Regla: no pueden estar ambos autos dentro al mismo tiempo
  - Si el segundo auto intenta entrar con el primero adentro → se cobra tarifa de Visitante
- "Liberar Mi Espacio" fines de semana → si un Visitante lo usa, el Pensionado recibe 10% como crédito

### Empleado (App de Escritorio)
- Login con Firebase Auth (rol restringido)
- Registrar entradas y salidas de vehículos
- Ver ocupación en tiempo real por zona
- Gestionar reubicaciones (overbooking entre zonas)
- Marcar placas con deuda pendiente
- Simular sensores de cajones (grid de botones con eventos)

### Administrador (App de Escritorio — Panel Completo)
- Todo lo del Empleado
- Gestionar tarifas dinámicas por horario:
  - 7:00–9:00 AM (hora pico): tarifa +30%
  - 2:00–4:00 PM (baja demanda): tarifa -20%
- Gestionar suscripciones de Pensionados
- Ver dashboard de ocupación predictiva (heatmap basado en historial 30 días)
- Reporte de rentabilidad por metro cuadrado por zona
- Ver cortes de caja y transacciones
- Gestionar alertas de placas marcadas

---

## Estados de un Espacio

```
DISPONIBLE → RESERVADO → OCUPADO → LIBERADO
                       ↘ EXPIRADO (reserva no reclamada)
```

Flag especial: `overbooking = true` cuando se reasigna a zona diferente.

---

## Estructura de Firestore

```
/usuarios/{uid}
  - nombre, email, rol (visitante/pensionado/empleado/admin)
  - saldo, puntos

/suscripciones/{uid}
  - activa: true/false
  - fechaVencimiento, placas[], planFamiliar: bool

/espacios/{espacioId}
  - zona, tipo (moto/normal/grande), estado
  - reservadoPor, horaBloqueo, overbooking: bool

/tickets/{ticketId}
  - placa, entrada, salida, total, overbooking: bool, bonoCanjado: bool

/transacciones/{txId}
  - uid, monto, tipo (recarga/pago/bono/credito)
  - fecha

/placasMarcadas/{placa}
  - deuda, motivo, fecha

/tarifas/{zonaId}
  - base, horariosPremium[], horariosEconomicos[]
```

---

## Modelo de Negocio Avanzado: "Precios Dinámicos y Bonos"
Tu idea inicial tiene precios fijos por zona. Podemos añadirle una capa de estrategia que simula la gestión de un negocio real.

Tarifas por Horario (Happy Hour / Hora Pico):

Lógica: En la base de datos, en lugar de un solo campo tarifa_hora, tendrías una tabla Tarifa_Zona_Horario.

Mejora Conceptual: De 7:00 AM a 9:00 AM (entrada de oficinas) la tarifa es Premium (+30%). De 2:00 PM a 4:00 PM (hora de baja demanda) la tarifa es Económica (-20%).

Objetivo: Demostrar manejo de fechas y reglas de negocio condicionales complejas.

Sistema de "Billetera Virtual" y Bonos de Fidelidad:

Idea Actual: Gestión de dinero simple.

Mejora: Los usuarios Visitantes pueden recargar saldo en la App. Cada vez que gastan $100, el sistema acumula 1 punto. Con 10 puntos, desbloquean un "Bono de 1 Hora Gratis" canjeable en la siguiente visita.

Impacto en el Proyecto: Requiere una tabla de Transacciones_Billetera y una tabla de Bonos_Disponibles. Es una excelente excusa para explicar Transacciones ACID en la memoria del proyecto.

2. Gestión de Estacionamiento Inteligente (Overbooking y Reservas)
Normalmente los proyectos de la universidad solo marcan "Ocupado/Libre". Vamos a añadir matices de negocio.

Reserva Anticipada de Espacios (Solo para Visitantes App):

Funcionalidad: Un visitante puede pagar por adelantado para "apartar" un espacio de Carro Normal en un horario específico (Ej: de 10:00 a 12:00).

Regla de Negocio Mejorada: El sistema debe bloquear ese espacio 15 minutos antes de la hora reservada. Si el usuario no llega en los primeros 20 minutos de su reserva, el sistema libera el espacio y no reembolsa el dinero (Política de penalización). Esto simula un reto de lógica de estados muy interesante: DISPONIBLE -> RESERVADO -> OCUPADO o EXPIRADO.

Gestión de Desbordamiento (Overflow):

Problema Real: ¿Qué pasa si la zona de Carros Normales está llena, pero la de Carros Grandes está vacía?

Mejora de la Idea: El sistema, al detectar que la zona destino está al 100%, puede ofrecerle al empleado (en la app de escritorio) la opción "Reubicación Temporal". El usuario paga tarifa de Carro Normal, pero se estaciona en zona Grande. El ticket queda marcado con un flag overbooking = true. Esto es oro para la sección de "Soluciones a Problemas Reales" en la documentación del proyecto.

3. Privilegios por Mensualidad: "El Club del Pensionado"
Vamos a expandir el concepto de "Privilegios" más allá de simplemente no pagar.

Plan Multivehículo Familiar:

Idea: Un pensionado paga una mensualidad base por 1 auto. Puede añadir un Segundo Auto (Cónyuge/Hijo) a su plan pagando un 40% adicional sobre su mensualidad.

Lógica de Validación: Al entrar la placa del segundo auto, el sistema debe validar que el "Auto Principal" no esté dentro del estacionamiento al mismo tiempo (Regla: Un cuerpo, un espacio). Si ambos autos intentan entrar, al segundo se le cobra tarifa de Visitante.

El "Parking Sharing" (Compartir Pensión):

Idea Avanzada: Los fines de semana, el espacio fijo del pensionado está vacío. La app podría permitirle "Liberar Mi Espacio" los sábados y domingos. Si un Visitante usa ese espacio, el Pensionado recibe un 10% del pago como crédito para su siguiente mensualidad. Esto introduce el concepto de Economía Colaborativa dentro del estacionamiento.

4. Aplicación de Escritorio: Panel de Control Ejecutivo
Para que la app de escritorio no sea solo una caja registradora, añade estos módulos de "Jefe de Piso":

Vista de Ocupación Predictiva (Heatmap):

En lugar de solo ver cuántos lugares hay libres ahora, el sistema analiza el historial de los últimos 30 días y muestra un gráfico de barras simple: "Se espera que la zona de Motos esté llena en 45 minutos". Se basa en calcular el promedio de entradas por hora del día de la semana actual.

Gestión de Incidentes y Placas "Marcadas":

Mejora de "Historial de Placas": Si una placa se va sin pagar (rompe la pluma en la vida real), el empleado puede "Marcar Placa". La próxima vez que ese auto intente entrar (incluso si es en 3 meses), el sistema lanza una Alerta Roja en la app de escritorio: *"ATENCIÓN: PLACA ABC-123 TIENE DEUDA PENDIENTE DE $45. NO PERMITIR SALIDA HASTA PAGO"*.

5. Base de Datos y Análisis (El "Plus" para la Memoria del Proyecto)
Como es un proyecto universitario, añadir una capa de Business Intelligence ficticia (o básica) impresiona mucho.

Reporte de Rentabilidad por Metro Cuadrado:

Idea: El sistema calcula automáticamente cuál zona genera más dinero por hora por espacio.

Hallazgo esperado: "Las Motos ocupan menos espacio pero tienen una rotación altísima, generando un 40% más de ingresos por metro cuadrado que los Carros Grandes."

Conclusión para la defensa del proyecto: El sistema podría recomendar al administrador convertir 2 espacios de Carro Grande en 5 espacios de Moto.

Simulación de Sensores (Estado de Cajones):

En lugar de que el empleado haga click para "Ocupar" manualmente, tu programa puede tener un Simulador de Hardware.

Propuesta: Una ventana aparte con un Grid de botones que parpadean. El profesor hace click en un botón (Simula sensor de piso) y automáticamente el sistema principal registra la entrada/salida. Esto demuestra Manejo de Eventos y Hilos (Threads) sin necesidad de hardware real.

6. Resumen de la "Versión Mejorada" del Proyecto
Si aplicas estas mejoras conceptuales, tu descripción del proyecto pasaría de ser un simple "Control de Estacionamiento" a un "Sistema Inteligente de Gestión de Espacios y Fidelización de Clientes".

Área	Idea Original	Idea Mejorada (Conceptual)
Precios	Fijo por zona	Dinámico por horario + Bonos de fidelidad
Ocupación	Binario (Libre/Ocupado)	Multiestado (Reservado, Liberado, Overbooking)
Pensionados	No pagan	Plan Familiar, Parking Sharing
Escritorio	Caja registradora	Dashboard Predictivo y Alerta de Morosos
Análisis	Corte de caja	Recomendador de Distribución de Zonas