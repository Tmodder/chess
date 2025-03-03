package service;

import RequestResult.CreateGameRequest;
import RequestResult.CreateGameResult;
import RequestResult.ListGamesRequest;
import RequestResult.ListGamesResult;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.Game;

public class GameService {
    private static final UserDAO userDatabase = UserDAO.makeInstance();
    private static final AuthDAO authDatabase = AuthDAO.makeInstance();
    private static final GameDAO gameDatabase = GameDAO.makeInstance();

    public static ListGamesResult listGames(ListGamesRequest req)
    {
        var token = authDatabase.findAuth(req.authToken());
        if (token == null) return null;
        return new ListGamesResult(gameDatabase.getGamesList());
    }

    public static CreateGameResult createGame(CreateGameRequest req)
    {
        var token = authDatabase.findAuth(req.authToken());
        if (token == null) return null;
        var newGame = new Game(-1,null,null,req.gameName(),new ChessGame());
        return new CreateGameResult(gameDatabase.createGame(newGame));
    }

}
