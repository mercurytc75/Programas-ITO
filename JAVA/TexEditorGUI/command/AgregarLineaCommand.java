package TexEditorGUI.command;

import java.util.List;

public class AgregarLineaCommand implements Command {
    
    private List<String> documento ;
    private String texto;

    public AgregarLineaCommand(List<String> documento, String texto){
        this.documento = documento;
        this.texto = texto;
    }

    @Override
    public void execute() {
        documento.add(texto);
    }

    @Override
    public void undo() {
        if(!documento.isEmpty()){
            documento.remove(documento.size() - 1);
        }
   }
}
