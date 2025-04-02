import chess.*;
import communication.WebSocketCommunicator;
import ui.ClientUI;
import websocket.commands.UserGameCommand;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var socket = new WebSocketCommunicator();
        socket.send(new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,"1234",1234));
        var scan = new Scanner(System.in);
        String fullName = scan.nextLine();
        //var ui = new ClientUI();
       // ui.runMenu();
    }
}