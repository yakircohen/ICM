package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import boundary.GuiManager;
import common.MsgEnum;
import common.ObjectManager;
import entity.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
/**
 * Controller for menu sidebar. Controls the bottons, logout and master view
 * @author Ron
 *
 */
public class MenuController implements Initializable{
	@FXML
    private Button btnLogout;
    @FXML
    private Label lblName;
    @FXML
    private Label lblRole;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnMessages;
    @FXML
    private Button btnNewRequest;
    @FXML
    private Button btnMyRequests;
    @FXML
    private Button btnProcesses;
    @FXML
    private Button btnReports;
    @FXML
    private Button btnEmployees;
    @FXML
    private Hyperlink hlProfile;
    @FXML
    private AnchorPane apCenterContent;
    @FXML
    private BorderPane bpRoot;
    @FXML
    private Pane pRole;
    @FXML
    private Separator spEmployees;
    @FXML
    private Separator spReports;
    @FXML
    private Separator spProcesses;
    @FXML
    private Stage menuStage;
    
    private Button btnTemp;
	private User user;
	/**
	 * Initializes the controller with default actions
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		btnTemp = new Button();
		sceneManager("HomePane", btnHome);
	}
	/**
	 * Set data and settings before initializing
	 * 
	 * @param user The user entered to the system.
	 * @param stage The controller's stage for later use.
	 */
	public void initData(User user, Stage stage) {
		this.user = user;
		menuStage = stage;
		permissionCheck(user.getRole()); //update gui regarding the role of user
		lblName.setText(user.getFirstName() + " " + user.getLastName());
		lblRole.setText(user.getRole());
		lblRole.layoutXProperty().bind(pRole.widthProperty().subtract(lblRole.widthProperty()).divide(2));
		Platform.setImplicitExit(false);
		menuStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				e.consume();
				logoutUser();
			}	            	
        });	
	}
	/**
	 * When click on Home button. Changes the window title and button design.
	 * Shows the relevant screen
	 */
	@FXML
    void homeClick(ActionEvent event) {
		menuStage.setTitle("ICM -> Home");
		sceneManager("HomePane", btnHome);
    }
	/**
	 * When click on Messages button. Changes the window title and button design.
	 * Shows the relevant screen
	 */
    @FXML
    void messagesClick(ActionEvent event) {
    	menuStage.setTitle("ICM -> Messages");
    	sceneManager("MessagesPane", btnMessages);
    }
    /**
	 * When click on My Requests button. Changes the window title and button design.
	 * Shows the relevant screen
	 */
    @FXML
    void myRequestsClick(ActionEvent event) {
    	menuStage.setTitle("ICM -> My Requests");
    	sceneManager("MyRequestsPane", btnMyRequests);
    }
    /**
	 * When click on New Request button. Changes the window title and button design.
	 * Shows the relevant screen
	 */
    @FXML
    void newRequestClick(ActionEvent event) {
    	menuStage.setTitle("ICM -> New Request");
    	sceneManager("NewRequestPane", btnNewRequest);
	}
    /**
	 * When click on Logout button. 
	 * Shows an alert, and if 'ok' chosen, disconnects user and shows login screen.
	 */
    @FXML
    void logoutClick(ActionEvent event) {
    	logoutUser();
	}
    /**
	 * When click on Processes button. Changes the window title and button design.
	 * Shows the relevant screen
	 * Only permitted users can see this button
	 */
    @FXML
    void processesClick(ActionEvent event) {
    	menuStage.setTitle("ICM -> Processes");
    	buttonStyle(btnProcesses);
    	if(user.getRole().compareTo("Inspector") == 0 || user.getRole().compareTo("Manager") == 0) { //if user is Inspector
    		sceneManager("ProcessPaneInspector", btnProcesses); 
    	}
    	else {
    		//sceneManager("ProcessesPane", btnProcesses);
    		FXMLLoader loader = new FXMLLoader();
    		try {
    			AnchorPane newLoadedPane;
    			loader.setLocation(controller.ProcessesController.class.getResource("/boundary/guifiles/ProcessesPane.fxml"));
    			newLoadedPane =  loader.load();
    			ProcessesController pController = loader.getController();
    			pController.initData(pController);
    			apCenterContent.getChildren().clear();
    			apCenterContent.getChildren().add(newLoadedPane);
    			} catch (IOException e) {
						e.printStackTrace();
				}		
    	}
    	
    }
    /**
	 * When click on Reports button. Changes the window title and button design.
	 * Shows the relevant screen
	 * Only Manager can see this button
	 */
    @FXML
    void reportsClick(ActionEvent event) {
    	menuStage.setTitle("ICM -> Reports");
    	sceneManager("ReportsPane", btnReports);
    }
    /**
   	 * When click on Employees button. Changes the window title and button design.
   	 * Shows the relevant screen
   	 * Only Manager can see this button
   	 */
    @FXML
    void employeesClick(ActionEvent event) {
    	menuStage.setTitle("ICM -> Employees");
    	sceneManager("EmployeesPane", btnEmployees);
    }
    /**
	 * When click on Profile button. Changes the window title and button design.
	 * Shows the relevant screen
	 */
    @FXML
    void profileClick(ActionEvent event) {   	 
        try {
        	AnchorPane newLoadedPane; 
			newLoadedPane =  FXMLLoader.load(getClass().getResource("/boundary/guifiles/ProfilePane.fxml"));
			apCenterContent.getChildren().clear();
		    apCenterContent.getChildren().add(newLoadedPane);
		} catch (IOException ex) {
			Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
	/**
	 * Change the view to the relevant view for the button clicked 
	 *
	 * @param fxmlName The name of the fxml file to load.
	 * @param button The button clicked by user.
	 */
	private void sceneManager(String fxmlName, Button button) {
		buttonStyle(button);
		try 
        {
            AnchorPane newLoadedPane;  
            newLoadedPane =  FXMLLoader.load(getClass().getResource("/boundary/guifiles/" + fxmlName + ".fxml"));
            apCenterContent.getChildren().clear();
            apCenterContent.getChildren().add(newLoadedPane);
        }
        catch (IOException ex) {
            Logger.getLogger(EnterController.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	/**
	 * Changes the style of button when pressed
	 * 
	 * @param button The pressed button
	 */
	private void buttonStyle(Button button) {
		if(button.equals(btnTemp)) return;
		button.getStyleClass().add("btnClicked");
		btnTemp.getStyleClass().clear();
		btnTemp.getStyleClass().add("button");
		btnTemp = button;
	}
	/**
	 * Called when pressed on Logout.
	 * Shows an Alert, if user decided to logout - sends logout request to server, removes user from loggedIn users, and shows login screen
	 * if user cancel logout: just shows the same screen before clicked on Logout
	 */
	private void logoutUser() {
		ColorAdjust adj = new ColorAdjust(0, -0.2, -0.1, 0);
        GaussianBlur blur = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        bpRoot.setEffect(adj);
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.getButtonTypes().remove(ButtonType.OK);
    	alert.getButtonTypes().add(ButtonType.YES);
    	alert.setTitle("Exit ICM");
    	alert.setHeaderText("You are about to exit ICM");
    	alert.setContentText("Are you sure you want to exit?");
    	alert.showAndWait();
    	if (alert.getResult() == ButtonType.YES) {
    		ObjectManager msg = new ObjectManager(user, MsgEnum.LOGOUT);
    		ConnectionController.getClient().handleMessageFromClientUI(msg);
    		PauseTransition pause = new PauseTransition(Duration.seconds(2));
    		pause.setOnFinished(e -> {
    			try {
    				GuiManager.guiLoader("Enter", new Stage());
    				menuStage.close();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    		});
    		pause.play();
    	}else {
    		bpRoot.setEffect(null);
    	}
		
	}
	/**
	 * Shows menu that matches the user role (permission)
	 * 
	 * @param role The user's role
	 */
	private void permissionCheck(String role) {
		switch(role) {
		case "Manager":
			btnReports.setVisible(true);
			spReports.setVisible(true);
			btnEmployees.setVisible(true);
			spEmployees.setVisible(true);
			break;
		case "Inspector":
			btnReports.setVisible(false);
			spReports.setVisible(false);
			btnEmployees.setVisible(false);
			spEmployees.setVisible(false);
			break;
		case "Review Leader":
			btnReports.setVisible(false);
			spReports.setVisible(false);
			btnEmployees.setVisible(false);
			spEmployees.setVisible(false);
			break;
		case "Review Member":
			btnReports.setVisible(false);
			spReports.setVisible(false);
			btnEmployees.setVisible(false);
			spEmployees.setVisible(false);
			break;
		case "Information Engineer":
			btnReports.setVisible(false);
			spReports.setVisible(false);
			btnEmployees.setVisible(false);
			spEmployees.setVisible(false);
			break;
		default: //Lecturer/Worker/Student
			btnProcesses.setVisible(false);
			spProcesses.setVisible(false);
			btnReports.setVisible(false);
			spReports.setVisible(false);
			btnEmployees.setVisible(false);
			spEmployees.setVisible(false);
			break;
		}
		
	}

}
