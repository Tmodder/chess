package websocket.commands;

import chess.ChessMove;

public class MoveGameCommand extends UserGameCommand
{
    private final ChessMove move;
    public MoveGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
    }
    public ChessMove getMove ()
    {
        return move;
    }
}
