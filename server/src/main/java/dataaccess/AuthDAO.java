package dataaccess;

import model.Authtoken;

public interface AuthDAO {

    void createAuth(Authtoken auth) throws DataAccessException;

    Authtoken findAuth(String authString) throws DataAccessException;

    void deleteAuth(Authtoken authToken) throws DataAccessException;

    void clear() throws DataAccessException;

}
