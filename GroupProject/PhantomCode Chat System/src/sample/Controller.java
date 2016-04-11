package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Controller {

    @FXML
    public TextArea messages = new TextArea();

    @FXML
    private TextField input = new TextField();

    private static SocketThread client;

    public void sendMessage(){
        String message = "Client: ";
        message += input.getText();
        input.clear();

        messages.appendText(message + "\n");

        try {
            client.sendMessage(message);
        }
        catch (Exception e) {
            messages.appendText("Failed to send\n");
        }
    }
    
    public TextArea getTextArea()
    {
    	return messages;
    }
    public static void beginConnection() throws Exception {
        //client = new SocketThread(8080,"127.0.0.1", this);
        //client.start();
    }
}
