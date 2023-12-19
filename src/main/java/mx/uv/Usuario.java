package mx.uv;

public class Usuario {
    String id;
    String correo;
    String password;
    String nombre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

        public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", correo=" + correo + ", password=" + password + ", nombre=" + nombre +"]";
    }

    public Usuario(String id, String correo, String password, String nombre) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
    }

}
