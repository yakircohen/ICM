package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.Messages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
/**
 * Controller for messages received.
 *
 */
public class MessagesController implements Initializable
{
	@FXML
	private TableView<Messages> tblMessages;
	@FXML
	private TableColumn<Messages, String> dateColumn;
	@FXML
	private TableColumn<Messages, String> titleColumn;
	@FXML
	private Label lblNoMessages;
	@FXML
    private TitledPane tpBody;
    @FXML
    private Label lblPickMsg;
    @FXML
    private AnchorPane apBody;
    @FXML
    private AnchorPane apLoading;
    @FXML
    private TextArea taContent;
    @FXML
    private Label lblTitle;

	private ClientConnector client = ConnectionController.getClient();
	private static ObservableList<Messages> List = FXCollections.observableArrayList();
	private static ArrayList<Messages> arralistofmessages = null;
	private Messages message;
	
	/**
	 * Set arralistofmessages with data from DB, in order to populate the messages table
	 * @param array The array from DB.
	 */
	public static void setListOfMessages(ArrayList<Messages> array) {
		arralistofmessages = new ArrayList<>(array);
	}
	/**
	 * Initializing messages controller, populates data into table and prepare GUI.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tblMessages.setVisible(false);
		tpBody.setVisible(false);
		apBody.setVisible(false);
		lblNoMessages.setVisible(false);
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateMessage"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("titleMessage"));
		 //Task to get data from DB
 	    Task<ArrayList<Messages>> task = new Task<ArrayList<Messages>>() {
 	        @Override
 	        public ArrayList<Messages> call() throws InterruptedException {
 	        	ObjectManager viewMesages = new ObjectManager(LoginController.getLoggedUser(), MsgEnum.VIEW_MESSAGES);
 	        	client.handleMessageFromClientUI(viewMesages);
 	       	   	Thread.sleep(500);
 	       	   	return arralistofmessages;
 	        }
 	    };
 	    task.setOnSucceeded(e -> {
 	    	ArrayList<Messages> result = task.getValue();
 	    	apLoading.setVisible(false);
 	    	if (arralistofmessages.isEmpty())
				lblNoMessages.setVisible(true);
			else {
				List = FXCollections.observableArrayList(result);
				tblMessages.setItems(List);
				tpBody.setVisible(true);
				tblMessages.setVisible(true);
			}
 	    });  
 	    task.setOnRunning(event -> {
 	    	apLoading.setVisible(true);
 	    }); 
 	    new Thread(task).start();
	}
	/**
	 * When click on an item from table, shows data of message.
	 * @param event
	 */
	@FXML
	private void onItemClick(MouseEvent event) {
		if(tblMessages.getSelectionModel().isEmpty()) {//if nothing selected
			apBody.setVisible(false);
			lblPickMsg.setVisible(true);
			return; 
		}
		message = tblMessages.getSelectionModel().getSelectedItem(); //get selected message
		lblTitle.setText(message.getTitleMessage());
		taContent.setText(message.getContentMessage());
		apBody.setVisible(true);
		lblPickMsg.setVisible(false);
	}   
	    
	    
	    
	    
}
