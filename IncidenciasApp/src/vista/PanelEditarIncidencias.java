package vista;

import controlador.IncidenciaController;
import modelo.Incidencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PanelEditarIncidencias extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JComboBox<String> cbEstado;
    private IncidenciaController controller;
    private int idSeleccionado = -1;

    public PanelEditarIncidencias() {
        controller = new IncidenciaController();

        setTitle("Editar Incidencias");
        setSize(750, 450);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Estado", "Cliente", "Consultor"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 450, 350);
        add(scroll);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(490, 30, 80, 25);
        add(lblTitulo);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(570, 30, 140, 25);
        add(txtTitulo);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(490, 70, 80, 25);
        add(lblDescripcion);
        txtDescripcion = new JTextArea();
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBounds(570, 70, 140, 80);
        add(scrollDesc);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(490, 160, 80, 25);
        add(lblEstado);
        cbEstado = new JComboBox<>(new String[]{"1 - Pendiente", "2 - Asignado", "3 - Resuelto"});
        cbEstado.setBounds(570, 160, 140, 25);
        add(cbEstado);

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setBounds(540, 210, 150, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarCambios());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                idSeleccionado = (int) modelo.getValueAt(tabla.getSelectedRow(), 0);
                txtTitulo.setText((String) modelo.getValueAt(tabla.getSelectedRow(), 1));
                cbEstado.setSelectedIndex((int) modelo.getValueAt(tabla.getSelectedRow(), 2) - 1);
                txtDescripcion.setText(controller.getDescripcionPorId(idSeleccionado));
            }
        });

        cargarIncidencias();
    }

    private void cargarIncidencias() {
        modelo.setRowCount(0);
        List<Incidencia> lista = controller.obtenerTodasActivasConNombres();
        for (Incidencia i : lista) {
            modelo.addRow(new Object[]{
                i.getId(),
                i.getTitulo(),
                i.getEstado(),
                i.getNombreCliente(),
                i.getNombreConsultor() != null ? i.getNombreConsultor() : "Sin asignar"
            });
        }
    }

    private void guardarCambios() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia.");
            return;
        }

        String titulo = txtTitulo.getText();
        String descripcion = txtDescripcion.getText();
        int estado = cbEstado.getSelectedIndex() + 1;

        Incidencia i = new Incidencia();
        i.setId(idSeleccionado);
        i.setTitulo(titulo);
        i.setDescripcion(descripcion);
        i.setEstado(estado);

        boolean ok = controller.actualizarIncidencia(i);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Incidencia actualizada.");
            cargarIncidencias();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.");
        }
    }
}
