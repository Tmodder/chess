package dataaccess;

import model.Authtoken;

public interface AuthDAO {

    void createAuth(Authtoken auth);

    Authtoken findAuth(String authString);

    void deleteAuth(String authToken) throws DataAccessException;
}
