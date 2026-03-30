package TexEditorGUI.service;

import java.util.Stack;

import TexEditorGUI.command.Command;
import TexEditorGUI.model.Documento;

public class EditorService {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
    private Documento documento;
    
    public EditorService(Documento documento) {
        this.documento = documento;
    }
    public void ejecutar(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
    }

    public void deshacer() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    public void rehacer() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }

    public Documento getDocumento() {
        return documento;
    }
}
