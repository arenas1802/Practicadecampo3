package vista;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controlador.IncidenciaController;
import controlador.UsuarioController;

public class PanelEstadisticas extends JPanel {

    private JLabel lblTotalIncidencias, lblPendientes, lblAsignadas, lblResueltas;
    private JLabel lblAdmins, lblConsultores, lblClientes;
    private IncidenciaController incidenciaController;
    private UsuarioController usuarioController;

    public PanelEstadisticas() {
        this.incidenciaController = new IncidenciaController();
        this.usuarioController = new UsuarioController();

        setLayout(new GridLayout(2, 1));

        // Panel 1: Incidencias
        JPanel panelIncidencias = new JPanel(new GridLayout(4, 1));
        panelIncidencias.setBorder(BorderFactory.createTitledBorder("Estadísticas de Incidencias"));

        lblTotalIncidencias = new JLabel();
        lblPendientes = new JLabel();
        lblAsignadas = new JLabel();
        lblResueltas = new JLabel();

        panelIncidencias.add(lblTotalIncidencias);
        panelIncidencias.add(lblPendientes);
        panelIncidencias.add(lblAsignadas);
        panelIncidencias.add(lblResueltas);

        // Panel 2: Usuarios
        JPanel panelUsuarios = new JPanel(new GridLayout(3, 1));
        panelUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios por Rol"));

        lblAdmins = new JLabel();
        lblConsultores = new JLabel();
        lblClientes = new JLabel();

        panelUsuarios.add(lblAdmins);
        panelUsuarios.add(lblConsultores);
        panelUsuarios.add(lblClientes);

        add(panelIncidencias);
        add(panelUsuarios);

        actualizarDatos();
    }

    private void actualizarDatos() {
        int total = incidenciaController.contarTotal();
        int pendientes = incidenciaController.contarPorEstado(1);
        int asignadas = incidenciaController.contarPorEstado(2);
        int resueltas = incidenciaController.contarPorEstado(3);

        lblTotalIncidencias.setText("Total de incidencias: " + total);
        lblPendientes.setText("Pendientes: " + pendientes);
        lblAsignadas.setText("Asignadas: " + asignadas);
        lblResueltas.setText("Resueltas: " + resueltas);

        lblAdmins.setText("Administradores: " + usuarioController.contarPorRol(1));
        lblConsultores.setText("Consultores: " + usuarioController.contarPorRol(2));
        lblClientes.setText("Clientes: " + usuarioController.contarPorRol(3));
    }
}
