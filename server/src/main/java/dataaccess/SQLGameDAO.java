package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO
{
    private int gameCounter = 0;
    public SQLGameDAO()
            //run every time? clear then calling over functions will return not exist
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
        DatabaseManager.createTables();
        String gameName = newGame.gameName();
        String gameJson = serializeGame(newGame.game());
        int gameID = this.gameCounter + 1000;
        this.gameCounter += 1;
        String insertGameDataStatement = "INSERT INTO games_list VALUES(?,?,NULL,NULL)";
        String insertGamesListStatement = "INSERT INTO game_data VALUES(?,?)";
        try(var conn = DatabaseManager.getConnection())
        {
           try (var stmt = conn.prepareStatement(insertGameDataStatement))
           {
               stmt.setInt(1,gameID);
               stmt.setString(2,gameName);
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
        DatabaseManager.createTables();
        try(var conn = DatabaseManager.getConnection())
        {
            String selectAndJoinGame = """
            SELECT game_data.game_json, games_list.game_name, games_list.white_username, games_list.black_username
            FROM game_data JOIN games_list WHERE game_data.game_id = ?
            """;
             try (var stmt = conn.prepareStatement(selectAndJoinGame))
             {
                 stmt.setInt(1,gameID);
                 var rs = stmt.executeQuery();
                 String json = rs.getString(1);
                 String gameName = rs.getString(2);
                 String whiteUsername = rs.getString(3);
                 String blackUsername = rs.getString(4);
                 return new Game(gameID,whiteUsername,blackUsername,gameName,deserializeGame(json));
             }
       }
       catch (SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<Game> getGamesList() throws DataAccessException{
        DatabaseManager.createTables();
        String getAllGameData = """
                SELECT DISTINCT game_data.game_id, game_data.game_json, games_list.game_name, games_list.white_username, games_list.black_username
                FROM game_data JOIN games_list
                """;
        ArrayList<Game> gamesList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection())
        {
            try (var stmt = conn.prepareStatement(getAllGameData))
            {
               var rs = stmt.executeQuery();
               while(rs.next())
               {
                   int gameId = rs.getInt(1);
                   String json = rs.getString(2);
                   String gameName = rs.getString(3);
                   String whiteUsername = rs.getString(4);
                   String blackUsername = rs.getString(5);
                   gamesList.add(new Game(gameId,whiteUsername,blackUsername,gameName,deserializeGame(json)));
               }
               return gamesList;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
