package ui;

import chess.*;
import communication.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

public class PlayGameRepl extends ObserveGameRepl
{
    protected String playerColor;

    PlayGameRepl(ServerFacade facade)
    {
        super(facade);
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
                        if (args.length < 3 || args.length > 5) {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        if (args.length == 3)
                        {
                            move(args[1],args[2]);
                        }
                        else {
                            move(args[1],args[2],args[3]);
                        }
                        break;
                    case "leave":
                        assert args.length == 1;
                        leave();
                        return;
                    case "resign":
                        assert args.length == 1;
                        resign();
                        break;
                    case "highlight":
                        if (args.length != 2) {
                            throw new IllegalArgumentException("Command used incorrectly(illegal number of arguments)");
                        }
                        highlightMoves(args[1]);
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
        System.out.println("highlight <PIECE_POSITION>");
        System.out.println("     type the location of a piece to see its valid moves highlighted");
        System.out.println("leave");
        System.out.println("     Exit the game and return to login menu(without ending the game)");
        System.out.println("resign");
        System.out.println("    Forfeit the game");
    }

    private void move(String posOne, String posTwo)
    {
        move(posOne, posTwo, "none");
    }

    private void move(String posOne, String posTwo, String promoPiece) throws IllegalArgumentException
    {
        var promotion = convertStringToPiece(promoPiece);
        var piece = board.getPiece(convertNotationToPos(posOne));

        if (piece == null)
        {
            throw new IllegalArgumentException("No piece selected!");
        }
        var pieceColor = piece.getTeamColor();
        if (pieceColor == ChessGame.TeamColor.WHITE && Objects.equals(playerColor, "WHITE")
                || pieceColor ==ChessGame.TeamColor.BLACK && Objects.equals(playerColor, "BLACK"))
        {
            facade.makeMove(currGameNumber, new ChessMove(convertNotationToPos(posOne),convertNotationToPos(posTwo),promotion));
        }
        else
        {
            throw new IllegalArgumentException("You can't move the other player's pieces!");
        }
    }
    @Override
    protected void redraw()
    {
        if (board == null)
        {
            throw new RuntimeException();
        }
        boardUI.drawBoard(Objects.equals("WHITE",playerColor),board);

    }

    private void resign()
    {
        System.out.println("Are you sure you want to resign? Type 'y' for yes or 'n' for no.");
        var scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (Objects.equals(command,"y"))
        {
            facade.resign(currGameNumber);
        }
        return;

    }

    private ChessPiece.PieceType convertStringToPiece(String pieceString) throws IllegalArgumentException
    {
        return switch (pieceString.toLowerCase())
        {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "pawn" -> ChessPiece.PieceType.PAWN;
            case "none" -> null;
            default -> throw new IllegalArgumentException("Piece not recognized");
        };
    }

    @Override
    protected void highlightMoves(String stringPos)
    {
        var position = convertNotationToPos(stringPos);
        boardUI.drawBoardWithHighlights(Objects.equals(playerColor,"WHITE"),board,position);
    }

    @Override
    public void receiveBoard(ChessBoard board)
    {
        this.board = board;
        boardUI.drawBoard(Objects.equals("WHITE",playerColor),board);
    }


}
