package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaDetalleIncidencia extends JDialog {
    public VentanaDetalleIncidencia(JFrame parent, String[] datosIncidencia) {
        super(parent, "Detalles de Incidencia", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JTextArea detallesArea = new JTextArea();
        detallesArea.setEditable(false);
        detallesArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        StringBuilder detalles = new StringBuilder();
        detalles.append("ID: ").append(datosIncidencia[0]).append("\n");
        detalles.append("Título: ").append(datosIncidencia[1]).append("\n");
        detalles.append("Descripción: ").append(datosIncidencia[2]).append("\n");
        detalles.append("Estado: ").append(datosIncidencia[3]).append("\n");
        detalles.append("Fecha creación: ").append(datosIncidencia[4]).append("\n");

        if (datosIncidencia.length > 5) {
            detalles.append("Cliente: ").append(datosIncidencia[5]).append("\n");
        }
        if (datosIncidencia.length > 6) {
            detalles.append("Consultor: ").append(datosIncidencia[6]).append("\n");
        }

        detallesArea.setText(detalles.toString());
        add(new JScrollPane(detallesArea), BorderLayout.CENTER);
    }
}
