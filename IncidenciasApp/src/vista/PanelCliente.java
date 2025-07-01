package vista;

import controlador.IncidenciaController;
import modelo.Incidencia;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCliente extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private IncidenciaController incidenciaController;
    private Usuario usuario;

    public PanelCliente(Usuario usuarioLogueado) {
        this.usuario = usuarioLogueado;
        this.incidenciaController = new IncidenciaController();

        setLayout(new BorderLayout());

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Título", "Descripción", "Estado", "F. Creación", "F. Asignación", "F. Resolución"
        }, 0);

        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Botón para registrar nueva incidencia
        JButton btnRegistrar = new JButton("➕ Registrar nueva incidencia");
        btnRegistrar.addActionListener(e -> registrarIncidencia());
        add(btnRegistrar, BorderLayout.SOUTH);

        cargarIncidencias();
    }

    private void cargarIncidencias() {
        modeloTabla.setRowCount(0);
        List<Incidencia> lista = incidenciaController.obtenerPorCliente(usuario.getId());

        for (Incidencia i : lista) {
            if (i.getActivo() == 1) {
                modeloTabla.addRow(new Object[]{
                        i.getId(),
                        i.getTitulo(),
                        i.getDescripcion(),
                        i.getEstadoComoTexto(),
                        i.getFechaCreacion() != null ? i.getFechaCreacion() : "—",
                        i.getFechaAsignacion() != null ? i.getFechaAsignacion() : "—",
                        i.getFechaResolucion() != null ? i.getFechaResolucion() : "—"
                });
            }
        }
    }

    private void registrarIncidencia() {
        String titulo = JOptionPane.showInputDialog(this, "📝 Ingrese el *Título* de la incidencia:");
        if (titulo == null) return;
        if (titulo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El campo *Título* es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String descripcion = JOptionPane.showInputDialog(this, "📄 Ingrese la *Descripción* de la incidencia:");
        if (descripcion == null) return;
        if (descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El campo *Descripción* es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Incidencia nueva = new Incidencia();
        nueva.setTitulo(titulo.trim());
        nueva.setDescripcion(descripcion.trim());
        nueva.setIdCliente(usuario.getId());
        nueva.setActivo(1); // activa por defecto

        boolean ok = incidenciaController.registrarIncidencia(nueva);
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Incidencia registrada correctamente.");
            cargarIncidencias();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al registrar la incidencia.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
