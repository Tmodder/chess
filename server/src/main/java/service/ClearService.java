package service;
import dataaccess.*;
public class ClearService {
    public static void clear()
    {
        AuthDAO.MemoryAuthDAO.clear();
        MemoryUserDAO.clear();
    }

}
