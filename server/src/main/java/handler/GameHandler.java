package handler;
import RequestResult.CreateGameRequest;
import RequestResult.ListGamesRequest;
import RequestResult.ListGamesResult;
import service.GameService;
import spark.Response;
import spark.Request;
public class GameHandler extends Handler{
    public Object listGames(Request request, Response response)
    {
        String authToken = request.headers("authorization");
        ListGamesResult result =  GameService.listGames(new ListGamesRequest(authToken));
        return resultToJson(result);
    }

    public Object createGame(Request request, Response response)
    {
        var body = getBody(request, CreateGameRequest.class);
        String authToken = request.headers("authorization");
        var requestWithHeader = new CreateGameRequest(authToken,body.gameName());
        return resultToJson(GameService.createGame(requestWithHeader));
    }

}
