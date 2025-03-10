package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO
{
    private int gameCounter = 0;
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
        int gameID = this.gameCounter + 1000;
        this.gameCounter += 1;
        String insertGameDataStatement = "INSERT INTO game_data VALUES(?,?,?,?)";
        String insertGamesListStatement = "INSERT INTO games_list VALUES(?,?)";
        try(var conn = DatabaseManager.getConnection())
        {
           try (var stmt = conn.prepareStatement(insertGameDataStatement))
           {
               stmt.setInt(1,gameID);
               stmt.setString(2,gameName);
               stmt.setString(3,whiteUsername);
               stmt.setString(4,blackUsername);
               stmt.executeUpdate();
           }

           try (var stmtTwo = conn.prepareStatement(insertGamesListStatement))
           {
               stmtTwo.setInt(1,gameID);
               stmtTwo.setString(2,gameJson);
               stmtTwo.executeUpdate();
           }

        }
        catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        return gameID;
    }

    @Override
    public Game getGame(int gameID) throws DataAccessException{
////        try(var conn = DatabaseManager.getConnection())
////        {
////            String selectF
////
////            try (var stmt = conn.prepareStatement())
////            {
////                stmt.executeQuery("")
////            }
////        }
//        catch (SQLException e)
//        {
//            throw new DataAccessException(e.getMessage());
//        }
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
