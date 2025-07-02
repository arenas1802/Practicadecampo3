package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.ConexionBD;
import modelo.Incidencia;

public class IncidenciaController {

    public boolean registrarIncidencia(Incidencia inc) {
        String sql = "INSERT INTO incidencia (titulo, descripcion, id_cliente, estado, activo, fecha_creacion) "
                   + "VALUES (?, ?, ?, 1, 1, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inc.getTitulo());
            stmt.setString(2, inc.getDescripcion());
            stmt.setInt(3, inc.getIdCliente());
            stmt.setString(4, LocalDate.now().toString());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar incidencia: " + e.getMessage());
            return false;
        }
    }

    public List<Incidencia> obtenerTodasActivasConNombres() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre AS nombre_cliente FROM incidencia i "
                   + "JOIN usuario u ON i.id_cliente = u.id WHERE i.activo = 1 "
                   + "ORDER BY i.fecha_creacion DESC";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId(rs.getInt("id"));
                i.setTitulo(rs.getString("titulo"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setEstado(rs.getInt("estado"));
                i.setIdCliente(rs.getInt("id_cliente"));
                i.setIdConsultor(rs.getInt("id_consultor"));
                i.setActivo(rs.getInt("activo"));
                i.setFechaCreacion(rs.getString("fecha_creacion"));
                i.setFechaResolucion(rs.getString("fecha_resolucion"));
                i.setRespuesta(rs.getString("respuesta"));
                i.setNombreCliente(rs.getString("nombre_cliente"));
                i.setFechaAsignacion(rs.getString("fecha_asignacion"));

                lista.add(i);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener incidencias: " + e.getMessage());
        }

        return lista;
    }

    public List<Incidencia> obtenerPorCliente(int idCliente) {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre AS nombre_cliente FROM incidencia i "
                   + "INNER JOIN usuario u ON i.id_cliente = u.id "
                   + "WHERE i.id_cliente = ? AND i.activo = 1 "
                   + "ORDER BY i.fecha_creacion DESC";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId(rs.getInt("id"));
                i.setTitulo(rs.getString("titulo"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setEstado(rs.getInt("estado"));
                i.setIdCliente(rs.getInt("id_cliente"));
                i.setIdConsultor(rs.getInt("id_consultor"));
                i.setActivo(rs.getInt("activo"));
                i.setFechaCreacion(rs.getString("fecha_creacion"));
                i.setFechaResolucion(rs.getString("fecha_resolucion"));
                i.setRespuesta(rs.getString("respuesta"));
                i.setNombreCliente(rs.getString("nombre_cliente"));
                i.setFechaAsignacion(rs.getString("fecha_asignacion"));

                lista.add(i);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener incidencias del cliente: " + e.getMessage());
        }

        return lista;
    }

    public List<Incidencia> obtenerPorConsultor(int idConsultor) {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre AS nombre_cliente FROM incidencia i "
                   + "INNER JOIN usuario u ON i.id_cliente = u.id "
                   + "WHERE i.id_consultor = ? AND i.activo = 1 "
                   + "ORDER BY i.fecha_creacion DESC";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsultor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId(rs.getInt("id"));
                i.setTitulo(rs.getString("titulo"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setEstado(rs.getInt("estado"));
                i.setIdCliente(rs.getInt("id_cliente"));
                i.setIdConsultor(rs.getInt("id_consultor"));
                i.setActivo(rs.getInt("activo"));
                i.setFechaCreacion(rs.getString("fecha_creacion"));
                i.setFechaResolucion(rs.getString("fecha_resolucion"));
                i.setRespuesta(rs.getString("respuesta"));
                i.setNombreCliente(rs.getString("nombre_cliente"));
                i.setFechaAsignacion(rs.getString("fecha_asignacion"));

                lista.add(i);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener incidencias por consultor: " + e.getMessage());
        }

        return lista;
    }

    public boolean asignarIncidencia(int idIncidencia, int idConsultor) {
        String sql = "UPDATE incidencia SET estado = 2, id_consultor = ?, fecha_asignacion = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConsultor);
            stmt.setString(2, LocalDate.now().toString());
            stmt.setInt(3, idIncidencia);

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al asignar incidencia: " + e.getMessage());
            return false;
        }
    }

    public boolean resolverIncidencia(int incidenciaId, String respuesta) {
        String sql = "UPDATE incidencia SET estado = 3, respuesta = ?, fecha_resolucion = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, respuesta);
            stmt.setString(2, LocalDate.now().toString());
            stmt.setInt(3, incidenciaId);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al resolver incidencia: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarIncidencia(Incidencia incidencia) {
        String sql = "UPDATE incidencia SET titulo = ?, descripcion = ?, estado = ?, fecha_resolucion = ?, respuesta = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, incidencia.getTitulo());
            stmt.setString(2, incidencia.getDescripcion());
            stmt.setInt(3, incidencia.getEstado());

            if (incidencia.getFechaResolucion() != null) {
                stmt.setString(4, incidencia.getFechaResolucion());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setString(5, incidencia.getRespuesta());
            stmt.setInt(6, incidencia.getId());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar incidencia: " + e.getMessage());
            return false;
        }
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM incidencia WHERE activo = 1";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
				return rs.getInt(1);
			}
        } catch (SQLException e) {
            System.err.println("❌ Error al contar incidencias: " + e.getMessage());
        }
        return 0;
    }

    public int contarPorEstado(int estado) {
        String sql = "SELECT COUNT(*) FROM incidencia WHERE estado = ? AND activo = 1";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estado);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
				return rs.getInt(1);
			}
        } catch (SQLException e) {
            System.err.println("❌ Error al contar por estado: " + e.getMessage());
        }
        return 0;
    }

    public String getDescripcionPorId(int idIncidencia) {
        String sql = "SELECT descripcion FROM incidencia WHERE id = ? AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idIncidencia);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("descripcion");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener descripción: " + e.getMessage());
        }

        return null;
    }

    public boolean eliminarLogicamente(int idIncidencia) {
        String sql = "UPDATE incidencia SET activo = 0 WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idIncidencia);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar lógicamente la incidencia: " + e.getMessage());
            return false;
        }
    }

    public List<Incidencia> obtenerIncidenciasPendientes() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre AS nombre_cliente FROM incidencia i "
                   + "JOIN usuario u ON i.id_cliente = u.id "
                   + "WHERE i.estado = 1 AND i.activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId(rs.getInt("id"));
                i.setTitulo(rs.getString("titulo"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setEstado(rs.getInt("estado"));
                i.setIdCliente(rs.getInt("id_cliente"));
                i.setActivo(rs.getInt("activo"));
                i.setFechaCreacion(rs.getString("fecha_creacion"));
                i.setNombreCliente(rs.getString("nombre_cliente"));
                lista.add(i);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener incidencias pendientes: " + e.getMessage());
        }

        return lista;
    }

    public List<Incidencia> obtenerIncidenciasPorEstado(int estado) {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre AS nombre_cliente FROM incidencia i "
                   + "LEFT JOIN usuario u ON i.id_cliente = u.id "
                   + "WHERE i.estado = ? AND i.activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Incidencia inc = new Incidencia();
                inc.setId(rs.getInt("id"));
                inc.setTitulo(rs.getString("titulo"));
                inc.setDescripcion(rs.getString("descripcion"));
                inc.setEstado(rs.getInt("estado"));
                inc.setIdCliente(rs.getInt("id_cliente"));
                inc.setNombreCliente(rs.getString("nombre_cliente"));
                inc.setFechaCreacion(rs.getString("fecha_creacion"));
                lista.add(inc);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener incidencias por estado: " + e.getMessage());
        }

        return lista;
    }
}
