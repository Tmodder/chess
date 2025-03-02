package dataaccess;

public class MemoryGameDAO implements GameDAO{
    private static MemoryGameDAO instance;

    public static synchronized MemoryGameDAO getInstance() {
        if (instance == null)
        {
            instance =  new MemoryGameDAO();
        }
        return instance;
    }
    public void clear()
    {

    }

}
