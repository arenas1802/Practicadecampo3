package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class InsertarIncidenciasPrueba {
    public static void main(String[] args) {
        insertarIncidencia("Error en el módulo de facturación", "Al generar una factura, el sistema lanza una excepción.");
        insertarIncidencia("Problema de acceso", "No se puede acceder con credenciales válidas.");
        insertarIncidencia("Reporte no se exporta", "Al intentar exportar un reporte a PDF, no genera archivo.");
        insertarIncidencia("Pantalla se congela", "La interfaz se queda congelada al hacer clic en Guardar.");
        insertarIncidencia("Cálculo erróneo de IGV", "El valor del impuesto no coincide con lo esperado.");
    }
    
    private static void insertarIncidencia(String titulo, String descripcion) {
        String sql = "INSERT INTO incidencia (titulo, descripcion, id_cliente, estado, fecha_creacion, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setInt(3, 3); // ID del cliente creador
            stmt.setInt(4, 1); // Estado: 1 = Pendiente
            stmt.setString(5, LocalDate.now().toString());
            stmt.setInt(6, 1); // Activo

            stmt.executeUpdate();
            System.out.println("✅ Incidencia insertada: " + titulo);

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar incidencia: " + e.getMessage());
        }
    }

}
