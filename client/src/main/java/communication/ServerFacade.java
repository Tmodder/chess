package communication;

import requestandresult.*;

import java.util.ArrayList;

public class ServerFacade
{
    private final ArrayList<Integer> gameIdList = new ArrayList<>();
    private final  ClientCommunicator communicator = new ClientCommunicator("http://localhost:8080");
    private String authToken;

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
        assert authToken != null;
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
        var stringOut = new StringBuilder();
        var res = communicator.makeRequest("GET","/game", new ListGamesRequest(authToken), ListGamesResult.class, authToken);
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


    public void playGame(int gameNumber,String color)
    {
        //get gameId from facade vector
        int index = gameNumber -1;
        int gameId = gameIdList.get(index);

        //joinGame with given color using id
        communicator.makeRequest("PUT","/game",new JoinGameRequest(authToken,color,gameId),null,authToken);


    }

    public void observeGame ()
    {

    }

}
