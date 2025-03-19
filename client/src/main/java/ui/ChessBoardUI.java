package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChessBoardUI
{
    public enum SquareColor
    {
        BLACK,
        WHITE
    }

    public void drawBoard(boolean whitePerspective)
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        ChessBoard gameBoard = new ChessBoard();
        gameBoard.resetBoard();

        //Split string board representation into arrays
        String board = gameBoard.toString();
        SquareColor squareColor = SquareColor.WHITE;
        String [] columnArray = {"a","b","c","d","e","f","g","h"};
        ArrayList<String> columnList = new ArrayList<>(Arrays.asList(columnArray));
        boolean isReversed = false;
        if (!whitePerspective)
        {
            var sBuilder = new StringBuilder(board);
            sBuilder.reverse();
            board = String.valueOf(sBuilder);
            //get rid of newline character
            board = board.substring(1);
            Collections.reverse(columnList);
            isReversed = true;
        }
        String [] rows = board.split("\n");

        drawHeader(columnList, out);
        for(int i = 0; i < 8; i++)
        {
            drawSidebar(8-i,isReversed,out);
            drawRow(squareColor,rows[i],out);
            drawSidebar(8-i,isReversed,out);
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            squareColor = swapColor(squareColor);
            out.print("\n");

        }
        drawHeader(columnList, out);
        //reset
        out.print(EscapeSequences.RESET_TEXT_COLOR);
        out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
        out.print(EscapeSequences.RESET_BG_COLOR);

    }

    public void drawHeader(ArrayList<String> columnList, PrintStream out)
    {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        //three empty spaces so a starts at first column
        out.print("   ");
        for (int i = 0; i < 8; i++)
        {
            out.print(" ");
            out.print(columnList.get(i));
            out.print(" ");
        }
        //three spaces to complete the square
        out.print("   ");
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print("\n");
    }

    public void drawSidebar(int index, boolean isReversed, PrintStream out)
    {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        int rank = isReversed ? 9 - index : index;
        out.print(" ");
        out.print(rank);
        out.print(" ");
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
            out.print(EscapeSequences.SET_BG_COLOR_RED);
        }
        else if (squareColor == SquareColor.WHITE)
        {
            out.print(EscapeSequences.SET_BG_COLOR_BLUE);
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
