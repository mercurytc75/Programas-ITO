package TexEditor;

import java.util.ArrayList;

public class EliminarlineaCommand  implements Command{

    private ArrayList<String> documento;
    private int indice;
    private String lineaEliminada;

    public EliminarlineaCommand(ArrayList<String> documento, int indice){
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
