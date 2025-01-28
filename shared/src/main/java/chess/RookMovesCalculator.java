package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class to calculate rook moves at the given board state
 */
public class RookMovesCalculator implements PiecesMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        String[] lineList = {"rank","file"};
        for (String lineType : lineList) {
            PiecesMovesCalculator.addLine(board, myPosition, moves, lineType);
        }
        return moves;
    }
}
