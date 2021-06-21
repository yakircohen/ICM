package controller;

import entity.Document;
import entity.Request;
import entity.User;
import common.ClientConnector;
import common.MsgEnum;
import common.MsgHandler;
import common.MyFile;
import common.ObjectManager;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import boundary.GuiManager;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NewRequestController implements Initializable{
	
	
	
	@FXML
    private Label lblFiles;
	
	@FXML
    private Label lblDate;
	
	
	@FXML
    private Label lblPageName;

    @FXML
    private Label lblPageNavigationName;

    @FXML
    private TextField tbState;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> cbSystem;

    @FXML
    private TextField tbChange;

    @FXML
    private TextArea tbReason;

    @FXML
    private TextArea tbComment;

    @FXML
    private Button btnFile;

    @FXML
    private Label lblError;
    
    @FXML
    private Pane paneFile;
    
    @FXML
    private TextArea taFiles;
    
    
    
    //attributes
    private static Request req = new Request(); 
	private List<File> files = new ArrayList<>();
	private List<Document> myDocuments = new ArrayList<>();
	private ClientConnector client = ConnectionController.getClient();
	private String filesPaths ="";
	private Service<Void> addReqBackground;
	private static  Integer idfromserver;
    
	
	
	
	/**
	 * when clear button is clicked this function is called.
	 * @param event
	 * @throws InterruptedException
	 * @throws IOException
	 */
    public void clearClick(ActionEvent event) throws InterruptedException, IOException {
    	clearAll();
    }
    
    /**
     * this function is called when user click "upload files" button.
     * it shows file-chooser dialog with the following file-formats allowed:
     * *pdf, *jpg, *txt
     * @param event
     * @throws InterruptedException
     * @throws IOException
     */
    public void fileClick(ActionEvent event) throws InterruptedException, IOException {
    	
    	
    	FileChooser fileChooser = new FileChooser();
    	
    	FileChooser.ExtensionFilter fileExtensions = 
  			  new FileChooser.ExtensionFilter(
  			    "All", "*.txt", "*.pdf", "*.jpg");
    	
    	FileChooser.ExtensionFilter fileExtensions1 = 
    			  new FileChooser.ExtensionFilter(
    			    "txt", "*.txt");
    	FileChooser.ExtensionFilter fileExtensions2 = 
  			  new FileChooser.ExtensionFilter(
  			    "pdf", "*.pdf");
    	FileChooser.ExtensionFilter fileExtensions3 = 
  			  new FileChooser.ExtensionFilter(
  			    "jpg", "*.jpg");

    	fileChooser.getExtensionFilters().add(fileExtensions);
    	fileChooser.getExtensionFilters().add(fileExtensions1);
    	fileChooser.getExtensionFilters().add(fileExtensions2);
    	fileChooser.getExtensionFilters().add(fileExtensions3);
    	
    	Window wind = btnFile.getScene().getWindow();
    	files.addAll(fileChooser.showOpenMultipleDialog(wind));
    	
    	taFiles.clear();
    	filesPaths="";
    	for(int i=0;i<files.size();i++) {
    		filesPaths+=files.get(i).getPath()+"\n";
    		taFiles.appendText(files.get(i).getPath()+"\n");
    	}
    	
    	
    }
    /**
     * this function create the request.
     * *if fields are empty, it lets the user know
     * *if send the server the request info and the files the user picked
     * 
     * @param event
     * @throws InterruptedException
     * @throws IOException
     */
    public void createClick(ActionEvent event) throws InterruptedException, IOException {
    	
    	lblError.setVisible(false);
    	
    	String system = cbSystem.getValue();
    	String state = tbState.getText();
    	String change = tbChange.getText();
    	String reason = tbReason.getText();
    	
    	if(!checkFormFields(system,state,change,reason).equals("Input is ok")){
    		lblError.setText(checkFormFields(system,state,change,reason));
    		lblError.setVisible(true);
    		return;
    	}
    	
    	//building the request entity (same order as request-table in DB, except ID)
    	
    	//id will be added in server
    	req.setCurState(tbState.getText());
    	req.setRequestedChange(tbChange.getText());
    	req.setPurpose(tbReason.getText());
    	
    	if(!tbComment.getText().isEmpty()) {
    		req.setComments(tbComment.getText());
    	}
    	
    	req.setSystem(cbSystem.getValue());
    	req.setOpentDate(lblDate.getText());
    	req.setIdUser(LoginController.getLoggedUser().getIdUser());
    	req.setStatus("not started");
    	req.setTotalTime("0");
 
    	ObjectManager requestMsg = new ObjectManager(req, MsgEnum.ADD_REQUEST);
        client.handleMessageFromClientUI(requestMsg);
        
       
        Thread.sleep(2000);
        
        req.setIdReq(idfromserver.toString());
        
        //Handling files
        
        

        Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Your request has been submitted!");
    	alert.showAndWait();

        //in case of no files
        if(files.isEmpty())
        {
        	clearAll();
        	
        	return;
        }
        	
        //converting the Files we got to Document that has MyFiles
        //this Document still doesnt have id, it will be added in server-side
        for(File fl : files) {
        	Document temp = new Document();
        	temp.setFileName(fl.getName());
        	temp.setFileType(fl.getName().substring(fl.getName().lastIndexOf(".")+1));
        	temp.setIdrequest(req.getIdReq());
        	temp.setPath(fl.getPath());
        	temp.setMyFile(temp.getMyFile());
        	myDocuments.add(temp);
        	
        }
        
        ObjectManager filesMsg = new ObjectManager(myDocuments, MsgEnum.ADD_FILES);
        client.handleMessageFromClientUI(filesMsg);     
    	alert.showAndWait();
        
        clearAll();
         
		
    }
    
    
    /**
     * Check the forms fields.
     * @return "Empty" if all fields are filled, otherwise return a string that describes 
     * the missing fields  
     */
	public String checkFormFields(String system, String state, String change, String purpose) {
		
		
		StringBuilder errorLable = new StringBuilder("The following fields are empty: ");
    	
    	boolean errFlag = false; 
    	
    	 if(system.isEmpty()) {
    		 errFlag = true;
    		 errorLable.append("System, ");
     	}
    	 if(state.isEmpty()) {
    		 errFlag = true;
    		errorLable.append("Current state, ");
    	}
    	 if(change.isEmpty()) {
    		 errFlag = true;
    		errorLable.append("Change, ");
    	}
    	 if(purpose.isEmpty()){
    		 errFlag = true;
    		errorLable.append("Reason, ");
    	}
    	
    	
    	if(errFlag) {
    		errorLable.setCharAt(errorLable.length()-2, '.');
        	return errorLable.toString();
    	}
    	

    	return "Input is ok";
	}
	
	
    /**
     * clear all fields in the form
     */
    
    public void clearAll() {
    	tbState.clear();
    	tbChange.clear();
    	tbReason.clear();
    	tbComment.clear();
    	taFiles.clear();
    	files.removeAll(files);
    	myDocuments.removeAll(myDocuments);
    	cbSystem.getSelectionModel().clearSelection();
    	
    	
    }
    /**
     * initialize the current view and its fields, including:
     * setting the combo-box
     * getting current date and putting it at the top-right
     * disable editing of files-path text area
     */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//building the Systems options
		List<String> list = new ArrayList<String>();
        list.add("Moodle");
        list.add("Office");
        list.add("Library");
        list.add("Info Braude");
        list.add("Braude Website");
        ObservableList obList = FXCollections.observableList(list);
		cbSystem.getItems().clear();
		cbSystem.setItems(obList);
		
		//setting the current date 
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		lblDate.setText(dtf.format(localDate));
		
		//setting file paths to un-editable
		taFiles.setEditable(false);
		
		//setting error lable invisible
		lblError.setVisible(false);
		
		
		
	}
	public static void setNewID (Integer id) {
		idfromserver = id;
	}
	

	
}
