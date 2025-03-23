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
        var res = communicator.makeRequest("POST","/game", new CreateGameRequest(authToken,gameName), CreateGameResult.class,authToken);
    }

    public void listGames()
    {
        var res = communicator.makeRequest("GET","/game", new ListGamesRequest(authToken), ListGamesResult.class, authToken);
    }


    public void playGame()
    {

    }

    public void observeGame ()
    {

    }

}
