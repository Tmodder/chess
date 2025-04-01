package server;
import dataaccess.*;
import handler.*;
import spark.Spark;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

@WebSocket
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        UserDAO userDb = new SQLUserDAO();
        AuthDAO authDb = new SQLAuthDAO();
        GameDAO gameDb = new SQLGameDAO();
        var userHandler = new UserHandler(userDb, authDb);
        var clearHandler = new ClearHandler(userDb,authDb,gameDb);
        var gameHandler = new GameHandler(authDb,gameDb);
        Spark.webSocket("/ws",Server.class);
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

    @OnWebSocketMessage
    public void onMessage(Session session, String message)
    {
        try
        {
            System.out.printf("Received: %s", message);
            session.getRemote().sendString("WebSocket response: " + message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}
