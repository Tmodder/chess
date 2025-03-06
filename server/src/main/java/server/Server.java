package server;
import dataaccess.*;
import handler.*;
import spark.*;
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        UserDAO userDb = new MemoryUserDAO();
        AuthDAO authDb = new MemoryAuthDAO();
        GameDAO gameDb = new MemoryGameDAO();
        var userHandler = new UserHandler(userDb, authDb);
        var clearHandler = new ClearHandler(userDb,authDb,gameDb);
        var gameHandler = new GameHandler(authDb,gameDb);
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);
        Spark.delete("/session", userHandler::logout);
        Spark.delete("/db",clearHandler::clear);

        Spark.get("/game", gameHandler::listGames);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
