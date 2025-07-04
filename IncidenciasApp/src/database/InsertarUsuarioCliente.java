package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.SeguridadUtil;

public class InsertarUsuarioCliente {
    public static void main(String[] args) {
        String sql = "INSERT INTO usuario (nombre, correo, clave, rol, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Cliente Demo");
            stmt.setString(2, "cliente@erp.com");
            String claveEncriptada = SeguridadUtil.encriptarSHA256("clave123");
            stmt.setString(3, claveEncriptada);
            stmt.setInt(4, 3); // Cliente
            stmt.setInt(5, 1); // Activo

            stmt.executeUpdate();
            System.out.println("✅ Cliente insertado");

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar cliente: " + e.getMessage());
        }
    }
}
