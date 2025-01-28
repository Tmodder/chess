package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class to calculate queen moves at the given board state
 */
public class QueenMovesCalculator implements PiecesMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        String[] lineList = {"right","left","rank","file"};
        for (String lineType : lineList) {
            PiecesMovesCalculator.addLine(board, myPosition, moves, lineType);
        }
        return moves;
    }
}
