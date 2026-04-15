package Monopoly;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class MonopolyFrame extends JFrame {

    private static final Color FONDO_PRINCIPAL = new Color(16, 24, 40);
    private static final Color FONDO_SECUNDARIO = new Color(27, 39, 58);
    private static final Color TEXTO_CLARO = new Color(245, 247, 250);
    private static final Color ACENTO = new Color(240, 200, 86);
    private static final Font TITULO = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font SUBTITULO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TEXTO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font TEXTO_PEQUENO = new Font(
        "Segoe UI",
        Font.PLAIN,
        11
    );

    private GameEngine gameController;
    private final JPanel tableroPanel;
    private final JPanel[] casillaPaneles;
    private final JLabel[] casillaTituloLabels;
    private final JLabel[] casillaPosicionLabels;
    private final JLabel[] casillaDetalleLabels;
    private final JTextArea registroArea;
    private final JLabel estadoLabel;
    private final JLabel turnoLabel;
    private final JLabel ganadorLabel;
    private final JButton siguienteTurnoButton;
    private final JButton reiniciarButton;
    private final JButton modoAutomaticoButton;
    private final JButton juegoRealButton;
    private final DefaultListModel<String> jugadoresModel;
    private final JList<String> jugadoresList;
    private Timer autoTurnTimer;

    public MonopolyFrame() {
        super("Monopoly - Interfaz Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1380, 900));
        setLocationRelativeTo(null);

        tableroPanel = new JPanel(new GridLayout(5, 8, 6, 6));
        tableroPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        tableroPanel.setBackground(FONDO_PRINCIPAL);

        casillaPaneles = new JPanel[40];
        casillaTituloLabels = new JLabel[40];
        casillaPosicionLabels = new JLabel[40];
        casillaDetalleLabels = new JLabel[40];

        registroArea = new JTextArea();
        estadoLabel = new JLabel("Partida lista para comenzar");
        turnoLabel = new JLabel("Turno 0");
        ganadorLabel = new JLabel("Sin ganador todavía");
        siguienteTurnoButton = new JButton("Siguiente turno");
        reiniciarButton = new JButton("Reiniciar partida");
        modoAutomaticoButton = new JButton("Modo automático");
        juegoRealButton = new JButton("Jugar");
        jugadoresModel = new DefaultListModel<>();
        jugadoresList = new JList<>(jugadoresModel);

        configurarAparienciaGeneral();
        crearJuegoInicial();
        construirInterfaz();
        actualizarInterfaz();
    }

    private void configurarAparienciaGeneral() {
        getContentPane().setBackground(FONDO_PRINCIPAL);

        registroArea.setEditable(false);
        registroArea.setLineWrap(true);
        registroArea.setWrapStyleWord(true);
        registroArea.setRows(10);
        registroArea.setBackground(new Color(13, 18, 28));
        registroArea.setForeground(TEXTO_CLARO);
        registroArea.setFont(TEXTO);
        registroArea.setBorder(new EmptyBorder(12, 12, 12, 12));

        jugadoresList.setBackground(FONDO_SECUNDARIO);
        jugadoresList.setForeground(TEXTO_CLARO);
        jugadoresList.setFont(TEXTO);
        jugadoresList.setBorder(new EmptyBorder(8, 8, 8, 8));
        jugadoresList.setCellRenderer(new RendererJugadores());

        siguienteTurnoButton.setFont(TEXTO);
        siguienteTurnoButton.setBackground(ACENTO);
        siguienteTurnoButton.setForeground(Color.BLACK);
        siguienteTurnoButton.setFocusPainted(false);

        reiniciarButton.setFont(TEXTO);
        reiniciarButton.setBackground(new Color(64, 78, 100));
        reiniciarButton.setForeground(TEXTO_CLARO);
        reiniciarButton.setFocusPainted(false);

        modoAutomaticoButton.setFont(TEXTO);
        modoAutomaticoButton.setBackground(new Color(76, 150, 245));
        modoAutomaticoButton.setForeground(Color.BLACK);
        modoAutomaticoButton.setFocusPainted(false);

        juegoRealButton.setFont(TEXTO);
        juegoRealButton.setBackground(new Color(96, 115, 140));
        juegoRealButton.setForeground(TEXTO_CLARO);
        juegoRealButton.setFocusPainted(false);

        siguienteTurnoButton.addActionListener(evento -> ejecutarTurno());
        reiniciarButton.addActionListener(evento -> reiniciarPartida());
        modoAutomaticoButton.addActionListener(evento ->
            activarModoAutomatico()
        );
        juegoRealButton.addActionListener(evento -> activarJuegoReal());

        autoTurnTimer = new Timer(900, evento -> {
            if (!gameController.estaTerminado()) {
                ejecutarTurno();
            } else {
                autoTurnTimer.stop();
            }
        });
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(FONDO_PRINCIPAL);

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        add(crearPanelLateral(), BorderLayout.EAST);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(FONDO_SECUNDARIO);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel titulo = new JLabel("MONOPOLY");
        titulo.setFont(TITULO);
        titulo.setForeground(ACENTO);

        JLabel subtitulo = new JLabel(
            "Tablero interactivo en Swing con turnos, registro y estado de jugadores"
        );
        subtitulo.setFont(SUBTITULO);
        subtitulo.setForeground(TEXTO_CLARO);

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
        texto.add(titulo);
        texto.add(Box.createVerticalStrut(6));
        texto.add(subtitulo);

        JPanel estado = new JPanel();
        estado.setOpaque(false);
        estado.setLayout(new BoxLayout(estado, BoxLayout.Y_AXIS));
        turnoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        turnoLabel.setForeground(TEXTO_CLARO);
        estadoLabel.setFont(TEXTO);
        estadoLabel.setForeground(TEXTO_CLARO);
        ganadorLabel.setFont(TEXTO);
        ganadorLabel.setForeground(ACENTO);
        estado.add(turnoLabel);
        estado.add(Box.createVerticalStrut(6));
        estado.add(estadoLabel);
        estado.add(Box.createVerticalStrut(4));
        estado.add(ganadorLabel);

        panel.add(texto, BorderLayout.WEST);
        panel.add(estado, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(FONDO_PRINCIPAL);
        panel.setBorder(new EmptyBorder(0, 12, 0, 12));

        construirTablero();

        JScrollPane scrollTablero = new JScrollPane(tableroPanel);
        scrollTablero.setBorder(BorderFactory.createEmptyBorder());
        scrollTablero.getViewport().setBackground(FONDO_PRINCIPAL);
        scrollTablero.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollTablero.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        panel.add(scrollTablero, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setBackground(FONDO_PRINCIPAL);
        panel.setBorder(new EmptyBorder(12, 0, 12, 12));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel botones = new JPanel(new GridLayout(2, 2, 8, 8));
        botones.setOpaque(false);
        botones.add(siguienteTurnoButton);
        botones.add(reiniciarButton);
        botones.add(modoAutomaticoButton);
        botones.add(juegoRealButton);

        JPanel jugadoresPanel = new JPanel(new BorderLayout(8, 8));
        jugadoresPanel.setBackground(FONDO_SECUNDARIO);
        jugadoresPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(54, 68, 90)),
                new EmptyBorder(12, 12, 12, 12)
            )
        );

        JLabel titulo = new JLabel("Jugadores");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ACENTO);
        jugadoresPanel.add(titulo, BorderLayout.NORTH);

        JScrollPane scrollJugadores = new JScrollPane(jugadoresList);
        scrollJugadores.setBorder(BorderFactory.createEmptyBorder());
        scrollJugadores.getViewport().setBackground(FONDO_SECUNDARIO);
        jugadoresPanel.add(scrollJugadores, BorderLayout.CENTER);

        panel.add(botones);
        panel.add(Box.createVerticalStrut(12));
        panel.add(jugadoresPanel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearPanelResumen());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FONDO_SECUNDARIO);
        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(54, 68, 90)),
                new EmptyBorder(12, 12, 12, 12)
            )
        );

        JLabel titulo = new JLabel("Reglas rápidas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ACENTO);

        JLabel linea1 = new JLabel("• Cada turno tira dos dados.");
        JLabel linea2 = new JLabel(
            "• Las casillas especiales aplican su efecto."
        );
        JLabel linea3 = new JLabel("• Reinicia la partida cuando quieras.");

        for (JLabel etiqueta : List.of(titulo, linea1, linea2, linea3)) {
            etiqueta.setForeground(TEXTO_CLARO);
            etiqueta.setFont(TEXTO_PEQUENO);
            etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(etiqueta);
            panel.add(Box.createVerticalStrut(6));
        }

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(FONDO_PRINCIPAL);
        panel.setBorder(new EmptyBorder(0, 12, 12, 12));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(FONDO_PRINCIPAL);

        JLabel titulo = new JLabel("Registro de partida");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(ACENTO);
        encabezado.add(titulo, BorderLayout.WEST);

        panel.add(encabezado, BorderLayout.NORTH);

        JScrollPane scrollRegistro = new JScrollPane(registroArea);
        scrollRegistro.setBorder(
            BorderFactory.createLineBorder(new Color(54, 68, 90))
        );
        scrollRegistro.setPreferredSize(new Dimension(0, 230));
        scrollRegistro.setMinimumSize(new Dimension(0, 180));
        scrollRegistro.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        panel.add(scrollRegistro, BorderLayout.CENTER);

        return panel;
    }

    private void construirTablero() {
        tableroPanel.removeAll();

        for (
            int posicion = 0;
            posicion < gameController.getTablero().getTotalCasillas();
            posicion++
        ) {
            JPanel casilla = new JPanel(new BorderLayout(2, 2));
            casilla.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(45, 56, 74)),
                    new EmptyBorder(6, 6, 6, 6)
                )
            );
            casilla.setOpaque(true);

            JLabel posicionLabel = new JLabel(
                "#" + posicion,
                SwingConstants.CENTER
            );
            posicionLabel.setFont(TEXTO_PEQUENO);
            posicionLabel.setForeground(Color.DARK_GRAY);

            JLabel tituloLabel = new JLabel("", SwingConstants.CENTER);
            tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            tituloLabel.setForeground(Color.BLACK);
            tituloLabel.setVerticalAlignment(SwingConstants.CENTER);

            JLabel detalleLabel = new JLabel("", SwingConstants.CENTER);
            detalleLabel.setFont(TEXTO_PEQUENO);
            detalleLabel.setForeground(Color.DARK_GRAY);

            casilla.add(posicionLabel, BorderLayout.NORTH);
            casilla.add(tituloLabel, BorderLayout.CENTER);
            casilla.add(detalleLabel, BorderLayout.SOUTH);

            casillaPaneles[posicion] = casilla;
            casillaPosicionLabels[posicion] = posicionLabel;
            casillaTituloLabels[posicion] = tituloLabel;
            casillaDetalleLabels[posicion] = detalleLabel;
            tableroPanel.add(casilla);
        }

        tableroPanel.revalidate();
        tableroPanel.repaint();
    }

    private void crearJuegoInicial() {
        gameController = new GameEngine();
        gameController.agregarJugador(new Jugador(1, "Juan"));
        gameController.agregarJugador(new Jugador(2, "María"));
        gameController.agregarJugador(new Jugador(3, "Carlos"));
        activarJuegoReal();
        registroArea.setText("Partida creada con Juan, María y Carlos.\n");
    }

    private void reiniciarPartida() {
        boolean autoActivo = autoTurnTimer != null && autoTurnTimer.isRunning();
        crearJuegoInicial();
        siguienteTurnoButton.setEnabled(true);
        actualizarInterfaz();

        if (autoActivo) {
            activarModoAutomatico();
        }
    }

    private void ejecutarTurno() {
        String evento = gameController.avanzarTurno();
        if (evento != null && !evento.isBlank()) {
            appendRegistro(evento);
        }

        actualizarInterfaz();

        if (gameController.estaTerminado()) {
            if (autoTurnTimer != null && autoTurnTimer.isRunning()) {
                autoTurnTimer.stop();
            }
            siguienteTurnoButton.setEnabled(false);
            if (gameController.getGanador() != null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Ganador: " +
                        gameController.getGanador().getNombre() +
                        " con $" +
                        gameController.getGanador().getDinero(),
                    "Partida finalizada",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    private void actualizarInterfaz() {
        actualizarJugadores();
        actualizarTablero();

        turnoLabel.setText("Turno: " + gameController.getTurnoActual());
        Jugador jugadorEnTurno = gameController.getJugadorEnTurno();
        estadoLabel.setText(
            jugadorEnTurno != null
                ? "Siguiente jugador: " + jugadorEnTurno.getNombre()
                : "No hay jugador en turno"
        );

        Jugador ganador = gameController.getGanador();
        ganadorLabel.setText(
            ganador != null
                ? "Ganador: " +
                  ganador.getNombre() +
                  " ($" +
                  ganador.getDinero() +
                  ")"
                : "Sin ganador todavía"
        );

        if (gameController.estaTerminado()) {
            siguienteTurnoButton.setEnabled(false);
        }
    }

    private void actualizarJugadores() {
        jugadoresModel.clear();
        for (Jugador jugador : gameController.obtenerJugadores()) {
            String estado = jugador.estaBancarro()
                ? "Bancarrota"
                : jugador.estaEnCarcel()
                    ? "Cárcel"
                    : "Activo";

            jugadoresModel.addElement(
                jugador.getNombre() +
                    " | $" +
                    jugador.getDinero() +
                    " | Pos: " +
                    jugador.getPosicion() +
                    " | Prop: " +
                    jugador.getPropiedades().tamanio() +
                    " | " +
                    estado
            );
        }
    }

    private void actualizarTablero() {
        Map<Integer, List<String>> ocupantes = new HashMap<>();
        for (Jugador jugador : gameController.obtenerJugadores()) {
            if (jugador.estaBancarro()) {
                continue;
            }

            ocupantes
                .computeIfAbsent(jugador.getPosicion(), posicion ->
                    new ArrayList<>()
                )
                .add(jugador.getNombre());
        }

        for (
            int posicion = 0;
            posicion < gameController.getTablero().getTotalCasillas();
            posicion++
        ) {
            Casilla casilla = gameController.getTablero().getCasilla(posicion);
            String nombreCasilla =
                casilla != null
                    ? casilla.getNombre()
                    : gameController.getTablero().getNombreCasilla(posicion);
            String detalle = ocupantes.containsKey(posicion)
                ? String.join(", ", ocupantes.get(posicion))
                : "";

            casillaPaneles[posicion].setBackground(colorCasilla(casilla));
            casillaTituloLabels[posicion].setText(nombreCasilla);
            casillaDetalleLabels[posicion].setText(
                detalle.isBlank() ? "" : "Jugadores: " + detalle
            );
            casillaPosicionLabels[posicion].setText("#" + posicion);
        }
    }

    private Color colorCasilla(Casilla casilla) {
        if (casilla == null) {
            return new Color(240, 229, 194);
        }

        if (casilla instanceof Salida) {
            return new Color(114, 190, 120);
        }

        if (casilla instanceof Carcel) {
            return new Color(233, 115, 115);
        }

        if (casilla instanceof Impuesto) {
            return new Color(241, 196, 103);
        }

        if (casilla instanceof EstacionamientoLibre) {
            return new Color(144, 174, 219);
        }

        return new Color(220, 225, 235);
    }

    private void appendRegistro(String evento) {
        if (registroArea.getText().isBlank()) {
            registroArea.setText(evento + "\n");
        } else {
            registroArea.append(evento + "\n");
        }

        registroArea.setCaretPosition(registroArea.getDocument().getLength());
    }

    private void activarModoAutomatico() {
        gameController.setModoAutomatico(true);
        gameController.setPurchaseDecisionStrategy(
            (jugador, propiedadCasilla) -> true
        );
        siguienteTurnoButton.setEnabled(false);
        if (!autoTurnTimer.isRunning()) {
            autoTurnTimer.start();
        }
        estadoLabel.setText("Modo automático activo");
    }

    private void activarJuegoReal() {
        gameController.setModoAutomatico(false);
        gameController.setPurchaseDecisionStrategy(this::preguntarCompra);
        if (autoTurnTimer.isRunning()) {
            autoTurnTimer.stop();
        }
        if (!gameController.estaTerminado()) {
            siguienteTurnoButton.setEnabled(true);
        }
        estadoLabel.setText("Juego real activo (compra opcional)");
    }

    private boolean preguntarCompra(
        Jugador jugador,
        PropiedadCasilla propiedadCasilla
    ) {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            jugador.getNombre() +
                " cayó en " +
                propiedadCasilla.getNombre() +
                "\nPrecio: $" +
                propiedadCasilla.getPropiedad().getPrecio() +
                "\nDinero actual: $" +
                jugador.getDinero() +
                "\n¿Deseas comprarla?",
            "Compra de propiedad",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return opcion == JOptionPane.YES_OPTION;
    }

    private static class RendererJugadores implements ListCellRenderer<String> {

        @Override
        public Component getListCellRendererComponent(
            JList<? extends String> lista,
            String valor,
            int indice,
            boolean seleccionado,
            boolean foco
        ) {
            JLabel etiqueta = new JLabel(valor);
            etiqueta.setOpaque(true);
            etiqueta.setFont(TEXTO);
            etiqueta.setBorder(new EmptyBorder(6, 8, 6, 8));
            etiqueta.setForeground(seleccionado ? Color.BLACK : TEXTO_CLARO);
            etiqueta.setBackground(seleccionado ? ACENTO : FONDO_SECUNDARIO);
            return etiqueta;
        }
    }

    public static void abrir() {
        SwingUtilities.invokeLater(() -> new MonopolyFrame().setVisible(true));
    }
}
