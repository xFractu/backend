package mx.uv;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Data Access Object
public class DAO {
    // en el dao se establece la conexion a la BD
    private static Conexion c = new Conexion();
    // este metodo regresa un conjunto de usuarios que cumpla un criterio
    public static List<Usuario> dameUsuarios() {
        Statement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Usuario> resultado = new ArrayList<>();

        conn = Conexion.getConnection();

        try {
            String sql = "SELECT * from usuarios";
            stm = (Statement) conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                Usuario u = new Usuario(rs.getString("id"), rs.getString("correo"), rs.getString("password"), rs.getString("nombre"));
                resultado.add(u);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            rs = null;
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return resultado;
    }

    public static String crearUsuario(Usuario u) {
        PreparedStatement stm = null;
        Connection conn = null;
        String msj = "";

        conn = Conexion.getConnection();
        try {
            String sql = "INSERT INTO usuarios (id, correo, password,nombre) values (?,?,?,?)";
            stm = (PreparedStatement) conn.prepareStatement(sql);
            stm.setString(1, u.getId());
            stm.setString(2, u.getCorreo());
            stm.setString(3, u.getPassword());
            stm.setString(4, u.getNombre());
            if (stm.executeUpdate() > 0)
                msj = "usuario agregado";
            else
                msj = "usuario no agregado";

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return msj;
    }

    public static boolean validarUsuario(String correo, String password) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);
            stm.setString(2, password);

            ResultSet rs = stm.executeQuery();

            return rs.next(); // Devuelve true si hay una coincidencia, false si no hay coincidencia

        } catch (Exception e) {
            System.out.println(e);
            return false; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static String obtenerIdUsuario(String correo, String password) {
        PreparedStatement stm = null;
        Connection conn = null;
    
        conn = Conexion.getConnection();
        try {
            String sql = "SELECT id FROM usuarios WHERE correo = ? AND password = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);
            stm.setString(2, password);
    
            ResultSet rs = stm.executeQuery();
    
            if (rs.next()) {
                return rs.getString("id"); // Devuelve el ID si hay una coincidencia
            } else {
                return "falso"; // Devuelve -1 si no hay coincidencia
            }
    
        } catch (Exception e) {
            System.out.println(e);
            return "falso"; // Manejar adecuadamente las excepciones en tu aplicación real
        } finally {
            // Cerrar recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}