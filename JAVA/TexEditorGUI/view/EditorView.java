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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import TexEditorGUI.controller.EditorController;

public class EditorView extends JFrame{
    
    private EditorController controller;
    private JTextArea textArea;
    private JLabel statusBar;
    private File archivoActual;
    

    // Paleta de colores moderna y agradable
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);      // Azul profesional
    private final Color COLOR_SECUNDARIO = new Color(44, 62, 80);      // Gris azulado oscuro
    private final Color COLOR_TERCERO = new Color(236, 240, 241);      // Gris muy claro
    private final Color COLOR_TEXTO = new Color(44, 62, 80);           // Texto oscuro
    private final Color COLOR_TEXTO_CLARO = new Color(255, 255, 255); // Texto blanco
    private final Color COLOR_ACCENTO = new Color(26, 188, 156);       // Verde turquesa
    private final Color COLOR_BOTON = new Color(52, 152, 219);         // Azul botones
    private final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);   // Azul más oscuro

    public EditorView(EditorController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setTitle("Mercury Editor - Editor de Texto ");
        setSize(1000, 700); 
        setResizable(true); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(COLOR_PRIMARIO);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(COLOR_PRIMARIO);
        titleBar.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblLogo = new JLabel(" MERCURY"); 
        lblLogo.setForeground(COLOR_TEXTO_CLARO);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleBar.add(lblLogo, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Editor de Texto", SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEXTO_CLARO);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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

        JButton btnAgregar = crearBotonToolbar("Agregar Linea", "Agregar Linea");
        JButton btnEliminar = crearBotonToolbar("Eliminar", "Eliminar Linea");
        JButton btnUndo = crearBotonToolbar("Deshacer", "Undo");
        JButton btnRedo = crearBotonToolbar("Rehacer", "Redo");

        toolBar.add(btnAgregar);
        toolBar.add(Box.createHorizontalStrut(8));
        toolBar.add(btnEliminar);
        toolBar.addSeparator();
        toolBar.add(btnUndo);
        toolBar.add(Box.createHorizontalStrut(8));
        toolBar.add(btnRedo);

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
        textArea.setEditable(false); 
        textArea.setLineWrap(true);

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

        btnAgregar.addActionListener(e -> {
            String texto = JOptionPane.showInputDialog(this, "Ingrese texto:");
            if (texto != null && !texto.trim().isEmpty()) {
                controller.agregarLinea(texto);
                actualizarTexto();
            }
        });

        btnEliminar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Índice a eliminar:");
            try {
                int index = Integer.parseInt(input);
                controller.eliminarLinea(index);
                actualizarTexto();
            } catch (Exception ignored) {}
        });

        btnUndo.addActionListener(e -> {
            controller.deshacer();
            actualizarTexto();
        });

        btnRedo.addActionListener(e -> {
            controller.rehacer();
            actualizarTexto();
        });
    }

    private JButton crearBotonToolbar(String tooltip, String texto) {
        JButton btn = new JButton(texto);
        btn.setToolTipText(tooltip);
        btn.setFocusPainted(false);
        btn.setBackground(COLOR_BOTON);
        btn.setForeground(COLOR_TEXTO_CLARO);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_BOTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_BOTON);
            }
        });
        
        return btn;
    }

    private void actualizarTexto() {
        textArea.setText("");
        var lineas = controller.getDocumento().getLineas();

        for (int i = 0; i < lineas.size(); i++) {
            textArea.append(String.format("%3d │ %s\n", i, lineas.get(i)));
        }

        String estadoEditor = lineas.isEmpty() ? "▪ Vacío" : "▸ Activo";
        statusBar.setText(String.format(" %s | Total líneas: %d | Documento actualizado", 
            estadoEditor, lineas.size()));
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
                statusBar.setText(" ✗ Guardado cancelado");
                return;
            }
        }
        
        try (FileWriter writer = new FileWriter(archivoActual)) {
            var lineas = controller.getDocumento().getLineas();
            for (String linea : lineas) {
                writer.write(linea + "\n");
            }
            statusBar.setText(" ✓ Archivo guardado: " + archivoActual.getName());
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
                
                controller.getDocumento().getLineas().clear();
                String[] lineas = contenido.toString().split("\n");
                for (String linea : lineas) {
                    if (!linea.isEmpty()) {
                        controller.getDocumento().getLineas().add(linea);
                    }
                }
                
                actualizarTexto();
                statusBar.setText(" ✓ Archivo abierto: " + archivoActual.getName());
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
            controller.getDocumento().getLineas().clear();
            archivoActual = null;
            actualizarTexto();
            statusBar.setText(" ✓ Nuevo documento creado");
        }
    }

    // ============== MÉTODOS DE EDICIÓN ==============
    private void cortarTexto() {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(selectedText), null);
            statusBar.setText(" ✓ Texto cortado");
        } else {
            statusBar.setText(" ✗ No hay texto seleccionado");
        }
    }

    private void copiarTexto() {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(selectedText), null);
            statusBar.setText(" ✓ Texto copiado");
        } else {
            statusBar.setText(" ✗ No hay texto seleccionado");
        }
    }

    private void pegarTexto() {
        try {
            String clipboard = (String) java.awt.Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(java.awt.datatransfer.DataFlavor.stringFlavor);
            String input = JOptionPane.showInputDialog(this, "Pegar en línea #:", "0");
            if (input != null) {
                int index = Integer.parseInt(input);
                controller.getDocumento().getLineas().add(index, clipboard);
                actualizarTexto();
                statusBar.setText(" ✓ Texto pegado");
            }
        } catch (Exception ex) {
            statusBar.setText(" ✗ Error al pegar");
        }
    }

    private void buscarTexto() {
        String buscar = JOptionPane.showInputDialog(this, "Buscar:");
        if (buscar != null && !buscar.isEmpty()) {
            var lineas = controller.getDocumento().getLineas();
            int encontradas = 0;
            
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).contains(buscar)) {
                    encontradas++;
                }
            }
            
            statusBar.setText(String.format(" 🔍 Encontradas %d coincidencias", encontradas));
            if (encontradas == 0) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados");
            }
        }
    }

    // ============== MÉTODOS DE VISUALIZACIÓN ==============
    private void mostrarEspacios(boolean mostrar) {
        if (mostrar) {
            statusBar.setText(" ⦾ Mostrando espacios en blanco");
        } else {
            statusBar.setText(" ⦾ Espacios ocultos");
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
