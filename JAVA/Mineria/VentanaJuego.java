package Mineria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaJuego extends JFrame {
    private final GameEngine gameEngine;
    private final PanelMapa panelMapa;
    private final PanelRobots panelRobots;
    private final PanelContratos panelContratos;
    private final PanelFundidora panelFundidora;
    private final PanelBodega panelBodega;
    private final JTextArea areaLog;
    private JLabel lblDinero;
    private JLabel lblRonda;
    private JLabel lblTiempo;
    private JLabel lblEstado;
    private JLabel lblRobots;
    private final Timer timerRefresco;
    private final SimpleDateFormat formato = new SimpleDateFormat("mm:ss");
    private final long inicioSesion;
    private boolean juegoIniciado;
    private final int robotsIniciales;

    public VentanaJuego() {
        this(3);
    }

    public VentanaJuego(int robotsIniciales) {
        this.robotsIniciales = Math.max(1, robotsIniciales);
        this.gameEngine = new GameEngine(this.robotsIniciales);
        this.inicioSesion = System.currentTimeMillis();
        this.juegoIniciado = false;

        setTitle("MineRush - Ventana de Juego");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1440, 920));
        setPreferredSize(new Dimension(1440, 920));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(14, 18, 28));

        add(crearCabecera(), BorderLayout.NORTH);

        panelMapa = new PanelMapa(gameEngine);
        panelRobots = new PanelRobots(gameEngine, this::agregarLog);
        panelContratos = new PanelContratos(gameEngine, this::agregarLog);
        panelFundidora = new PanelFundidora(gameEngine, this::agregarLog);
        panelBodega = new PanelBodega(gameEngine, this::agregarLog);

        JSplitPane centro = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelMapa, panelRobots);
        centro.setResizeWeight(0.45);
        centro.setBorder(null);
        centro.setDividerLocation(520);
        centro.setContinuousLayout(true);
        add(centro, BorderLayout.CENTER);

        JPanel derecha = new JPanel(new BorderLayout(10, 10));
        derecha.setOpaque(false);
        derecha.add(panelContratos, BorderLayout.CENTER);
        derecha.add(crearAccionesLaterales(), BorderLayout.SOUTH);
        add(derecha, BorderLayout.EAST);

        JPanel inferior = new JPanel(new BorderLayout(10, 10));
        inferior.setOpaque(false);
        inferior.add(panelFundidora, BorderLayout.NORTH);
        inferior.add(panelBodega, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        areaLog = new JTextArea(7, 80);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaLog.setBackground(new Color(9, 12, 18));
        areaLog.setForeground(new Color(220, 230, 240));
        areaLog.setCaretColor(Color.WHITE);
        areaLog.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(60, 70, 90)), "Registro en tiempo real"));
        add(scrollLog, BorderLayout.WEST);

        timerRefresco = new Timer(500, e -> refrescarVista());
        timerRefresco.start();
        refrescarVista();
    }

    private JComponent crearCabecera() {
        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(12, 12, 0, 12));

        JPanel barra = new JPanel(new GridLayout(1, 5, 10, 10));
        barra.setOpaque(false);

        lblDinero = crearTarjetaCabezera("Dinero", "$5,000");
        lblRonda = crearTarjetaCabezera("Ronda", "1/10");
        lblTiempo = crearTarjetaCabezera("Tiempo", "00:00");
        lblRobots = crearTarjetaCabezera("Robots", String.valueOf(robotsIniciales));
        lblEstado = crearTarjetaCabezera("Estado", "Preparado");

        barra.add(lblDinero);
        barra.add(lblRonda);
        barra.add(lblTiempo);
        barra.add(lblRobots);
        barra.add(lblEstado);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        acciones.setOpaque(false);
        JButton btnIniciar = new JButton("Iniciar partida");
        JButton btnTienda = new JButton("Tienda");
        JButton btnDetener = new JButton("Detener");
        JButton btnSalir = new JButton("Salir");

        btnIniciar.addActionListener(this::iniciarJuego);
        btnTienda.addActionListener(e -> abrirTienda());
        btnDetener.addActionListener(e -> detenerJuego());
        btnSalir.addActionListener(e -> dispose());

        estilizarBoton(btnIniciar, new Color(41, 128, 185));
        estilizarBoton(btnTienda, new Color(127, 140, 141));
        estilizarBoton(btnDetener, new Color(192, 57, 43));
        estilizarBoton(btnSalir, new Color(44, 62, 80));

        acciones.add(btnIniciar);
        acciones.add(btnTienda);
        acciones.add(btnDetener);
        acciones.add(btnSalir);

        contenedor.add(barra, BorderLayout.CENTER);
        contenedor.add(acciones, BorderLayout.SOUTH);
        return contenedor;
    }

    private JLabel crearTarjetaCabezera(String titulo, String valorInicial) {
        JLabel etiqueta = new JLabel("<html><div style='padding:6px 10px;'><span style='color:#9fb3c8;font-size:11px;'>" + titulo + "</span><br><span style='font-size:18px;color:white;'><b>" + valorInicial + "</b></span></div></html>");
        etiqueta.setBorder(BorderFactory.createLineBorder(new Color(56, 68, 84), 1, true));
        etiqueta.setOpaque(true);
        etiqueta.setBackground(new Color(23, 28, 38));
        return etiqueta;
    }

    private JPanel crearAccionesLaterales() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 8, 8));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton btnActualizar = new JButton("Actualizar vista");
        btnActualizar.addActionListener(e -> refrescarVista());
        estilizarBoton(btnActualizar, new Color(52, 73, 94));

        JButton btnContrato = new JButton("Revisar contratos");
        btnContrato.addActionListener(e -> panelContratos.actualizar());
        estilizarBoton(btnContrato, new Color(22, 160, 133));

        panel.add(btnActualizar);
        panel.add(btnContrato);
        return panel;
    }

    private void estilizarBoton(JButton boton, Color color) {
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
    }

    private void iniciarJuego(ActionEvent event) {
        if (!juegoIniciado) {
            gameEngine.iniciar();
            juegoIniciado = true;
            agregarLog("Partida iniciada con " + robotsIniciales + " robots.");
        }
    }

    private void detenerJuego() {
        gameEngine.detener();
        juegoIniciado = false;
        agregarLog("Partida detenida manualmente.");
    }

    private void abrirTienda() {
        PanelTienda dialogo = new PanelTienda(this, gameEngine, this::agregarLog);
        dialogo.setVisible(true);
    }

    public void agregarLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            String tiempo = new SimpleDateFormat("HH:mm:ss").format(new Date());
            areaLog.append("[" + tiempo + "] " + mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    private void refrescarVista() {
        if (!gameEngine.isJuegoActivo()) {
            juegoIniciado = false;
        }
        lblDinero.setText("<html><div style='padding:6px 10px;'><span style='color:#9fb3c8;font-size:11px;'>Dinero</span><br><span style='font-size:18px;color:white;'><b>$" + gameEngine.getDinero() + "</b></span></div></html>");
        lblRonda.setText("<html><div style='padding:6px 10px;'><span style='color:#9fb3c8;font-size:11px;'>Ronda</span><br><span style='font-size:18px;color:white;'><b>" + gameEngine.getRondaActual() + "/" + gameEngine.getRondasTotales() + "</b></span></div></html>");
        long segundos = Math.max(0, (System.currentTimeMillis() - inicioSesion) / 1000);
        lblTiempo.setText("<html><div style='padding:6px 10px;'><span style='color:#9fb3c8;font-size:11px;'>Tiempo</span><br><span style='font-size:18px;color:white;'><b>" + formato.format(new Date(segundos * 1000)) + "</b></span></div></html>");
        lblRobots.setText("<html><div style='padding:6px 10px;'><span style='color:#9fb3c8;font-size:11px;'>Robots</span><br><span style='font-size:18px;color:white;'><b>" + gameEngine.getRobots().size() + "</b></span></div></html>");
        lblEstado.setText("<html><div style='padding:6px 10px;'><span style='color:#9fb3c8;font-size:11px;'>Estado</span><br><span style='font-size:18px;color:white;'><b>" + (gameEngine.isJuegoActivo() ? "En curso" : "En pausa") + "</b></span></div></html>");

        panelMapa.actualizar();
        panelRobots.actualizar();
        panelContratos.actualizar();
        panelFundidora.actualizar();
        panelBodega.actualizar();
    }
}
