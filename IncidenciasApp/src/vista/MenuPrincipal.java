package vista;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import modelo.Usuario;

public class MenuPrincipal extends JFrame {

    private final Usuario usuario;

    public MenuPrincipal(Usuario usuarioLogueado) {
        this.usuario = usuarioLogueado;

        setTitle("Sistema de Gestión de Incidencias - Rol: " + usuario.getRolComoTexto());
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        agregarPanelSuperior();
        agregarContenidoPorRol();

        setVisible(true);
    }

    private void agregarPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());

        JLabel lblRol = new JLabel("Rol actual: " + usuario.getRolComoTexto());
        lblRol.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(lblRol, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("🔒 Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelSuperior.add(btnCerrarSesion, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas cerrar sesión?",
            "Confirmar cierre de sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            dispose(); // Cierra esta ventana
            new LoginView(); // Regresa al login
        }
    }

    private void agregarContenidoPorRol() {
        JTabbedPane tabs = new JTabbedPane();

        switch (usuario.getRol()) {
            case 1 -> {
                tabs.addTab("Gestión de Incidencias", new PanelAdmin());
                tabs.addTab("Gestión de Usuarios", new PanelUsuario());
                tabs.addTab("📊 Estadísticas", new PanelEstadisticas());
            }
            case 2 -> tabs.addTab("Incidencias Asignadas", new PanelConsultor(usuario));
            case 3 -> tabs.addTab("Mis Incidencias", new PanelCliente(usuario));
            default -> {
                JOptionPane.showMessageDialog(this, "Rol desconocido");
                return;
            }
        }

        add(tabs, BorderLayout.CENTER);
    }
}
