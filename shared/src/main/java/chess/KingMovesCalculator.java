package chess;

import java.util.Collection;
import java.util.List;

/**
 * A class to calculate king moves at the given board state
 */
public class KingMovesCalculator implements PiecesMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return List.of();
    }
}
