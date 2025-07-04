package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.ConexionBD;
import modelo.Usuario;
import util.SeguridadUtil;

public class UsuarioController {

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuario (nombre, correo, clave, rol, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getCorreo());
            stmt.setString(3, SeguridadUtil.encriptarSHA256(u.getClave()));
            stmt.setInt(4, u.getRol());
            stmt.setInt(5, u.getActivo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE activo = 1";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setClave(rs.getString("clave"));
                u.setRol(rs.getInt("rol"));
                u.setActivo(rs.getInt("activo"));
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuario SET nombre = ?, correo = ?, clave = ?, rol = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getCorreo());
            stmt.setString(3, SeguridadUtil.encriptarSHA256(u.getClave()));
            stmt.setInt(4, u.getRol());
            stmt.setInt(5, u.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamente(int id) {
        String sql = "UPDATE usuario SET activo = 0 WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> obtenerConsultores() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE rol = 2 AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setClave(rs.getString("clave"));
                u.setRol(rs.getInt("rol"));
                u.setActivo(rs.getInt("activo"));
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener consultores: " + e.getMessage());
        }

        return lista;
    }

    public Usuario login(String correo, String clave) {
        String sql = "SELECT * FROM usuario WHERE correo = ? AND clave = ? AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String claveEncriptada = SeguridadUtil.encriptarSHA256(clave);
            stmt.setString(1, correo);
            stmt.setString(2, claveEncriptada);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setClave(rs.getString("clave"));
                u.setRol(rs.getInt("rol"));
                u.setActivo(rs.getInt("activo"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al hacer login: " + e.getMessage());
        }

        return null;
    }

    public int contarPorRol(int rol) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol = ? AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rol);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al contar usuarios por rol: " + e.getMessage());
        }

        return 0;
    }

    public boolean correoExiste(String correo) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ? AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al verificar correo: " + e.getMessage());
        }

        return false;
    }

    public Usuario obtenerPorCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE correo = ? AND activo = 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setClave(rs.getString("clave"));
                u.setRol(rs.getInt("rol"));
                u.setActivo(rs.getInt("activo"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario por correo: " + e.getMessage());
        }

        return null;
    }

    public int contarActivos() {
        String sql = "SELECT COUNT(*) FROM usuario WHERE activo = 1";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al contar usuarios activos: " + e.getMessage());
        }

        return 0;
    }

   
}
