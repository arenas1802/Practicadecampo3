package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AgregarCampoClienteId {
    public static void main(String[] args) {
        String sql = "ALTER TABLE incidencia ADD COLUMN cliente_id INTEGER";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("✅ Columna cliente_id agregada con éxito.");

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar columna: " + e.getMessage());
        }
    }
}
