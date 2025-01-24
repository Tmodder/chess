package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * A class to calculate pawn moves at the given board state
 */
public class PawnMovesCalculator implements PiecesMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        Collection<ChessPosition> positions = new ArrayList<>();
        if (myColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7 || myColor == ChessGame.TeamColor.WHITE &&
        myPosition.getRow() == 2)
        {
            positions = advance(board, myPosition, true, myColor);
        }
        else {
            positions = advance(board, myPosition, false, myColor);
        }
        Collection<ChessPosition> capturePositions = capture(board, myPosition, myColor);
        positions.addAll(capturePositions);

        if (!positions.isEmpty())
        {
            //add to ChessPiece?
            // refactor the check for promotion
            EnumSet<ChessPiece.PieceType> types = EnumSet.range(ChessPiece.PieceType.QUEEN,ChessPiece.PieceType.ROOK);
            for (ChessPosition movePosition : positions)
            {
                // if on either edge of the board note that white cant be 1 and black cant be on 8
                if (movePosition.getRow() == 8 || movePosition.getRow() == 1) {
                    for (ChessPiece.PieceType type : types) {
                        var move = new ChessMove(myPosition, movePosition, type);
                        moves.add(move);
                    }
                }

                else
                {
                    var move = new ChessMove(myPosition, movePosition, null);
                    moves.add(move);
                }

            }
        }
        return moves;
    }

    public Collection<ChessPosition> capture(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor)
    {
        Collection<ChessPosition> positions = new ArrayList<>();
        int rowShift = myColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int moveRow = myPosition.getRow() + rowShift;
        int curCol = myPosition.getColumn();
        int leftMove = curCol - 1;
        int rightMove = curCol + 1;
        if(leftMove >= 1)
        {
            ChessPosition captureLeft = new ChessPosition(moveRow,leftMove);
            positions.add(captureLeft);
        }

        if (rightMove <= 8)
        {
            ChessPosition captureRight = new ChessPosition(moveRow,rightMove);
            positions.add(captureRight);
        }

        Collection<ChessPosition> finalPositions = new ArrayList<>();
        for (ChessPosition position : positions)
        {
            ChessPiece blocker = board.getPiece(position);
            if(blocker == null)
            {
                continue;
            }
            else if (blocker.getTeamColor() != myColor)
            {
                finalPositions.add(position);
            }

        }
        return finalPositions;



    }

    public Collection<ChessPosition> advance(ChessBoard board, ChessPosition myPosition, boolean isDouble,
                                             ChessGame.TeamColor myColor)
    {
        Collection<ChessPosition> positions = new ArrayList<>();
        int rowShift = myColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int curRow = myPosition.getRow();
        int repeatCount = 1;
        if(isDouble)
        {
            repeatCount++;
        }

        for (int i = 0; i < repeatCount; i++) {
            int moveRow = curRow + rowShift;
            ChessPosition movePosition = new ChessPosition(moveRow, myPosition.getColumn());
            if (board.getPiece(movePosition) == null)
            {
                positions.add(movePosition);
                rowShift += (myColor == ChessGame.TeamColor.WHITE ? 1 : -1);
            }
            else
            {
                return positions;
            }

        }

        return positions;
    }
}
