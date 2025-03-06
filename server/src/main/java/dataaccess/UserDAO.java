package dataaccess;
import model.User;

public interface UserDAO {

    void createUser(User newUser);
    User findUser(String username);
    void clear();

}
