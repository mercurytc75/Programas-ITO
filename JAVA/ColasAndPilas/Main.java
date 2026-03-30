public class Main{
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ControladorEstructuras(vista);
            vista.setVisible(true);
        });
    }
}