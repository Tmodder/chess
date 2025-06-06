package ui;

import communication.ResponseException;
import communication.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import java.util.Scanner;

public class ClientUI implements ServerMessageObserver
{
    private final ChessBoardUI boardUI = new ChessBoardUI();
    private final ServerFacade facade;
    private PlayGameRepl gameLoop = null;
    private ObserveGameRepl observeLoop = null;
    private String playerColor;
    private String playerName;
    private Integer currGameNumber;

    public ClientUI(String serverName) {
        facade = new ServerFacade(serverName, this);
    }

    public void runMenu()
    {
        System.out.println("♕ Lets play some freakin chess.");
        System.out.println("Need some help? Type help and press enter");
        boolean keepPlaying = true;
        boolean loggedIn = false;
        while(keepPlaying)
        {
            loggedIn = runPreLogin();
            if (loggedIn)
            {
                runPostLogin();
                loggedIn = false;
            }
            else
            {
                keepPlaying = false;
            }
        }
    }

    private boolean runPreLogin() {

        while (true) {
            var scanner = new Scanner(System.in);
            System.out.print("[LOGGED_OUT] >>>");
            String command = scanner.nextLine();
            String[] args = command.split(" ");
            try
            {
                switch (args[0]) {
                    case "help":
                        assert args.length == 1;
                        preLoginHelp();
                        break;
                    case "login":
                        if(args.length != 3)
                        {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        login(args[1],args[2]);
                        return true;
                    case "register":
                        if(args.length != 4)
                        {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        register(args[1],args[2],args[3]);
                        return true;
                    case "quit":
                        assert args.length == 1;
                        quit();
                        return false;
                    default:
                        throw new IllegalArgumentException("Command not found");
                }
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Stop, get some help");
            }
            catch(ResponseException e)
            {
                if(e.getStatus() == 401)
                {
                    System.out.println("Login failed. Password or username does not match");
                }
                else if(e.getStatus() == 403)
                {
                    System.out.println("Username already taken. Please try a different one");
                }
                else
                {
                    System.out.println("We are having technical difficulties, please try again later!");
                }
            }
        }
    }
    private void quit()
    {
        System.out.println("goodbye :)");
    }
    private void preLoginHelp()
        {
            System.out.println("Options: ");
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> ");
            System.out.println("     Create your account");
            System.out.println("login <USERNAME> <PASSWORD>");
            System.out.println("     Login into your account");
            System.out.println("quit");
            System.out.println("     End your session");
            System.out.println("help");
            System.out.println("    Print out this menu");
        }

    private void login(String username, String password)
    {
        facade.login(username, password);
        System.out.println("Ok buddy your logged in");
        playerName = username;
    }
    private void register(String username, String password, String email)
    {
        facade.register(username, password, email);
        System.out.println("Ok bro you registered");
        playerName = username;
    }
    private void runPostLogin()
    {
        while (true) {
            System.out.print("[LOGGED_IN] >>>");
            var scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String[] args = command.split(" ");
            try
            {
                switch (args[0]) {
                    case "help":
                        assert args.length == 1;
                        postLoginHelp();
                        break;
                    case "logout":
                        assert args.length == 1;
                        logout();
                        return;
                    case "create":
                        if(args.length != 2)
                        {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        createGame(args[1]);
                        break;
                    case "list":
                        assert args.length == 1;
                        listGames();
                        break;
                    case "play":
                        if(args.length != 3)
                        {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        playGame(args[1],args[2]);
                        break;
                    case "observe":
                        if(args.length != 2)
                        {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        observeGame(args[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Command not found");
                }
            } catch (NumberFormatException e) {
                System.out.println("Number not recognized, use digits for game index not words(\"1\" not \"one\")");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Stop, get some help");
            }
            catch (IndexOutOfBoundsException e)
            {
                System.out.println("That game does not exist. Try a different one");
            }
            catch(ResponseException e)
            {
                if(e.getStatus() == 403)
                {
                System.out.println("Color already taken, try a different color or game");
                }
                else if(e.getStatus() == 400)
                {
                    System.out.println("That color does not exist, choose black or white");
                }
                else
                {
                    System.out.println("We are having technical difficulties, please try again later!");
                }

            }

        }
    }

    private void postLoginHelp()
    {
        System.out.println("Options: ");
        System.out.println("create <NAME>");
        System.out.println("     Create a game with the given name");
        System.out.println("list");
        System.out.println("     Print out the list of games");
        System.out.println("play <ID> [WHITE|BLACK]");
        System.out.println("     Join a game with the given ID as white or black");
        System.out.println("observe <ID>");
        System.out.println("     Observe a game with the given ID");
        System.out.println("help");
        System.out.println("    Print out this menu");
        System.out.println("logout");
        System.out.println("    Log out of your account");
    }
    private void playGame(String id, String color)
    {
        gameLoop = new PlayGameRepl(facade);
        gameLoop.run(Integer.parseInt(id),color,playerName);
    }
    private void logout()
    {
        facade.logout();
        System.out.println("logging out!");
    }
    private void createGame(String gameName)
    {
        facade.createGame(gameName);
        System.out.println("Game created :)");
    }
    private void listGames()
    {
        String out = facade.listGames();
        System.out.print(out);
    }

    private void observeGame(String gameNumber)
    {
        observeLoop = new ObserveGameRepl(facade);
        observeLoop.run(Integer.parseInt(gameNumber),playerName);
    }

    @Override
    public void notify(ServerMessage notificationMessage) {
        switch (notificationMessage.getServerMessageType())
        {
            case LOAD_GAME:
                var loadMsg = (LoadGameMessage) notificationMessage;
                if (gameLoop != null)
                {
                    gameLoop.receiveBoard(loadMsg.getGame().getBoard());
                }
                else if (observeLoop != null)
                {
                    observeLoop.receiveBoard(loadMsg.getGame().getBoard());
                }
                break;
            case NOTIFICATION:
                var notifyMsg = (NotificationMessage) notificationMessage;
                System.out.println(notifyMsg.getMessage());
                break;
            case ERROR:
                var errMsg = (ErrorMessage) notificationMessage;
                System.out.println(errMsg.getMessage());
                break;
        }

        System.out.println();

    }
}
