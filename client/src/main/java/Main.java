import chess.*;
import communication.WebSocketCommunicator;
import ui.ClientUI;
import websocket.commands.UserGameCommand;

public class Main {
    public static void main(String[] args) {
        var socket = new WebSocketCommunicator();

        //var ui = new ClientUI();
       // ui.runMenu();
    }
}