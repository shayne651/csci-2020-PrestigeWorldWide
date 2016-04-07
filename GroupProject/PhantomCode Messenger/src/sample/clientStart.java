package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class clientStart extends Application {
	private Stage primaryStage;
	private BorderPane mainLayout;

    public static TextArea messages = new TextArea();
    private SocketThread client;

    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
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
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }
    
    public void showMainView() throws IOException{
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("sample.fxml"));
    	mainLayout = loader.load();
    	Scene scene = new Scene(mainLayout);
    	primaryStage.setScene(scene);
    	mainLayout.setCenter(createContent());
    	primaryStage.show();
    }
    
    public void chatWindow() throws IOException{
    	mainLayout.setCenter(createContent());
    }

    @Override
    public void init() throws Exception {
        client = new SocketThread(8080,"127.0.0.1");
        client.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("PhantomCode Messenger");
    	showMainView();
//        primaryStage.setScene(new Scene(createContent()));
//        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
    	client.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

