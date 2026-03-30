package TexEditorGUI.main;

import javax.swing.SwingUtilities;
import TexEditorGUI.controller.EditorController;
import TexEditorGUI.model.Documento;
import TexEditorGUI.service.EditorService;
import TexEditorGUI.view.EditorView2;


public class Main2 {
    public static void main(String[] args) {

SwingUtilities.invokeLater(() -> {

            // 1. Modelo
            Documento documento = new Documento();

            // 2. Servicio (lógica)
            EditorService service = new EditorService(documento);

            // 3. Controlador
            EditorController controller = new EditorController(service);

            // 4. Vista (UI)
            EditorView2 view = new EditorView2(controller);

            // 5. Mostrar ventana
            view.setVisible(true);
        });
    }
}
