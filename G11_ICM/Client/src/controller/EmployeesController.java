package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import boundary.GuiManager;
import common.MsgEnum;
import common.ObjectManager;
import entity.Systems;
import entity.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class EmployeesController implements Initializable{
	/**
	 * Tables and Objects for department attach view
	 */
	@FXML
	private TableView<User> tblEmployees;
	@FXML
	private TableView<User> tblToAdd;
	@FXML
	private TableColumn<User, String> col_id;
	@FXML
	private TableColumn<User, String> col_idToAdd;
	@FXML
	private TableColumn<User, String> col_nameToAdd;
	@FXML
	private TableColumn<User, String> col_name;
	@FXML
	private TableColumn<User, String> col_role;
	@FXML
	private TableColumn<User, String> col_lastName;
	@FXML
	private TableColumn<User, String> col_lastNameToAdd;
	@FXML
	private TableColumn<User, User> col_optionsToAdd;
	@FXML
	private TableColumn<User, User> col_options;
	@FXML
	private Tab tabPermissions;
	@FXML
	private Tab tabAttach;
	@FXML
	private Pane pMsg;
	@FXML
	private Label lblMsg;
	/**
	 * Tables and Objects for permissions view
	 */
	@FXML
	private TableView<User> tblEmployeesWithRoles;
	@FXML
	private TableColumn<User, String> col_empID;
	@FXML
	private TableColumn<User, String> col_empFirstName;
	@FXML
	private TableColumn<User, String> col_empLastName;
	@FXML
	private TableColumn<User, String> col_empEmail;
	@FXML
	private TableColumn<User, String> col_empRole;

	@FXML
	private AnchorPane apLoading;
	@FXML
    private TitledPane paneInSide;
	@FXML
    private TitledPane paneOutSide;
	@FXML
    private TitledPane panePermission;
	@FXML
    private Label lblManID;
    @FXML
    private Label lblManName;
    @FXML
    private Label lblManLast;
    @FXML
    private Label lblManEmail;
    @FXML
    private Hyperlink hlReplaceIns;
    @FXML
    private Label lblInsID;
    @FXML
    private Label lblInsName;
    @FXML
    private Label lblInsLast;
    @FXML
    private Label lblInsEmail;
    @FXML
    private ComboBox<String> cmbSelectEmp;
    @FXML
    private Button btnAppointEmp;
    @FXML
    private Hyperlink hlCancel;
    @FXML
    private AnchorPane apReplacePer;
    @FXML
    private TableView<Systems> tblSystems;
    @FXML
    private TableColumn<Systems, String> col_SysName;
    @FXML
    private TableColumn<Systems, String> col_Sys_ID;
    @FXML
    private TableColumn<Systems, String> col_Sys_Name;
    @FXML
    private TableColumn<Systems, String> col_Sys_Last;
    @FXML
    private TableColumn<Systems, Systems> col_Sys_Options;
    @FXML
    private TableView<User> tblReview;
    @FXML
    private TableColumn<User, String> col_RoleName;
    @FXML
    private TableColumn<User, String> col_Role_ID;
    @FXML
    private TableColumn<User, String> col_Role_Name;
    @FXML
    private TableColumn<User, String> col_Role_Lase;
    @FXML
    private TableColumn<User, User> col_Role_Options;
	
	public static ArrayList<User> arralistOfEmployees = new ArrayList<User>();
	public static ArrayList<User> arralistOfEmp = new ArrayList<User>(); //for employees to be appointed by Manager
	public static ArrayList<Systems> arralistOfSystems = new ArrayList<Systems>();
	private ArrayList<User> inDepartment;
	private ArrayList<User> outSideDepartment;
	private ObservableList<User> List = null;
	private ObservableList<Systems> ListSystems = null;
	private User selectedUser;
	private String type;
	private Systems selectedSystem;
	private User ins;
	public static void setListOfEmployees(ArrayList<User> array) {
		arralistOfEmployees = new ArrayList<>(array);
    }
	
	public static void setListOfSystems(ArrayList<Systems> array) {
		arralistOfSystems = new ArrayList<>(array);
    }
	public static void setListOfEmpTo(ArrayList<User> array) {
		arralistOfEmp = new ArrayList<>(array);
	}
	
	@FXML
	private void onTabAttachSelect(Event ev) {
		if (tabAttach.isSelected()) {
			paneOutSide.setVisible(false);
			paneInSide.setVisible(false);
			arralistOfEmployees.clear();
			inDepartment = new ArrayList<User>();
	   		outSideDepartment = new ArrayList<User>();   		
	    	//Task to get data from DB
	   		Task<ArrayList<User>> task = new Task<ArrayList<User>>() {
	   			@Override
	   			public ArrayList<User> call() throws InterruptedException {
	   				ObjectManager userWithRoles = new ObjectManager(arralistOfEmployees, MsgEnum.VIEW_EMPLOYEES); //updating departments for employee changed
	   				ConnectionController.getClient().handleMessageFromClientUI(userWithRoles);
	   				while(arralistOfEmployees.isEmpty())
	   					Thread.sleep(800);
	   				return arralistOfEmployees;
	   			}
	   		};
	   		task.setOnSucceeded(e -> {
	    	    	ArrayList<User> result = task.getValue();
	    	    	apLoading.setVisible(false);
	    	    	col_id.setCellValueFactory(new PropertyValueFactory<>("idUser"));
	    	   		col_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    	   		col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName")); 	//type of employee. Worker/Information Engineer/Manager
	    	   		col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
	    	   		
	    	   		col_idToAdd.setCellValueFactory(new PropertyValueFactory<>("idUser"));
	    	   		col_nameToAdd.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    	   		col_lastNameToAdd.setCellValueFactory(new PropertyValueFactory<>("lastName"));
	    	   		col_options.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	    	   		col_options.setCellFactory(param -> new TableCell<User, User>() { 
	    	   			@FXML
	    	   		    private final Button btn = new Button("Remove From Department");

	    	   		    protected void updateItem(User user, boolean empty) {
	    	   		        super.updateItem(user, empty);

	    	   		        if (user == null) {
	    	   		            setGraphic(null);
	    	   		            return;
	    	   		        }

	    	   		        setGraphic(btn);
	    	   		        btn.setOnAction(
	    	   		            event -> {
	    	   		            	String role = user.getRole();
	    	   		            	if(role.equals("Inspector") || role.equals("Manager") || role.equals("Review Leader") || role.equals("Review Member")) {
	    	   		            		Alert alert = new Alert(AlertType.INFORMATION);
	    	   		                	alert.setTitle("Unable to remove "+ role);
	    	   		                	alert.setHeaderText("You are trying to remove "+role + " Employee");
	    	   		                	alert.setContentText("First, remove the role of this employee by choosing the Permission tab, and then you will be able to remove him.");
	    	   		                	alert.showAndWait();
	    	   		            	}else {
	    	   		            		tblToAdd.getItems().add(user);
	    	   			            	getTableView().getItems().remove(user);
	    	   		            	}
	    	   		            }
	    	   		        );
	    	   		    }
	    	   		});
	    	   		
	    	   		col_optionsToAdd.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
	    	   		col_optionsToAdd.setCellFactory(param -> new TableCell<User, User>() { 
	    	   			@FXML
	    	   		    private final Button btn = new Button("Add To Department");

	    	   		    protected void updateItem(User user, boolean empty) {
	    	   		        super.updateItem(user, empty);

	    	   		        if (user == null) {
	    	   		            setGraphic(null);
	    	   		            return;
	    	   		        }
	    	   		        setGraphic(btn);
	    	   		        btn.setOnAction(
	    	   		            event -> {
	    	   		            	tblEmployees.getItems().add(user);
	    	   		            	getTableView().getItems().remove(user);
	    	   		            }
	    	   		        );
	    	   		    }
	    	   		});    	   
	    	    	for(User u : result) {
		   				String dep = u.getDepartment();
		   				if(dep.equals("Information Technology")) 
		   					inDepartment.add(u);
		   				else outSideDepartment.add(u);
		   	    	}
		   			List = FXCollections.observableArrayList(inDepartment);
		   			tblEmployees.setItems(List);
		   			List = FXCollections.observableArrayList(outSideDepartment);
		   			tblToAdd.setItems(List);
		   			paneInSide.setVisible(true);
	    	    	paneOutSide.setVisible(true);
	    	    });
	    	    task.setOnFailed(event -> {
	    	    	paneInSide.setVisible(false);
	    	    	paneOutSide.setVisible(false);
	    	    });     
	    	    task.setOnRunning(event -> {
	    	    	apLoading.setVisible(true);
	    	    }); 
	    	    new Thread(task).start();
	    }
	}
	/**
	 * Fills the Employees combo box with workers
	 */
	private void fillCmbEmployees() {
		ObjectManager viewEmployees = new ObjectManager(arralistOfEmp, MsgEnum.VIEW_EMPLOYEES_TO_APPOINT_MANAGER);
		ConnectionController.getClient().handleMessageFromClientUI(viewEmployees);
		ObservableList<String> empList;
		List<String> emp = new ArrayList<String>();

		try {
			Thread.sleep(1000);
			for (User u : arralistOfEmp) {
				emp.add(u.getFirstName() + " " + u.getLastName());
			}
			empList = FXCollections.observableList(emp);
			cmbSelectEmp.getItems().clear();
			cmbSelectEmp.setItems(empList);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		apReplacePer.setVisible(true);
	}
	/**
	 * When click on Permission tab, loads view with user permissions and options
	 * @param ev
	 */
	@FXML
    private void onTabPermissionsSelect(Event ev) {
       if (tabPermissions.isSelected()) {
    	   panePermission.setVisible(false);
    	   arralistOfEmployees.clear();
    	   col_empID.setCellValueFactory(new PropertyValueFactory<>("idUser"));
    	   col_empFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    	   col_empLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    	   col_empEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    	   col_empRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    	   
    	   //Build Review Team Table
    	   col_Role_ID.setCellValueFactory(new PropertyValueFactory<>("idUser"));
    	   col_Role_Name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    	   col_Role_Lase.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    	   col_RoleName.setCellValueFactory(new PropertyValueFactory<>("role"));
    	   col_Role_Options.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    	   col_Role_Options.setCellFactory(param -> new TableCell<User, User>() { 
	   			@FXML
	   		    private final Button btn = new Button("Replace");

	   		    protected void updateItem(User user, boolean empty) {
	   		        super.updateItem(user, empty);

	   		        if (user == null) {
	   		            setGraphic(null);
	   		            return;
	   		        }

	   		        setGraphic(btn);
	   		        btn.setOnAction(
	   		            event -> {
	   		            	selectedUser = user; //keep for later
	   		            	fillCmbEmployees();
	   		            	type = "User";
	   		            }
	   		        );
	   		    }
	   		});
    	   
    	   //Build System Charge Table
    	   col_SysName.setCellValueFactory(new PropertyValueFactory<>("systemName"));
    	   col_Sys_ID.setCellValueFactory(new PropertyValueFactory<>("iduser"));    	   
    	   col_Sys_Name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    	   col_Sys_Last.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    	   col_Sys_Options.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    	   col_Sys_Options.setCellFactory(param -> new TableCell<Systems, Systems>() { 
	   			@FXML
	   		    private final Button btn2 = new Button("Replace");

	   		    protected void updateItem(Systems system, boolean empty) {
	   		        super.updateItem(system, empty);

	   		        if (system == null) {
	   		            setGraphic(null);
	   		            return;
	   		        }

	   		        setGraphic(btn2);
	   		        btn2.setOnAction(
	   		            event -> {
	   		            	selectedSystem = system; //keep for later
	   		            	fillCmbEmployees();
	   		            	type = "System";
	   		            }
	   		        );
	   		    }
	   		});
    	   
    	   //Task to get data from DB
    	   Task<ArrayList<User>> task = new Task<ArrayList<User>>() {
    	        @Override
    	        public ArrayList<User> call() throws InterruptedException {
    	        	ObjectManager userWithRoles = new ObjectManager(arralistOfEmployees, MsgEnum.VIEW_EMPLOYEES_WITH_ROLES); //get employee with roles
    	       	   	ConnectionController.getClient().handleMessageFromClientUI(userWithRoles);
    	       	   	ObjectManager systemCharge = new ObjectManager(arralistOfSystems, MsgEnum.VIEW_SYSTEMS); //get systems with their charge
    	       	 	ConnectionController.getClient().handleMessageFromClientUI(systemCharge);
    	       	   	while(arralistOfEmployees.isEmpty())
    	       	   		Thread.sleep(700);
    	       	   	return arralistOfEmployees;
    	        }
    	    };
    	    task.setOnSucceeded(e -> {
    	    	ArrayList<User> result = task.getValue(); //arraylist of employees
    	    	ArrayList<User> reviewTeam = new ArrayList<User>(); //for review team table
    	    	for(User u : result) {
    	    		if(u.getRole().equals("Manager")) { //set Manager data
    	    			lblManID.setText(u.getIdUser());
    	    		    lblManName.setText(u.getFirstName());
    	    		    lblManLast.setText(u.getLastName());
    	    		    lblManEmail.setText(u.getEmail());
    	    		}
    	    		if(u.getRole().equals("Inspector")) { //set Inspector data
    	    			ins = u;
    	    			lblInsID.setText(u.getIdUser());
    	    		    lblInsName.setText(u.getFirstName());
    	    		    lblInsLast.setText(u.getLastName());
    	    		    lblInsEmail.setText(u.getEmail());
    	    		}
    	    		if(u.getRole().equals("Review Leader") || u.getRole().equals("Review Member")) { //set Review Team data
    	    			reviewTeam.add(u);
    	    		}
    	    	}
    	    	apLoading.setVisible(false);
    	    	List = FXCollections.observableArrayList(result);
    	    	tblEmployeesWithRoles.setItems(List);
    	    	List = FXCollections.observableArrayList(reviewTeam);
    	    	tblReview.setItems(List);
    	    	ListSystems = FXCollections.observableArrayList(arralistOfSystems);
    	    	tblSystems.setItems(ListSystems);
    	    	panePermission.setVisible(true);
    	    });
    	    task.setOnFailed(event -> {
    	    	panePermission.setVisible(false);
    	    });     
    	    task.setOnRunning(event -> {
    	    	apLoading.setVisible(true);
    	    }); 
    	    new Thread(task).start();
       	}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		arralistOfEmployees.clear();
		apReplacePer.setVisible(false);
		clearScreen();
	}
	/**
	 * 
	 * @param event
	 */
	@FXML
	void onAppointSelectedClick(ActionEvent event) {
		if(cmbSelectEmp.getSelectionModel().isEmpty()) 
			return;
		if(type.equals("User")) {
			if(selectedUser.getRole().equals("Review Leader")){
				String SelectedID = arralistOfEmp.get(cmbSelectEmp.getSelectionModel().getSelectedIndex()).getIdUser();
				ObjectManager update = new ObjectManager(SelectedID, selectedUser, MsgEnum.REPLACE_REVIEW_LEADER);
				ConnectionController.getClient().handleMessageFromClientUI(update);
			}
			if(selectedUser.getRole().equals("Review Member")){
				String SelectedID = arralistOfEmp.get(cmbSelectEmp.getSelectionModel().getSelectedIndex()).getIdUser();
				ObjectManager update = new ObjectManager(SelectedID, selectedUser, MsgEnum.REPLACE_REVIEW_MEMBER);
				ConnectionController.getClient().handleMessageFromClientUI(update);
			}
			if(selectedUser.getRole().equals("Inspector")){
				String SelectedID = arralistOfEmp.get(cmbSelectEmp.getSelectionModel().getSelectedIndex()).getIdUser();
				ObjectManager update = new ObjectManager(SelectedID, selectedUser, MsgEnum.REPLACE_INSPECTOR);
				ConnectionController.getClient().handleMessageFromClientUI(update);
			}
		}else {
			String SelectedID = arralistOfEmp.get(cmbSelectEmp.getSelectionModel().getSelectedIndex()).getIdUser();
			ObjectManager update = new ObjectManager(SelectedID, selectedSystem, MsgEnum.REPLACE_SYSTEM_CHARGE);
			ConnectionController.getClient().handleMessageFromClientUI(update);
		}
		btnAppointEmp.setVisible(false);
	}
	@FXML
	void onRepInspector(ActionEvent event) {
		selectedUser = ins;
		fillCmbEmployees();
	}
	/**
	 * 
	 * @param event
	 */
	@FXML
	void onCancelClick(ActionEvent event) {
		apReplacePer.setVisible(false);
	}
	/**
	 * Updates the department employees with changes.
	 * @param event When click on button
	 */
	@FXML
    private void onUpdateDepartmentClick(ActionEvent event) { 
		ArrayList<User> changed = new ArrayList<User>(); //contains all changes in department to perform
		//users inside department
		for(User u : inDepartment) {
			if(tblToAdd.getItems().contains(u)) { //check if employee removed from department
				u.setDepartment("");		//update this user department
				changed.add(u);
			}
		}
		//users outside department
		for(User u : outSideDepartment) {
			if(tblEmployees.getItems().contains(u)) { //check if employee added to department
				u.setDepartment("Information Technology");		//update this user department
				changed.add(u);
			}
		}
		if(changed.isEmpty()) { //if nothing changed
			GuiManager.showError(lblMsg, pMsg, "No Changes Detected");
		}else {
			ObjectManager updateDepartment = new ObjectManager(changed, MsgEnum.UPDADE_DEPARTMENT); //updating departments for employee changed
			ConnectionController.getClient().handleMessageFromClientUI(updateDepartment);
			GuiManager.showSuccess(lblMsg, pMsg, "Department Updated Successfuly!");
			
			for(User u : changed) {
				if(inDepartment.contains(u))inDepartment.remove(u);
				else inDepartment.add(u);
				if(outSideDepartment.contains(u)) outSideDepartment.remove(u);
				else outSideDepartment.add(u);
			}
		}
	}
	/**
	 * Hide all before stage starts
	 */
	private void clearScreen() {
		apLoading.setVisible(false);
		pMsg.setVisible(false);
		panePermission.setVisible(false);
		paneOutSide.setVisible(false);
		paneInSide.setVisible(false);
	}
}
