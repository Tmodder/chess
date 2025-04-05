package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebSocketHandler
{
    @OnWebSocketMessage
    public void onMessage(Session session, String message)
    {
        try
        {
            System.out.printf("Received: %s", message);
            session.getRemote().sendString(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
