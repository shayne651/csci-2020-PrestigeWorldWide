package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class serverStart1 extends Application {
	private Stage primaryStage;
	private BorderPane mainLayout;

    public TextArea messages = new TextArea();
    private ServerSocketThread server;

    //creates messaging area that will be placed in the center of the mainLayout's border pane
    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
           String message = "Server: "; //immutable to the user this allows us to see who is who in the chat
            message += input.getText(); //the users input is now conjoined with the previous string
            input.clear(); //clears the textfield for new input

            messages.appendText(message + "\n"); //places text onto the text area from the text field

            try {
                server.sendMessage(message); //sends message
            }
            catch (Exception e) {
                messages.appendText("Failed to send\n"); //appends to text area after an error
            }
        });
        Button upload = new Button();
        upload.setText("upload");
        upload.setOnAction(event -> {
        	try 
        	{
                FileChooser filePicker = new FileChooser();
                filePicker.setTitle("Open File");
                File file = filePicker.showOpenDialog(primaryStage);
                server s = new server(file);
                s.start();
			} 
        	catch (Exception e) 
        	{
				e.printStackTrace();
			}
         });
        Button download = new Button();
        download.setText("download");
        download.setOnAction(event -> {
            try
            {
                server s = new server("127.0.0.1"); //creates the thread
                s.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(5, messages, input, upload, download);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void init() throws Exception {
        server = new ServerSocketThread(8080, this);
        server.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //initializes the applications GUI
    	this.primaryStage = primaryStage;
    	this.primaryStage.setTitle("PhantomCode Messenger");
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("sample.fxml"));
    	mainLayout = loader.load();
    	Scene sceneA = new Scene(mainLayout);
    	primaryStage.setScene(sceneA);
    	mainLayout.setCenter(createContent());
    	primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
    	server.close(); // ends connection
    }

    public static void main(String[] args) {
        launch(args);
    }
}
