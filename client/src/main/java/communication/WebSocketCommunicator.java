package communication;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Scanner;
import com.google.gson.Gson;
import ui.ChessBoardUI;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

@ClientEndpoint
public class WebSocketCommunicator extends Endpoint
{
    private String authToken;
    private int gameId;
    private String teamColor;
    public Session session;
    public WebSocketCommunicator(String color) throws ResponseException {
        try {
            this.teamColor = color;
            URI uri = new URI("ws://localhost:8080/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message)
                {
                    var cmd = new Gson().fromJson(message, ServerMessage.class);
                    switch(cmd.getServerMessageType())
                    {
                        case LOAD_GAME:
                            var loadCmd = new Gson().fromJson(message, LoadGameMessage.class);
                            loadGame(loadCmd);
                            break;
                        case NOTIFICATION:
                            var notifCmd = new Gson().fromJson(message, NotificationMessage.class);
                            break;
                        case ERROR:
                            var errorCmd = new Gson().fromJson(message, ErrorMessage.class);
                            break;


                    }
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

        private void loadGame(LoadGameMessage msg)
        {
            var chessGame = msg.getGame();
            new ChessBoardUI().drawBoard(Objects.equals(teamColor,"white"),chessGame.getBoard());


        }

}
