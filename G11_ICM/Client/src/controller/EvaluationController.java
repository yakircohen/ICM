package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import boundary.GuiManager;
import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.EvaluationReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
/**
 * 
 * the controller of evaluation view
 *
 */
public class EvaluationController implements Initializable{

    @FXML
    private Label lblErr;

    @FXML
    private TextArea tbTime;

    @FXML
    private TextArea tbRisk;

    @FXML
    private TextArea tbResult;

    @FXML
    private TextArea tbDescription;

    @FXML
    private TextField tbLocation;

    @FXML
    private Button btnEvalue;

    @FXML
    private Hyperlink hyperReqInfo;

    //attributes
    private ClientConnector client = ConnectionController.getClient();
    private static boolean previousReport;
    private static EvaluationReport report;
	  private static ProcessesController d;
   
    public static void setCont(ProcessesController p) {
    	d=p;
    }

    /**
     * get the report that was submitted
     * @return get the report that was submitted
     */
    public static EvaluationReport getReport() {
		return report;
	}
/**
 * set the report that was submitted
 * @param report - the report that was submitted
 */
	public static void setReport(EvaluationReport report) {
		EvaluationController.report = report;
	}
/**
 * a flag the tells if there is previews evaluation report(when request goes from review back to evaluation)
 * @return a flag the tells if there is previews evaluation report(when request goes from review back to evaluation)
 */
	public static boolean isPreviousReport() {
		return previousReport;
	}
/**
 * a flag the tells if there is previews evaluation report(when request goes from review back to evaluation)
 * @param previousReport
 */
	public static void setPreviousReport(boolean previousReport) {
		EvaluationController.previousReport = previousReport;
	}

	/**
	 * this function is called when the user click evaluate button
	 * @param event
	 */
    @FXML
    void clickEvaluate(ActionEvent event) {
    	
    	//empty report
    	if(checkInput()) {
    		lblErr.setVisible(true);
    		lblErr.setTextFill(Color.RED);
    		lblErr.setText("One filed or more are empty!");
    		return;
    	}
    	
    	if(!isInteger(tbTime.getText())) {
    		lblErr.setVisible(true);
    		lblErr.setTextFill(Color.RED);
    		lblErr.setText("The time you entered is not a number!");
    		return;
    	}
    	
    	//build the report
    	EvaluationReport temp = new EvaluationReport();
    	temp.setDescription(tbDescription.getText());
    	temp.setLocation(tbLocation.getText());
    	temp.setResult(tbResult.getText());
    	temp.setRisk(tbRisk.getText());
    	temp.setTime(tbTime.getText());
    	temp.setIdReq(ProcessesController.getSelected().getIdrequest());
    	
    	//sending the report to server
    	ObjectManager msg = new ObjectManager(temp, MsgEnum.ADD_EV_REPORT);
    	client.handleMessageFromClientUI(msg);
    	
    	//waiting for the server
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
    	
    	d.getCont().removeSelected(ProcessesController.getSelected()); //remove object from table
    	clearAll();

    }

    /**
     * show the evaluator the reuqet's detailes
     * @param event
     * @throws IOException
     */
    @FXML
    void clickInfo(ActionEvent event) throws IOException { //raise popup with selected request details
    	GuiManager.popUpLoader("RequestView", ProcessesController.getSelected().getIdrequest());
    }
/**
 * checking if there is previous report,
 * if there is: show him that report
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lblErr.setVisible(false);
		
		
		//checking if there is previous evaluation report
		ObjectManager msg = new ObjectManager(ProcessesController.getSelected().getIdrequest(), MsgEnum.CHECK_PRE_EV);
		client.handleMessageFromClientUI(msg);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(report!=null) {
			tbDescription.setText(report.getDescription());
			tbLocation.setText(report.getLocation());
			tbResult.setText(report.getResult());
			tbRisk.setText(report.getRisk());
			tbTime.setText(report.getTime());
			return;
		}
		
		
	}
	/**
	 * clear all the fields in the form
	 */
	public void clearAll() {
		tbDescription.clear();
		tbLocation.clear();
		tbResult.clear();
		tbRisk.clear();
		tbTime.clear();
	}
	
	public boolean checkInput() {
		if(tbDescription.getText().isEmpty())
			return true;
		
		if(tbLocation.getText().isEmpty())
			return true;
		
		if(tbResult.getText().isEmpty())
			return true;
		
		if(tbResult.getText().isEmpty())
			return true;
		
		if(tbTime.getText().isEmpty())
			return true;
		
		return false;
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
