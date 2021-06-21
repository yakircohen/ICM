package gui;

import java.io.IOException;
import java.util.ArrayList;
import client.ClientConnector;
import common.clientConnectionIF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainController implements clientConnectionIF {
	
	 /**
	   * GUI elements
	   *
	   */
	@FXML
    private Button btnStart;
    @FXML
    private Button btnOptions;
    @FXML
    private Button btnQuit;
    @FXML
    private Button btnConnect;   
    @FXML
    private Label lblSrvStatus;
   
    /**
	   * Variables to store data.
	   *
	   */
    public final static int DEFAULT_PORT = 5555;			//default port
	public final static String DEFAULT_HOST = "localhost";	//default host
	private ClientConnector client;							//sever handler
	private int srvPort;									//port address
	private String srvHost;									//host name address
	private boolean isConnected = false;					//if connected to server already
	
	/**
	   * Initialize with default data
	   *	
	   */ 
	public MainController() {
		srvPort = DEFAULT_PORT;
		srvHost = DEFAULT_HOST;
	}
	
	/**
	   * change the default address of the server
	   *
	   *@param host The host address
	   *@param port The port address	
	   */ 
	public void ChangeAdressOfConnection(String host, int port) {
		srvHost = host;
		srvPort = port;
	}
	
	/**
	   * FXML GUI functions and actions
	   *
	   */  
	@FXML
	public void EstablishConnection(ActionEvent event) {	//when click on 'connect'
		try {

				this.client = new ClientConnector(srvHost, srvPort,this);
				ArrayList<ArrayList<String>> aList =  new ArrayList<ArrayList<String> >();
		    	client.handleMessageFromClientUI(aList); //creates table show from db
		    	this.isConnected = true;
		    	btnConnect.setVisible(false);
		    	lblSrvStatus.setText("Online");
		    	lblSrvStatus.setTextFill(Color.GREEN);

		} catch (IOException e) {
			ErrorMessage("Connection Error", e.toString(), "Please check connection settings");
		}
	}
	
	@FXML
	public void onQuitAppClick(ActionEvent event) {  //when click on 'quit'
    	try {
    		if (client != null && client.isConnected())
    			client.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 Platform.exit();
	     System.exit(0);
	}
	
	@FXML
	private void onOptionsClick(ActionEvent event) { //when click on 'options'
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(getClass().getResource("/gui/OptionsController.fxml").openStream());
			OptionsController option = loader.getController();	
			option.setDetails(srvHost, srvPort, client);	//initialize the scene with data
			option.setBack(btnOptions.getScene()); //set back scene
			Scene scene = new Scene(root);
			stage.setTitle("Connection Settings");
			stage.setScene(scene);		
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void onStartClick(ActionEvent event) throws Exception { //when click on 'start'
		if(this.isConnected) { //start if connection to server has been established
			btnConnect.setStyle("-fx-border-color: black");
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			try {
				root = loader.load(getClass().getResource("/gui/RequestsView.fxml").openStream());
				RequestsViewController requests = loader.getController();	
				requests.setTable(this.client);			//creates table view with data from DB
				requests.setBack(btnStart.getScene()); //set back scene
				Scene scene = new Scene(root);	
				stage.setTitle("Requests List");
				stage.setScene(scene);
				stage.show();	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			btnConnect.setStyle("-fx-border-color: red");
			ErrorMessage("Connection Error", "Couldn't connect to server", "Make sure you clicked on 'connect' button");
		}
	}
	@Override
	public void display(String message) //implements
	  {
	    System.out.println("> " + message);
	  }
	
	/**
	   * Show alert dialog
	   *
	   *@param title The title for error
	   *@header Short brief appon error
	   *@param content Content of the error
	   */  
	private void ErrorMessage(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	  
}
