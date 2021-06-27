package common;

import java.net.InetAddress;
import controller.ServerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Loads the ServerController with relevant parameters
 * @author Ron
 *
 */
public class ServerMain  extends Application{
	
	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(controller.ServerController.class.getResource("/boundary/guifiles/Server.fxml"));
		Pane root = loader.load();
		ServerController controller = loader.getController();
		controller.initData(InetAddress.getLocalHost().getHostAddress().toString());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/boundary/guifiles/img/icmLogo.png"));
        primaryStage.setTitle("ICM Server Dashboard");
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);  
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
                System.exit(0);
				
			}	            	
        });
        primaryStage.show();
	}

}
