package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private TextArea messages = new TextArea();

    @FXML
    private TextField input = new TextField();

    private static SocketThread client;

    public void sendMessage(){
        String message = "Client: ";
        message += input.getText();
        input.clear();

        messages.appendText(message + "\n");

        try {
            client.sendMessage(client.getMessage());
        }
        catch (Exception e) {
            messages.appendText("Failed to send\n");
        }
    }

    public static void beginConnection() throws Exception {
        client = new SocketThread(8080,"127.0.0.1");
        client.start();
    }

/*
    private static Server createServer(){
        return new Server(55555, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    private static Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }
*/
}
