package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardUI
{
    public enum SquareColor
    {
        BLACK,
        WHITE
    }

    public static void main(String[] args)
    {
       var board = new ChessBoardUI();
        board.drawBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        out.print("\n");
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        out.print("\n");
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        out.print("\n");
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        out.print("\n");
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        out.print("\n");
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        board.drawSquare(SquareColor.BLACK,'b',out);
        board.drawSquare(SquareColor.WHITE,'Q',out);
        out.print("\n");
    }
    public void drawBoard()
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);

        //Black or white parameter will determine order of rank and file(normal or reverse) as well as which to start with
        //print header
        //while(loop 8 times)
        //increment row count
        //print row(row count,startwithblack or white)
        //print header

    }
    public void drawRow()
    {

    }
    public void drawSquare(SquareColor squareColor,Character piece,PrintStream out)
    {
        if (squareColor == SquareColor.BLACK)
        {
            out.print(EscapeSequences.SET_BG_COLOR_BLUE);
        }
        else if (squareColor == SquareColor.WHITE)
        {
            out.print(EscapeSequences.SET_BG_COLOR_RED);
        }

        else {
            throw new RuntimeException("Bad color type");
        }
        if (Character.isLowerCase(piece))
        {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        }
        else
        {
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        }
        out.print(" ");
        out.print(Character.toUpperCase(piece));
        out.print(" ");
        //get the current
    }
    //Convert this to emoji converter later
//    private static String convertCharToType(char c, ChessGame.TeamColor color) {
//        c = Character.toLowerCase(c);
//        ChessPiece.PieceType type = switch (c) {
//            case 'b'-> ChessPiece.PieceType.BISHOP;
//            case 'q'-> ChessPiece.PieceType.QUEEN;
//            case 'k'-> ChessPiece.PieceType.KING;
//            case 'p'-> ChessPiece.PieceType.PAWN;
//            case 'r'-> ChessPiece.PieceType.ROOK;
//            case 'n'-> ChessPiece.PieceType.KNIGHT;
//            default -> throw new IllegalStateException("Unexpected value: " + c);
//        };

}
