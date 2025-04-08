package server.websocket;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import service.GameService;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


@WebSocket
public class WebSocketHandler
{
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO)
    {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message)
    {
        try
        {
            System.out.printf("Received: %s", message);
            var command = new Gson().fromJson(message, UserGameCommand.class);

            var serverMessage = switch (command.getCommandType()) {
                case CONNECT -> connect(command);
                case LEAVE -> leave(command);
                default -> null;
            };
            session.getRemote().sendString(new Gson().toJson(serverMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ServerMessage connect(UserGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            var game = gameDAO.getGame(cmd.getGameID());
            // create a server message Load Game new ServerMessage()

            return new LoadGameMessage(game.game());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerMessage leave(UserGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            // create a server message Load Game new ServerMessage()

            return new NotificationMessage(auth.username() + "left the game");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerMessage makeMove(UserGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            // create a server message Load Game new ServerMessage()

            return new NotificationMessage(auth.username() + "left the game");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
