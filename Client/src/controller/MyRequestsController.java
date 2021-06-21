package controller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import boundary.GuiManager;
import common.ClientConnector;
import common.MsgEnum;
import common.ObjectManager;
import entity.Request;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class MyRequestsController implements Initializable{
	 @FXML
	    private Label lblPageName;

	    @FXML
	    private Label lblPageNavigationName;

	    @FXML
	    private TextField tbId;

	    @FXML
	    private Button btnSearch;

	    @FXML
	    private Button btnClear;

	    @FXML
	    private TableView<Request> requestTbl;

	    @FXML
	    private TableColumn<Request, String> idColumn;

	    @FXML
	    private TableColumn<Request, String> dateColumn;

	    @FXML
	    private TableColumn<Request, String> stageColumn;
	    @FXML
		private TableColumn<Request, Request> col_options;
	    @FXML
	    private Label lblStageInfo;
	    @FXML
	    private AnchorPane apLoading;
	    
	    //attributes
	    private static ArrayList<Request> rsNotStarted;
	    private static ArrayList<Request> rsStarted;
	    private ClientConnector client = ConnectionController.getClient();
	    private static ObservableList<Request> List = FXCollections.observableArrayList();
	    private ArrayList<Request> bothRS = new ArrayList<Request>();
	    
	    
	    /**
	     * clear the search text box
	     * @param event
	     */
	    @FXML
	    void clickClear(ActionEvent event) {
	    	tbId.clear();
	    }

	    /**
	     * search for request in the table.
	     * set the label accordingly, and jump to the required request in the table
	     * 
	     * possible inputs for the label:
	     * "You dont have any requests"
	     * "Empty input, please enter request ID"
	     * "You dont have such request"
	     * 
	     * 
	     * @param event
	     */
	    @FXML
	    void clickSearch(ActionEvent event) {
	    	
	    	String id = tbId.getText();
	    	
	    	//user dont have requests
	    	if(bothRS.isEmpty()) {
	    		
	    		lblStageInfo.setText("You dont have any requests");
	    		lblStageInfo.setVisible(true);
	    		return;
	    	}
	    	
	    	//input is empty
	    	if(id.isEmpty()) {
	    		
	    		lblStageInfo.setText("Empty input, please enter request ID");
	    		lblStageInfo.setVisible(true);
	    		return;
	    	}
	    	
	    	//searching for the request
	    	Request temp = null;
	    	for(Request rq : bothRS)
	    	{
	    		if(rq.getIdReq().equals(id)) {
	    			temp = rq;
	    			break;
	    		}
	    	}
	    	
	    	//if request wasnt found
	    	if(temp==null) {
	    		lblStageInfo.setText("You dont have such request");
	    		lblStageInfo.setVisible(true);
	    		return;
	    	}
	    	
	    	//jumping to the request in the table in case it exists
	    	lblStageInfo.setVisible(false);
	    	
	    	requestTbl.getSelectionModel().select(temp);
	    	requestTbl.requestFocus();
	    	requestTbl.scrollTo(temp);
	    	

	    }
	    
	    
	    /**
	     * sending request to the server, and filling the table with the results
	     */
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			lblStageInfo.setVisible(false);
			lblStageInfo.setTextFill(Color.RED);
			apLoading.setVisible(false);
			requestTbl.setVisible(false);
			Task<Void> task = new Task<Void>() {
    	        @Override
    	        public Void call() throws InterruptedException {
    	        	//sending id of user to server
    				ObjectManager requestsMsg1 = new ObjectManager(LoginController.getLoggedUser().getIdUser(), MsgEnum.GET_REQUESTS_BY_ID_STARTED);
    				client.handleMessageFromClientUI(requestsMsg1);
    				ObjectManager requestsMsg2 = new ObjectManager(LoginController.getLoggedUser().getIdUser(), MsgEnum.GET_REQUESTS_BY_ID_NOSTARTED);
    				client.handleMessageFromClientUI(requestsMsg2);
    				Thread.sleep(1000);
    				
    	       	   	return null;
    	        }
    	    };
    	    task.setOnSucceeded(e -> {
    	    	idColumn.setCellValueFactory(new PropertyValueFactory<>("idReq"));
    			dateColumn.setCellValueFactory(new PropertyValueFactory<>("stageDueDate"));
    			stageColumn.setCellValueFactory(new PropertyValueFactory<>("currentStage"));
    			
    			col_options.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    			col_options.setCellFactory(param -> new TableCell<Request, Request>() { 
    				@FXML
    			    private final Button btn = new Button("View");

    			    protected void updateItem(Request req, boolean empty) {
    			        super.updateItem(req, empty);

    			        if (req == null) {
    			            setGraphic(null);
    			            return;
    			        }

    			        setGraphic(btn);
    			        btn.setOnAction(
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
    	    	bothRS.addAll(rsStarted);
				bothRS.addAll(rsNotStarted);
				
				//the user does not have requests
				if(bothRS.isEmpty())
				{
					requestTbl.setPlaceholder(new Label("You dont have any requests"));
					apLoading.setVisible(false);
					return;
				}
				
				List = FXCollections.observableArrayList(bothRS);
				requestTbl.setItems(List);
				requestTbl.setVisible(true);
				apLoading.setVisible(false);
    	    });    
    	    task.setOnRunning(event -> {
    	    	apLoading.setVisible(true);
    	    }); 
    	    new Thread(task).start();
		}

		/**
		 * let other classes set the not started requests list
		 * @param requestarray
		 */
		
		public static void setRsNotStarted(ArrayList<Request> requestarray) {
			rsNotStarted =  requestarray;
		}
		
		/**
		 * let other classes set the started requests list
		 * @param requestarray
		 */
		public static void setRsStarted(ArrayList<Request> requestarray) {
			rsStarted = requestarray;
		}

		


}
