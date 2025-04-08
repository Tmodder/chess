package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private boolean gameOver = false;
    private ChessBoard board = new ChessBoard();
    private TeamColor turnColor;
    private HashSet<ChessPosition> blackPositions;
    private HashSet<ChessPosition> whitePositions;

    public ChessGame() {
       this.board.resetBoard();
       this.turnColor = TeamColor.WHITE;
    }

    private void countPositions(ChessBoard chessBoard)
    {
        this.blackPositions = new HashSet<>();
        this.whitePositions = new HashSet<>();
        this.blackPositions = chessBoard.countTeamPieces(TeamColor.BLACK);
        this.whitePositions = chessBoard.countTeamPieces(TeamColor.WHITE);
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
       Collection<ChessMove> posMoves =  board.getPiece(startPosition).pieceMoves(board,startPosition);
       Collection<ChessMove> finalMoves = new ArrayList<>();

       for (ChessMove move : posMoves)
       {
           ChessBoard testBoard = new ChessBoard(board);
           ChessPiece movePiece = board.getPiece(startPosition);
           if (move.getPromotionPiece() != null)
           {
               movePiece = new ChessPiece(movePiece.getTeamColor(), move.getPromotionPiece());
           }
           testBoard.addPiece(move.getEndPosition(),movePiece);
           testBoard.addPiece(startPosition,null);
           if (!isInCheck(movePiece.getTeamColor(),testBoard))
           {
               finalMoves.add(move);
           }
       }
       return finalMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
            ChessPiece movingPiece = board.getPiece(move.getStartPosition());
            if (movingPiece == null)
            {
                throw new InvalidMoveException("Move not valid");
            }
            if (movingPiece.getTeamColor() != getTeamTurn())
            {
                throw new InvalidMoveException("Move not valid");
            }
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
            if (!validMoves.contains(move))
            {
                throw new InvalidMoveException("Move not valid"); // will be caught in main loop
            }

            if (move.getPromotionPiece() != null)
            {
                movingPiece = new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getEndPosition(), movingPiece);
            board.addPiece(move.getStartPosition(), null);

            if(getTeamTurn() == TeamColor.WHITE)
            {
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn(TeamColor.WHITE);
            }
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard chessBoard)
    {
        countPositions(chessBoard);
        Collection<ChessPosition> oppositeTeamPieces;

        if (teamColor == TeamColor.WHITE)
        {
            oppositeTeamPieces = blackPositions;
        }
        else
        {
            oppositeTeamPieces = whitePositions;
        }
        for (ChessPosition pos : oppositeTeamPieces)
        {
            ChessPiece attackPiece = chessBoard.getPiece(pos);
            Collection<ChessMove> attackMoves = new ArrayList<>(attackPiece.pieceMoves(chessBoard, pos));
            for (ChessMove attack : attackMoves)
            {
                ChessPiece attackedPiece = chessBoard.getPiece(attack.getEndPosition());
                if (attackedPiece != null)
                {
                    if (attackedPiece.getPieceType() == ChessPiece.PieceType.KING)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
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
       return isInCheck(teamColor,this.board);
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
        countPositions(this.board);
        Collection<ChessPosition> teamSpots = teamColor == TeamColor.WHITE ? whitePositions : blackPositions;

        for (ChessPosition spot : teamSpots)
        {
            if (!validMoves(spot).isEmpty())
            {
                return false;
            }
        }
        return true;
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

    public void endGame() { gameOver = true;}

    public boolean getGameStatus() {return gameOver;}
}
