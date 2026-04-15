package Mineria;

import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PanelFundidora extends JPanel {

    private final GameEngine gameEngine;
    private final Consumer<String> logger;
    private final JProgressBar barraCalor;
    private final JLabel lblEstado;

    public PanelFundidora(GameEngine gameEngine, Consumer<String> logger) {
        this.gameEngine = gameEngine;
        this.logger = logger;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(25, 18, 18));

        JLabel titulo = new JLabel("Fundidora");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        barraCalor = new JProgressBar(0, 100);
        barraCalor.setStringPainted(true);
        barraCalor.setForeground(new Color(230, 126, 34));
        barraCalor.setBackground(new Color(40, 28, 28));
        lblEstado = new JLabel("Estado: normal");
        lblEstado.setForeground(Color.WHITE);

        JButton btnEnfriar = new JButton("Enfriar");
        btnEnfriar.addActionListener(e -> {
            gameEngine.enfriarFundidora();
            this.logger.accept("Se solicitó enfriamiento de la fundidora.");
            actualizar();
        });

        JPanel centro = new JPanel(new GridLayout(3, 1, 8, 8));
        centro.setOpaque(false);
        centro.add(barraCalor);
        centro.add(lblEstado);
        centro.add(btnEnfriar);
        add(centro, BorderLayout.CENTER);
    }

    public void actualizar() {
        int calor = gameEngine.getFundidora().getNivelCalor();
        barraCalor.setValue(calor);
        barraCalor.setString(calor + "%");
        if (gameEngine.getFundidora().isSobrecalentada()) {
            lblEstado.setText("Estado: sobrecalentada");
            lblEstado.setForeground(new Color(231, 76, 60));
        } else if (calor >= 70) {
            lblEstado.setText("Estado: alerta alta");
            lblEstado.setForeground(new Color(241, 196, 15));
        } else {
            lblEstado.setText("Estado: normal");
            lblEstado.setForeground(Color.WHITE);
        }
    }
}
