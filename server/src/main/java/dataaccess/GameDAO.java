package dataaccess;

import model.Game;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

     static GameDAO makeInstance()  {
        return MemoryGameDAO.getInstance();
    }
    void createGame(Game newGame);

    Game getGame(Integer gameID);

    ArrayList<Game> getGamesList();

    void joinGame();
}
