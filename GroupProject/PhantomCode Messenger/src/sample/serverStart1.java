package sample;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class serverStart1 extends Application {

    public static TextArea messages = new TextArea();
    private ServerSocketThread server;

    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
           String message = "Server : ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            try {
                server.sendMessage(message);
            }
            catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void init() throws Exception {
        server = new ServerSocketThread(8080);
        server.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
    	server.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

