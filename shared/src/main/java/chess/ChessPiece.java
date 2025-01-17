package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        String output = "";
        switch(pieceType)
        {
            case KING -> output = "K";
            case QUEEN -> output = "Q";
            case BISHOP -> output = "B";
            case KNIGHT -> output = "N";
            case ROOK -> output = "R";
            case PAWN -> output = "P";
        }
        if (pieceColor != ChessGame.TeamColor.WHITE && !output.equals(" "))
        {
            output.toLowerCase();
        }
        return output;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.pieceType) {
            case KING -> getKingMoves(board, myPosition);
            case QUEEN -> getQueenMoves(board, myPosition);
            case BISHOP -> getBishopMoves(board, myPosition);
            case KNIGHT -> getKnightMoves(board, myPosition);
            case ROOK -> getRookMoves(board, myPosition);
            case PAWN -> getPawnMoves(board, myPosition);
            default -> throw new RuntimeException("Invalid piece type");
        };
    }

    /**
     *Helper function for pieceMoves to return all the positions the king can move to
     * validly in the given board state.
     * @return Collection of valid move
     */

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     *Helper function for pieceMoves to return all the positions the queen can move to
     * validly in the given board state.
     * @return Collection of valid move
     */

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     *Helper function for pieceMoves to return all the positions the bishop can move to
     * validly in the given board state.
     * @return Collection of valid move
     */

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        boolean isBlocked = false;
        boolean offBoard = false;
        int rowShift = 1;
        int colShift = 1;
        ChessPosition movePosition = myPosition;
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            while (!isBlocked && !offBoard) {
                int moveRow = movePosition.getRow() + rowShift;
                int moveColumn = movePosition.getColumn() + colShift;

                // this should only throw off board if it is on the edge and would go over
                if (moveRow > 8 || moveColumn > 8 || moveRow < 1 || moveColumn < 1) {
                    offBoard = true;
                    break;
                }
                movePosition.setNewPosition(moveRow, moveColumn);
                //TODO make boundary checking smart so this doesnt find something out of bounds
                ChessPiece blockingPiece = board.getPiece(movePosition);
                if (blockingPiece != null) {
                    isBlocked = true;
                    if (blockingPiece.getTeamColor() != this.getTeamColor()) {
                        ChessMove captureMove = new ChessMove(myPosition, movePosition, null);
                        moves.add(captureMove);
                        break;
                    }
                    break;
                }
                ChessMove move = new ChessMove(myPosition, movePosition, null);
                moves.add(move);
            }
            // rotate to the diagonal across y axis if same sign
            if (rowShift > 0 && colShift > 0 || rowShift < 0 && colShift < 0) {
                colShift *= -1;
            }
            // else rotate diagonal across the x axis
            else {
                rowShift *= -1;
            }
            movePosition = myPosition;
        }
        return moves;
    }


    /**
     *Helper function for pieceMoves to return all the positions the knight can move to
     * validly in the given board state.
     * @return Collection of valid move
     */

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     *Helper function for pieceMoves to return all the positions the rook can move to
     * validly in the given board state.
     * @return Collection of valid move
     */

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     *Helper function for pieceMoves to return all the positions the king can move to
     * validly in the given board state.
     * @return Collection of valid move
     */

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

}






