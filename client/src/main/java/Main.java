import chess.*;
import ui.ClientUI;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        var board = new ChessBoard();
        var position = new ChessPosition(4,4);
        board.addPiece(position, piece);
        var pos2 = new ChessPosition(6,6);
        var piece2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        board.addPiece(pos2, piece2);
        var movesList = piece.pieceMoves(board,position);
        System.out.println("♕ 240 Chess Client: " + board + movesList);
        var ui = new ClientUI();
        ui.runMenu();
    }
}