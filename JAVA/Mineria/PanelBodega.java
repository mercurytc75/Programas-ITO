package Mineria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class PanelBodega extends JPanel {
    private final GameEngine gameEngine;
    private final Consumer<String> logger;
    private final JProgressBar barraBodega;
    private final JLabel lblInfo;

    public PanelBodega(GameEngine gameEngine, Consumer<String> logger) {
        this.gameEngine = gameEngine;
        this.logger = logger;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(18, 26, 20));

        JLabel titulo = new JLabel("Bodega");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        barraBodega = new JProgressBar();
        barraBodega.setStringPainted(true);
        barraBodega.setForeground(new Color(46, 204, 113));
        barraBodega.setBackground(new Color(30, 38, 30));
        lblInfo = new JLabel("Capacidad: 0/0");
        lblInfo.setForeground(Color.WHITE);

        JButton btnVender = new JButton("Vender ahora");
        btnVender.addActionListener(e -> {
            int ganancia = gameEngine.venderMinerales();
            logger.accept("Venta realizada por $" + ganancia + ".");
            actualizar();
        });

        JPanel centro = new JPanel(new GridLayout(3, 1, 8, 8));
        centro.setOpaque(false);
        centro.add(barraBodega);
        centro.add(lblInfo);
        centro.add(btnVender);
        add(centro, BorderLayout.CENTER);
    }

    public void actualizar() {
        int cantidad = gameEngine.getBodega().getCantidadAlmacenada();
        int capacidad = gameEngine.getBodega().getCapacidadMaxima();
        barraBodega.setMaximum(Math.max(1, capacidad));
        barraBodega.setValue(cantidad);
        barraBodega.setString(cantidad + "/" + capacidad);
        lblInfo.setText("Capacidad: " + cantidad + "/" + capacidad + (gameEngine.getBodega().isCompradorEspecialActivo() ? " - comprador especial activo" : ""));
    }
}
