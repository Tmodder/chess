package ui;

public class ClientUI
{
    public void runMenu()
    {
        boolean keepPlaying = true;
        boolean loggedIn = false;
        while(keepPlaying)
        {
            loggedIn = runPreLogin();
            if (loggedIn)
            {
                runPostLogin();
            }
            else
            {
                keepPlaying = false;
            }
        }
    }

    private boolean runPreLogin()
    {
        return true;
    }
    private void quit()
    {

    }
    //private void preLoginHelp();
    //private void login(String username, String password);
    //private void register(String username, String password, String email);
    private void runPostLogin()
    {}

    //private void postLoginHelp();
    //private void logout();
    //private void createGame(String gameName);
    //private void listGames();
    //private void playGame(int gameNumber);
    //private void observeGame(int gameNumber);



}
