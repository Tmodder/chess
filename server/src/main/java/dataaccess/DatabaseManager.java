package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void createTables() throws DataAccessException {
            ArrayList<String> statementsList = new ArrayList<>();
            var statementDb = "USE " + DATABASE_NAME;
            var statementUser = """
                    CREATE TABLE IF NOT EXISTS user_data (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY(username)
                    )
                    """;
            var statementAuth = """
                    CREATE TABLE IF NOT EXISTS authentication_data (
                     username VARCHAR(255) NOT NULL,
                     auth_token VARCHAR(255) NOT NULL
                    )
                    """;
            var statementGamesList = """
                    CREATE TABLE IF NOT EXISTS games_list (
                    game_id INT NOT NULL AUTO_INCREMENT,
                    game_name VARCHAR(255) NOT NULL,
                    white_username VARCHAR(255),
                    black_username VARCHAR(255),
                    PRIMARY KEY(game_id)
                    )
                    """;
            var statementGameData = """
                    CREATE TABLE IF NOT EXISTS game_data (
                    game_id INT NOT NULL,
                    game_json VARCHAR(5000) NOT NULL,
                    PRIMARY KEY(game_id)
                    )""";
            statementsList.add(statementDb);
            statementsList.add(statementUser);
            statementsList.add(statementAuth);
            statementsList.add(statementGamesList);
            statementsList.add(statementGameData);
            try(var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD))
            {
                for (int i = 0; i < statementsList.size(); i++)
                {

                    try (var stmt = conn.createStatement()){
                        stmt.executeUpdate(statementsList.get(i));
                    }

                }
            }
            catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }


    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
