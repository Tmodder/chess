package server;
import handler.*;
import service.ClearService;
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
        var clearHandler = new ClearHandler();
        var gameHandler = new GameHandler();
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);
        Spark.delete("/session", userHandler::logout);
        Spark.delete("/db",clearHandler::clear);
        Spark.get("/game", gameHandler::listGames);
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
