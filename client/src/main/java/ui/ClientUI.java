package ui;

import communication.ServerFacade;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class ClientUI
{
    private final ChessBoardUI boardUI = new ChessBoardUI();
    private final ServerFacade facade = new ServerFacade();
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
                            throw new IllegalArgumentException("Illegal number of arguments");
                        }
                        login(args[1],args[2]);
                        return true;
                    case "register":
                        if(args.length != 4)
                        {
                            throw new IllegalArgumentException("Illegal number of arguments");
                        }
                        register(args[1],args[2],args[3]);
                        return true;
                    case "quit":
                        assert args.length == 1;
                        quit();
                        return false;
                    default:
                        throw new IllegalArgumentException("that aint no good command bruh");
                }
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Stop, get some help");
            }

        }
    }
    private void quit()
    {
        System.out.println("goodbye :)");
    }
    private void preLoginHelp()
        {
            System.out.println("Here some help bro");
        }

    private void login(String username, String password)
    {
        facade.login(username, password);
        System.out.println("Ok buddy your logged in");
    }
    private void register(String username, String password, String email)
    {
        facade.register(username, password, email);
        System.out.println("Ok bro you registered dawg");
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
                            throw new IllegalArgumentException("Illegal number of arguments");
                        }
                        createGame(args[1]);
                        break;
                    case "list":
                        assert args.length == 1;
                        listGames();
                        break;
                    case "play":
                        if(args.length != 2)
                        {
                            throw new IllegalArgumentException("Illegal number of arguments");
                        }
                        playGame(args[1]);
                        break;
                    case "observe":
                        if(args.length != 2)
                        {
                            throw new IllegalArgumentException("Illegal number of arguments");
                        }
                        observeGame(args[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("aint no good command bruh");
                }
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Stop, get some help");
            }

        }
    }

    private void postLoginHelp()
    {
        System.out.println("Have some more help");
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
    private void playGame(String gameNumber)
    {
        facade.playGame();
        System.out.println("playing game");
        boardUI.drawBoard(true);
    }
    private void observeGame(String gameNumber)
    {
        System.out.println("observing game");
        boardUI.drawBoard(false);
    }



}
