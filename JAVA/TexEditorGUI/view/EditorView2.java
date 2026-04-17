package TexEditorGUI.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import TexEditorGUI.controller.EditorController;

public class EditorView2 extends JFrame {
    private EditorController controller;
    private JTextArea textArea;
    private JLabel statusBar;
    private File archivoActual;
    private UndoManager undoManager;

    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);      // Azul profesional
    private final Color COLOR_SECUNDARIO = new Color(44, 62, 80);      // Gris azulado oscuro
    private final Color COLOR_TERCERO = new Color(236, 240, 241);      // Gris muy claro
    private final Color COLOR_TEXTO = new Color(44, 62, 80);           // Texto oscuro
    private final Color COLOR_TEXTO_CLARO = new Color(255, 255, 255); // Texto blanco
    private final Color COLOR_ACCENTO = new Color(26, 188, 156);       // Verde turquesa
    private final Color COLOR_BOTON = new Color(52, 152, 219);         // Azul botones
    private final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);   // Azul más oscuro

    public EditorView2(EditorController controller){
        this.controller = controller;
        intUI();
    }

    private void intUI(){
        setTitle("Editor de texto");
        setSize(1000, 700);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(COLOR_PRIMARIO);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(COLOR_PRIMARIO);
        titleBar.setBorder(new EmptyBorder(12, 20, 12, 20)); 

        JLabel lblTitulo = new JLabel("Editor de texto", SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEXTO_CLARO);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN,14));
        titleBar.add(lblTitulo, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COLOR_PRIMARIO);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        
        // Menú ARCHIVO
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(COLOR_TEXTO_CLARO);
        JCheckBoxMenuItem chkGuardar = new JCheckBoxMenuItem("Guardar");
        JCheckBoxMenuItem chkAbrir = new JCheckBoxMenuItem("Abrir");
        JCheckBoxMenuItem chkNuevo = new JCheckBoxMenuItem("Nuevo");
        
        chkGuardar.addActionListener(e -> guardarArchivo());
        chkAbrir.addActionListener(e -> abrirArchivo());
        chkNuevo.addActionListener(e -> nuevoDocumento());
        
        menuArchivo.add(chkGuardar);
        menuArchivo.add(chkAbrir);
        menuArchivo.add(chkNuevo);
        menuBar.add(menuArchivo);
        
        // Menú EDITAR
        JMenu menuEditar = new JMenu("Editar");
        menuEditar.setForeground(COLOR_TEXTO_CLARO);
        JCheckBoxMenuItem chkCortar = new JCheckBoxMenuItem("Cortar");
        JCheckBoxMenuItem chkCopiar = new JCheckBoxMenuItem("Copiar");
        JCheckBoxMenuItem chkPegar = new JCheckBoxMenuItem("Pegar");
        JCheckBoxMenuItem chkBuscar = new JCheckBoxMenuItem("Buscar");
        
        chkCortar.addActionListener(e -> cortarTexto());
        chkCopiar.addActionListener(e -> copiarTexto());
        chkPegar.addActionListener(e -> pegarTexto());
        chkBuscar.addActionListener(e -> buscarTexto());
        
        menuEditar.add(chkCortar);
        menuEditar.add(chkCopiar);
        menuEditar.add(chkPegar);
        menuEditar.add(new JSeparator());
        menuEditar.add(chkBuscar);
        menuBar.add(menuEditar);
        
        // Menú VER
        JMenu menuVer = new JMenu("Ver");
        menuVer.setForeground(COLOR_TEXTO_CLARO);
        JCheckBoxMenuItem chkNumLineas = new JCheckBoxMenuItem("Números de Línea");
        JCheckBoxMenuItem chkEspacios = new JCheckBoxMenuItem("Mostrar espacios");
        JCheckBoxMenuItem chkSaltos = new JCheckBoxMenuItem("Mostrar saltos");
        chkNumLineas.setSelected(true);
        
        chkNumLineas.addActionListener(e -> actualizarTexto());
        chkEspacios.addActionListener(e -> mostrarEspacios(chkEspacios.isSelected()));
        chkSaltos.addActionListener(e -> mostrarSaltos(chkSaltos.isSelected()));
        
        menuVer.add(chkNumLineas);
        menuVer.add(chkEspacios);
        menuVer.add(chkSaltos);
        menuBar.add(menuVer);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(COLOR_SECUNDARIO);
        toolBar.setBorder(new EmptyBorder(8, 10, 8, 10));

        topContainer.add(titleBar, BorderLayout.NORTH);
        topContainer.add(menuBar, BorderLayout.CENTER);
        topContainer.add(toolBar, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(150, 0));
        sidebar.setBackground(COLOR_TERCERO);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, COLOR_PRIMARIO));
        add(sidebar, BorderLayout.WEST);

        textArea = new JTextArea();
        textArea.setBackground(COLOR_TERCERO);
        textArea.setForeground(COLOR_TEXTO);
        textArea.setCaretColor(COLOR_ACCENTO);
        textArea.setMargin(new Insets(15, 15, 15, 15));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        
        // Configurar UndoManager
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
        
        // Keybindings para Ctrl+Z (Undo) y Ctrl+Y (Redo)
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        textArea.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                    statusBar.setText(" ↶ Deshacer ejecutado");
                }
            }
        });
        
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
        textArea.getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                    statusBar.setText(" ↷ Rehacer ejecutado");
                }
            }
        });

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COLOR_TERCERO);
        add(scroll, BorderLayout.CENTER);

        statusBar = new JLabel("Listo | Líneas: 0 | Editor en espera");
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_ACCENTO);
        statusBar.setForeground(COLOR_TEXTO_CLARO);
        statusBar.setBorder(new EmptyBorder(8, 15, 8, 15));
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void actualizarTexto() {
        if (archivoActual != null) {
            int lineas = textArea.getLineCount();
            statusBar.setText(String.format(" Archivo: %s | Líneas: %d | Edición en progreso", 
                archivoActual.getName(), lineas));
        } else {
            int lineas = textArea.getLineCount();
            statusBar.setText(String.format(" Nuevo documento | Líneas: %d | Edición en progreso", lineas));
        }
    }

    // ============== MÉTODOS DE ARCHIVO ==============
    private void guardarArchivo() {
        if (archivoActual == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                archivoActual = fileChooser.getSelectedFile();
            } else {
                statusBar.setText(" Guardado cancelado");
                return;
            }
        }
        
        try (FileWriter writer = new FileWriter(archivoActual)) {
            writer.write(textArea.getText());
            statusBar.setText(" Archivo guardado: " + archivoActual.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            try (FileReader reader = new FileReader(archivoActual)) {
                StringBuilder contenido = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    contenido.append((char) character);
                }
                
                textArea.setText(contenido.toString());
                undoManager.discardAllEdits();
                actualizarTexto();
                statusBar.setText(" Archivo abierto: " + archivoActual.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void nuevoDocumento() {
        int confirmar = JOptionPane.showConfirmDialog(this, 
            "¿Descartar el documento actual?", "Nuevo Documento", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            textArea.setText("");
            undoManager.discardAllEdits();
            archivoActual = null;
            statusBar.setText(" Nuevo documento creado");
        }
    }

    // ============== MÉTODOS DE EDICIÓN ==============
    private void cortarTexto() {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(selectedText), null);
            statusBar.setText(" Texto cortado");
        } else {
            statusBar.setText(" No hay texto seleccionado");
        }
    }

    private void copiarTexto() {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(selectedText), null);
            statusBar.setText(" Texto copiado");
        } else {
            statusBar.setText(" No hay texto seleccionado");
        }
    }

    private void pegarTexto() {
        try {
            String clipboard = (String) java.awt.Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(java.awt.datatransfer.DataFlavor.stringFlavor);
            
            int caretPos = textArea.getCaretPosition();
            textArea.insert(clipboard, caretPos);
            statusBar.setText(" Texto pegado");
        } catch (Exception ex) {
            statusBar.setText(" Error al pegar");
        }
    }

    private void buscarTexto() {
        String buscar = JOptionPane.showInputDialog(this, "Buscar:");
        if (buscar != null && !buscar.isEmpty()) {
            String texto = textArea.getText();
            int encontradas = 0;
            int index = 0;
            
            while ((index = texto.indexOf(buscar, index)) != -1) {
                encontradas++;
                index += buscar.length();
            }
            
            statusBar.setText(String.format(" Encontradas %d coincidencias", encontradas));
            if (encontradas == 0) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados");
            }
        }
    }

    // ============== MÉTODOS DE VISUALIZACIÓN ==============
    private void mostrarEspacios(boolean mostrar) {
        if (mostrar) {
            statusBar.setText(" Mostrando espacios en blanco");
        } else {
            statusBar.setText(" Espacios ocultos");
            actualizarTexto();
        }
    }

    private void mostrarSaltos(boolean mostrar) {
        if (mostrar) {
            statusBar.setText(" ↩ Mostrando saltos de línea");
        } else {
            statusBar.setText(" ↩ Saltos de línea ocultos");
            actualizarTexto();
        }
    }
        
    
}
