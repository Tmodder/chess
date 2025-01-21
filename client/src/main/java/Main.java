import chess.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        var board = new ChessBoard();
        var position = new ChessPosition(4,4);
        board.addPiece(position, piece);
        var movesList = piece.pieceMoves(board,position);
        System.out.println("♕ 240 Chess Client: " + board + movesList);
    }
}