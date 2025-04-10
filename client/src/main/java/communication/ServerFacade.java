package communication;

import chess.ChessMove;
import chess.ChessPosition;
import requestandresult.*;
import ui.ServerMessageObserver;
import websocket.commands.MoveGameCommand;
import websocket.commands.UserGameCommand;

import java.util.ArrayList;
import java.util.Objects;

public class ServerFacade
{
    private final ArrayList<Integer> gameIdList = new ArrayList<>();
    private final  ClientCommunicator communicator;
    private ListGamesResult listRes = null;
    private final WebSocketCommunicator socket;
    private String authToken;
    private String username;

    public ServerFacade(int port, ServerMessageObserver msgObserver) {
        String url = "http://localhost:" + String.valueOf(port);
        communicator = new ClientCommunicator(url);
        socket =  new WebSocketCommunicator(msgObserver);
    }

    public void login(String username, String password)
    {
        var res = communicator.makeRequest("POST","/session", new LoginRequest(username,password), LoginResult.class, null);
        authToken = res.authToken();

    }

    public void register(String username, String password, String email)
    {
        var res = communicator.makeRequest("POST","/user",new RegisterRequest(username, password, email), RegisterResult.class, null);
        authToken = res.authToken();
    }

    public void logout()
    {
        var req = communicator.makeRequest("DELETE", "/session", new LogoutRequest(authToken),null,authToken);
        authToken = null;
    }

    public void createGame(String gameName)
    {
        assert authToken != null;
        var res = communicator.makeRequest("POST","/game", new CreateGameRequest(authToken,gameName), CreateGameResult.class, authToken);
    }

    public String listGames()
    {
        assert authToken != null;
        gameIdList.clear();
        var stringOut = new StringBuilder();
        var res = communicator.makeRequest("GET","/game", new ListGamesRequest(authToken), ListGamesResult.class, authToken);
        listRes = res;
        if (res.games().isEmpty())
        {
            return null;
        }
        stringOut.append("Games List: \n");
        for (int i = 0; i < res.games().size(); i++)
        {

            var game = res.games().get(i);
            gameIdList.add(game.gameID());
            stringOut.append(i + 1);
            stringOut.append(". ");
            stringOut.append(game.gameName());
            stringOut.append("\n     ");
            stringOut.append("White:");
            if (game.whiteUsername() != null)
            {
                stringOut.append(game.whiteUsername());
            }
            else
            {
                stringOut.append("Available");
            }
            stringOut.append(" ");
            stringOut.append("Black:");
            if (game.blackUsername() != null)
            {
                stringOut.append(game.blackUsername());
            }
            else
            {
                stringOut.append("Available");
            }
            stringOut.append("\n");
        }
        stringOut.append("\n");
        return stringOut.toString();
    }

    public boolean tryHotJoin(int gameNumber, String color, String username)
    {
        int index = gameNumber -1;
        int gameId = gameIdList.get(index);
        var curGame = listRes.games().get(index);
        if (Objects.equals(curGame.whiteUsername(), username) && Objects.equals(color, "WHITE")
                || Objects.equals(curGame.blackUsername(), username) && Objects.equals(color, "BLACK"))
        {
            socket.send(new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameId));
            return true;
        }
        return false;
    }


    public void playGame(int gameNumber,String color)
    {
        //get gameId from facade vector
        int index = gameNumber -1;
        int gameId = gameIdList.get(index);

        //joinGame with given color using id
        communicator.makeRequest("PUT","/game",new JoinGameRequest(authToken,color,gameId),null,authToken);
        socket.send(new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameId));

    }

    public void observeGame(int gameNumber)
    {
        int gameId = gameIdList.get(gameNumber -1);
        socket.send(new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameId));
    }

    public void clear()
    {
        communicator.makeRequest("DELETE","/db",null,null,null);
    }

    public void makeMove(int gameNumber,ChessMove move)
    {
        int gameId = gameIdList.get(gameNumber - 1);
        socket.send(new MoveGameCommand(authToken,gameId,move));
    }

    public void resign()
    {

    }

    public void leave(int gameNumber)
    {
        int gameId = gameIdList.get(gameNumber - 1);
        socket.send(new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken,gameId));
    }


}
