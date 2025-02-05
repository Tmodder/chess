package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];
    final private String startBoard = """
            [r] [n] [b] [q] [k] [b] [n] [r]
            [p] [p] [p] [p] [p] [p] [p] [p]
            [~] [~] [~] [~] [~] [~] [~] [~]
            [~] [~] [~] [~] [~] [~] [~] [~]
            [~] [~] [~] [~] [~] [~] [~] [~]
            [~] [~] [~] [~] [~] [~] [~] [~]
            [P] [P] [P] [P] [P] [P] [P] [P]
            [R] [N] [B] [Q] [K] [B] [N] [R]
            """;
    public ChessBoard() {
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        String output = "ChessBoard{ board = \n";
        ChessPiece piece = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                piece = board[i][j];
                output += "[";
                if (piece != null) {
                    output += piece.toString();
                }
                else
                {
                    output += "~";
                }
                output += "] ";
            }
            output += "\n";
        }
        return output;
    }

    //inspired by test utilities
    private ChessPiece[][] loadBoard(String boardText)
    {
        var newBoard = new ChessPiece[8][8];
        int row = 0;
        int col = 0;
        for (char c : boardText.toCharArray())
        {
            switch(c)
            {
                case '\n':
                    col = 0;
                    row++;
                    break;

                case '[':
                case ']':
                case '~':
                    break;

                case ' ':
                    col++;
                    break;
                default:
                    ChessGame.TeamColor color = Character.isLowerCase(c) ? ChessGame.TeamColor.BLACK :
                    ChessGame.TeamColor.WHITE;
                    var newPiece = convertCharToType(c, color);
                    newBoard[row][col] = newPiece;
                    }
            }
        return newBoard;
        }

    private static ChessPiece convertCharToType(char c, ChessGame.TeamColor color) {
        c = Character.toLowerCase(c);
        ChessPiece.PieceType type = switch (c) {
            case 'b'-> ChessPiece.PieceType.BISHOP;
            case 'q'-> ChessPiece.PieceType.QUEEN;
            case 'k'-> ChessPiece.PieceType.KING;
            case 'p'-> ChessPiece.PieceType.PAWN;
            case 'r'-> ChessPiece.PieceType.ROOK;
            case 'n'-> ChessPiece.PieceType.KNIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
        var newPiece = new ChessPiece(color,type);
        return newPiece;
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // convert from 1 based index system starting bottom left to 0 top-left start system
        int row = 8 - position.getRow();
        int col = position.getColumn() - 1;
        this.board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //convert from 0 top left start to 1 bottom left start
        int row = 8 - position.getRow();
        int col = position.getColumn() - 1;
        return board[row][col];
    }

    public HashSet<ChessPosition> countTeamPieces(ChessGame.TeamColor teamColor)
    {
        HashSet<ChessPosition> teamPositions = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null) {
                    if(board[i][j].getTeamColor() == teamColor)
                    {
                        ChessPosition pos = new ChessPosition(8-i, j + 1);
                        teamPositions.add(pos);
                    }
                }
            }
        }
        return teamPositions;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null) {
                    board[i][j] = null;
                }
            }
        }
        this.board = this.loadBoard(startBoard);

    }
}
