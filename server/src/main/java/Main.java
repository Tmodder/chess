import chess.*;
import dataaccess.DataAccessException;
import server.*;
import dataaccess.testDb;
public class Main {
    public static void main(String[] args)  {
        var testServer = new Server();
        testServer.run(8080);


    }
}