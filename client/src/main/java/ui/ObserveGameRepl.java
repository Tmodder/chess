package ui;

import chess.ChessBoard;
import chess.ChessPosition;
import communication.ServerFacade;

import java.util.Scanner;

public class ObserveGameRepl
{
    protected final ChessBoardUI boardUI = new ChessBoardUI();
    protected ServerFacade facade;
    protected ChessBoard board = null;
    protected int currGameNumber;

    public void run(int currGameNumber,String playerName)
    {
        this.currGameNumber = currGameNumber;
        facade.observeGame(currGameNumber);
        System.out.println("observing game " + currGameNumber);
        while (true) {
            System.out.print("[OBSERVE_GAME] >>>");
            var scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String[] args = command.split(" ");
            try {
                switch (args[0]) {
                    case "help":
                        assert args.length == 1;
                        observeGameHelp();
                    case "redraw":
                        assert args.length == 1;
                        redraw();
                        break;
                    case "leave":
                        assert args.length == 1;
                        leave();
                        return;
                    case "highlight":
                        if (args.length != 2) {
                            throw new IllegalArgumentException("Command used incorrectly(incorrect number of arguments)");
                        }
                        highlightMoves(args[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Command not encountered");
                }
            }   catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Stop, get some help");
            }
        }

    }

    ObserveGameRepl(ServerFacade facade)
    {
        this.facade = facade;
    }


    protected void redraw()
    {
        if (board == null)
        {
            throw new RuntimeException();
        }
        boardUI.drawBoard(true,board);

    }

    private void observeGameHelp()
    {
        System.out.println("Options: ");
        System.out.println("redraw");
        System.out.println("     Redraw the chess board");
        System.out.println("highlight <PIECE_POSITION>");
        System.out.println("     type the location of a piece to see its valid moves highlighted");
        System.out.println("leave");
        System.out.println("     Stop observing the game and return to login menu");
    }

    protected void highlightMoves(String stringPos)
    {
        var position = convertNotationToPos(stringPos);
        boardUI.drawBoardWithHighlights(true,board,position);
    }

    protected ChessPosition convertNotationToPos(String stringPos) throws IllegalArgumentException
    {
        var chars = stringPos.toCharArray();
        int row;
        int col;
        if (Character.isAlphabetic(chars[0]) && (chars[0] <= 'h' && chars[0] >= 'a'))
        {
            col = Character.getNumericValue(chars[0]) - 9;
        }
        else
        {
            throw new IllegalArgumentException("Chess notation not recognized!");
        }
        int digit = Character.getNumericValue(chars[1]);
        if (Character.isDigit(chars[1]) && (digit <= 8 && digit >= 1))
        {
            row = digit;
        }
        else
        {
            throw new IllegalArgumentException("Chess notation not recognized!");
        }
        return new ChessPosition(row,col);
    }

    public void receiveBoard(ChessBoard board)
    {
        this.board = board;
        boardUI.drawBoard(true,board);
    }

    protected void leave()
    {
        facade.leave(currGameNumber);
    }
}
