package requestandresult;
import model.Game;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<SingleGameResult> games) {
    record SingleGameResult(int gameID, String whiteUsername, String blackUsername, String gameName )
    {}
    public static ListGamesResult convertGameModelToResult(ArrayList<Game> gamesList)
    {
        var resultsList = new ArrayList<SingleGameResult>();
        for (Game game : gamesList)
        {
            var gameResult = new SingleGameResult(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName());
            resultsList.add(gameResult);
        }
        return new ListGamesResult(resultsList);
    }
}
