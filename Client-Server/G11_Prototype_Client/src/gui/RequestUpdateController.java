package gui;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RequestUpdateController {
	 /**
	   * GUI elements
	   *
	   */
	@FXML
    private ScrollPane spCurrent;
    @FXML
    private ScrollPane spRequest;
    @FXML
    private TextField tbStatus;
    @FXML
    private Label lblApplicant;
    @FXML
    private Label lblBoard;
    @FXML
    private Label lblHandler;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnBack;

    /**
	   * Variables to store data.
	   *
	   */
    private ClientConnector uClient;		//client connection
    private String status;					//current status of request
    private int celId;						//keeps the chosen cell id
    private Scene rScene;					//saves the previous scene
    
    /**
	   * before loading scene, updating it with data
	   *
	   *@param cellId The chosen cell id
	   *@param client The client connection for server
	   */
	public void setForm(int celId, ClientConnector client) {
		this.uClient = client;
		this.celId = celId;
		Text cText = new Text(uClient.getListj().get(celId).get(2).toString()); //current state text
		Text rText = new Text(uClient.getListj().get(celId).get(3).toString()); //request description text
		spCurrent.setContent(cText);
		spRequest.setContent(rText);
		lblApplicant.setText(uClient.getListj().get(celId).get(0).toString());
		lblBoard.setText(uClient.getListj().get(celId).get(1).toString());
		tbStatus.setText(uClient.getListj().get(celId).get(4).toString());
		lblHandler.setText(uClient.getListj().get(celId).get(5).toString());
		this.status = uClient.getListj().get(celId).get(4).toString(); //keep current status
	}
	
	/**
	   * FXML GUI functions and actions
	   *
	   */ 
	@FXML
	private void onUpdateClick(ActionEvent event) {
		if(this.status.equals(tbStatus.getText())) {
			showNotUpdated();
		}
		else {
			ArrayList<String> data = uClient.getListj().get(celId); //get selected cell arraylist
			data.set(4, tbStatus.getText()); //update status in arraylist
			this.status = tbStatus.getText(); //update local status
			uClient.handleMessageFromClientUI(data); //send to ClientConnector
			showUpdated(); //show success message
		}
	}
	 @FXML
	 private void goBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(getClass().getResource("/gui/RequestsView.fxml").openStream());
			RequestsViewController requests = loader.getController();	
			requests.setTable(uClient); //update table view after changes
			requests.setBack(rScene); //set back button to get main scene
			Scene scene = new Scene(root);
			stage.setTitle("Requests List");
			stage.setScene(scene);			
			stage.show();
		} catch (IOException e) {
			// TODO add to error manager
			e.printStackTrace();
		}
	}
	 
	 /**
	   * Show alert dialog
	   *	If updates success: showUpdated(), else showNotUpdated()
	   */  
	private void showNotUpdated() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Update Failed");
		alert.setHeaderText("No update to perform");
		alert.setContentText("Please make sure you have changed the status");
		alert.show();
	}
	private void showUpdated() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Update Success");
		alert.setHeaderText("Status has been updated successfuly");
		alert.setContentText("The new status is: " + tbStatus.getText());
		alert.show();
	}
	
	/**
	   * update the previous scene
	   *
	   *@param g The scene sent from previous window
	   */
	public void setBack(Scene g) {
    	this.rScene = g;
    }
}
