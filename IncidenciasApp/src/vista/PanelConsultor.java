package vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controlador.IncidenciaController;
import modelo.Incidencia;
import modelo.Usuario;

public class PanelConsultor extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextArea txtRespuesta;
    private JButton btnResponder;
    private JButton btnVerDetalles;

    private Usuario usuario; // Consultor actual

    private IncidenciaController incidenciaController = new IncidenciaController();

    public PanelConsultor(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("📋 Incidencias asignadas a: " + usuario.getNombre());
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Título", "Descripción", "Cliente", "F. Creación", "F. Asignación", "F. Resolución", "Respuesta"
        }, 0);

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());

        txtRespuesta = new JTextArea(4, 30);
        txtRespuesta.setBorder(BorderFactory.createTitledBorder("✍️ Escribe tu respuesta"));
        panelInferior.add(new JScrollPane(txtRespuesta), BorderLayout.CENTER);

        btnVerDetalles = new JButton("🔍 Ver Detalles");
        btnVerDetalles.addActionListener(e -> verDetallesIncidencia());
        panelInferior.add(btnVerDetalles, BorderLayout.NORTH);

        btnResponder = new JButton("✅ Marcar como resuelto");
        btnResponder.addActionListener(e -> resolverIncidencia());
        panelInferior.add(btnResponder, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        cargarIncidencias();
    }

    private void cargarIncidencias() {
        modelo.setRowCount(0);
        List<Incidencia> lista = incidenciaController.obtenerPorConsultor(usuario.getId());

        for (Incidencia i : lista) {
            if (i.getEstado() == 2) { // Solo mostrar las asignadas
                modelo.addRow(new Object[]{
                    i.getId(),
                    i.getTitulo(),
                    i.getDescripcion(),
                    i.getNombreCliente(),
                    i.getFechaCreacion() != null ? i.getFechaCreacion() : "—",
                    i.getFechaAsignacion() != null ? i.getFechaAsignacion() : "—",
                    i.getFechaResolucion() != null ? i.getFechaResolucion() : "—",
                    i.getRespuesta() != null ? i.getRespuesta() : "—"
                });
            }
        }
    }

    private void resolverIncidencia() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una incidencia primero.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String respuesta = txtRespuesta.getText().trim();
        if (respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La respuesta no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idIncidencia = (int) modelo.getValueAt(fila, 0);

        boolean exito = incidenciaController.resolverIncidencia(idIncidencia, respuesta);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Incidencia resuelta correctamente.");
            cargarIncidencias();
            txtRespuesta.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al resolver la incidencia.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetallesIncidencia() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona una incidencia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder detalles = new StringBuilder();
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            detalles.append(tabla.getColumnName(i)).append(": ")
                    .append(tabla.getValueAt(fila, i)).append("\n");
        }

        JOptionPane.showMessageDialog(this, detalles.toString(), "Detalles de Incidencia", JOptionPane.INFORMATION_MESSAGE);
    }
}
