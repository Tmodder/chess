package service;

import RequestResult.*;
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
        return ListGamesResult.convertGameModelToResult(gameDatabase.getGamesList());
    }

    public static CreateGameResult createGame(CreateGameRequest req)
    {
        var token = authDatabase.findAuth(req.authToken());
        if (token == null) return null;
        var newGame = new Game(-1,null,null,req.gameName(),new ChessGame());
        return new CreateGameResult(gameDatabase.createGame(newGame));
    }

    public static String joinGame(JoinGameRequest req)
    {
        var token = authDatabase.findAuth(req.authToken());
        if (token == null) return null;
        var gameToJoin = gameDatabase.getGame(req.gameID());
        if (gameToJoin == null) return null;
        if (req.playerColor().equals("BLACK") && gameToJoin.blackUsername().isEmpty())
            gameDatabase.addPlayerToGame("BLACK",token.username(),gameToJoin);
        else if (req.playerColor().equals("WHITE") && gameToJoin.whiteUsername().isEmpty())
            gameDatabase.addPlayerToGame("WHITE",token.username(),gameToJoin);
        return "";
    }

}
