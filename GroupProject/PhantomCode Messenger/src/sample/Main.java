package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void init() throws Exception {
		Controller.beginConnection();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("PhantomCode messenger");
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
	}

	public static void main(String[] args) {
		launch(args);
	}
}
