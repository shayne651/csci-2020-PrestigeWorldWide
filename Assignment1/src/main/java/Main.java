import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.image.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.*;
import javafx.stage.DirectoryChooser;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Main extends Application {
    private Stage window;
    private Scene scene, sceneIntro;
    private BorderPane layout;
    private TableView<TestFile> table;
    private TextField emailNameField, fnameField, lnameField, spamProbabilityField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Spam Master v1.869.2754");

        /* create the menu (for the top of the user interface) */
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New", imageFile("images/new.png"));
        newMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        fileMenu.getItems().add(newMenuItem);
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(new MenuItem("Open...", imageFile("images/open.png")));
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(new MenuItem("Save", imageFile("images/save.png")));
        fileMenu.getItems().add(new MenuItem("Save As...", imageFile("images/save_as.png")));
        fileMenu.getItems().add(new SeparatorMenuItem());
        MenuItem exitMenuItem = new MenuItem("Exit", imageFile("images/exit.png"));
        fileMenu.getItems().add(exitMenuItem);
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitMenuItem.setOnAction( e -> System.exit(0) );

        Menu editMenu = new Menu("Edit");
        editMenu.getItems().add(new MenuItem("Cut", imageFile("images/cut.png")));
        editMenu.getItems().add(new MenuItem("Copy", imageFile("images/copy.png")));
        editMenu.getItems().add(new MenuItem("Paste", imageFile("images/paste.png")));

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().add(new MenuItem("About...", imageFile("images/about.png")));
        helpMenu.getItems().add(new SeparatorMenuItem());
        helpMenu.getItems().add(new MenuItem("Help...", imageFile("images/help.png")));

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(editMenu);
        menuBar.getMenus().add(helpMenu);

        //selects directory for spamfilter
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        String mainPath = mainDirectory.getAbsolutePath();

        //creates data file to read from
        Training trainy = new Training(mainPath);
        trainy.Train();

        /* create the table (for the center of the user interface) */
        table = new TableView<>();
        table.setItems(DataSource.getAllTestFiles());
        table.setEditable(true);

        TableColumn<TestFile,String> emailNameColumn = null;
        emailNameColumn = new TableColumn<>("Actual Class");
        emailNameColumn.setMinWidth(250);
        emailNameColumn.setCellValueFactory(new PropertyValueFactory<>("emailName"));
        emailNameColumn.setCellFactory(TextFieldTableCell.<TestFile>forTableColumn());
        emailNameColumn.setOnEditCommit((CellEditEvent<TestFile, String> event) -> {
            ((TestFile)event.getTableView().getItems().get(event.getTablePosition().getRow())).setEmailName(event.getNewValue());
        });

        TableColumn<TestFile,String> actualClassColumn = null;
        actualClassColumn = new TableColumn<>("Actual Class");
        actualClassColumn.setMinWidth(100);
        actualClassColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        actualClassColumn.setCellFactory(TextFieldTableCell.<TestFile>forTableColumn());
        actualClassColumn.setOnEditCommit((CellEditEvent<TestFile, String> event) -> {
            ((TestFile)event.getTableView().getItems().get(event.getTablePosition().getRow())).setActualClass(event.getNewValue());
        });

        TableColumn<TestFile,Double> spamProbabilityColumn = null;
        spamProbabilityColumn = new TableColumn<>("Spam Probability");
        spamProbabilityColumn.setMinWidth(250);
        spamProbabilityColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));

        table.getColumns().add(emailNameColumn);
        table.getColumns().add(actualClassColumn);
        table.getColumns().add(spamProbabilityColumn);

        /* create an edit form (for the bottom of the user interface) */
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        double accuracy = 0.1234;
        String sAccuracy = String.valueOf(accuracy);

        Label accLabel = new Label("Accuracy:");
        editArea.add(accLabel, 0,1);
        TextField accField = new TextField();
        accField.setPromptText(sAccuracy);
        editArea.add(accField, 1, 1);

        double precision = 0.7070;
        String sPrecision = String.valueOf(precision);


        Label preLabel = new Label("Precision");
        editArea.add(preLabel, 0, 2);
        TextField preField = new TextField();
        preField.setPromptText(sPrecision);
        editArea.add(preField, 1, 2);

        /* arrange all components in the main user interface */
        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(table);
        layout.setBottom(editArea);

        /*
        //creating intro scene and button to choose a directory and take one to the next scene
        Label label1 = new Label("Welcome to Spam Master V1.869.2754!");
        Button button1 = new Button("Choose file to filter spam");
        button1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                

                


                primaryStage.setScene(scene); //sets scene for data table
            }
        });

        //Layout 1 - arranges button "choose file to filter spam" on first scene
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, button1);
        sceneIntro = new Scene(layout1, 600, 600);
        */
        scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene); //sets scene for first window that appears
        primaryStage.show();
    }

    private ImageView imageFile(String filename) {
        return new ImageView(new Image("file:"+filename));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
