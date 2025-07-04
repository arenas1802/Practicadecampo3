package modelo;

/**
 * Representa un usuario del sistema.
 * Puede tener rol de Administrador, Consultor o Cliente.
 */
public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String clave;
    private int rol;     // 1 = Admin, 2 = Consultor, 3 = Cliente
    private int activo;  // 1 = activo, 0 = eliminado lógicamente

    public Usuario() {
    }

    public Usuario(int id, String nombre, String correo, String clave, int rol, int activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    /**
     * Devuelve el rol del usuario en texto legible.
     */
    public String getRolComoTexto() {
        return switch (rol) {
            case 1 -> "Administrador";
            case 2 -> "Consultor";
            case 3 -> "Cliente";
            default -> "Desconocido";
        };
    }

    public String getRolTexto() {
        return switch (this.rol) {
            case 1 -> "Administrador";
            case 2 -> "Consultor";
            case 3 -> "Cliente";
            default -> "Desconocido";
        };
    }

    /**
     * Muestra el nombre del usuario en el combo (por ejemplo: en PanelAdmin)
     */
    @Override
    public String toString() {
        return nombre;
    }
}
