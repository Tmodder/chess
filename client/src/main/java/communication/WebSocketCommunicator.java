package communication;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Scanner;
import com.google.gson.Gson;
import ui.ChessBoardUI;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

@ClientEndpoint
public class WebSocketCommunicator extends Endpoint
{
    private ServerMessageObserver msgObserver;
    public Session session;
    public WebSocketCommunicator(ServerMessageObserver msgObserver) throws ResponseException {
        try {
            this.msgObserver = msgObserver;
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
                            msgObserver.notify(loadCmd);
                            break;
                        case NOTIFICATION:
                            var notifCmd = new Gson().fromJson(message, NotificationMessage.class);
                            msgObserver.notify(notifCmd);
                            break;
                        case ERROR:
                            var errorCmd = new Gson().fromJson(message, ErrorMessage.class);
                            msgObserver.notify(errorCmd);
                            break;
                    }
                }
            });
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

        }

}
