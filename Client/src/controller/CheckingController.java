package controller;

import java.net.URL;
import java.util.ResourceBundle;

import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
/**
 * 
 * the controller of the checking view
 *
 */
public class CheckingController implements Initializable {

    @FXML
    private TextArea tbFailure;

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnFailure;

    @FXML
    private Label lblStatus;
    
    //attributes
    private ClientConnector client = ConnectionController.getClient();
    private static ProcessesController d;
    public static void setCont(ProcessesController p) {
    	d=p;
    }
    /**
     * this function is called when "approve" button is clicked, and cause the request
     * to move from Checking to Closing
     * @param event
     */
    @FXML
    void clickApprove(ActionEvent event) {
    	ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.APPROVE_CHECKING);
    	client.handleMessageFromClientUI(msg);
    	
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Your approval has been submitted");
    	alert.showAndWait();
    	
    	d.getCont().removeSelected(ProcessesController.getSelected());
    }

    /**
     * this function is called when "approve" button is clicked, and cause the request
     * to move from Checking to Execution
     * @param event
     */
    @FXML
    void clickFailure(ActionEvent event) {
    	
    	if(tbFailure.getText().isEmpty()) {
    		lblStatus.setVisible(true);
    		lblStatus.setText("Please enter your failure!");
    		lblStatus.setTextFill(Color.RED);
    		return;
    	}
    	
    	
    	String str = ProcessesController.getSelected().getIdrequest()+"_"+tbFailure.getText();
    	
    	
    	ObjectManager msg = new ObjectManager(str, MsgEnum.REJECT_CHECKING);
    	client.handleMessageFromClientUI(msg);
    	
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Evaluation report added successfully!");
    	alert.showAndWait();
    	
    	d.getCont().removeSelected(ProcessesController.getSelected());

    }
/**
 * this function initialize the Checking view, and hide the Label
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lblStatus.setVisible(false);
		
	}

}
