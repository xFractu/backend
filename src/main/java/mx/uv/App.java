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


        post("/frontend/login", (request, response)->{
            response.type("application/json");
            String payload = request.body();
            Usuario usuario = gson.fromJson(payload, Usuario.class);
            System.out.println("usuario"+usuario);
            System.out.println("payload "+payload);
            String id = UUID.randomUUID().toString();
            usuario.setId(id);
            usuarios.put(id, usuario);
            // DAO.crearUsuario(usuario);
            System.out.println("n "+usuario.getCorreo());
            System.out.println("p "+usuario.getPassword());
            System.out.println("i "+usuario.getId());

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("msj", "Se creo el usuario");
            respuesta.addProperty("id", id);
            return gson.toJson(usuario);
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
