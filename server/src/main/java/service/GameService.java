package service;

import RequestResult.ListGamesRequest;
import RequestResult.ListGamesResult;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

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


}
