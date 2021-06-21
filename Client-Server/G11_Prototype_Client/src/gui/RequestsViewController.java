package gui;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RequestsViewController{
	/**
	   * GUI elements
	   *
	   */
	@FXML
    private TableView<RequestsTable> tbl_requests;
    @FXML
    private TableColumn<RequestsTable, String> col_name;
    @FXML
    private TableColumn<RequestsTable, String> col_board;
    @FXML
    private TableColumn<RequestsTable, String> col_handler;
    @FXML
    private TableColumn<RequestsTable, String> col_status;  
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnBack;    
    @FXML
    private Label lblError;
    
    /**
	   * Variables to store data.
	   *
	   */
    private int celId = -1; 			//keeps the chosen cell id
    private ClientConnector rClient;	//client connection
    private Scene mScene;				//saves the previous scene
    ObservableList<RequestsTable> list = FXCollections.observableArrayList(); //for table view
    
    /**
	   * Before loading RequestView scene, updating it with data and prepare the table view
	   *
	   *@param client The client connection for server
	   */
    public void setTable(ClientConnector client){
    	this.rClient = client;
    	for (ArrayList<String> temp : client.getListj()) {
    		list.add(new RequestsTable(temp.get(0),temp.get(1),temp.get(5),temp.get(4)));
		}
		col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_board.setCellValueFactory(new PropertyValueFactory<>("board"));
		col_handler.setCellValueFactory(new PropertyValueFactory<>("handler"));
		col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		
		tbl_requests.setItems(list);	
	}
    
    /**
	   * FXML GUI functions and actions
	   *
	   */ 
    @FXML
    private void onItemClick(MouseEvent event) {
    	btnEdit.setStyle("-fx-background-color: #00ff00");
    	celId = tbl_requests.getSelectionModel().getSelectedIndex(); //save the selected item
    	lblError.setVisible(false);
	}
    @FXML
    void goBack(ActionEvent event)  throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage stage = new Stage();
		stage.setTitle("Requests Update Menu");
		stage.setScene(this.mScene);	
		stage.show();
    }
    @FXML
    private void onEditClick(ActionEvent event) {
    	if(celId != -1) {
    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    		Stage stage = new Stage();
    		FXMLLoader loader = new FXMLLoader();
    		Pane root;
    		try {
    			root = loader.load(getClass().getResource("/gui/RequestUpdate.fxml").openStream());
    			RequestUpdateController update = loader.getController();	
    			update.setForm(celId, rClient);
    			update.setBack(this.mScene); //send the main scene
    			Scene scene = new Scene(root);
    			stage.setTitle("Status Update");
    			stage.setScene(scene);		
    			stage.show();
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}else {
    		lblError.setVisible(true);
    	}
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
