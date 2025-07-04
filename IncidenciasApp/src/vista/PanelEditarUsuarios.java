package vista;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controlador.UsuarioController;
import modelo.Usuario;

public class PanelEditarUsuarios extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtCorreo;
    private JComboBox<String> cbRol;
    private UsuarioController controller;
    private int idSeleccionado = -1;

    public PanelEditarUsuarios() {
        controller = new UsuarioController();

        setTitle("Editar Usuarios");
        setSize(700, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Correo", "Rol"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 400, 300);
        add(scroll);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(440, 40, 80, 25);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(520, 40, 140, 25);
        add(txtNombre);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(440, 80, 80, 25);
        add(lblCorreo);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(520, 80, 140, 25);
        add(txtCorreo);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(440, 120, 80, 25);
        add(lblRol);
        cbRol = new JComboBox<>(new String[]{"1 - Administrador", "2 - Consultor", "3 - Cliente"});
        cbRol.setBounds(520, 120, 140, 25);
        add(cbRol);

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setBounds(490, 170, 160, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarCambios());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                idSeleccionado = (int) modelo.getValueAt(tabla.getSelectedRow(), 0);
                txtNombre.setText((String) modelo.getValueAt(tabla.getSelectedRow(), 1));
                txtCorreo.setText((String) modelo.getValueAt(tabla.getSelectedRow(), 2));
                int rol = switch ((String) modelo.getValueAt(tabla.getSelectedRow(), 3)) {
                    case "Administrador" -> 1;
                    case "Consultor" -> 2;
                    default -> 3;
                };
                cbRol.setSelectedIndex(rol - 1);
            }
        });

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);
        List<Usuario> lista = controller.obtenerTodos();
        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                    u.getId(),
                    u.getNombre(),
                    u.getCorreo(),
                    switch (u.getRol()) {
                        case 1 -> "Administrador";
                        case 2 -> "Consultor";
                        default -> "Cliente";
                    }
            });
        }
    }

    private void guardarCambios() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario primero.");
            return;
        }

        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        int rol = cbRol.getSelectedIndex() + 1;

        Usuario u = new Usuario(idSeleccionado, nombre, correo, "", rol, 1);
        boolean ok = controller.actualizarUsuario(u);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado.");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.");
        }
    }
}
