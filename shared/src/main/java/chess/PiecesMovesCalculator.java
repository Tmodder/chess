package chess;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Calculating class to return valid chess moves from given position
 */
interface PiecesMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

}
