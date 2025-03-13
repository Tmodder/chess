package service;

import dataaccess.DataAccessException;
import requestandresult.*;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.Game;

public class GameService {
    private final AuthDAO authDatabase;
    private final GameDAO gameDatabase;

    public GameService(AuthDAO authDatabase, GameDAO gameDatabase) {
        this.authDatabase = authDatabase;
        this.gameDatabase = gameDatabase;
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ServiceError
    {
        try
        {
            authorizeRequest(req.authToken());
            return ListGamesResult.convertGameModelToResult(gameDatabase.getGamesList());
        } catch (DataAccessException e) {
            throw new ServiceError(e.getMessage());
        }

    }

    public CreateGameResult createGame(CreateGameRequest req) throws ServiceError
    {
        try
        {
            authorizeRequest(req.authToken());
            var newGame = new Game(-1,null,null,req.gameName(),new ChessGame());
            return new CreateGameResult(gameDatabase.createGame(newGame));
        }
        catch (DataAccessException e)
        {
            throw  new ServiceError(e.getMessage());
        }

    }

    public String joinGame(JoinGameRequest req) throws ServiceError
    {
        try
        {
            authorizeRequest(req.authToken());
            var token = authDatabase.findAuth(req.authToken());
            var gameToJoin = gameDatabase.getGame(req.gameID());
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
                gameDatabase.addPlayerToGame("BLACK",token.username(),gameToJoin);
            }

            else if (req.playerColor().equals("WHITE") && gameToJoin.whiteUsername() == null)
            {
                gameDatabase.addPlayerToGame("WHITE",token.username(),gameToJoin);
            }

            else
            {
                throw new ServiceError("Error: already taken");
            }
            return "";
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Illegal operation on empty result set."))
            {
                throw new ServiceError("Error: bad request");
            }
            throw new ServiceError(e.getMessage());
        }

    }

    private void authorizeRequest(String authToken) throws ServiceError
    {
        try
        {
            var token = authDatabase.findAuth(authToken);
            if (token == null)
            {
                throw new ServiceError("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new ServiceError("Error: unauthorized");
        }


    }

}
