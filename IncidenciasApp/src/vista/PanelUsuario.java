package vista;

import controlador.UsuarioController;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUsuario extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtCorreo, txtClave;
    private JComboBox<String> comboRol;
    private JButton btnAgregar, btnEditar, btnEliminar;

    private UsuarioController usuarioController = new UsuarioController();

    public PanelUsuario() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("👥 Gestión de Usuarios");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Correo", "Rol"}, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Formulario"));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtClave = new JTextField();
        comboRol = new JComboBox<>(new String[]{"Administrador", "Consultor", "Cliente"});

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Clave:"));
        formulario.add(txtClave);
        formulario.add(new JLabel("Rol:"));
        formulario.add(comboRol);

        btnAgregar = new JButton("➕ Agregar");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");

        formulario.add(btnAgregar);
        formulario.add(btnEditar);

        add(formulario, BorderLayout.SOUTH);
        add(btnEliminar, BorderLayout.EAST);

        cargarUsuarios();

        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        tabla.getSelectionModel().addListSelectionListener(e -> cargarFormularioDesdeTabla());
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);
        List<Usuario> lista = usuarioController.obtenerTodos();
        for (Usuario u : lista) {
            if (u.getActivo() == 1) {
                modelo.addRow(new Object[]{
                        u.getId(), u.getNombre(), u.getCorreo(), u.getRolTexto()
                });
            }
        }
    }

    private void agregarUsuario() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String clave = txtClave.getText().trim();
        int rol = comboRol.getSelectedIndex() + 1;

        if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios.");
            return;
        }

        if (usuarioController.correoExiste(correo)) {
            JOptionPane.showMessageDialog(this, "❌ Ya existe un usuario con ese correo.");
            return;
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setCorreo(correo);
        nuevo.setClave(clave); // Se encripta en el controlador
        nuevo.setRol(rol);
        nuevo.setActivo(1);

        if (usuarioController.insertar(nuevo)) {
            JOptionPane.showMessageDialog(this, "✅ Usuario agregado.");
            cargarUsuarios();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al agregar usuario.");
        }
    }

    private void editarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un usuario.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String clave = txtClave.getText().trim();
        int rol = comboRol.getSelectedIndex() + 1;

        if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios.");
            return;
        }

        Usuario existente = usuarioController.obtenerPorCorreo(correo);
        if (existente != null && existente.getId() != id) {
            JOptionPane.showMessageDialog(this, "❌ Ya existe otro usuario con ese correo.");
            return;
        }

        Usuario u = new Usuario(id, nombre, correo, clave, rol, 1);
        boolean exito = usuarioController.actualizarUsuario(u);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Usuario actualizado.");
            cargarUsuarios();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar.");
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        boolean exito = usuarioController.eliminarLogicamente(id);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Usuario eliminado.");
            cargarUsuarios();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al eliminar.");
        }
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtCorreo.setText(modelo.getValueAt(fila, 2).toString());
            txtClave.setText(""); // no mostrar clave actual
            comboRol.setSelectedIndex(obtenerIndiceRol(modelo.getValueAt(fila, 3).toString()));
        }
    }

    private int obtenerIndiceRol(String rolTexto) {
        return switch (rolTexto) {
            case "Administrador" -> 0;
            case "Consultor" -> 1;
            case "Cliente" -> 2;
            default -> 0;
        };
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtClave.setText("");
        comboRol.setSelectedIndex(0);
        tabla.clearSelection();
    }
}
