package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;
import websocket.commands.MoveGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler
{
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ConnectionManager connections = new ConnectionManager();

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
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE)
            {
                command = new Gson().fromJson(message, MoveGameCommand.class);
            }

            switch (command.getCommandType()) {
                case CONNECT:
                    connect(session, command);
                case RESIGN:
                    resign(command);
                case LEAVE:
                    leave(command);
                case MAKE_MOVE:
                    makeMove((MoveGameCommand) command);
                default:
                    throw new RuntimeException("Unknown crap");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void resign(UserGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            // create a server message Load Game new ServerMessage()
            var gameData = gameDAO.getGame(cmd.getGameID());
            var game = gameData.game();
            game.endGame();
            saveGame(gameData,game);
            connections.broadcast(null,new NotificationMessage(auth.username() + "resigned the game"));

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(Session session, UserGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            String username = auth.username();
            var game = gameDAO.getGame(cmd.getGameID());
            // create a server message Load Game new ServerMessage()
            connections.add(username,session);


            connections.broadcast(username,new NotificationMessage("Everyone welcome " + username +" to the game!"));
            connections.getConnection(username).send(new LoadGameMessage(game.game()));

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void leave(UserGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            String username = auth.username();
            // create a server message Load Game new ServerMessage()
            connections.broadcast(username,new NotificationMessage(username + "left the game"));
            connections.remove(username);
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMove(MoveGameCommand cmd)
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            // create a server message Load Game new ServerMessage()
            String username = auth.username();

            var gameData = gameDAO.getGame(cmd.getGameID());
            var chessGame = gameData.game();
            chessGame.makeMove(cmd.getMove());
            saveGame(gameData,chessGame);

            connections.broadcast(null,new LoadGameMessage(chessGame));
            connections.broadcast(username,new NotificationMessage(username + "moved from don't care to couldn't care less" ));
        }
        catch (DataAccessException | InvalidMoveException |IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveGame(Game oldGame, ChessGame newGame)
    {
        try
        {
            var gameData = new Game(oldGame.gameID(),oldGame.whiteUsername(),oldGame.blackUsername(),oldGame.gameName(),newGame);
            gameDAO.updateGame(gameData);
        }
        catch (DataAccessException e)

        {
            throw new RuntimeException(e.getMessage());
        }
    }

}
