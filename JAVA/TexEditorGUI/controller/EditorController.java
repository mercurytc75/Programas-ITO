package TexEditorGUI.controller;

import TexEditorGUI.model.Documento;
import TexEditorGUI.service.EditorService;
import TexEditorGUI.command.AgregarLineaCommand;
import TexEditorGUI.command.ElimminarLineaCommand;

public class EditorController {
     private EditorService service;

    public EditorController(EditorService service) {
        this.service = service;
    }

    public void agregarLinea(String texto) {
        service.ejecutar(new AgregarLineaCommand(
            service.getDocumento().getLineas(), texto));
    }

    public void eliminarLinea(int index) {
        service.ejecutar(new ElimminarLineaCommand(
            service.getDocumento().getLineas(), index));
    }

    public void deshacer() {
        service.deshacer();
    }

    public void rehacer() {
        service.rehacer();
    }

    public Documento getDocumento() {
        return service.getDocumento();
    }
}
