package chess;
import java.util.Collection;

/**
 * Calculating class to return valid chess moves from given position
 */
interface PiecesMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    /**
     * Used to test if a square can
     * @param board the chess board
     * @param movePosition the position to move to
     * @param color the team color of moving piece
     * @return boolean value if movement is possible
     */
    static boolean canTakeSquare(ChessBoard board, ChessPosition movePosition, ChessGame.TeamColor color) {
        ChessPiece blockingPiece = board.getPiece(movePosition);
        if (blockingPiece != null) {

            // if they are opposite, enable capturing
            return blockingPiece.getTeamColor() != color;
        }
        return true;
    }

    //static boolean isOffBoard(board,movePosition) optional move here

    /**
     * A static method to calculate all the possible moves moving down a line(rank, file or diagonal)
     * @param board ChessBoard object which gives the current board state
     * @param myPosition ChessPosition object which gives the current position
     * @param moves ChessMoves object that we will update with new moves and return
     * @param lineType string which gives type of line (rank, file, right, or left diagonal)
     * @return Collection of ChessMove
     */
    static Collection<ChessMove> addLine(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, String lineType) {
        boolean isBlocked = false;
        boolean offBoard = false;
        int rowShift = 0;
        int colShift = 0;
        switch (lineType) {
            case "right":
                rowShift = 1;
                colShift = 1;
                break;
            case "left":
                rowShift = 1;
                colShift = -1;
                break;
            case "rank":
                colShift = 1;
                break;
            case "file":
                rowShift = 1;
                break;
        }

        for (int i = 0; i < 2; i++)
        {
            ChessPosition movePosition = new ChessPosition(myPosition);
            while (!isBlocked && !offBoard) {
                int moveRow = movePosition.getRow() + rowShift;
                int moveColumn = movePosition.getColumn() + colShift;

                // this should only throw off board if it is on the edge and would go over
                if (moveRow > 8 || moveColumn > 8 || moveRow < 1 || moveColumn < 1) {
                    offBoard = true;
                    break;
                }

                movePosition.setPosition(moveRow, moveColumn);
                isBlocked = !(canTakeSquare(board, movePosition, board.getPiece(myPosition).getTeamColor()));
                if (isBlocked) {
                    break;
                }
                    var start = new ChessPosition(myPosition);
                    var end = new ChessPosition(movePosition);
                    var move = new ChessMove(start, end, null);
                    moves.add(move);

                    //refactor this to make it nice
                    if (board.getPiece(end) != null) {
                        break;
                    }

            }

            //flip signs for both
            rowShift *= -1;
            colShift *= -1;

            //reset variables
            isBlocked = false;
            offBoard = false;

        }

        return moves;
    }
}
