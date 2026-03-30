package TexEditorGUI.command;

import java.util.List;

public class ElimminarLineaCommand implements Command {
    
    private List<String> documento;
    private int indice;
    private String lineaEliminada;

    public ElimminarLineaCommand(List<String> documento, int indice){
        this.documento = documento;
        this.indice = indice;
    }

    @Override
    public void execute() {
        lineaEliminada = documento.remove(indice);
    }

    @Override
    public void undo() {
        documento.add(indice, lineaEliminada);
    }
    
}
