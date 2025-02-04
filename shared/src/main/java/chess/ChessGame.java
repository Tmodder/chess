package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor turnColor;
    private ArrayList<ChessPosition> blackPositions;
    private ArrayList<ChessPosition> whitePositions;

    public ChessGame() {
        this.board.resetBoard();
        this.turnColor = TeamColor.WHITE;
        initializePositions();
    }

    private void initializePositions()
    {
        this.blackPositions = new ArrayList<>();
        this.whitePositions = new ArrayList<>();
        for (int row = 1; row < 3; row++)
        {
            for (int col = 1; col < 8; col++)
            {
                ChessPosition posWhite = new ChessPosition(row,col);
                ChessPosition posBlack = new ChessPosition(row + 6,col);
                this.whitePositions.add(posWhite);
                this.blackPositions.add(posBlack);
            }
        }
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // does this work
        return board.getPiece(startPosition).pieceMoves(board,startPosition);
        // TODO will need to check for check
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move))
        {
            throw new InvalidMoveException("Move not valid"); // will be caught in main loop
        }
        if(getTeamTurn() == TeamColor.WHITE)
        {
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }
        //TODO actually make move


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // for piece in opposite team pieces check possible moves
        // if there is a possible move that can take the kings square(move.end == king.pos)
        //return false
        //TODO Add real implementation
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noPossibleMove(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
       return !isInCheck(teamColor) && noPossibleMove(teamColor);
    }

    public boolean noPossibleMove(TeamColor teamColor)
    {
        return false;
        //TODO add real implementation
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
