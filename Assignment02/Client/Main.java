import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private TreeView<File> tree;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("File Sharing Lite");

        //initial directory for client
        File clientDirectory = new File("./..");

        //list for server directory initially empty until load Dir command
        GridPane commandArea = new GridPane();
        listViewRight = new ListView<>();
        listViewRight.setMinWidth(300);
        listViewRight.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Create top of tree
        TreeItem<File> appleTree = new TreeItem<>(clientDirectory);
        appleTree.setExpanded(true);

        //Create subfolders for client directory
        createSubs(clientDirectory, appleTree);

        //Create treeviw for client directory
        tree = new TreeView<>(appleTree);
        tree.setMinWidth(300);
        tree.setMinHeight(600);
        tree.setShowRoot(false);

        //Create buttons
        Button DButton = new Button("Download");
        DButton.setOnAction (e -> {
            try {
                DbuttonClicked();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        });
        commandArea.add(DButton, 2, 0);
        Button UButton = new Button("Upload");
        UButton.setOnAction (e -> {
            try {
                UbuttonClicked();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        commandArea.add(UButton, 1, 0);
        Button LButton = new Button("load DIR");
        LButton.setOnAction (e -> {
            try {
                LbuttonClicked();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        });
        commandArea.add(LButton, 3, 0);

        GridPane treeArea = new GridPane();
        treeArea.add(tree, 1, 0);


        //create borderpane [top is buttons][left is client treeview][right is server listview]
        layout = new BorderPane();
        layout.setTop(commandArea);
        layout.setRight(listViewRight);
        layout.setLeft(treeArea);

        //creates and shows GUI
        scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene); //sets scene for first window that appears
        primaryStage.show();
    }

    //Download button activates server client transfers
    private void DbuttonClicked() throws IOException {
        String message = "";
        ObservableList<File> files;
        //gets current selected files for transfer
        files = listViewRight.getSelectionModel().getSelectedItems();
        for(File m: files){
            message += m + "\n";
            try
            {
                server Server = new server(false,m);
                Server.start();
                client Client = new client(false,m);
                Client.start();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println(message);
    }

    //Upload button activates server client transfers
    private void UbuttonClicked() throws IOException {
        String message = "";
        ObservableList<File> files;
        //gets current selected files for transfer
        files = listViewLeft.getSelectionModel().getSelectedItems();
        for(File m: files){
            message += m + "\n";
            try
            {
                server Server = new server(true,m);
                Server.start();
                client Client = new client(true,m);
                Client.start();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println(message);
    }

    //load Dir function Button loads the available files from the server
    private void LbuttonClicked() throws IOException {
        File serverDirectory = new File("./../Server");
        listViewRight.getItems().addAll(serverDirectory.listFiles());
    }

    
    //Creates route to subdirectories
    private void createSubs(File dir, TreeItem<File> previous) throws IOException {
        TreeItem<File> root = new TreeItem<>(dir); //initializing root item
        root.setExpanded(true); //initially dropped down
        try {
            //loads files into array and iterate through
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) createSubs(file,root); //recursively goes through directory to create sub directories of branches
                else root.getChildren().add(new TreeItem<>(file)); //adds leafs to tree
            }
            if(previous==null)tree.setRoot(root); //if no previous directory set as root
            else previous.getChildren().add(root); // if previous directory get root
        } catch (IOException e) {
           e.printStackTrace();
        }
    } 

    public static void main(String[] args) {
        launch(args);
    }
}
