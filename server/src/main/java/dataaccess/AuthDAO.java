package dataaccess;

import model.Authtoken;

public interface AuthDAO {

    void createAuth(Authtoken auth);

    Authtoken findAuth(String authString);

    void deleteAuth(Authtoken authToken) throws DataAccessException;

    void clear();

}
