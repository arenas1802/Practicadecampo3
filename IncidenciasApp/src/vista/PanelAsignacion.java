package vista;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controlador.IncidenciaController;
import controlador.UsuarioController;
import modelo.Incidencia;
import modelo.Usuario;

public class PanelAsignacion extends JFrame {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JComboBox<Usuario> cbConsultores;
    private JButton btnAsignar;
    private IncidenciaController incController;
    private UsuarioController userController;
    private int idSeleccionado = -1;
    private List<Usuario> consultores;

    public PanelAsignacion() {
        incController = new IncidenciaController();
        userController = new UsuarioController();

        setTitle("📌 Asignación de Incidencias - Administrador");
        setSize(800, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Título", "Cliente"}, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBounds(20, 20, 500, 400);
        add(scrollTabla);

        JLabel lblConsultor = new JLabel("👨‍💼 Asignar a:");
        lblConsultor.setBounds(550, 40, 100, 25);
        add(lblConsultor);

        cbConsultores = new JComboBox<>();
        cbConsultores.setBounds(630, 40, 140, 25);
        add(cbConsultores);

        btnAsignar = new JButton("✅ Asignar");
        btnAsignar.setBounds(620, 80, 100, 30);
        add(btnAsignar);

        btnAsignar.addActionListener(e -> asignar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                idSeleccionado = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            }
        });

        cargarConsultores();
        cargarIncidencias();
    }

    private void cargarConsultores() {
        consultores = userController.obtenerConsultores();
        cbConsultores.removeAllItems();
        for (Usuario u : consultores) {
            cbConsultores.addItem(u);
        }
    }

    private void cargarIncidencias() {
        modeloTabla.setRowCount(0);
        List<Incidencia> pendientes = incController.obtenerIncidenciasPendientes();
        for (Incidencia i : pendientes) {
            modeloTabla.addRow(new Object[]{
                i.getId(),
                i.getTitulo(),
                "Cliente #" + i.getIdCliente()
            });
        }
    }

    private void asignar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una incidencia de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario consultor = (Usuario) cbConsultores.getSelectedItem();
        if (consultor == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un consultor de la lista.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = incController.asignarIncidencia(idSeleccionado, consultor.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Consultor asignado correctamente.");
            cargarIncidencias();  // Recargar tabla
            idSeleccionado = -1;
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al asignar consultor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
