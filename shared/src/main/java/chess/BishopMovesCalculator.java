package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements the PiecesMovesCalculator interface returns
 */
public class BishopMovesCalculator implements PiecesMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        String[] lineList = {"right","left"};
        for (String lineType : lineList) {
            // a whole debate in my head if moves is really going to get updated
            PiecesMovesCalculator.addLine(board, myPosition, moves, lineType);
        }
        return moves;
    }
}
