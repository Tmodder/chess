package server.websocket;

import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import dataaccess.SQLGameDAO;
import websocket.messages.ServerMessage;


@WebSocket
public class WebSocketHandler
{
    private final SQLGameDAO gameDAO = new SQLGameDAO();
    @OnWebSocketMessage
    public void onMessage(Session session, String message)
    {
        try
        {
            System.out.printf("Received: %s", message);
            var command = new Gson().fromJson(message, UserGameCommand.class);

            var serverMessage = switch (command.getCommandType()) {
                case CONNECT -> connect(command);
                default -> null;
            };
            session.getRemote().sendString(new Gson().toJson("TEST TEST TEST"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ServerMessage connect(UserGameCommand cmd)
    {
        try
        {
            var game = gameDAO.getGame(cmd.getGameID());
            // create a server message Load Game new ServerMessage()
            game.game();
            System.out.println("Called it!");
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }



    }
}
