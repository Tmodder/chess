import requestandresult.*;

import java.util.ArrayList;

public class ServerFacade
{
    private final ArrayList<Integer> gameIdList = new ArrayList<>();
    private final  ClientCommunicator communicator = new ClientCommunicator("http/localhost:8080");
    private String authToken;
    void login(String username, String password)
    {
        var res = communicator.makeRequest("POST","/session", new LoginRequest(username,password), LoginResult.class);
        authToken = res.authToken();

    }

    void register(String username, String password, String email)
    {
        var res = communicator.makeRequest("POST","/user",new RegisterRequest(username, password, email), RegisterResult.class);
        authToken = res.authToken();
    }

    void logout()
    {
        assert authToken != null;
        var req = communicator.makeRequest("POST", "/session", new LogoutRequest(authToken),null);
        authToken = null;
    }

    void createGame()
    {
        //add gameId to list
    }

    void listGames()
    {

    }


    void playGame()
    {

    }

    void observeGame ()
    {

    }

}
