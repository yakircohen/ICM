package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	/**
	   * Loads the first window of system
	   *
	   */ 
	@Override
	public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(gui.MainController.class.getResource("/gui/MainController.fxml"));
			Pane requestsMain = loader.load();
            Scene scene = new Scene(requestsMain);
			primaryStage.setTitle("Requests Update Menu");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		launch(args);
	}

}
