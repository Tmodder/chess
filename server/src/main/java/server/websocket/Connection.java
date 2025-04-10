package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public int gameId;

    public Connection(String username, Session session, int gameId) {
        this.username = username;
        this.session = session;
        this.gameId = gameId;
    }

    public void send(ServerMessage msg) throws IOException {
        session.getRemote().sendString(new Gson().toJson(msg));
    }
}