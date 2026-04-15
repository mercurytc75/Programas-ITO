package Mineria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class PanelRobots extends JPanel {
    private final GameEngine gameEngine;
    private final Consumer<String> logger;
    private final JTextArea areaRobots;
    private final JComboBox<Integer> comboRobot;
    private final JComboBox<String> comboZona;

    public PanelRobots(GameEngine gameEngine, Consumer<String> logger) {
        this.gameEngine = gameEngine;
        this.logger = logger;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(22, 28, 38));

        JLabel titulo = new JLabel("Robots");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        add(titulo, BorderLayout.NORTH);

        areaRobots = new JTextArea();
        areaRobots.setEditable(false);
        areaRobots.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaRobots.setBackground(new Color(14, 18, 26));
        areaRobots.setForeground(new Color(225, 235, 245));
        add(new JScrollPane(areaRobots), BorderLayout.CENTER);

        JPanel controles = new JPanel(new GridLayout(2, 1, 8, 8));
        controles.setOpaque(false);

        JPanel fila1 = new JPanel(new GridLayout(1, 2, 8, 8));
        fila1.setOpaque(false);
        comboRobot = new JComboBox<>();
        comboZona = new JComboBox<>(new String[]{"A", "B", "C"});
        fila1.add(comboRobot);
        fila1.add(comboZona);

        JPanel fila2 = new JPanel(new GridLayout(1, 3, 8, 8));
        fila2.setOpaque(false);
        JButton btnRecargar = new JButton("Recargar");
        JButton btnReparar = new JButton("Reparar");
        JButton btnAsignar = new JButton("Cambiar zona");
        btnRecargar.addActionListener(e -> ejecutarRecarga());
        btnReparar.addActionListener(e -> ejecutarReparacion());
        btnAsignar.addActionListener(e -> ejecutarAsignacion());
        fila2.add(btnRecargar);
        fila2.add(btnReparar);
        fila2.add(btnAsignar);

        controles.add(fila1);
        controles.add(fila2);
        add(controles, BorderLayout.SOUTH);
    }

    private void ejecutarRecarga() {
        Integer id = (Integer) comboRobot.getSelectedItem();
        if (id != null && gameEngine.recargarRobot(id)) {
            logger.accept("Robot #" + id + " recargado.");
        }
    }

    private void ejecutarReparacion() {
        Integer id = (Integer) comboRobot.getSelectedItem();
        if (id != null && gameEngine.repararRobot(id)) {
            logger.accept("Robot #" + id + " reparado.");
        }
    }

    private void ejecutarAsignacion() {
        Integer id = (Integer) comboRobot.getSelectedItem();
        String zona = (String) comboZona.getSelectedItem();
        if (id != null && zona != null) {
            gameEngine.asignarRobotAZona(id, zona);
            logger.accept("Robot #" + id + " reasignado a zona " + zona + ".");
        }
    }

    public void actualizar() {
        List<Robot> robots = gameEngine.getRobots();
        comboRobot.removeAllItems();
        StringBuilder texto = new StringBuilder();
        for (Robot robot : robots) {
            comboRobot.addItem(robot.getRobotId());
            texto.append(String.format("#%d | Nivel %d | Energia %d%% | Zona %s | %s\n",
                    robot.getRobotId(), robot.getNivel(), robot.getEnergia(), robot.getZonaAsignada(), robot.getEstado()));
        }
        areaRobots.setText(texto.length() == 0 ? "No hay robots disponibles.\n" : texto.toString());
        revalidate();
        repaint();
    }
}
