package server;
import handler.*;
import spark.*;
import com.google.gson.Gson;
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
       // Spark.init();
        var userHandler = new UserHandler();
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);
        Spark.delete("/db", (req, res) -> "{\"name\":\"John\", \"age\":30, \"car\":null}");
        //do the same for post and each method repeating if there is a different path

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private static <T> T getBody(Request request, Class<T> outputClass)
    {
        return new Gson().fromJson(request.body(),outputClass);

    }
}
