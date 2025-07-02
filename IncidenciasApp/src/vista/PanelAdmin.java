package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controlador.IncidenciaController;
import controlador.UsuarioController;
import modelo.Incidencia;
import modelo.Usuario;

public class PanelAdmin extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<Usuario> comboConsultores;
    private JButton btnAsignar, btnEliminar, btnExportarCSV, btnExportarUsuariosCSV;
    private JComboBox<String> comboEstados;

    private IncidenciaController incController = new IncidenciaController();
    private UsuarioController usuarioController = new UsuarioController();

    public PanelAdmin() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("🛠 Gestión de Incidencias (Administrador)");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
                "ID", "Título", "Descripción", "Cliente", "F. Creación", "F. Asignación", "F. Resolución"
        }, 0);

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout());

        comboEstados = new JComboBox<>(new String[]{"Pendiente", "Asignado", "Resuelto"});
        comboEstados.addActionListener(e -> cargarIncidencias());
        panelInferior.add(new JLabel("Filtrar por estado:"));
        panelInferior.add(comboEstados);

        comboConsultores = new JComboBox<>();
        cargarConsultores();
        panelInferior.add(new JLabel("Asignar a:"));
        panelInferior.add(comboConsultores);

        btnAsignar = new JButton("✅ Asignar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnExportarCSV = new JButton("📤 Exportar Incidencias CSV");
        btnExportarUsuariosCSV = new JButton("📤 Exportar Usuarios CSV");

        panelInferior.add(btnAsignar);
        panelInferior.add(btnEliminar);
        panelInferior.add(btnExportarCSV);
        panelInferior.add(btnExportarUsuariosCSV);

        add(panelInferior, BorderLayout.SOUTH);

        btnAsignar.addActionListener(e -> asignarIncidencia());
        btnEliminar.addActionListener(e -> eliminarIncidencia());
        btnExportarCSV.addActionListener(e -> exportarCSV());
        btnExportarUsuariosCSV.addActionListener(e -> exportarUsuariosCSV());

        cargarIncidencias();
    }

    private void cargarConsultores() {
        comboConsultores.removeAllItems();
        List<Usuario> consultores = usuarioController.obtenerConsultores();
        for (Usuario u : consultores) {
            comboConsultores.addItem(u);
        }
    }

    private void cargarIncidencias() {
        modelo.setRowCount(0);

        String estadoSeleccionado = (String) comboEstados.getSelectedItem();
        int estadoFiltro = switch (estadoSeleccionado) {
            case "Asignado" -> 2;
            case "Resuelto" -> 3;
            default -> 1;
        };

        List<Incidencia> lista = incController.obtenerIncidenciasPorEstado(estadoFiltro);

        for (Incidencia i : lista) {
            modelo.addRow(new Object[]{
                    i.getId(),
                    i.getTitulo(),
                    i.getDescripcion(),
                    i.getNombreCliente(),
                    i.getFechaCreacion() != null ? i.getFechaCreacion() : "—",
                    i.getFechaAsignacion() != null ? i.getFechaAsignacion() : "—",
                    i.getFechaResolucion() != null ? i.getFechaResolucion() : "—"
            });
        }
    }

    private void asignarIncidencia() {
        int fila = tabla.getSelectedRow();
        Usuario consultor = (Usuario) comboConsultores.getSelectedItem();

        if (fila == -1 || consultor == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia y un consultor.");
            return;
        }

        int idIncidencia = (int) modelo.getValueAt(fila, 0);
        boolean exito = incController.asignarIncidencia(idIncidencia, consultor.getId());

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Incidencia asignada.");
            cargarIncidencias();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al asignar.");
        }
    }

    private void eliminarIncidencia() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia.");
            return;
        }

        int idIncidencia = (int) modelo.getValueAt(fila, 0);
        boolean exito = incController.eliminarLogicamente(idIncidencia);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Incidencia eliminada lógicamente.");
            cargarIncidencias();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al eliminar.");
        }
    }

    private void exportarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar incidencias como...");
        int opcion = fileChooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("ID,Título,Descripción,Cliente,Fecha Creación,Fecha Asignación,Fecha Resolución");

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    StringBuilder fila = new StringBuilder();
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        Object valor = modelo.getValueAt(i, j);
                        fila.append("\"").append(valor != null ? valor.toString() : "").append("\"");
                        if (j < modelo.getColumnCount() - 1) {
							fila.append(",");
						}
                    }
                    writer.println(fila);
                }

                JOptionPane.showMessageDialog(this, "✅ Incidencias exportadas con éxito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al exportar incidencias: " + ex.getMessage());
            }
        }
    }

    private void exportarUsuariosCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar usuarios como...");
        int opcion = fileChooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.println("ID,Nombre,Correo,Rol,Activo");

                List<Usuario> lista = usuarioController.obtenerTodos();

                for (Usuario u : lista) {
                    String linea = String.format("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\"",
                            u.getId(),
                            u.getNombre(),
                            u.getCorreo(),
                            u.getRolComoTexto(),
                            u.getActivo() == 1 ? "Sí" : "No"
                    );
                    writer.println(linea);
                }

                JOptionPane.showMessageDialog(this, "✅ Usuarios exportados con éxito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al exportar usuarios: " + ex.getMessage());
            }
        }
    }
}
