package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import boundary.GuiManager;
import common.ClientConnector;
import common.ConnectorIF;
import common.ClientConnector.ConnectionDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ConnectionController implements Initializable, ConnectorIF{
	public final static int DEFAULT_PORT = 5555;			//default port
	public final static String DEFAULT_HOST = "localhost";	//default host
	public static ClientConnector client;							//sever handler

	@FXML
	private Pane insidePane;
	@FXML
	private TextField tbHostName;
	@FXML
	private Button btnConnect;
	@FXML
	private Button btnDisconnect;
	@FXML
	private Label lblConnected;
	@FXML
	private TextField tbPort;
	@FXML
	private Pane pMessageDown;
	@FXML
	private Label lblMessageDown;
	
	@FXML
	public void ConnectClick(ActionEvent event) {	//when click on 'connect'
		try {
			if(tbHostName.getText().isEmpty() || tbPort.getText().isEmpty()) {
				GuiManager.showError(lblMessageDown, pMessageDown, "HostName and Port can't be empty");
			}else {
				ConnectionDetails.setServerDetails(tbHostName.getText(), Integer.parseInt(tbPort.getText()));
				client = new ClientConnector(this);
				connectionEstablished(true);
				GuiManager.showSuccess(lblMessageDown, pMessageDown,"Connection Established Successfuly!");
			}	
		} catch (IOException e) {
			GuiManager.showError(lblMessageDown, pMessageDown,"Error: "+ e.getMessage());
		}
	}
	@FXML
	public void DisconnectClick(ActionEvent event) {	//when click on 'disconnect'
		try {
    		client.closeConnection();
    		connectionEstablished(false);
    		lblConnected.setVisible(false);
    		GuiManager.showSuccess(lblMessageDown, pMessageDown,"Disconnected Successfuly!");
		} catch (IOException e) {
			GuiManager.showError(lblMessageDown, pMessageDown,"Error: "+ e.getMessage());
		}
	}
	public static ClientConnector getClient() {		
		return client;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pMessageDown.setVisible(false);
		if (ConnectionController.getClient() != null && ConnectionController.getClient().isConnected()) {
			connectionEstablished(true);
			lblConnected.setVisible(true);
		}
		tbHostName.setText(ConnectionDetails.getHost());
		tbPort.setText(Integer.toString(ConnectionDetails.getPort()));
	}
	
	private void connectionEstablished(boolean isConnected) {
		btnDisconnect.setVisible(isConnected);
		btnConnect.setDisable(isConnected);
		tbHostName.setDisable(isConnected);
		tbPort.setDisable(isConnected);
	}
	@Override
	public void display(String message) {
		System.out.println(message);
		
	}
	
}
