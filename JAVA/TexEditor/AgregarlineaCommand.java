package TexEditor;

import java.util.ArrayList;

public class AgregarlineaCommand implements Command {

    private ArrayList<String> documento ;
    private String texto;

    public AgregarlineaCommand(ArrayList<String> documento, String texto){
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
