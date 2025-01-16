import chess.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        var board = new ChessBoard();
        //TODO Fix position and indexing so 0 0 doesnt crash
        var position = new ChessPosition(1,1);
        board.addPiece(position, piece);
        var movesList = piece.pieceMoves(board,position);
        System.out.println("♕ 240 Chess Client: " + movesList);
    }
}