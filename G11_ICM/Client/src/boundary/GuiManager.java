package boundary;

import java.io.IOException;

import common.Main;
import controller.MenuController;
import controller.RequestViewController;
import entity.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class GuiManager {
	public static void guiLoader(String fxmlName, Stage stage) throws IOException {
		String path = "/boundary/guifiles/" + fxmlName + ".fxml";
		FXMLLoader loader = new FXMLLoader();	
		try {  
				loader.setLocation(Main.class.getResource(path));
				Pane root = loader.load();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.getIcons().add(new Image("/boundary/guifiles/img/icmLogo.png"));
	            stage.setTitle("ICM -> ");
	            stage.setResizable(false);
	            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent e) {
						Platform.exit();
		                System.exit(0);
						
					}	            	
	            });
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void guiLoader(String fxmlName, User user) throws IOException {
		String path = "/boundary/guifiles/" + fxmlName + ".fxml";
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		try {  
				loader.setLocation(controller.MenuController.class.getResource(path));
				Pane root = loader.load();
				MenuController controller = loader.getController();
				controller.initData(user, stage);
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.getIcons().add(new Image("/boundary/guifiles/img/icmLogo.png"));
	            stage.setTitle("ICM -> Home");
	            stage.setResizable(false);  
	            stage.setX(bounds.getMinX());
	    		stage.setY(bounds.getMinY());
	    		stage.setWidth(bounds.getWidth());
	    		stage.setHeight(bounds.getHeight());
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	public static void popUpLoader(String fxmlName, String requestId) throws IOException {
		String path = "/boundary/guifiles/" + fxmlName + "Popup.fxml";		
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		try {  
				RequestViewController.initData(requestId);
				loader.setLocation(RequestViewController.class.getResource(path));
				Pane root = loader.load();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	 public static void showError(Label lblError, Pane pError, String message) {
		 pError.setStyle("-fx-background-color: #FF5C5C;" + "-fx-background-radius: 20");
		 lblError.layoutXProperty().bind(pError.widthProperty().subtract(lblError.widthProperty()).divide(2));
	     lblError.setText(message);
		 pError.setVisible(true);
		 PauseTransition pause = new PauseTransition(Duration.seconds(5));
		 pause.setOnFinished(e -> pError.setVisible(false));
		 pause.play();
	}
	 
	public static void showSuccess(Label lblSuccess, Pane pSuccess, String message) {
		 pSuccess.setStyle("-fx-background-color: #01BF27;" + "-fx-background-radius: 20");
		 lblSuccess.layoutXProperty().bind(pSuccess.widthProperty().subtract(lblSuccess.widthProperty()).divide(2));
		 lblSuccess.setText(message);
		 pSuccess.setVisible(true);
		 PauseTransition pause = new PauseTransition(Duration.seconds(5));
		 pause.setOnFinished(e -> pSuccess.setVisible(false));
		 pause.play();
	}
}
