package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO
{
    public SQLGameDAO()
    {
        try
        {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clear() throws DataAccessException{
        try(var conn = DatabaseManager.getConnection())
        {
            try(var stmt = conn.createStatement())
            {
                stmt.executeUpdate("DROP TABLE game_data");
                stmt.executeUpdate("DROP TABLE games_list");
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public int createGame(Game newGame) throws DataAccessException{
        String gameName = newGame.gameName();
        String whiteUsername = newGame.whiteUsername();
        String blackUsername = newGame.blackUsername();
        String gameJson = serializeGame(newGame.game());
        //first get data out of model and serialize the game
        //second create gameId
        //third insert into game_data the game id and game json string
        //fourth insert into games_list rest of data
        //return game_Id
        return 0;
    }

    @Override
    public Game getGame(int gameID) throws DataAccessException{
        //Select * from games_list WHERE gameID = gameID
        // rs.next() get all the data
        //Select game_json from game_data WHERE gameID = gameID
        // deserialize chess game string
        // create a model with all that data
        return null;
    }

    @Override
    public ArrayList<Game> getGamesList() throws DataAccessException{
        // join tables
        // get all the data for each row deserialize game_json
        // create a new Game object and add to list
        // return list
        return null;
    }

    @Override
    public void addPlayerToGame(String color, String username, Game game) {

    }

    private String serializeGame(ChessGame game)
    {
        var g = new Gson();
        return g.toJson(new ChessGame());

    }

    private ChessGame  deserializeGame(String json)
    {
        var g = new Gson();
        return g.fromJson(json,ChessGame.class);
    }

}
