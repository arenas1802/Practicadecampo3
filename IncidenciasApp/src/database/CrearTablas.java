package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearTablas {

    public static void crearTablas() {
        try (Connection conn = ConexionBD.conectar(); Statement stmt = conn.createStatement()) {

            // Tabla de usuarios
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    correo TEXT NOT NULL UNIQUE,
                    contrasena TEXT NOT NULL,
                    rol INTEGER NOT NULL,
                    activo INTEGER NOT NULL DEFAULT 1
                );
            """);

            // Tabla de incidencias (ya con fechas incluidas)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS incidencia (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    descripcion TEXT NOT NULL,
                    estado INTEGER NOT NULL DEFAULT 1,
                    id_cliente INTEGER NOT NULL,
                    id_consultor INTEGER,
                    respuesta TEXT,
                    fecha_creacion TEXT,
                    fecha_resolucion TEXT,
                    activo INTEGER NOT NULL DEFAULT 1,
                    FOREIGN KEY (id_cliente) REFERENCES usuario(id),
                    FOREIGN KEY (id_consultor) REFERENCES usuario(id)
                );
            """);

            System.out.println("✅ Tablas creadas correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al crear tablas: " + e.getMessage());
        }
    }

    public static void agregarColumnaRespuesta() {
        try (Connection conn = ConexionBD.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE incidencia ADD COLUMN respuesta TEXT;");
            System.out.println("✅ Columna 'respuesta' agregada.");
        } catch (SQLException e) {
            System.err.println("⚠️ Columna 'respuesta' probablemente ya existe.");
        }
    }

    public static void agregarFechas() {
        try (Connection conn = ConexionBD.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE incidencia ADD COLUMN fecha_creacion TEXT;");
            stmt.execute("ALTER TABLE incidencia ADD COLUMN fecha_resolucion TEXT;");
            System.out.println("✅ Columnas de fecha agregadas.");
        } catch (SQLException e) {
            System.err.println("⚠️ Columnas de fecha probablemente ya existen.");
        }
    }

    public static void main(String[] args) {
        crearTablas();
        agregarColumnaRespuesta();
        agregarFechas();            // agrega columnas de fecha si faltan
    }
}
