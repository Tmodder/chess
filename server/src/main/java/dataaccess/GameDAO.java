package dataaccess;

import model.Game;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(Game newGame) throws DataAccessException;
    void updateGame(Game newGame) throws DataAccessException;
    Game getGame(int gameID) throws DataAccessException;

    ArrayList<Game> getGamesList() throws DataAccessException;

    void addPlayerToGame (String color,String username, Game game) throws DataAccessException;
    void removePlayerFromGame (String color, Game game) throws DataAccessException;
}
