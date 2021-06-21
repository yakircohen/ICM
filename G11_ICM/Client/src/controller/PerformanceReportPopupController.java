package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.MsgEnum;
import common.ObjectManager;
import entity.PerformanceReport;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class PerformanceReportPopupController implements Initializable{
	@FXML
    private Label durationApprovedExtensions;
	@FXML
	private Label durationActivityTimeAdded;
	
	public static ArrayList<Integer> approvedExtensions= new ArrayList<Integer>();
	public static ArrayList<Integer> addedActivityTime= new ArrayList<Integer>();
	
	public static void setApprovedExtensions(ArrayList<Integer> approvedExtensions) {
		PerformanceReportPopupController.approvedExtensions = approvedExtensions;
	}

	public static void setAddedActivityTime(ArrayList<Integer> addedActivityTime) {
		PerformanceReportPopupController.addedActivityTime = addedActivityTime;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Integer sumOfApprovedExtensions=0, sumOfAddedActivityTime=0;
		ObjectManager generatePerformanceReport =new ObjectManager(approvedExtensions,addedActivityTime, MsgEnum.CREATE_PERFORMANCE_REPORT);
		ConnectionController.getClient().handleMessageFromClientUI(generatePerformanceReport);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(Integer val :approvedExtensions)
			sumOfApprovedExtensions+=val;
		for(Integer val :addedActivityTime)
			sumOfAddedActivityTime+=val;
		durationApprovedExtensions.setText(sumOfApprovedExtensions.toString());
		durationActivityTimeAdded.setText(sumOfAddedActivityTime.toString());
	}

}
