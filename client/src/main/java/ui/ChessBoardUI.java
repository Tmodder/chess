package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardUI
{
    public enum SquareColor
    {
        BLACK,
        WHITE
    }

    public static void main(String[] args) {
        var board = new ChessBoardUI();
        board.drawBoard();
    }

    public void drawBoard()
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        ChessBoard gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        String board = gameBoard.toString();
        String [] rows = board.split("\n");
        SquareColor squareColor = SquareColor.WHITE;
        for(int i = 0; i < 8; i++)
        {
            drawRow(squareColor,rows[i],out);
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            squareColor = swapColor(squareColor);
            out.print("\n");

        }

    }
    public void drawRow(SquareColor initColor, String row, PrintStream out)
    {
        SquareColor color = initColor;
        char [] rowPieces = row.toCharArray();
        for(int i = 1; i < rowPieces.length; i++)
        {
            char currChar = rowPieces[i-1];
            if ( currChar == '[' || currChar == ']' || currChar == ' ')
            {
                continue;
            }
            if (currChar == '~')
            {
                currChar = ' ';
            }
            drawSquare(color,currChar,out);
            color = swapColor(color);
        }
    }
    public void drawSquare(SquareColor squareColor,char piece, PrintStream out)
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
    private SquareColor swapColor(SquareColor color)
    {
        if (color == SquareColor.BLACK)
        {
            color = SquareColor.WHITE;
        }
        else
        {
            color = SquareColor.BLACK;
        }
        return color;
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
