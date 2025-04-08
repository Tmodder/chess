package communication;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

@ClientEndpoint
public class WebSocketCommunicator extends Endpoint
{
    private String authToken;
    private int gameId;
    public Session session;
    public WebSocketCommunicator() throws ResponseException {
        try {
            URI uri = new URI("ws://localhost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message)
                {
                    echo(message);
                }
            });
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void echo (String message)
    {
        System.out.println("\n" + message);
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        send(command);
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
            System.out.print("Websocket open");
            send(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId));
        }

}
