package communication;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

public class WebSocketCommunicator extends Endpoint
{
    private String authToken;
    private int gameId;
    public Session session;
    public WebSocketCommunicator() throws ResponseException {
        try {
            URI uri = new URI("ws://localhpost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler((MessageHandler.Whole<String>) System.out::println);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
        public void send (UserGameCommand command)
        {
            try {

                this.session.getBasicRemote().sendText(new Gson().toJson(command));
            } catch (IOException e) {
                throw new ResponseException(500,e.getMessage());
            }
        }

        public void onOpen(Session session, EndpointConfig endpointConfig)
        {

            send(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId));
        }
}
