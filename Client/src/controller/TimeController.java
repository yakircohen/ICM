package controller;

import java.net.URL;
import java.util.ResourceBundle;

import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.RequestHandling;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class TimeController implements Initializable{

    @FXML
    private TextField tbTime;

    @FXML
    private Button btnRequest;

    @FXML
    private Label lblErr;

    @FXML
    private Label lblTime;
    
    private static ProcessesController d;
    private static String flag ="";
    private ClientConnector client = ConnectionController.getClient();

    
	public static String isFlag() {
		return flag;
	}


	public static void setFlag(String flag) {
		TimeController.flag = flag;
	}


	public static void setCont(ProcessesController p) {
    	d=p;
    }
    
    
    @FXML
    void clickRequest(ActionEvent event) {
    	if(tbTime.getText().isEmpty()) {
    		lblErr.setVisible(true);
			lblErr.setText("Time field is empty!");
			return;
    	}
    	if(!isInteger(tbTime.getText())) {
    		lblErr.setVisible(true);
			lblErr.setText("Time field is not a number of days!");
			return;
    	}
    	
    	RequestHandling temp = ProcessesController.getSelected();
    	temp.setTimeNeeded(tbTime.getText());
    	ObjectManager msg = new ObjectManager(temp , MsgEnum. ADD_TIME_REQ);
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
	    	alert.setContentText("Your time request has been sent to the Inspector");
	    	alert.showAndWait();
	    	
	    	d.getCont().clearAP();
	    	
    		
    	
    	
    }

    
     
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ObjectManager msg = new ObjectManager(ProcessesController.getSelected(), MsgEnum.CHECK_TIME_SET);
		client.handleMessageFromClientUI(msg);
		
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(flag.equals("True")) {
			btnRequest.setVisible(false);
			lblErr.setVisible(true);
			tbTime.setVisible(false);
			lblTime.setVisible(false);
			lblErr.setText("You already subbmitted time-needed for this request!\n Please wait for inspector's decision");
			
		}
	}
	
	
	public  boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}

}
