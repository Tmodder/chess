package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // constructor allowing for initialization copying off other object
    // is it possible to use copy method?
    public ChessPosition(ChessPosition other) {
        this.row = other.row;
        this.col = other.col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
    /**
     * @return copy of this ChessPosition
     * @param position ChessPosition  object to copy onto
     *
     */
    public ChessPosition copy(ChessPosition position)
    {
        position.row = this.row;
        position.col = this.col;
        return position;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    public void setNewPosition(int row, int col)
    {
        this.row = row;
        this.col = col;
    }


    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}
