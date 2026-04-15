package Mineria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelMapa extends JPanel {
    private final GameEngine gameEngine;
    private final Map<String, JLabel> labelsReserva = new HashMap<>();
    private final Map<String, JLabel> labelsRobots = new HashMap<>();
    private final Map<String, JProgressBar> barras = new HashMap<>();

    public PanelMapa(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(19, 24, 35));

        JLabel titulo = new JLabel("Mapa de Zonas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 1, 10, 10));
        grid.setOpaque(false);
        grid.add(crearZonaCard("A", new Color(241, 196, 15)));
        grid.add(crearZonaCard("B", new Color(52, 152, 219)));
        grid.add(crearZonaCard("C", new Color(155, 89, 182)));
        add(grid, BorderLayout.CENTER);
    }

    private JPanel crearZonaCard(String nombre, Color colorAcento) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento.darker(), 2, true),
                new EmptyBorder(10, 10, 10, 10)));
        card.setBackground(new Color(28, 34, 48));

        JLabel titulo = new JLabel("Zona " + nombre);
        titulo.setForeground(colorAcento);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel reserva = new JLabel("Reserva: 0%", SwingConstants.LEFT);
        reserva.setForeground(Color.WHITE);
        JLabel robots = new JLabel("Robots: 0", SwingConstants.LEFT);
        robots.setForeground(Color.WHITE);
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);
        barra.setForeground(colorAcento);
        barra.setBackground(new Color(18, 22, 31));
        barra.setBorder(BorderFactory.createLineBorder(new Color(60, 70, 90)));

        JPanel info = new JPanel(new GridLayout(3, 1, 5, 5));
        info.setOpaque(false);
        info.add(reserva);
        info.add(robots);
        info.add(barra);

        labelsReserva.put(nombre, reserva);
        labelsRobots.put(nombre, robots);
        barras.put(nombre, barra);
        card.add(titulo, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    public void actualizar() {
        List<ZonaMina> zonas = gameEngine.getZonas();
        List<Robot> robots = gameEngine.getRobots();
        for (ZonaMina zona : zonas) {
            JLabel etiquetaReserva = labelsReserva.get(zona.getNombre());
            JLabel etiquetaRobots = labelsRobots.get(zona.getNombre());
            if (etiquetaReserva == null || etiquetaRobots == null) {
                continue;
            }
            etiquetaReserva.setText("Reserva: " + zona.getReserva() + "/" + zona.getReservaMaxima() + " (" + zona.getPorcentajeReserva() + "%)" + (zona.isBloqueada() ? " - BLOQUEADA" : ""));
            long conteo = robots.stream().filter(r -> zona.getNombre().equals(r.getZonaAsignada())).count();
            etiquetaRobots.setText("Robots asignados: " + conteo);
            JProgressBar barra = barras.get(zona.getNombre());
            if (barra != null) {
                int pct = zona.getPorcentajeReserva();
                barra.setValue(pct);
                barra.setString(pct + "%");
            }
        }
        revalidate();
        repaint();
    }
}
