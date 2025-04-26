
import ui.ClientUI;


public class Main {
    public static void main(String[] args) {
        var serverName = args.length > 0 ? args[0] : "localhost:8080";
        var ui = new ClientUI(serverName);
        ui.runMenu();
    }
}