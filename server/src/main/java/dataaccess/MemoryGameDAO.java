package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.Game;
public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer,Game> database = new HashMap<>();
    private int gameCounter = 0;
    private static MemoryGameDAO instance;

    private MemoryGameDAO() {}

    public static synchronized MemoryGameDAO getInstance() {
        if (instance == null)
        {
            instance =  new MemoryGameDAO();
        }
        return instance;
    }

    //C
    public int createGame(Game newGame)
    {
        String name = newGame.gameName();
        int gameID = this.gameCounter + 1000;
        this.gameCounter += 1;
        database.put(newGame.gameID(),new Game(gameID, null,null,name, newGame.game()));
        return newGame.gameID();
    }

    //R
    public Game getGame(int gameID)
    {
        return database.get(gameID);
    }

    public ArrayList<Game> getGamesList()
    {
        return new ArrayList<>(database.values());
    }
    //U
    public void joinGame()
    {}
    //D

    public void clear()
    {
        database.clear();
    }

}
