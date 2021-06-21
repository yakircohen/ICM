package controller;


import java.net.URL;
import java.util.ResourceBundle;

import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.EvaluationReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ReviewController implements Initializable {

    @FXML
    private TextArea tbReport;

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnReject;
    
    @FXML
    private Label lblStatus;

    @FXML
    private Button btnLeader;

    //attributes
    private ClientConnector client = ConnectionController.getClient();
    private static EvaluationReport report;
    private static ProcessesController d;
    private static String reviewExist;
   

    
    
    public static String isReviewExist() {
		return reviewExist;
	}


	public static void setReviewExist(String reviewExist) {
		ReviewController.reviewExist = reviewExist;
	}


	public static void setCont(ProcessesController p) {
    	d=p;
    }
    
    
    public static EvaluationReport getReport() {
		return report;
	}

	public static void setReport(EvaluationReport report) {
		ReviewController.report = report;
	}
	 @FXML
	void clickApproveLeader(ActionEvent event) {
		 ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.REVIEW_LEADER_APPROVE);
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
	    	alert.setContentText("Your decision has been submitted");
	    	alert.showAndWait();
	    	
	    	d.getCont().removeSelected(ProcessesController.getSelected());
		 
		 
		 
		 

	    }

	@FXML
    void clickApprove(ActionEvent event) {
		ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest()+"_Approve", MsgEnum.ADD_REVIEW);
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
    	
    	d.getCont().clearAP();
		hideButtons();
		
    }

	@FXML
    void clickReject(ActionEvent event) {
		ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest()+"_Reject", MsgEnum.ADD_REVIEW);
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
    	alert.setContentText("Your rejection has been submitted");
    	alert.showAndWait();
    	
    	d.getCont().clearAP();
		hideButtons();
    	
    }
	
    @FXML
    void clickInfo(ActionEvent event) {
    	ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.MORE_INFO_REVIEW);
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
    	alert.setContentText("Your rejection has been submitted");
    	alert.showAndWait();
    	
    	d.getCont().removeSelected(ProcessesController.getSelected());
    	
    }

   public void hideButtons() {
	   btnApprove.setVisible(false);
		btnInfo.setVisible(false);
		btnReject.setVisible(false);
   }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//gui mangement
		lblStatus.setVisible(false);
		
		
		
		ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.GET_EV_REPORT);
		client.handleMessageFromClientUI(msg);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String str = "Location of change: "+report.getLocation()+"\n\n"+
					"Description: "+report.getDescription()+"\n\n"+
					"Result: "+report.getResult()+"\n\n"+
					"Risk: "+report.getRisk()+"\n\n"+
					"Time evaluation: "+report.getTime();
		
		tbReport.setText(str);
		
		
		
		msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.CHECK_REVIEW_EXIST);
		client.handleMessageFromClientUI(msg);
		

		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		//if it's not leader hide decision button
		if(!LoginController.getLoggedUser().getRole().equals("Review Leader"))
		{
			
			btnLeader.setVisible(false);
			

			if(!reviewExist.isEmpty()) {
				btnApprove.setVisible(false);
				btnInfo.setVisible(false);
				btnReject.setVisible(false);
				lblStatus.setVisible(true);
				lblStatus.setText("There is already a decision for this request. Waiting for leader's approval");
				
			}
			
			
			
		}
		//leader
		else {
			
			if(!reviewExist.isEmpty()) {
				btnApprove.setVisible(false);
				btnInfo.setVisible(false);
				btnReject.setVisible(false);
				lblStatus.setVisible(true);
				lblStatus.setTextFill(Color.BLUE);
				lblStatus.setText("Team's Decision is : " + reviewExist);
				
			}
			
						
		}
			
	
	}
		
		
}		
		
		
		
	
    
    


