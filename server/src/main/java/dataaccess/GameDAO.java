package dataaccess;

public interface GameDAO {
    void clear();

     static GameDAO makeInstance()  {
        return MemoryGameDAO.getInstance();
    }
}
