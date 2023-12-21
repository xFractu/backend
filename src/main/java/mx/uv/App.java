package mx.uv;

/**
 * Hello world!
 *
 */

import static spark.Spark.*;

import java.util.HashMap;
import java.util.UUID;
import com.google.gson.*;

public class App 
{
    static Gson gson = new Gson();
    static HashMap<String, Usuario> usuarios = new HashMap<String, Usuario>();
    static String correoG;
    static String passwordG;
        static String nombreG;
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


        post("/login", (request, response)->{
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
            System.out.println("correo valido "+correoG);
            System.out.println("password valido "+passwordG);
            String id = DAO.obtenerIdUsuario(correoG,passwordG);
            
            Usuario usuario = DAO.obtenerDatosUsuario(id);
            nombreG = usuario.getNombre();
            System.out.println("nombre valido: "+nombreG);
            respuesta.addProperty("msj", "Valido");
            respuesta.addProperty("nombre", usuario.getNombre());
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
            } else {
                respuesta.addProperty("msj", "Invalido");
                return "invalido";
            }
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
