package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:incidencias.db";

    // Cargar el driver de SQLite una sola vez al iniciar la clase
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el driver de SQLite.");
        }
    }

    /**
     * Retorna una conexión activa a la base de datos SQLite.
     * 
     * @return Conexión válida o null si ocurre un error.
     */
    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            // Mensaje útil para debugging, puedes comentar en producción:
            System.out.println("✅ Conexión a la base de datos establecida.");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }
}
