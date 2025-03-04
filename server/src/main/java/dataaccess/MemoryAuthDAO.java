package dataaccess;

import model.Authtoken;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    private final HashSet<Authtoken> database = new HashSet<>();
    private static MemoryAuthDAO instance;

    private MemoryAuthDAO() {}
    @Override
    public void createAuth(Authtoken auth) {
        database.add(auth);

    }

    @Override
    public Authtoken findAuth(String authString) {
        for (Authtoken t : database) {
            if (t.authToken().equals(authString))
            {
                return t;
            }

        }
        return null;
    }

    @Override
    public void deleteAuth(Authtoken authToken) throws DataAccessException {
        database.remove(authToken);
    }

    @Override
    public void clear()
    {
        database.clear();
    }

    public static synchronized MemoryAuthDAO getInstance() {
        if (instance == null)
        {
            instance =  new MemoryAuthDAO();
        }
        return instance;
    }
}
