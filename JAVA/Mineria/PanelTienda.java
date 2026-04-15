package Mineria;

import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PanelTienda extends JDialog {

    private final GameEngine gameEngine;
    private final Consumer<String> logger;
    private final JLabel lblDinero;
    private final JLabel lblEstado;

    public PanelTienda(
        Frame owner,
        GameEngine gameEngine,
        Consumer<String> logger
    ) {
        super(owner, "Tienda", true);
        this.gameEngine = gameEngine;
        this.logger = logger;

        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 22, 30));

        JLabel titulo = new JLabel("Tienda de MineRush", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(6, 1, 10, 10));
        centro.setBorder(new EmptyBorder(12, 12, 12, 12));
        centro.setOpaque(false);

        lblDinero = new JLabel("Dinero disponible: $" + gameEngine.getDinero());
        lblDinero.setForeground(Color.WHITE);
        lblEstado = new JLabel("Selecciona una compra.");
        lblEstado.setForeground(new Color(200, 220, 240));

        JButton btnRobot1 = new JButton("Comprar Robot Nivel 1 - $500");
        JButton btnRobot2 = new JButton("Comprar Robot Nivel 2 - $1200");
        JButton btnRobot3 = new JButton("Comprar Robot Nivel 3 - $2500");
        JButton btnFundidora = new JButton("Mejorar Fundidora - $800");
        JButton btnBodega = new JButton("Mejorar Bodega - $600");

        btnRobot1.addActionListener(e -> comprarRobot(1));
        btnRobot2.addActionListener(e -> comprarRobot(2));
        btnRobot3.addActionListener(e -> comprarRobot(3));
        btnFundidora.addActionListener(e -> mejorarFundidora());
        btnBodega.addActionListener(e -> mejorarBodega());

        centro.add(lblDinero);
        centro.add(btnRobot1);
        centro.add(btnRobot2);
        centro.add(btnRobot3);
        centro.add(btnFundidora);
        centro.add(btnBodega);
        add(centro, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(8, 8));
        inferior.setOpaque(false);
        inferior.setBorder(new EmptyBorder(0, 12, 12, 12));
        inferior.add(lblEstado, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        inferior.add(btnCerrar, BorderLayout.EAST);
        add(inferior, BorderLayout.SOUTH);
    }

    private void comprarRobot(int nivel) {
        boolean ok = gameEngine.getTienda().comprarRobot(nivel);
        if (ok) {
            logger.accept("Se compró un robot nivel " + nivel + ".");
            lblEstado.setText("Robot nivel " + nivel + " comprado.");
        } else {
            lblEstado.setText("No fue posible comprar el robot.");
        }
        refrescarDinero();
    }

    private void mejorarFundidora() {
        boolean ok = gameEngine.getTienda().mejorarFundidora();
        lblEstado.setText(
            ok ? "Fundidora mejorada." : "No se pudo mejorar la fundidora."
        );
        if (ok) {
            logger.accept("Fundidora mejorada desde la tienda.");
        }
        refrescarDinero();
    }

    private void mejorarBodega() {
        boolean ok = gameEngine.getTienda().mejorarBodega();
        lblEstado.setText(
            ok ? "Bodega mejorada." : "No se pudo mejorar la bodega."
        );
        if (ok) {
            logger.accept("Bodega mejorada desde la tienda.");
        }
        refrescarDinero();
    }

    private void refrescarDinero() {
        lblDinero.setText("Dinero disponible: $" + gameEngine.getDinero());
    }
}
