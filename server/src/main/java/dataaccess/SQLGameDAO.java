package dataaccess;

import model.Game;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO
{
    @Override
    public void clear() {
        //do sql command drop table game_data, drop table games_list
    }

    @Override
    public int createGame(Game newGame) {
        //first get data out of model and serialize the game
        //second create gameId
        //third insert into game_data the game id and game json string
        //fourth insert into games_list rest of data
        //return game_Id
        return 0;
    }

    @Override
    public Game getGame(int gameID) {
        //Select * from games_list WHERE gameID = gameID
        // rs.next() get all the data
        //Select game_json from game_data WHERE gameID = gameID
        // deserialize chess game string
        // create a model with all that data
        return null;
    }

    @Override
    public ArrayList<Game> getGamesList() {
        // join tables
        // get all the data for each row deserialize game_json
        // create a new Game object and add to list
        // return list
        return null;
    }

    @Override
    public void addPlayerToGame(String color, String username, Game game) {

    }
}
