package Mineria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class PanelContratos extends JPanel {
    private final GameEngine gameEngine;
    private final Consumer<String> logger;
    private final DefaultListModel<String> modelo;
    private final JList<String> lista;

    public PanelContratos(GameEngine gameEngine, Consumer<String> logger) {
        this.gameEngine = gameEngine;
        this.logger = logger;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(24, 20, 34));

        JLabel titulo = new JLabel("Contratos activos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultListModel<>();
        lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setBackground(new Color(16, 14, 22));
        lista.setForeground(new Color(235, 235, 245));
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridLayout(1, 2, 8, 8));
        acciones.setOpaque(false);
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnRechazar = new JButton("Rechazar");
        btnAceptar.addActionListener(e -> aceptarSeleccionado());
        btnRechazar.addActionListener(e -> rechazarSeleccionado());
        acciones.add(btnAceptar);
        acciones.add(btnRechazar);
        add(acciones, BorderLayout.SOUTH);
    }

    private void aceptarSeleccionado() {
        Contrato contrato = contratoSeleccionado();
        if (contrato != null && gameEngine.aceptarContrato(contrato.getId())) {
            logger.accept("Contrato #" + contrato.getId() + " aceptado.");
        }
    }

    private void rechazarSeleccionado() {
        Contrato contrato = contratoSeleccionado();
        if (contrato != null) {
            gameEngine.rechazarContrato(contrato.getId());
            logger.accept("Contrato #" + contrato.getId() + " rechazado.");
        }
    }

    private Contrato contratoSeleccionado() {
        int indice = lista.getSelectedIndex();
        List<Contrato> contratos = gameEngine.getContratosActivos();
        if (indice < 0 || indice >= contratos.size()) {
            return null;
        }
        return contratos.get(indice);
    }

    public void actualizar() {
        List<Contrato> contratos = gameEngine.getContratosActivos();
        int indiceSeleccionado = lista.getSelectedIndex();
        modelo.clear();
        for (Contrato contrato : contratos) {
            modelo.addElement(String.format("#%d | %s | %s | Recompensa $%d",
                    contrato.getId(), contrato.getTipoMineral(), contrato.getProgressBar(), contrato.getRecompensa()));
        }
        if (modelo.isEmpty()) {
            modelo.addElement("No hay contratos activos.");
        }
        if (indiceSeleccionado >= 0 && indiceSeleccionado < modelo.size()) {
            lista.setSelectedIndex(indiceSeleccionado);
        }
        revalidate();
        repaint();
    }
}
