package handler;
import requestandresult.CreateGameRequest;
import requestandresult.JoinGameRequest;
import requestandresult.ListGamesRequest;
import requestandresult.ListGamesResult;
import service.GameService;
import service.ServiceError;
import spark.Response;
import spark.Request;
public class GameHandler extends Handler{
    public Object listGames(Request request, Response response)
    {
        try
        {
            String authToken = request.headers("authorization");
            ListGamesResult result =  GameService.listGames(new ListGamesRequest(authToken));
            if (result == null)
            {
                return "";
            }

            return resultToJson(result);
        }
        catch (ServiceError error)
        {

            return ErrorHandler.handleServiceError(error,response);
        }

    }

    public Object createGame(Request request, Response response)
    {
        try
        {
            var body = getBody(request, CreateGameRequest.class);
            String authToken = request.headers("authorization");
            var requestWithHeader = new CreateGameRequest(authToken,body.gameName());
            return resultToJson(GameService.createGame(requestWithHeader));
        }

         catch (ServiceError error)
        {
            String message = error.getMessage();
            if (message.equals("Error: unauthorized"))
            {
                response.status(401);
            }
            else {
                response.status(500);
            }
            return ErrorHandler.handleServiceError(error,response);
        }
    }

    public Object joinGame(Request request, Response response)
    {
        try {
            var body = getBody(request, JoinGameRequest.class);
            String authToken = request.headers("authorization");
            var requestWithHeader = new JoinGameRequest(authToken,body.playerColor(),body.gameID());
            return GameService.joinGame(requestWithHeader);
        }

        catch (ServiceError error)
        {
            return ErrorHandler.handleServiceError(error,response);
        }
    }


}
