package dataaccess;

import model.User;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
    private static final HashSet<User> database = new HashSet<>();

    @Override
    public void createUser(User newUser) {
        database.add(newUser);
    }

    @Override
    public User findUser(String username) {
        for (User i : database)
        {
            if (i.username().equals(username))
            {
                return i;
            }
        }
        return null;
    }

    public void clearUsers()
    {
        database.clear();
    }
}
