package dataaccess;

import model.User;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
    private static final HashSet<User> userDatabase = new HashSet<>();

    @Override
    public void createUser(String username, String password, String email) {
        var newUser = new User(username, password, email);
        userDatabase.add(newUser);
    }

    @Override
    public User findUser(String username) {
        for (User i : userDatabase)
        {
            if (i.username().equals(username))
            {
                return i;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        User u = findUser(username);
        if (u != null)
        {
            userDatabase.remove(u);
        }
    }
}
