package websocket.commands;

import chess.ChessMove;

public class MoveGameCommand extends UserGameCommand
{
    private final ChessMove move;
    MoveGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
    }
    ChessMove getMove ()
    {
        return move;
    }
}
