package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * A class to calculate king moves at the given board state
 */
public class KingMovesCalculator implements PiecesMovesCalculator{
    final int[] rowShiftList = {1, 1, 1, 0, 0, -1, -1, -1};
    final int[] colShiftList = {1, 0, -1, 1, -1, 1, 0, -1};
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int rowShift;
        int colShift;
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();
        for (int i = 0; i < 8; i++)
        {
            rowShift = rowShiftList[i];
            colShift = colShiftList[i];

            int moveRow  = rowShift + curRow;
            int moveCol = colShift + curCol;

            if (moveRow < 1 || moveRow > 8 || moveCol < 1 || moveCol > 8)
            {
                continue;
            }

            var movePosition = new ChessPosition(moveRow,moveCol);
            var myColor = board.getPiece(myPosition).getTeamColor();

            if(PiecesMovesCalculator.canTakeSquare(board,movePosition, myColor))
            {
                var newMove = new ChessMove(myPosition, movePosition, null);
                moves.add(newMove);
            }

        }

        return moves;
    }
}
