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
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

public class ExecutionController implements Initializable {

    @FXML
    private TextArea tbInfo;

    @FXML
    private Button btnDone;
    
    @FXML
    private Label lblInfo;
    

    //attributes
    private ClientConnector client = ConnectionController.getClient();
    private static String info;
    private static ProcessesController d;
    public static void setCont(ProcessesController p) {
    	d=p;
    }
    @FXML
    void clickDone(ActionEvent event) {
    	ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(),MsgEnum.EXECUTION_DONE);
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
    	alert.setContentText("Your execution has been submitted");
    	alert.showAndWait();
    	
    	d.getCont().removeSelected(ProcessesController.getSelected());
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lblInfo.setVisible(false);
		ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.GET_CHANGE_INFO);
		client.handleMessageFromClientUI(msg);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tbInfo.setText(info);
	}

	public static String getInfo() {
		return info;
	}

	public static void setInfo(String info) {
		ExecutionController.info = info;
	}
	
	

}
