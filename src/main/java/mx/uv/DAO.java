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

    public static boolean existeUsuarioPorCorreo(String correo) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT COUNT(*) as num_usuarios FROM usuarios WHERE correo = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, correo);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt("num_usuarios") > 0; // Devuelve true si existe al menos un usuario con ese correo
            } else {
                return false; // Devuelve false si no hay usuarios con ese correo
            }

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

    public static String cambiarContrasena(String correo, String nuevaContrasena) {
        String mensaje = "";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stm = conn.prepareStatement("UPDATE usuarios SET password = ? WHERE correo = ?")) {

            // Verificar si el usuario existe
            if (existeUsuarioPorCorreo(correo)) {
                stm.setString(1, nuevaContrasena);
                stm.setString(2, correo);

                if (stm.executeUpdate() > 0) {
                    mensaje = "Contraseña actualizada correctamente";
                } else {
                    mensaje = "No se pudo actualizar la contraseña";
                }
            } else {
                mensaje = "Usuario no encontrado";
            }

        } catch (SQLException e) {
            // Manejo de excepciones (puedes personalizar este manejo según tus necesidades)
            mensaje = "Error al actualizar la contraseña";
            e.printStackTrace(); // O registra la excepción en un sistema de registro
        }

        return mensaje;
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

    public static Usuario obtenerDatosUsuario(String id) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, id);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                // Crear un objeto Usuario con los datos del usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getString("id"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setNombre(rs.getString("nombre"));
                // Agregar más campos según tu estructura de base de datos

                return usuario;
            } else {
                return null; // Devuelve null si no hay coincidencia
            }

        } catch (Exception e) {
            System.out.println(e);
            return null; // Manejar adecuadamente las excepciones en tu aplicación real
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


    //Reservaciones


    public static String hacerReservacion(Reservaciones r) {
        PreparedStatement stm = null;
        Connection conn = null;
        String msj = "";

        conn = Conexion.getConnection();
        try {
            String sql = "INSERT INTO reservacion (id_usuario, id_hotel, check_in, check_out, personas) VALUES (?,?,?,?,?)";
            stm = conn.prepareStatement(sql);
            stm.setString(1, r.getIdU());
            stm.setString(2, r.getIdH());
            stm.setString(3, r.getCheckIn());
            stm.setString(4, r.getCheckOut());
            stm.setString(5, r.getPersonas());

            if (stm.executeUpdate() > 0)
                msj = "Reservación realizada con éxito";
            else
                msj = "No se pudo realizar la reservación";

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return msj;
    }

    public static int obtenerNumeroReservaciones(String idUsuario) {
        PreparedStatement stm = null;
        Connection conn = null;

        conn = Conexion.getConnection();
        try {
            String sql = "SELECT COUNT(*) as num_reservaciones FROM reservacion WHERE id_usuario = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, idUsuario);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt("num_reservaciones"); // Devuelve el número de reservaciones del usuario
            } else {
                return 0; // Devuelve 0 si no hay reservaciones
            }

        } catch (Exception e) {
            System.out.println(e);
            return 0; // Manejar adecuadamente las excepciones en tu aplicación real
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

    public static List<Reservaciones> dameReservacionesPorUsuario(String idUsuario) {
        System.out.println("ENTRO AL METODO: dameReservacionesPorUsuario");
        Statement stm = null;
        ResultSet rs = null;
        Connection conn = null;
    
        List<Reservaciones> resultado = new ArrayList<>();
    
        conn = Conexion.getConnection();
        int i = 0;
        try {
            String sql = "SELECT r.id_reservacion, r.id_usuario, r.id_hotel, h.nombre as nombre_hotel, h.precio, "
                       + "r.check_in, r.check_out, r.personas "
                       + "FROM reservacion r "
                       + "JOIN hotel h ON r.id_hotel = h.id "
                       + "WHERE r.id_usuario = '" + idUsuario + "'";
            stm = (Statement) conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                Reservaciones reservacion = new Reservaciones(
                    rs.getString("id_reservacion"),
                    rs.getString("id_usuario"),
                    rs.getString("id_hotel"),
                    rs.getString("nombre_hotel"),
                    rs.getString("precio"),
                    rs.getString("check_in"),
                    rs.getString("check_out"),
                    rs.getString("personas")
                );
                resultado.add(reservacion);
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


    public static boolean eliminarReservacion(String idReservacion) {
        System.out.println("ENTRO AL METODO: eliminarReservacion");
        Statement stm = null;
        Connection conn = null;
        
        conn = Conexion.getConnection();
    
        try {
            String sql = "DELETE FROM reservacion WHERE id_reservacion = '" + idReservacion + "'";
            stm = (Statement) conn.createStatement();
            int filasAfectadas = stm.executeUpdate(sql);
    
            // Verificamos si se eliminó alguna fila
            if (filasAfectadas > 0) {
                System.out.println("Reservación eliminada exitosamente.");
                return true;
            } else {
                System.out.println("No se encontró ninguna reservación con el ID especificado.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la reservación: " + e);
        } finally {
            // Cierre de recursos
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    
        return false;
    }
    



}