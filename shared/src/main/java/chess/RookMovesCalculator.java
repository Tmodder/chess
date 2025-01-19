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
        boolean isBlocked = false;
        boolean offBoard = false;
        int rowShift = 1;
        int colShift = 0;
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            ChessPosition movePosition = new ChessPosition(myPosition);
            while (!isBlocked && !offBoard) {
                int moveRow = movePosition.getRow() + rowShift;
                int moveColumn = movePosition.getColumn() + colShift;

                // this should only throw off board if it is on the edge and would go over
                if (moveRow > 8 || moveColumn > 8 || moveRow < 1 || moveColumn < 1) {
                    offBoard = true;
                    break;
                }
                movePosition.setNewPosition(moveRow, moveColumn);
                ChessPiece blockingPiece = board.getPiece(movePosition);
                if (blockingPiece != null) {
                    isBlocked = true;
                    // if they are opposite, enable capturing
                    if (blockingPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        ChessMove captureMove = new ChessMove(myPosition, movePosition, null);
                        moves.add(captureMove);
                        break;
                    }
                    break;
                }
                var start = new ChessPosition(myPosition);
                var end = new ChessPosition(movePosition);
                var move = new ChessMove(start, end, null);
                moves.add(move);
            }
            // change the path from forward to backward to left and right
            if (rowShift == 1) {
                rowShift = -1;
            } else if (rowShift == -1) {
                rowShift = 0;
                colShift = 1;
            } else {
                colShift = -1;
            }
            isBlocked = false;
            offBoard = false;
        }
        return moves;
    }
}
