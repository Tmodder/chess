package dataaccess;

import model.Game;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

     static GameDAO makeInstance()  {
        return MemoryGameDAO.getInstance();
    }
    int createGame(Game newGame);

    Game getGame(int gameID);

    ArrayList<Game> getGamesList();

    void addPlayerToGame(String color,String username, Game game);
}
