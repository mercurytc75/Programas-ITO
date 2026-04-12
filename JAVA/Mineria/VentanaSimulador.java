package Mineria;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaSimulador extends JFrame {

    private JTextField txtRobots;
    private JButton btnIniciar, btnDetener, btnReiniciar;
    private JTextArea txtLog;
    private JLabel lblRefinado, lblBodega, lblRobots, lblEstado;
    private JProgressBar progresoBodega;

    private Simulador simulador;
    private Thread threadSimulacion;
    private Thread threadActualizacion;
    private boolean simulacionEnCurso;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public VentanaSimulador() {
        setTitle("Simulador de Minería - Control");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Título
        JLabel lblTitulo = new JLabel("Sistema de Minería");
        lblTitulo.setBounds(20, 10, 400, 20);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo);

        // Panel de control
        JLabel lblCantidad = new JLabel("Cantidad de Robots:");
        lblCantidad.setBounds(20, 40, 120, 25);
        add(lblCantidad);

        txtRobots = new JTextField("3");
        txtRobots.setBounds(140, 40, 50, 25);
        add(txtRobots);

        // Botones
        btnIniciar = new JButton("▶ Iniciar");
        btnIniciar.setBounds(200, 40, 100, 25);
        btnIniciar.setBackground(new Color(50, 150, 50));
        btnIniciar.setForeground(Color.WHITE);
        add(btnIniciar);

        btnDetener = new JButton("⏹ Detener");
        btnDetener.setBounds(310, 40, 100, 25);
        btnDetener.setBackground(new Color(200, 50, 50));
        btnDetener.setForeground(Color.WHITE);
        btnDetener.setEnabled(false);
        add(btnDetener);

        btnReiniciar = new JButton("🔄 Reiniciar");
        btnReiniciar.setBounds(420, 40, 100, 25);
        btnReiniciar.setBackground(new Color(50, 50, 200));
        btnReiniciar.setForeground(Color.WHITE);
        add(btnReiniciar);

        // Estado
        lblEstado = new JLabel("Estado: Inactivo");
        lblEstado.setBounds(530, 40, 300, 25);
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        add(lblEstado);

        // Área de logs
        JLabel lblLogs = new JLabel("Registro de Operaciones:");
        lblLogs.setBounds(20, 75, 400, 15);
        lblLogs.setFont(new Font("Arial", Font.BOLD, 11));
        add(lblLogs);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setBackground(new Color(240, 240, 240));
        txtLog.setFont(new Font("Courier New", Font.PLAIN, 10));
        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setBounds(20, 95, 500, 400);
        add(scroll);

        // Panel de estadísticas
        JLabel lblStats = new JLabel("ESTADÍSTICAS EN TIEMPO REAL");
        lblStats.setBounds(530, 75, 300, 20);
        lblStats.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblStats);

        // Refinado
        lblRefinado = new JLabel("Mineral Refinado: 0");
        lblRefinado.setBounds(530, 110, 300, 25);
        lblRefinado.setFont(new Font("Arial", Font.PLAIN, 11));
        add(lblRefinado);

        // Bodega con barra de progreso
        JLabel lblBodegaLbl = new JLabel(" Bodega Almacenada:");
        lblBodegaLbl.setBounds(530, 145, 300, 25);
        lblBodegaLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        add(lblBodegaLbl);

        lblBodega = new JLabel("0 / 500clacl");
        lblBodega.setBounds(530, 165, 300, 20);
        add(lblBodega);

        progresoBodega = new JProgressBar(0, 500);
        progresoBodega.setBounds(530, 185, 300, 20);
        progresoBodega.setStringPainted(true);
        add(progresoBodega);

        // Robots activos
        lblRobots = new JLabel("Robots Activos: 0/0");
        lblRobots.setBounds(530, 220, 300, 25);
        lblRobots.setFont(new Font("Arial", Font.PLAIN, 11));
        add(lblRobots);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setBounds(530, 260, 300, 1);
        add(sep);

        // Info adicional
        JLabel lblInfo = new JLabel("<html><b>Información:</b><br>" +
                "• Robots recolectan minerales<br>" +
                "• Bodega almacena hasta 50<br>" +
                "• Fundidora refina los minerales<br>" +
                "• Simulación automática</html>");
        lblInfo.setBounds(530, 270, 300, 120);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        add(lblInfo);

        eventos();
    }

    private void eventos() {
        btnIniciar.addActionListener(e -> iniciarSimulacion());
        btnDetener.addActionListener(e -> detenerSimulacion());
        btnReiniciar.addActionListener(e -> reiniciarSimulador());
    }

    private void iniciarSimulacion() {
        try {
            int cantRobots = Integer.parseInt(txtRobots.getText());
            if (cantRobots <= 0 || cantRobots > 20) {
                mostrarError("Ingresa entre 1 y 20 robots");
                return;
            }

            simulacionEnCurso = true;
            txtRobots.setEnabled(false);
            btnIniciar.setEnabled(false);
            btnDetener.setEnabled(true);
            txtLog.setText("");

            simulador = new Simulador(cantRobots);
            log("Iniciando simulación con " + cantRobots + " robots");
            Log("Hora: " + sdf.format(new Date()));

            // Thread para ejecutar la simulación
            threadSimulacion = new Thread(() -> {
                simulador.iniciar();
                
            });
            threadSimulacion.start();

            // Thread para actualizar UI
            iniciarActualizacionUI();

        } catch (NumberFormatException ex) {
            mostrarError("Ingresa un número válido de robots");
        }
    }
    private  void reiniciarSimulador() {
        if (simulacionEnCurso) {
            detenerSimulacion();
        }
        iniciarSimulacion();
    }

    private void detenerSimulacion() {
        if (simulacionEnCurso && simulador != null) {
            simulador.detener();
            if (threadSimulacion != null && threadSimulacion.isAlive()) {
                threadSimulacion.interrupt();
            }
            if (threadActualizacion != null && threadActualizacion.isAlive()) {
                threadActualizacion.interrupt();
            }
            finalizarSimulacion("Simulación detenida");
        }
    }

    private void iniciarActualizacionUI() {
        threadActualizacion = new Thread(() -> {
            while (simulacionEnCurso && simulador != null) {
                try {
                    Thread.sleep(500);
                    actualizarEtadisticas();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        threadActualizacion.setDaemon(true);
        threadActualizacion.start();
    }

    private synchronized void actualizarEtadisticas() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (simulador == null) return;

                int mineralRefinado = simulador.getFundidora().getMineralRefinado();
                int bodegaActual = simulador.getBodega().getCantidadAlmacenada();
                int robotsActivos = simulador.getRobotsActivos();
                int robotsTotal = simulador.getRobotsActivos(); // puedes ajustar esto

                lblRefinado.setText("Mineral Refinado: " + mineralRefinado);
                lblBodega.setText(bodegaActual + " / 500");
                lblRobots.setText("Robots Activos: " + robotsActivos);

                progresoBodega.setValue(bodegaActual);

                if (bodegaActual >= 450) {
                    lblEstado.setText("Estado: BODEGA CASI LLENA");
                    lblEstado.setForeground(new Color(255, 100, 0));
                } else if (robotsActivos == 0) {
                    lblEstado.setText("Estado: COMPLETADO");
                    lblEstado.setForeground(new Color(50, 150, 50));
                } else {
                    lblEstado.setText("Estado: EN EJECUCIÓN (" + robotsActivos + " robots activos)");
                    lblEstado.setForeground(new Color(0, 100, 200));
                }
            } catch (Exception e) {
                Log("Error al actualizar: " + e.getMessage());
            }
        });
    }

    private void finalizarSimulacion(String razon) {
        simulacionEnCurso = false;
        SwingUtilities.invokeLater(() -> {
            txtRobots.setEnabled(true);
            btnIniciar.setEnabled(true);
            btnDetener.setEnabled(false);
            log("📊 " + razon);
            Log("⏱ Fin: " + sdf.format(new Date()));
        });
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            txtLog.append("[" + sdf.format(new Date()) + "] " + msg + "\n");
            txtLog.setCaretPosition(txtLog.getDocument().getLength());
        });
    }

    private void Log(String msg) {
        SwingUtilities.invokeLater(() -> {
            txtLog.append(msg + "\n");
            txtLog.setCaretPosition(txtLog.getDocument().getLength());
        });
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de Entrada", JOptionPane.ERROR_MESSAGE);
    }
}