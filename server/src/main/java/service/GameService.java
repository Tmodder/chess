package service;

import requestandresult.*;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.Game;

public class GameService {
    private final AuthDAO AUTH_DATABASE;
    private final GameDAO GAME_DATABASE;

    public GameService(AuthDAO AUTH_DATABASE, GameDAO GAME_DATABASE) {
        this.AUTH_DATABASE = AUTH_DATABASE;
        this.GAME_DATABASE = GAME_DATABASE;
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ServiceError
    {
        authorizeRequest(req.authToken());
        return ListGamesResult.convertGameModelToResult(GAME_DATABASE.getGamesList());
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ServiceError
    {
        authorizeRequest(req.authToken());
        var newGame = new Game(-1,null,null,req.gameName(),new ChessGame());
        return new CreateGameResult(GAME_DATABASE.createGame(newGame));
    }

    public String joinGame(JoinGameRequest req) throws ServiceError
    {
        authorizeRequest(req.authToken());
        var token = AUTH_DATABASE.findAuth(req.authToken());
        var gameToJoin = GAME_DATABASE.getGame(req.gameID());
        if (gameToJoin == null)
        {
            throw new ServiceError("Error: bad request");
        }
        else if (req.playerColor() == null)
        {
            throw new ServiceError("Error: bad request");
        }

        else if (!(req.playerColor().equals("BLACK") || req.playerColor().equals("WHITE")))
        {
            throw new ServiceError("Error: bad request");
        }

        else if (req.playerColor().equals("BLACK") && gameToJoin.blackUsername() == null)
        {
            GAME_DATABASE.addPlayerToGame("BLACK",token.username(),gameToJoin);
        }

        else if (req.playerColor().equals("WHITE") && gameToJoin.whiteUsername() == null)
        {
            GAME_DATABASE.addPlayerToGame("WHITE",token.username(),gameToJoin);
        }

        else
        {
            throw new ServiceError("Error: already taken");
        }
        return "";
    }

    private void authorizeRequest(String authToken) throws ServiceError
    {
        var token = AUTH_DATABASE.findAuth(authToken);
        if (token == null)
        {
            throw new ServiceError("Error: unauthorized");
        }

    }

}
