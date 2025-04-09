package websocket.commands;

import chess.ChessMove;

public class MoveGameCommand extends UserGameCommand
{
    private final ChessMove move;
    public MoveGameCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }
    public ChessMove getMove ()
    {
        return move;
    }
}
