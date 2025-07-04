package vista;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import controlador.IncidenciaController;
import modelo.Incidencia;
import modelo.Usuario;

public class PanelResolverIncidencias extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextArea txtRespuesta;
    private IncidenciaController controller;
    private Usuario consultor;

    public PanelResolverIncidencias(Usuario usuario) {
        this.consultor = usuario;
        this.controller = new IncidenciaController();

        setTitle("Resolver Incidencias Asignadas");
        setSize(750, 480);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Tabla
        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Descripción", "Estado", "Respuesta"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 700, 280);
        add(scroll);

        // Área para mostrar respuesta
        JLabel lblRespuesta = new JLabel("Respuesta del consultor:");
        lblRespuesta.setBounds(20, 310, 300, 25);
        add(lblRespuesta);

        txtRespuesta = new JTextArea();
        txtRespuesta.setEditable(false);
        txtRespuesta.setLineWrap(true);
        txtRespuesta.setWrapStyleWord(true);
        JScrollPane scrollRespuesta = new JScrollPane(txtRespuesta);
        scrollRespuesta.setBounds(20, 340, 700, 80);
        add(scrollRespuesta);

        // Listener para mostrar la respuesta
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int fila = tabla.getSelectedRow();
                int estado = (int) modelo.getValueAt(fila, 3);
                String respuesta = (String) modelo.getValueAt(fila, 4);

                if (estado == 3 && respuesta != null && !respuesta.isEmpty()) {
                    txtRespuesta.setText("✔ Respuesta del consultor:\n" + respuesta);
                } else {
                    txtRespuesta.setText("ℹ Esta incidencia aún no ha sido resuelta.");
                }
            }
        });

        // Cargar datos
        cargarIncidencias();
    }

    private void cargarIncidencias() {
        List<Incidencia> lista = controller.obtenerPorConsultor(consultor.getId());

        modelo.setRowCount(0); // Limpiar
        for (Incidencia i : lista) {
            modelo.addRow(new Object[]{
                i.getId(),
                i.getTitulo(),
                i.getDescripcion(),
                i.getEstado(), // puedes mapearlo a texto si quieres
                i.getRespuesta()
            });
        }
    }
}
