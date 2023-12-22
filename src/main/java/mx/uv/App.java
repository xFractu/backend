package mx.uv;

/**
 * Hello world!
 *
 */

import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.google.gson.*;

public class App 
{
    static Gson gson = new Gson();
    static HashMap<String, Usuario> usuarios = new HashMap<String, Usuario>();
    static String correoG;
    static String passwordG;
    static String nombreG;
    static String idG;
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        //port(80);
        port(getHerokuAssignedPort());

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/backend/verificar-conexion", (request, response) -> {
            response.type("application/json");
            
            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Conexión exitosa al backend");
            
            return respuesta.toString();
        });

        post("/frontend/", (request, response)->{
            response.type("application/json");
            String payload = request.body();
            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Usuario usuario = gson.fromJson(jsonObject.get("datosFormulario"), Usuario.class);
            System.out.println("usuario"+usuario);
            System.out.println("payload "+payload);
            String id = UUID.randomUUID().toString();
            usuario.setId(id);
            usuarios.put(id, usuario);
            DAO.crearUsuario(usuario);
            System.out.println("i "+usuario.getId());
            System.out.println("n "+usuario.getCorreo());
            System.out.println("p "+usuario.getPassword());
            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("msj", "Se creo el usuario");
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
        });

        post("/frontend/obtenerUsuario", (request, response) -> {
            response.type("application/json");
        
            // Puedes acceder a las variables globales directamente o utilizar métodos getter según tu implementación
            String correo = correoG;
            String password = passwordG;
            String nombre = nombreG;
        
            // Construir un objeto JSON con los datos del usuario
            JsonObject usuarioJson = new JsonObject();
            usuarioJson.addProperty("correo", correo);
            usuarioJson.addProperty("password", password);
            usuarioJson.addProperty("nombre", nombre);
            System.out.println(nombre);;
            System.out.println(usuarioJson);
            return usuarioJson.toString();
        });


        post("/frontend/login", (request, response)->{
            response.type("application/json");
            String payload = request.body();
            System.out.println("payload "+payload);
            // DAO.crearUsuario(usuario);

            String correo = "";
            String password = "";

            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // Accede a la clave "datosFormulario" y luego obtén las claves "correo" y "password"
            JsonObject datosFormulario = jsonObject.getAsJsonObject("datosFormulario");

            if (datosFormulario != null && datosFormulario.has("correo") && datosFormulario.has("password")) {
                correo = datosFormulario.get("correo").getAsString();
                password = datosFormulario.get("password").getAsString();
                System.out.println("Correogson: " + correo);
                System.out.println("Passwordgson: " + password);
            } else {
                System.out.println("Las claves 'correo' y/o 'password' no están presentes en el JSON.");
            }
            boolean esUsuarioValido = DAO.validarUsuario(correo, password);
            JsonObject respuesta = new JsonObject();
            if (esUsuarioValido) {
            correoG = correo;
            passwordG = password;
            String id = DAO.obtenerIdUsuario(correoG,passwordG);
            idG = id;
            System.out.println("correo valido "+correoG);
            System.out.println("password valido "+passwordG);
            System.out.println("id valido "+passwordG);
            
            
            Usuario usuario = DAO.obtenerDatosUsuario(id);
            nombreG = usuario.getNombre();
            System.out.println("nombre valido: "+nombreG);
            respuesta.addProperty("msj", "Valido");
            respuesta.addProperty("nombre", usuario.getNombre());
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
            } else {
                respuesta.addProperty("msj", "Invalido");
                return "Invalido";
            }
        });


        post("/frontend/hacerReservacionHotel1", (request, response) -> {
            response.type("application/json");
            String payload = request.body();
            
            try {
                JsonElement jsonElement = JsonParser.parseString(payload);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject reservationDataJson = jsonObject.getAsJsonObject("reservationData");

                System.out.println(reservationDataJson);

                // Asignar valores faltantes a la reservación
                Reservaciones reservacion = new Reservaciones();
                reservacion.setIdR(UUID.randomUUID().toString());
                reservacion.setIdU(idG); // Asigna el ID del usuario
                reservacion.setIdH("1"); // Asigna el ID del hotel
                reservacion.setNombre("Holiday Inn Express"); // Asigna el nombre
                reservacion.setPrecio("1,600"); // Asigna el precio del hotel

                // Asegúrate de que estás usando las claves correctas del JSON
                if (reservationDataJson.has("checkInDate")) {
                    reservacion.setCheckIn(reservationDataJson.get("checkInDate").getAsString());
                }
                if (reservationDataJson.has("checkOutDate")) {
                    reservacion.setCheckOut(reservationDataJson.get("checkOutDate").getAsString());
                }
                if (reservationDataJson.has("quantity")) {
                    reservacion.setPersonas(reservationDataJson.get("quantity").getAsString());
                }

                // Puedes realizar acciones adicionales con la información de la reservación
                System.out.println("Reservación: " + reservacion);
        
                // Lógica para hacer la reservación en la base de datos
                String mensaje = DAO.hacerReservacion(reservacion);
        
                // Crear la respuesta
                JsonObject respuesta = new JsonObject();
                respuesta.addProperty("msj", mensaje);
        
                return gson.toJson(respuesta);
            } catch (JsonSyntaxException e) {
                // Manejar errores de formato JSON
                System.out.println("Error en el formato JSON: " + e.getMessage());
                response.status(400); // Bad Request
                return gson.toJson("Error en el formato JSON");
            } catch (Exception e) {
                // Manejar otros errores
                System.out.println("Error en la reservación: " + e.getMessage());
                response.status(500); // Internal Server Error
                return gson.toJson("Error en la reservación");
            }
        });


        post("/frontend/obtenerReservaciones", (request, response) -> {
            response.type("application/json");

            // Obtener el ID del usuario desde la variable global
            String idUsuario = idG;

            // Obtener las reservaciones para el usuario
            List<Reservaciones> reservaciones = DAO.dameReservacionesPorUsuario(idUsuario);

            int numeroDeReservaciones = reservaciones.size();

            System.out.println("Número de reservaciones: " + numeroDeReservaciones);

            // Construir un objeto JSON con las reservaciones
            JsonArray reservacionesArray = new JsonArray();
            System.out.println("Todavia no entra al for" );
            for (Reservaciones reservacion : reservaciones) {
                JsonObject reservacionJson = new JsonObject();
                reservacionJson.addProperty("id_reservacion", reservacion.getIdR());
                reservacionJson.addProperty("id_usuario", reservacion.getIdU());
                reservacionJson.addProperty("id_hotel", reservacion.getIdH());
                reservacionJson.addProperty("nombre_hotel", reservacion.getNombre());
                reservacionJson.addProperty("precio", reservacion.getPrecio());
                reservacionJson.addProperty("check_in", reservacion.getCheckIn());
                reservacionJson.addProperty("check_out", reservacion.getCheckOut());
                reservacionJson.addProperty("personas", reservacion.getPersonas());
                reservacionesArray.add(reservacionJson);
                System.out.println("hola");
            }

            // Crear el objeto final que contiene todas las reservaciones
            JsonObject responseJson = new JsonObject();
            responseJson.add("reservaciones", reservacionesArray);

            System.out.println(responseJson);
            return responseJson.toString();
        });

        post("/frontend/eliminarReservacion", (request, response) -> {
            response.type("application/json");
            String payload = request.body();
            JsonElement jsonElement = JsonParser.parseString(payload);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String idReservacion = jsonObject.get("datosId").getAsJsonObject().get("idReservacion").getAsString();
        
            // Lógica para eliminar la reservación usando el ID
            boolean eliminado = DAO.eliminarReservacion(idReservacion);
        
            JsonObject respuesta = new JsonObject();
            if (eliminado) {
                respuesta.addProperty("msj", "Reservación eliminada exitosamente.");
            } else {
                respuesta.addProperty("msj", "No se encontró ninguna reservación con el ID especificado.");
            }
        
            return respuesta.toString();
        });









    }



    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
