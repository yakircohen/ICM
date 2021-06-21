package controller;

import java.net.URL;
import java.util.ResourceBundle;

import common.MsgEnum;
import common.ObjectManager;
import entity.Request;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class RequestViewController implements Initializable{
	private static String idrequest;
	private static Request req = null;
    @FXML
    private Label lblRequestID;

    @FXML
    private TextField tbState;

    @FXML
    private TextField tbChange;

    @FXML
    private TextArea tbReason;

    @FXML
    private TextArea tbComment;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblSystem;

    @FXML
    private Label lblUserID;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObjectManager viewRequest = new ObjectManager(idrequest, MsgEnum.VIEW_REQUEST);
    	ConnectionController.getClient().handleMessageFromClientUI(viewRequest);
    	
    	try {
			Thread.sleep(1500);
			lblRequestID.setText(req.getIdReq());
			lblDate.setText(req.getOpentDate());
			lblSystem.setText(req.getSystem());
			lblUserID.setText(req.getIdUser());
			tbState.setText(req.getCurState());
			tbChange.setText(req.getRequestedChange());
			tbReason.setText(req.getPurpose());
			tbComment.setText(req.getComments());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void initData(String reqId) {
		idrequest = reqId;
	}
	public static void setRequest(Request reqFromDB) {
		req = reqFromDB;
    }
}
