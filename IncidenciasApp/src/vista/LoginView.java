package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controlador.UsuarioController;
import modelo.Usuario;

public class LoginView extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private final UsuarioController usuarioController;

    public LoginView() {
        this.usuarioController = new UsuarioController();

        setTitle("Login - Sistema de Incidencias");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponentes();

        setVisible(true);
    }

    private void initComponentes() {
        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        panelCampos.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelCampos.add(txtCorreo);

        panelCampos.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panelCampos.add(txtContrasena);

        add(panelCampos, BorderLayout.CENTER);

        // Panel del botón
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setPreferredSize(new Dimension(160, 30));
        btnLogin.addActionListener(e -> iniciarSesion());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnLogin);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        Usuario usuario = usuarioController.login(correo, contrasena);
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombre() + ".");
            dispose(); // Cierra la ventana actual
            new MenuPrincipal(usuario); // Abre el menú principal según el rol
        } else {
            JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}
