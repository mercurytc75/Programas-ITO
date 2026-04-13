package Monopoly;


import ui.MonopolyFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        

        SwingUtilities.invokeLater(() -> {
            MonopolyFrame frame = new MonopolyFrame();
            frame.setVisible(true);
        });
    }
}
