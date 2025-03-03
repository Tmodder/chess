package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import model.Game;
public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer,Game> database = new HashMap<>();
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
    public void createGame(Game newGame)
    {
        database.put(newGame.gameID(),newGame);
    }

    //R
    public Game getGame(Integer gameID)
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
