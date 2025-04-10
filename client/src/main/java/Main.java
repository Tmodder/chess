import chess.*;
import communication.WebSocketCommunicator;
import ui.ClientUI;
import websocket.commands.UserGameCommand;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var ui = new ClientUI();
        ui.runMenu();
    }
}