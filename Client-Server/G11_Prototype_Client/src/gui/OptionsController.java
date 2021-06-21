package gui;

import java.io.IOException;

import client.ClientConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OptionsController {
	 
    /**
	   * GUI elements
	   *
	   */
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;    
    @FXML
    private TextField tbIp;
    @FXML
    private TextField tbPort;
    
    /**
	   * Variables to store data.
	   *
	   */
    private String host; 					//host name address
    private int port; 						//port address
    private boolean addressChanged = false; //if address changed from default
    private ClientConnector client;			//client connection
    private Scene mScene;					//saves the previous scene
    
    /**
	   * FXML GUI functions and actions
	   *
	   */   
    @FXML
    void onApplyClick(ActionEvent event) { 			//when event click on apply, sends update host and ip or show error
    	if(!tbIp.getText().equals(host) || !tbPort.getText().equals(String.valueOf(port))) {
    		try {
        		if (client != null && client.isConnected())
        			client.closeConnection();
        		showUpdated();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	else showNotUpdated();
    }
    
    @FXML
    void onBackClick(ActionEvent event) {			//when event click on back, show user previous window
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(getClass().getResource("/gui/MainController.fxml").openStream());			
			if (addressChanged) {
				Scene scene = new Scene(root);	
				stage.setTitle("Requests Update Menu");
				stage.setScene(scene);	
				MainController main = loader.getController();
				String ip = tbIp.getText();
				int port = Integer.parseInt(tbPort.getText());
				main.ChangeAdressOfConnection(ip, port);
			}
			else stage.setScene(mScene);
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	   * Show alert dialog
	   *	If updates success: showUpdated(), else showNotUpdated()
	   */    
    private void showUpdated() {
    	this.addressChanged = true;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Server IP and Port updated");
		alert.setContentText("The new address is: " + tbIp.getText() +":"+tbPort.getText());
		alert.show();
	}
    
    private void showNotUpdated() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Failed");
		alert.setHeaderText("No update to perform");
		alert.setContentText("Please make sure you have changed the ip or port");
		alert.show();
	}
    
    /**
	   * before loading Options scene, updating it with data
	   *
	   *@param host The hostname for server
	   *@param port The port for server
	   *@param client The client connection for server
	   */
    public void setDetails(String host, int port, ClientConnector client) {
    	this.client = client;
    	tbIp.setText(host);
        tbPort.setText(String.valueOf(port));
        this.host = host;
        this.port = port;
    }
    
    /**
	   * update the previous scene
	   *
	   *@param g The scene sent from previous window
	   */
    public void setBack(Scene g) {
    	this.mScene = g;
    }

}
