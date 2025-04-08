package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;


import model.Game;
public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, Game> database = new HashMap<>();
    private int gameCounter = 0;

    //C
    public int createGame(Game newGame) throws DataAccessException{
        String name = newGame.gameName();
        int gameID = this.gameCounter + 1000;
        this.gameCounter += 1;
        database.put(gameID, new Game(gameID, null, null, name, newGame.game()));
        return gameID;
    }

    //R
    public Game getGame(int gameID) throws DataAccessException{
        return database.get(gameID);
    }

    public ArrayList<Game> getGamesList() throws DataAccessException{
        return new ArrayList<>(database.values());
    }

    @Override
    public void updateGame(Game newGame) throws DataAccessException {

    }

    //U
    public void addPlayerToGame(String color, String username, Game game) throws DataAccessException
    {
        Game updatedGame;
        if (color.equals("WHITE"))
        {
            updatedGame = new Game(game.gameID(),username,game.blackUsername(),game.gameName(),game.game());
        }

        else if (color.equals("BLACK")) {
            updatedGame = new Game(game.gameID(), game.whiteUsername(),username,game.gameName(),game.game());
        }
        else
        {
            return;
        }
        database.put(game.gameID(),updatedGame);
    }
    //D

    public void clear() throws DataAccessException
    {
        database.clear();
    }

}
