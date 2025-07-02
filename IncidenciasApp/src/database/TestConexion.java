package database;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestConexion {
    public static void main(String[] args) {
        String ruta = System.getProperty("user.dir") + "/database/incidencias.db";
        System.out.println("📍 Intentando conectar a: " + ruta);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ruta)) {
            System.out.println("✅ Conexión exitosa a la base de datos.");
        } catch (Exception e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
    }
}
