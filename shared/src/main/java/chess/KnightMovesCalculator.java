package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class to calculate knight moves at the given board state
 */
public class KnightMovesCalculator implements PiecesMovesCalculator{
    final int[] shiftList = {2,2,-2,-2,1,-1,1,-1};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int j;
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();
        boolean isBlocked = false;
        for (int i = 0; i < shiftList.length; i++)
        {
            //j will go opposite i
            j = shiftList.length - i - 1;
            int moveRow = shiftList[i] + curRow;
            int moveCol = shiftList[j] + curCol;
            if (moveRow > 8 || moveRow < 1 || moveCol > 8 || moveCol < 1)
            {
                continue;
            }
            ChessPosition movePosition = new ChessPosition(moveRow, moveCol);
            var myColor = board.getPiece(myPosition).getTeamColor();
            isBlocked = !(PiecesMovesCalculator.canTakeSquare(board, movePosition,myColor));
            if (!isBlocked)
            {
                var newMove = new ChessMove(myPosition, movePosition, null);
                moves.add(newMove);
            }

        } //not implemented yet
        return moves;
    }
}
