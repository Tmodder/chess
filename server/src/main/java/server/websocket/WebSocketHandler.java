package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

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
            if (!checkAuth(command.getAuthToken(),session) || !checkGame(command.getGameID(),session))
            {
                return;
            }

            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE)
            {
                command = new Gson().fromJson(message, MoveGameCommand.class);
            }

            switch (command.getCommandType()) {
                case CONNECT:
                    connect(session, command);
                    break;
                case RESIGN:
                    resign(command);
                    break;
                case LEAVE:
                    leave(command);
                    break;
                case MAKE_MOVE:
                    makeMove((MoveGameCommand) command);
                    break;
                default:
                    throw new RuntimeException("Unknown crap");
            }
        } catch (InvalidMoveException e) {
            handleError(e.getMessage(),session,e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void resign(UserGameCommand cmd) throws InvalidMoveException
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            // create a server message Load Game new ServerMessage()
            var gameData = gameDAO.getGame(cmd.getGameID());
            var game = gameData.game();
            String username = auth.username();
            if (!(Objects.equals(gameData.whiteUsername(), username) || Objects.equals(gameData.blackUsername(), username)))
            {
                throw new InvalidMoveException("isObserver");
            }
            if (game.isGameOver())
            {
                throw new InvalidMoveException("gameOver");
            }
            game.endGame();
            saveGame(gameData,game);
            connections.broadcast(null,new NotificationMessage(auth.username() + " resigned the game"), gameData.gameID());

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(Session session, UserGameCommand cmd) throws IOException
    {
        try {

            var gameData = gameDAO.getGame(cmd.getGameID());
            var auth = authDAO.findAuth(cmd.getAuthToken());
            String username = auth.username();
            connections.add(username,session,gameData.gameID());
            var teamColor = getTeamColor(gameData,username);
            String userType = "observer";
            if (teamColor == ChessGame.TeamColor.WHITE)
            {
                userType = "white";
            }
            else if (teamColor == ChessGame.TeamColor.BLACK)
            {
                userType = "black";
            }
            connections.broadcast(username,new NotificationMessage("Everyone welcome " + username +" as "+ userType + " to the game!"), gameData.gameID());
            connections.getConnection(username).send(new LoadGameMessage(gameData.game()));
        }
        catch (DataAccessException e)
        {
            handleError(null,session,e);
        }

    }

    private void handleError(String type, Session session, Exception e)
    {
        try
        {
            ErrorMessage errorMessage;
            if (Objects.equals(type,"gameNotFound"))
            {
                errorMessage = new ErrorMessage("Error: game not found");
            }

            else if (Objects.equals(type,"badAuth"))
            {
                errorMessage = new ErrorMessage(("Error: bad auth "));
            }

            else if (Objects.equals(type,"badMove") || Objects.equals(type,"Move not valid") )
            {
                errorMessage = new ErrorMessage("Error: move not valid");
            }

            else if (Objects.equals(type,"outOfOrder"))
            {
                errorMessage = new ErrorMessage("Error: move out of turn");
            }

            else if (Objects.equals(type,"isObserver"))
            {
                errorMessage = new ErrorMessage("Error: Observer cant move or resign");
            }

            else if (Objects.equals(type,"gameOver"))
            {
                errorMessage = new ErrorMessage("Error: Move forbidden because game is over");
            }

            else
            {
                errorMessage = new ErrorMessage("Error: not recognized");
            }
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public void leave(UserGameCommand cmd)
    {
        try
        {
            var authData = authDAO.findAuth(cmd.getAuthToken());
            var gameData = gameDAO.getGame(cmd.getGameID());
            String username = authData.username();
            if(Objects.equals(username, gameData.whiteUsername()))
            {
                gameDAO.removePlayerFromGame("WHITE",gameData);
            }
            else if (Objects.equals(username, gameData.blackUsername()))
            {
                gameDAO.removePlayerFromGame("BLACK",gameData);
            }
            //observer case: do nothing

            // create a server message Load Game new ServerMessage()
            connections.broadcast(username,new NotificationMessage(username + " left the game"), gameData.gameID());
            connections.remove(username);
        } catch (DataAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    public void makeMove(MoveGameCommand cmd) throws InvalidMoveException
    {
        try
        {
            var auth = authDAO.findAuth(cmd.getAuthToken());
            // create a server message Load Game new ServerMessage()
            String username = auth.username();

            var gameData = gameDAO.getGame(cmd.getGameID());
            var chessGame = gameData.game();

            if(gameData.game().isGameOver())
            {
                throw new InvalidMoveException("gameOver");
            }
            ChessGame.TeamColor teamColor = getTeamColor(gameData,username);

            if (teamColor == null)
            {
                throw new InvalidMoveException("isObserver");
            }

            if (teamColor != chessGame.getTeamTurn())
            {
               throw new InvalidMoveException("outOfOrder");
            }
            chessGame.makeMove(cmd.getMove());
            saveGame(gameData,chessGame);


            connections.broadcast(null,new LoadGameMessage(chessGame), gameData.gameID());

            var oppColor = teamColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            String oppUsername = Objects.equals(username, gameData.whiteUsername()) ? gameData.blackUsername() : gameData.whiteUsername();
            String statusMessage = oppUsername + " is in ";

            if (chessGame.isInCheckmate(oppColor)) {
                connections.broadcast(null, new NotificationMessage(statusMessage + " checkmate. Game over!"), gameData.gameID());
            }

            else if (chessGame.isInCheck(oppColor))
            {
                connections.broadcast(null,new NotificationMessage(statusMessage + " check"), gameData.gameID());
            }

            else if (chessGame.isInStalemate(oppColor))
            {
                statusMessage = "Both players in stalemate! Draw!";
                connections.broadcast(null,new NotificationMessage(statusMessage), gameData.gameID());
            }

            String moveStart = convertPosToNotation(cmd.getMove().getStartPosition());
            String moveEnd = convertPosToNotation(cmd.getMove().getEndPosition());
            String message = username + " moved from " + moveStart + " to " + moveEnd;
            connections.broadcast(null,new LoadGameMessage(chessGame), gameData.gameID());
            connections.broadcast(username,new NotificationMessage(message), gameData.gameID());
        }
        catch (DataAccessException|IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private String convertPosToNotation(ChessPosition pos)
    {
        char l = (char) (pos.getColumn() + 96);
        String letter = String.valueOf(l);
        String number = String.valueOf(pos.getRow());
        return letter + number;
    }

    private boolean checkGame(int id, Session session)
    {
        try
        {
            gameDAO.getGame(id);
        }
        catch (DataAccessException e)
        {
            handleError("gameNotFound", session, e);
            return false;
        }
        return true;

    }

    private boolean checkAuth(String authToken,Session session)
    {
        try
        {
            authDAO.findAuth(authToken);
        }
        catch (DataAccessException e)
        {
            handleError("badAuth", session, e);
            return false;
        }
        return true;
    }

    private static ChessGame.TeamColor getTeamColor(Game gameData, String username)  {
        ChessGame.TeamColor teamColor;
        if (Objects.equals(gameData.whiteUsername(), username))
        {
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(gameData.blackUsername(), username))
        {
            teamColor = ChessGame.TeamColor.BLACK;
        }
        else
        {
            teamColor = null;
        }
        return teamColor;
    }


}
