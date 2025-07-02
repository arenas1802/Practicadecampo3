package database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearUsuarioAdmin {

    public static void main(String[] args) {
        crearUsuarioAdmin("Administrador Daniel", "daniel@darenas.me", "prueba");
    }

    public static void crearUsuarioAdmin(String nombre, String correo, String clave) {
        String hash = encriptarSHA256(clave);

        if (hash == null) {
            System.err.println("❌ No se pudo cifrar la contraseña.");
            return;
        }

        String sql = "INSERT INTO usuario (nombre, correo, clave, rol, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, hash);
            stmt.setInt(4, 1);  // rol 1 = administrador
            stmt.setInt(5, 1);  // activo

            stmt.executeUpdate();
            System.out.println("✅ Usuario administrador creado correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar el usuario: " + e.getMessage());
        }
    }

    public static String encriptarSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(texto.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al encriptar: " + e.getMessage());
            return null;
        }
    }
}
