package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChessBoardUI
{
    public enum SquareColor
    {
        BLACK,
        WHITE,
        MOVING_PIECE,
        POSSIBLE_MOVE
    }

    public void drawBoardWithHighlights(boolean whitePerspective,ChessBoard gameBoard, ChessPosition startPos)
    {
        int[][] highlightArray = new int[8][8];
        if (startPos != null)
        {
            var endPositions = getMovePositions(gameBoard, startPos);
            addPosToArray(startPos,highlightArray,2);
            if (endPositions != null)
            {
                addPositionsToHighlightArray(endPositions,highlightArray);
            }

        }
        else {
            highlightArray = null;
        }

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        out.print(EscapeSequences.SET_TEXT_BOLD);


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
            if (highlightArray != null)
            {
                highlightArray = reverseTwoDArray(highlightArray);
            }

            isReversed = true;
        }
        String [] rows = board.split("\n");
        out.println();
        drawHeader(columnList, out);
        for(int i = 0; i < 8; i++)
        {
            drawSidebar(8-i,isReversed,out);
            if (highlightArray != null)
            {
                drawRow(squareColor,rows[i],out,highlightArray[i]);
            }

            else
            {
                drawRow(squareColor,rows[i],out,null);
            }

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

    public void drawBoard(boolean whitePerspective, ChessBoard gameBoard)
    {
        drawBoardWithHighlights(whitePerspective,gameBoard,null);

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
    public void drawRow(SquareColor initColor, String row, PrintStream out,int[] highlightPositions)
    {
        SquareColor color = initColor;
        char [] rowPieces = row.toCharArray();
        int j = 0;
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
            if(highlightPositions != null)
            {
                if (highlightPositions[j] == 1)
                {
                    drawSquare(SquareColor.POSSIBLE_MOVE,currChar,out);
                }
                else if (highlightPositions[j] == 2)
                {
                    drawSquare(SquareColor.MOVING_PIECE,currChar,out);
                }
                else
                {
                    drawSquare(color,currChar,out);
                }
            }
            else
            {
                drawSquare(color,currChar,out);
            }
            color = swapColor(color);
            j += 1;
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

        else if (squareColor == SquareColor.MOVING_PIECE)
        {
            out.print(EscapeSequences.SET_BG_COLOR_MAGENTA);
        }

        else if (squareColor == SquareColor.POSSIBLE_MOVE)
        {
            out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
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



    private Set<ChessPosition> getMovePositions(ChessBoard board, ChessPosition startPos)
    {
        var movePositions = new HashSet<ChessPosition>();
        var fakeGame = new ChessGame();
        fakeGame.setBoard(board);
        var moves = fakeGame.validMoves(startPos);
        if (moves.isEmpty())
        {
            return null;
        }
        for (var move : moves)
        {
           movePositions.add(move.getEndPosition());
        }
        return movePositions;
    }

    private void addPositionsToHighlightArray(Set<ChessPosition> endPositions, int[][] boardArray )
    {
        if (endPositions.isEmpty())
        {
            return;
        }
        for (var pos : endPositions)
        {
            addPosToArray(pos,boardArray,1);
        }
        return;
    }

    private void addPosToArray(ChessPosition position, int[][] boardArray, int val)
    {
        int row = 8 - position.getRow();
        int col = position.getColumn() - 1;
        boardArray[row][col] = val;
        return;
    }
    private int[][] reverseTwoDArray(int[][] array)
    {
        int[][] newArray = new int[8][8];
        int i = 0;
        int j = 7;
        for (var row : array)
        {
            newArray[j] = reverseOneDArray(array[i]);
            i += 1;
            j -= 1;
        }
        return newArray;
    }

    private int[] reverseOneDArray(int[] array)
    {
        int[] newArray = new int[array.length];
        int i = 0;
        int j = array.length - 1;
        for (int num : array)
        {
            newArray[j] = array[i];
            i += 1;
            j -= 1;
        }
        return newArray;
    }



}
