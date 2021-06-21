package controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EnterController implements Initializable{
	@FXML private Pane apInside;
	@FXML private BorderPane bpEnter;

	
	public void connectionPaneView(ActionEvent event) {
		sceneManager("ConnectionPane", "ICM -> Connection");
    }
	@FXML 
	public void loginPaneView(ActionEvent event) {
		sceneManager("LoginPane", "ICM -> Login");
       
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try 
        {
            Pane newLoadedPane;  
            newLoadedPane =  FXMLLoader.load(getClass().getResource("/boundary/guifiles/LoginPane.fxml"));
            apInside.getChildren().clear();
            apInside.getChildren().add(newLoadedPane);
        }
        catch (IOException ex) {
            Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	private void sceneManager(String fxmlName, String windowTitle) {
		Stage stage = (Stage)apInside.getScene().getWindow();
		stage.setTitle(windowTitle);
		try 
        {
			
            Pane newLoadedPane;  
            newLoadedPane =  FXMLLoader.load(getClass().getResource("/boundary/guifiles/"+fxmlName+".fxml"));
            apInside.getChildren().clear();
            apInside.getChildren().add(newLoadedPane);
        }
        catch (IOException ex) {
            Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
}
