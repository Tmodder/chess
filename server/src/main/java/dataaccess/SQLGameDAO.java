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
                stmt.executeUpdate("DROP TABLE IF EXISTS game_data");
                stmt.executeUpdate("DROP TABLE IF EXISTS games_list");
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
            String selectAndJoinGame ="""
            SELECT game_data.game_json, games_list.game_name, games_list.white_username, games_list.black_username
            FROM game_data JOIN games_list ON game_data.game_id = games_list.game_id AND game_data.game_id = ?
            """;
             try (var stmt = conn.prepareStatement(selectAndJoinGame))
             {
                 stmt.setInt(1,gameID);
                 var rs = stmt.executeQuery();
                 rs.next();
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
                SELECT game_data.game_id, game_data.game_json, games_list.game_name, games_list.white_username, games_list.black_username
                FROM game_data JOIN games_list ON game_data.game_id = games_list.game_id
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
    public void addPlayerToGame(String color, String username, Game game) throws DataAccessException {
        String updateStatement;
        if (color.equals("WHITE"))
        {
           updateStatement = "UPDATE games_list SET white_username = ? WHERE game_id = ?";
        }

        else if (color.equals("BLACK")) {
            updateStatement = "UPDATE games_list SET black_username = ? WHERE game_id = ?";
        }
        else
        {
            throw new DataAccessException("Error: bad request");
        }
        try(var conn = DatabaseManager.getConnection())
        {
            try(var stmt = conn.prepareStatement(updateStatement)) {
                stmt.setString(1,username);
                stmt.setInt(2,game.gameID());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
