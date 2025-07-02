package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.ConexionBD;
import modelo.Usuario;

public class LoginController {

    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ? AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getInt("rol"),
                    rs.getInt("activo")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error al autenticar: " + e.getMessage());
        }

        return usuario;
    }
}
