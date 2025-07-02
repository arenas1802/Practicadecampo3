package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertarUsuariosPrueba {
    public static void main(String[] args) {
        String sql = "INSERT INTO usuario (nombre, correo, clave, rol, activo) VALUES (?, ?, ?, ?, ?)";

        Object[][] usuarios = {
            {"Admin Uno", "admin1@erp.com", "0192023a7bbd73250516f069df18b500", 1}, // clave: admin123
            {"Admin Dos", "admin2@erp.com", "e99a18c428cb38d5f260853678922e03", 1}, // clave: admin456
            {"Consultor Uno", "consultor1@erp.com", "82c3f03544f3bfb1cbd453a078c84830c0df07f3db2cb9f79f7cf184c032d4f1", 2}, //clave: consult123
            {"Consultor Dos", "consultor2@erp.com", "aa9edbe6032f9fb305dab6f95a4cdd552bf20a43d6a1a29ae917e8ac863ab489", 2}, //clave: consult456
            {"Cliente Uno", "cliente1@erp.com", "b03147a88c8265b88a0a063d1e7c0f6768e0fa4f31a5f47f3a29b65ca7474056", 3},// clave: cliente123
            {"Cliente Dos", "cliente2@erp.com", "75a9898c930c4b3cefe6fce397d47e833db8cb1fbaec65564c346ae94df5d43e", 3}, // clave: cliente456
        };

        try (Connection conn = ConexionBD.conectar()) {
            for (Object[] u : usuarios) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, (String) u[0]);
                    stmt.setString(2, (String) u[1]);
                    stmt.setString(3, (String) u[2]);
                    stmt.setInt(4, (int) u[3]);
                    stmt.setInt(5, 1); // activo
                    stmt.executeUpdate();
                    System.out.println("✅ Insertado: " + u[1]);
                } catch (SQLException e) {
                    System.err.println("❌ Error con " + u[1] + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error general al conectar: " + e.getMessage());
        }
    }
}
