package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.Request;
import entity.RequestHandling;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ProcessesController implements Initializable {
	@FXML
	private AnchorPane apActions;
	@FXML
	private Label lblPickMsg;
	@FXML
	private TableView<RequestHandling> tblTimeDetermine1;
	@FXML
	private TableColumn<RequestHandling, String> col_requestId1;
	@FXML
	private TableColumn<RequestHandling, String> col_stage1;
	@FXML
	private TableColumn<RequestHandling, String> col_dueDate;
	@FXML
	private TableColumn<RequestHandling, String> col_timeLeft;
	@FXML
	private Button btnExtend;
	@FXML
	private TextField tbExtend;
	@FXML
	private TextField tbTime;	
	@FXML
	private Label lblInfo;
	
	// attribute
	private static ArrayList<RequestHandling> arralistOfProcesses = null;
	
	private ObservableList<RequestHandling> List = null;
	private static RequestHandling selected;
	private ProcessesController pController;
	private ClientConnector client = ConnectionController.getClient();
	private static String flag ="";

	public void initData(ProcessesController c) {
		pController = c;
		EvaluationController.setCont(c);
		ExecutionController.setCont(c);
		CheckingController.setCont(c);
		ReviewController.setCont(c);
		TimeController.setCont(c);
	}

	public ProcessesController getCont() {
		return pController;
	}

	public static void setListOfProcesses(ArrayList<RequestHandling> array) {
		arralistOfProcesses = new ArrayList<>(array);
	}

	public static RequestHandling getSelected() {
		return selected;
	}
	
	public static String getFlag() {
		return flag;
	}

	public static void setFlag(String flag) {
		ProcessesController.flag = flag;
	}

	public void clearAP() {
		apActions.getChildren().clear();
	}
	
	public void removeSelected(RequestHandling selected) {
		tblTimeDetermine1.getItems().remove(selected);
		apActions.getChildren().clear();
		
	}

	@FXML
	void clickExtend(ActionEvent event) {
		
		if(tbExtend.getText().isEmpty()||tbTime.getText().isEmpty()||!isInteger(tbTime.getText())) {
			lblInfo.setVisible(true);
			lblInfo.setText("Invalid input!");
			return;
		}
		
		selected.setTimeNeeded(tbTime.getText());
		selected.setExtendReason(tbExtend.getText());
		
		ObjectManager msg = new ObjectManager(selected, MsgEnum.INSERT_EXTEND);
		client.handleMessageFromClientUI(msg);
		
		try {
			Thread.sleep(1000);

			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Your extend request has been sent to the inspector");
    	alert.showAndWait();
		
		
		
		btnExtend.setVisible(false);
		tbExtend.setVisible(false);
		tbTime.setVisible(false);
		
		
		
	}

	

	@FXML
	private void onItemClick(MouseEvent event) {
		lblInfo.setVisible(false);
		selected = tblTimeDetermine1.getSelectionModel().getSelectedItem();
		if(selected.getTimeNum().isEmpty()) {
			actionsView("Time");
		}
		else {
			actionsView(selected.getCurrentStage());
			if (Integer.valueOf(selected.getTimeNum()) <= 3) {
				ObjectManager msg = new ObjectManager(selected, MsgEnum.CHECK_EXTEND);
				client.handleMessageFromClientUI(msg);
				
			
				try {
					Thread.sleep(1000);

					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(flag.equals("False")) {
					btnExtend.setVisible(true);
					tbExtend.setVisible(true);
					tbTime.setVisible(true);
				}
				else {
					lblInfo.setVisible(true);
					lblInfo.setText("This request has ongoing time-extension request");
				}
				
				
			}
		}

		
		

	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObjectManager viewProcesses = new ObjectManager(arralistOfProcesses, MsgEnum.VIEW_PROCESSES);
		ConnectionController.getClient().handleMessageFromClientUI(viewProcesses);
		apActions.getChildren().clear();
		apActions.getChildren().add(lblPickMsg);

		try {
			Thread.sleep(1000);

			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		col_requestId1.setCellValueFactory(new PropertyValueFactory<>("idrequest"));
		col_stage1.setCellValueFactory(new PropertyValueFactory<>("currentStage"));
		col_dueDate.setCellValueFactory(new PropertyValueFactory<>("executionTime"));
		col_timeLeft.setCellValueFactory(new PropertyValueFactory<>("timeNum"));
		ArrayList<RequestHandling> rhArray = new ArrayList<RequestHandling>();
		String userRole = LoginController.getLoggedUser().getRole();
		for(RequestHandling rh : arralistOfProcesses) { //Show View By Permissions
			if(userRole.equals("Review Member") || userRole.equals("Review Leader")) {
				if(rh.getCurrentStage().equals("Review")) {
					rhArray.add(rh);
				}else {
					if(LoginController.getLoggedUser().getIdUser().equals(rh.getIdCharge())) 
						rhArray.add(rh);
				}
			}
			else if(LoginController.getLoggedUser().getIdUser().equals(rh.getIdCharge())) 
				rhArray.add(rh);
		}
		List = FXCollections.observableArrayList(rhArray);
		tblTimeDetermine1.setItems(List);
		
	}
	
	private void actionsView(String stageToShow) {
		try 
        {
            AnchorPane newLoadedPane;
            
            newLoadedPane =  FXMLLoader.load(getClass().getResource("/boundary/guifiles/" + stageToShow + "Pane.fxml"));
          
            apActions.getChildren().clear();
            apActions.getChildren().add(newLoadedPane);
        }
	catch(IOException ex)
	{
		Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
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
