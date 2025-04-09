package ui;

import chess.*;
import communication.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

public class playGameRepl
{
    private ChessBoard board = null;
    private final ChessBoardUI boardUI = new ChessBoardUI();
    ServerFacade facade;
    String playerColor;
    int currGameNumber;
    playGameRepl(ServerFacade facade)
    {
        this.facade = facade;
    }

    public void run(int currGameNumber,String color,String playerName)
    {
        color = color.toUpperCase();
        playerColor = color;
        this.currGameNumber = currGameNumber;
        if(!facade.tryHotJoin(currGameNumber,color,playerName))
        {
            facade.playGame(currGameNumber, color);
        }

        System.out.print("playing game " + currGameNumber + " as ");
        System.out.println(color);
        //boardUI.drawBoard(Objects.equals(color, "WHITE"));
        while (true) {
            System.out.print("[IN_GAME] >>>");
            var scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String[] args = command.split(" ");
            try {
                switch (args[0]) {
                    case "help":
                        assert args.length == 1;
                        playGameHelp();
                    case "redraw":
                        assert args.length == 1;
                        redraw();
                        break;
                    case "move":
                        if (args.length != 4) {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        move(args[1],args[2],args[3]);
                        break;
                    case "leave":
                        assert args.length == 1;
                        return;
                    case "resign":
                        assert args.length == 1;
                        //resign();
                        break;
                    case "highlight":
                        if (args.length != 2) {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        //highlightMoves(args[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Command not found");
                }
            }   catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                System.out.println("Stop, get some help");
            }
        }

    }
    private void playGameHelp()
    {
        System.out.println("Options: ");
        System.out.println("redraw");
        System.out.println("     Redraw the chess board");
        System.out.println("move <POSITION_ONE> <POSITION_TWO> <PROMOTION_PIECE>");
        System.out.println("     Using chess notation, type where the piece starts from and where it ends");
        System.out.println("If applicable type what piece you want to promote to");
        System.out.println("highlight <PIECE_POSITION");
        System.out.println("     type the location of a piece to see its valid moves highlighted");
        System.out.println("leave");
        System.out.println("     Exit the game and return to login menu(without ending the game)");
        System.out.println("resign");
        System.out.println("    Forfeit the game");
    }

    private void redraw()
    {
        if (board == null) throw new RuntimeException();
        boardUI.drawBoard(Objects.equals(this.playerColor,"WHITE"),board);

    }

    private void move(String posOne, String posTwo, String promoPiece) throws IllegalArgumentException
    {
        var piece = convertStringToPiece(promoPiece);
        var pieceColor = board.getPiece(convertNotationToPos(posOne)).getTeamColor();
        if (pieceColor == ChessGame.TeamColor.WHITE && Objects.equals(playerColor, "WHITE")
                || pieceColor ==ChessGame.TeamColor.BLACK && Objects.equals(playerColor, "BLACK"))
        {
            facade.makeMove(currGameNumber, new ChessMove(convertNotationToPos(posOne),convertNotationToPos(posTwo),piece));
        }
        else
        {
            throw new IllegalArgumentException("You can't move the other player's pieces!");
        }



    }

    private ChessPiece.PieceType convertStringToPiece(String pieceString) throws IllegalArgumentException
    {
        return switch (pieceString.toLowerCase())
        {
            case "king" -> ChessPiece.PieceType.KING;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "pawn" -> ChessPiece.PieceType.PAWN;
            case "none" -> null;
            default -> throw new IllegalArgumentException("Piece not recognized");
        };
    }

    private ChessPosition convertNotationToPos(String stringPos) throws IllegalArgumentException
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
        boardUI.drawBoard(Objects.equals(this.playerColor,"WHITE"),board);
    }
}
