package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import boundary.GuiManager;
import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.ActionsNeeded;
import entity.Request;
import entity.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Controller for Processes management by the Inspector.
 * 
 * @author Ron
 */
public class ProcessInspectorController implements Initializable {
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tabWaitingActions;
	@FXML
	private TableView<ActionsNeeded> tblActionsNeeded;
	@FXML
	private TableColumn<ActionsNeeded, String> col_requestId;
	@FXML
	private TableColumn<ActionsNeeded, String> col_chargeId;
	@FXML
	private TableColumn<ActionsNeeded, String> col_stage;
	@FXML
	private TableColumn<ActionsNeeded, String> col_actions;
	@FXML
	private Label lblPickMsg;
	@FXML
	private AnchorPane apAppoint;
	@FXML
	private AnchorPane apAppointExecutor;
	@FXML
	private ComboBox<String> cmbEmployees;
	@FXML
	private Label lblSubTitle;
	@FXML
	private Button btnAppoint;
	@FXML
	private Button btnAppointExecutor;
	@FXML
	private Label lblId;
	@FXML
	private Hyperlink hlReplace;
	@FXML
	private AnchorPane apPick;
	@FXML
	private Label lblError;
	@FXML
	private Label lblGreat;
	@FXML
	private Pane pError;
	@FXML
	private AnchorPane apTimeApproval;
	@FXML
	private AnchorPane apLoading;
	@FXML
	private AnchorPane apClose;
	@FXML
	private Label lblSubTitleTime;
	@FXML
	private Button btnApproveTime;
	@FXML
	private Button btnRejectTime;
	@FXML
	private Pane paneTime;
	@FXML
	private Label lblTime;
	@FXML
	private Label lblReason;
	@FXML
	private TextArea taReason;
	
	//tab requests
	  @FXML
	  private Tab tabRequests;

	  @FXML
	  private TableView<Request> tblRequest;

	  @FXML
	  private TableColumn<Request, String> colIDtab2;

	  @FXML
	  private TableColumn<Request, String> colStageTab2;

	  @FXML
	  private TableColumn<Request, String> colStatusTab2;
	  
	  @FXML
	  private TableColumn<Request,Request> colOptions;

	  @FXML
	  private Button btnFreeze;

	  
	private Request selectedRequest = new Request();  
  private static ArrayList<Request> requestList = new ArrayList<Request>(); 
  private ObservableList<Request> oblRequests = FXCollections.observableArrayList();
    
	private ClientConnector client = ConnectionController.getClient();
	private static ArrayList<ActionsNeeded> arralistOfActions = null;
	private static ArrayList<User> arralistOfEmployees = null;
	private static ArrayList<String> timeReq = null;
	private static ObservableList<ActionsNeeded> List = FXCollections.observableArrayList();
	private boolean empListView = false;
	private ActionsNeeded action;
	
	
	public static void setRequestList(ArrayList<Request> list) {
		requestList = list;
	}

	/**
	 * Set arrayListofActions with data from DB, in order to populate the actions table
	 * 
	 * @param array The ArrayList from database
	 */
	public static void setListOfActions(ArrayList<ActionsNeeded> array) {
		arralistOfActions = new ArrayList<>(array);
	}

	/**
	 * Set arrayListofEmployees with data from DB, in order to populate the
	 * employees combo box.
	 * 
	 * @param array The ArrayList from database
	 */
	public static void setListOfEmployees(ArrayList<User> array) { // Set the list of employees received from server
		arralistOfEmployees = new ArrayList<>(array);
	}
	/**
	 * Set timeReq array with data from DB, in order to get time request data and show to Inspector
	 * 
	 * @param array The ArrayList from database
	 */
	public static void setTimeReq(ArrayList<String> array) {
		timeReq = new ArrayList<>(array);
	}
	/**
	 * Initializes the controller with default actions
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(LoginController.getLoggedUser().getRole().equals("Manager")) tabPane.getTabs().remove(tabWaitingActions); //Manager View
		apLoading.setVisible(false);
		lblGreat.setVisible(false);
		clearScreen();
		ObjectManager viewActions = new ObjectManager(arralistOfActions, MsgEnum.VIEW_ACTIONS);
		ConnectionController.getClient().handleMessageFromClientUI(viewActions);
		col_requestId.setCellValueFactory(new PropertyValueFactory<>("idrequest"));
		col_chargeId.setCellValueFactory(new PropertyValueFactory<>("idCharge"));
		col_stage.setCellValueFactory(new PropertyValueFactory<>("stage"));
		col_actions.setCellValueFactory(new PropertyValueFactory<>("actionsNeeded"));
		try {
			Thread.sleep(1000);
			if (arralistOfActions.isEmpty()) {
				tblActionsNeeded.setVisible(false);
				lblGreat.setVisible(true);
			}
			else {
				List = FXCollections.observableArrayList(arralistOfActions);
				tblActionsNeeded.setItems(List);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * When click on row from table, shows relevant options by the ActionNeeded.
	 * 
	 * @param event
	 */
	@FXML
	private void onItemClick(MouseEvent event) {
		if(tblActionsNeeded.getSelectionModel().isEmpty()) return; //if nothing selected
		action = tblActionsNeeded.getSelectionModel().getSelectedItem();
		String selected = action.getActionsNeeded();
		clearScreen();
		if (selected.equals("Evaluator Appointment")) { // if needs to appoint Evaluator
			if (action.getIdCharge().equals("None") || action.getIdCharge().equals(" ")) { // if no automatically Evaluator appointed
				lblSubTitle.setText("System Charge Does Not Exist, Appoint Evaluator Below:");
				lblId.setVisible(false);
				hlReplace.setVisible(false);
				fillCmbEmployees();
			} else {
				lblSubTitle.setText("System has automaticly appointed Evaluator ID:");
				lblId.setVisible(true);
				hlReplace.setVisible(true);
				lblId.setText(action.getIdCharge());
			}
			apAppoint.setVisible(true);
		}
		if (selected.equals("Executor Appointment")) { // if needs to appoint Executor
			fillCmbEmployees();
			apAppointExecutor.setVisible(true);
		}
		if (selected.equals("Time Approval")) { // if needs approve time
			apTimeApproval.setVisible(true);
			lblSubTitleTime.setText("Time For Stage Request");
			showTimeRequest();
			lblReason.setVisible(false);
			taReason.setVisible(false);
		}
		if (selected.equals("Extend Approval")) { // if needs to approve time extend
			apTimeApproval.setVisible(true);
			lblSubTitleTime.setText("Time Extend Request");
			showTimeRequest();
			lblReason.setVisible(true);
			taReason.setVisible(true);
		}
		if (selected.equals("Close Request")) { // if needs to approve time extend
			apClose.setVisible(true);
		}
		lblPickMsg.setVisible(false);
	}

	/**
	 * When clicked on change hyperLink in order to replace the evaluator, it shows
	 * combo box with employees to appoint.
	 * 
	 * @param event
	 */
	@FXML
	private void onChangeAutoEvaluatorClick(ActionEvent event) { // When click on replace the automatic appointed
																	// evaluator
		fillCmbEmployees();
		lblId.setVisible(false);
		hlReplace.setVisible(false);
		lblSubTitle.setText("Pick new Evaluator from the list below:");
	}

	/**
	 * When click on Approve Executor button: sends the selected employee from combo
	 * box to the server and inserts in the relevant request_handeling
	 * 
	 * @param event
	 */
	@FXML
	private void onApproveExecutorClick(ActionEvent event) {
		String empSelectedID = "";
		if (cmbEmployees.getSelectionModel().isEmpty()) {
			GuiManager.showError(lblError, pError, "You must pick an employee first");
		} else {
			empSelectedID = arralistOfEmployees.get(cmbEmployees.getSelectionModel().getSelectedIndex()).getIdUser();
			ObjectManager msg = new ObjectManager(empSelectedID, action, MsgEnum.APPOINT_STAGE_CHARGE);
			client.handleMessageFromClientUI(msg);

			try {
				Thread.sleep(500);
				tblActionsNeeded.getItems().remove(action); // remove the line from table.
				GuiManager.showSuccess(lblError, pError, "Employee: " + empSelectedID + " Is Now Executor"); //show success message
				clearScreen();
				lblPickMsg.setVisible(true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * When click on Approve Evaluator button: IF decides to approve the automatic
	 * appoint: sends the employee's id received to the server and inserts in the
	 * relevant request_handeling IF decides to change the automatic appoint: sends
	 * the selected employee from combo box to the server and inserts in the
	 * relevant request_handeling
	 * 
	 * @param event
	 */
	@FXML
	private void onApproveEvaluatorClick(ActionEvent event) {
		String empSelectedID = "";
		if (empListView) {
			if (cmbEmployees.getSelectionModel().isEmpty()) {
				GuiManager.showError(lblError, pError, "You must pick an employee first"); // show error
			} else {
				empSelectedID = arralistOfEmployees.get(cmbEmployees.getSelectionModel().getSelectedIndex())
						.getIdUser();
			}
		} else {
			empSelectedID = action.getIdCharge();
		}
		if (empSelectedID != "") { // if employee selected
			// send new evaluator id to server
			ObjectManager msg = new ObjectManager(empSelectedID, action, MsgEnum.APPOINT_STAGE_CHARGE);
			client.handleMessageFromClientUI(msg);

			try {
				Thread.sleep(500);
				tblActionsNeeded.getItems().remove(action); // remove the line from table.
				GuiManager.showSuccess(lblError, pError, "Employee: " + empSelectedID + " Is Now Evaluator"); //show success message																				// message
				clearScreen();
				lblPickMsg.setVisible(true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * When time request approved by inspector
	 * @param event
	 */
	@FXML
	private void onRejectTimeClick(ActionEvent event) { // When click on reject time for time request
		ObjectManager msg = new ObjectManager(timeReq, action, MsgEnum.REJECT_TIME); //object to send to server
		client.handleMessageFromClientUI(msg);
		try {
			Thread.sleep(500);
			tblActionsNeeded.getItems().remove(action); // remove the line from table.
			GuiManager.showSuccess(lblError, pError, "Time Request Rejected Successfuly!"); //show success message
			Thread.sleep(200);
			clearScreen();
			lblPickMsg.setVisible(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * When time request rejected by inspector
	 * @param event
	 */
	@FXML
	private void onApproveTimeClick(ActionEvent event) { // When click on approve time for time request
		ObjectManager msg = new ObjectManager(timeReq, action, MsgEnum.APPROVE_TIME); //object to send to server
		client.handleMessageFromClientUI(msg);
		try {
			Thread.sleep(500);
			tblActionsNeeded.getItems().remove(action); // remove the line from table.
			GuiManager.showSuccess(lblError, pError, "Time Updated!"); //show success message																				// message
			clearScreen();
			lblPickMsg.setVisible(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Fills the Employees combo box with workers
	 */
	private void fillCmbEmployees() {
		empListView = true;
		ObjectManager viewEmployees = new ObjectManager(arralistOfEmployees, MsgEnum.VIEW_EMPLOYEES_TO_APPOINT);
		ConnectionController.getClient().handleMessageFromClientUI(viewEmployees);
		ObservableList<String> empList;
		List<String> emp = new ArrayList<String>();

		try {
			Thread.sleep(1500);
			for (User u : arralistOfEmployees) {
				emp.add(u.getFirstName() + " " + u.getLastName());
			}
			empList = FXCollections.observableList(emp);
			cmbEmployees.getItems().clear();
			cmbEmployees.setItems(empList);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		apPick.setVisible(true);
	}
	/**
	 * Task run out of javafx thread and shows the time request from DB.
	 */
	private void showTimeRequest() {
		timeReq = new ArrayList<String>();
		Task<ArrayList<String>> task = new Task<ArrayList<String>>() {
	        @Override
	        public ArrayList<String> call() throws InterruptedException {
	        	ObjectManager msg = new ObjectManager(action, MsgEnum.VIEW_TIME); //getting the time requested from DB
	    		client.handleMessageFromClientUI(msg);
	       	   	while(timeReq.isEmpty())
	       	   		Thread.sleep(200);
	       	   	return timeReq;
	        }
	    };
	    task.setOnSucceeded(e -> {
	    	ArrayList<String> result = task.getValue();
	    	lblTime.setText(result.get(1) +" Days");
	    	taReason.setText(result.get(2));
	    	paneTime.setVisible(true);
	    	apLoading.setVisible(false);
	    });
	    task.setOnRunning(event -> {
	    	apLoading.setVisible(true);
	    }); 
	    new Thread(task).start();
	}
	/**
	 * Hide irrelevant AnchorPanes and Labels
	 */
	private void clearScreen() {
		empListView = false;
		apPick.setVisible(false);
		apAppoint.setVisible(false);
		apAppointExecutor.setVisible(false);
		apTimeApproval.setVisible(false);
		apClose.setVisible(false);
	}
	
	//tab 2 functions
		/**
		 * this function is called when freeze button is clicked. 
		 * it changes the status of the request to frozen
		 * @param event
		 */
		  @FXML
		    void clickFreeze(ActionEvent event) {
			  if(LoginController.getLoggedUser().getRole().equals("Manager")) {
				  	ObjectManager msg = new ObjectManager(selectedRequest.getIdReq(), MsgEnum.UNFREEZE_REQUEST);
				  	client.handleMessageFromClientUI(msg);
				  	try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				  	
				  	selectedRequest.setStatus("opened");
				  	for(Request r : oblRequests) {
				  		if(r.getIdReq().equals(selectedRequest.getIdReq())){
							oblRequests.remove(r);
							break;
				  		}
				  	}
				  	
				  	oblRequests.add(selectedRequest);
				  	tblRequest.setItems(oblRequests);
				  	btnFreeze.setVisible(false);
			  }else {
				  ObjectManager msg = new ObjectManager(selectedRequest.getIdReq(), MsgEnum.FREEZE_REQUEST);
				  	client.handleMessageFromClientUI(msg);
				  	try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				  	
				  	selectedRequest.setStatus("frozen");
				  	for(Request r : oblRequests) {
				  		if(r.getIdReq().equals(selectedRequest.getIdReq())){
							oblRequests.remove(r);
							break;
				  		}
				  	}
				  	
				  	oblRequests.add(selectedRequest);
				  	tblRequest.setItems(oblRequests);
				  	btnFreeze.setVisible(false);
			  }
				  
				
			  	
		    }

		  	/**
		  	 * when request is picked this function determine if the request
		  	 * is eligible for freezing button, therefore: non frozen requests, only opened requests, 
		  	 * and requests that arent in closing stage
		  	 * @param event
		  	 */
		    @FXML
		    void clickRequestsTbl(MouseEvent event) {
		    	btnFreeze.setVisible(false);
		    	
		    	selectedRequest = tblRequest.getSelectionModel().getSelectedItem();
		    	
		    	if(selectedRequest.getStatus().equals("opened")&&!selectedRequest.getCurrentStage().equals("Closing")) {
		    		if(LoginController.getLoggedUser().getRole().equals("Manager")) {
		    			btnFreeze.setText("UnFreeze Selected");
		    			btnFreeze.setVisible(false);
		    		}
		    		else btnFreeze.setVisible(true);
		    	}
		    	if(selectedRequest.getStatus().equals("frozen")) {
		    		if(LoginController.getLoggedUser().getRole().equals("Manager")) {
		    			btnFreeze.setText("UnFreeze Selected");
		    			btnFreeze.setVisible(true);
		    		}
		    	}
		    }
	/**
	 * this function initialize the requests tab with all the available requests
	 * @param event
	 */
		    @FXML
		    void loadRequestTable(Event event) {
		    	
		    	ObjectManager msg = new ObjectManager("", MsgEnum.REQUEST_FOR_INSPECTOR);
		    	client.handleMessageFromClientUI(msg);
		    	
		    	try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
		    	
		    	colIDtab2.setCellValueFactory(new PropertyValueFactory<>("idReq"));
		    	colStageTab2.setCellValueFactory(new PropertyValueFactory<>("currentStage"));
		    	colStatusTab2.setCellValueFactory(new PropertyValueFactory<>("status"));
		    	colOptions.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		    	colOptions.setCellFactory(param -> new TableCell<Request, Request>() { 
    				@FXML
    			    private final Button btnView = new Button("View Request");

    			    protected void updateItem(Request req, boolean empty) {
    			        super.updateItem(req, empty);

    			        if (req == null) {
    			            setGraphic(null);
    			            return;
    			        }
    			        setGraphic(btnView);    			        
    			        btnView.setOnAction(
    			            event -> {
    			            	Platform.runLater(new Runnable() {
    			            	    @Override
    			            	    public void run() {
    			            	    	try {
    										GuiManager.popUpLoader("RequestView", req.getIdReq());
    									} catch (IOException e) {e.printStackTrace();}
    			            	    }
    			            	});
    			            }
    			        );
    			    }
    			});
		    	oblRequests = FXCollections.observableArrayList(requestList);
		    	tblRequest.setItems(oblRequests);
		    	//for commit
		    	
		    }
		    @FXML
		    void onViewRequestClick(ActionEvent event) {
		    	Platform.runLater(new Runnable() {
            	    @Override
            	    public void run() {
            	    	try {
							GuiManager.popUpLoader("RequestView", action.getIdrequest());
						} catch (IOException e) {e.printStackTrace();}
            	    }
            	});
		    }
		    @FXML
		    void onCloseRequestClick(ActionEvent event) {
		    	ObjectManager msg = new ObjectManager(action.getIdrequest(), action, MsgEnum.CLOSE_REQUEST); //object to send to server
				client.handleMessageFromClientUI(msg);
				try {
					Thread.sleep(500);
					tblActionsNeeded.getItems().remove(action); // remove the line from table.
					GuiManager.showSuccess(lblError, pError, "Request Closed Successfuly!"); //show success message																				// message
					clearScreen();
					lblPickMsg.setVisible(true);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
}
