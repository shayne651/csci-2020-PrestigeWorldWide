package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.Scanner;
import java.io.*;

public class Main extends Application {
    private Stage window;
    private Scene scene;
    private BorderPane layout;
    private ListView<File> listViewLeft, listViewRight;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("File Sharing Lite");



        File serverDirectory = new File("C:\\Users\\100525235\\Desktop\\Client\\Server");
        File clientDirectory = new File("C:\\Users\\100525235\\Desktop\\Client");

        GridPane commandArea = new GridPane();
        listViewLeft = new ListView<>();
        listViewLeft.setMinWidth(300);
        listViewLeft.getItems().addAll(clientDirectory.listFiles());
        listViewLeft.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewRight = new ListView<>();
        listViewRight.setMinWidth(300);
        listViewRight.getItems().addAll(serverDirectory.listFiles());
        listViewRight.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button DButton = new Button("Download");
        DButton.setOnAction (e -> UbuttonClicked());
        commandArea.add(DButton, 1, 0);
        Button UButton = new Button("Upload");
        UButton.setOnAction (e -> DbuttonClicked());
        commandArea.add(UButton, 2, 0);



        layout = new BorderPane();
        layout.setTop(commandArea);
        layout.setLeft(listViewLeft);
        layout.setRight(listViewRight);

        scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene); //sets scene for first window that appears
        primaryStage.show();
    }


    private void UbuttonClicked(){

        String message = "";
        ObservableList<File> files;
        files = listViewRight.getSelectionModel().getSelectedItems();

        for(File m: files){
            message += m + "\n";

        }

        System.out.println(message);

    }

    private void DbuttonClicked(){

        String message = "";
        ObservableList<File> files;
        files = listViewLeft.getSelectionModel().getSelectedItems();

        for(File m: files){
            message += m + "\n";
        }

        System.out.println(message);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
