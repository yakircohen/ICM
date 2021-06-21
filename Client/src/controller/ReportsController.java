package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entity.ActivityReport;
import entity.PerformanceBehindReport;
import entity.PerformanceReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReportsController implements Initializable {
	
	@FXML
    private Label lblPageName;

    @FXML
    private Label lblPageNavigationName;

    @FXML
    private ComboBox<String> cmbReports;

    @FXML
    private Button btnGenerateReport;

    @FXML
    private AnchorPane duration;
    
    @FXML
    private Label lblchoice;
    
    @FXML
    private TableView<ActivityReport> tblActivityReports;

    @FXML
    private TableColumn<ActivityReport, String> colStartDate;

    @FXML
    private TableColumn<ActivityReport, String> colEndDate;
    
    @FXML
    private DatePicker dateStart;

    @FXML
    private DatePicker dateEnd;
    
    @FXML
    private Label lbldates;
    

    @FXML
    void generateNewReport(ActionEvent event) {
    	lbldates.setVisible(false);
    	if(cmbReports.getSelectionModel().isEmpty()) 
    	{
    		lblchoice.setVisible(true);
    	}
    	else 
    	{
    		lblchoice.setVisible(false);
    		if(cmbReports.getSelectionModel().getSelectedItem().equals("Activity report")) 
    		{
    			if(dateStart.getValue()==null || dateEnd.getValue()==null)
    			{
    				lbldates.setVisible(true);
    			}
    			else if(dateStart.getValue().compareTo(dateEnd.getValue())>0)
    			{
    				lbldates.setVisible(true);
    			}
    			else
    			{
    				Stage stage= new Stage();
        			FXMLLoader loader=new FXMLLoader();
        			try {
        				ActivityReportPopupController.setStartDate(dateStart.getValue());
        				ActivityReportPopupController.setEndDate(dateEnd.getValue());
        				loader.setLocation(ActivityReportPopupController.class.getResource("/boundary/guifiles/ActivityReportPopup.fxml"));
        				Pane root=loader.load();
        				Scene scene=new Scene(root);
        				stage.setTitle("Activity report");
        				stage.setScene(scene);
        				stage.show();
        			}catch (IOException e) {
        				e.printStackTrace();
        			}
    			}
    		}
    		else if(cmbReports.getSelectionModel().getSelectedItem().equals("Performance report")) 
    		{
    			Stage stage= new Stage();
    			FXMLLoader loader=new FXMLLoader();
    			try {
    				loader.setLocation(PerformanceReportPopupController.class.getResource("/boundary/guifiles/PerformanceReportPopup.fxml"));
    				Pane root=loader.load();
    				Scene scene=new Scene(root);
    				stage.setTitle("Performance report");
    				stage.setScene(scene);
    				stage.show();
    			}catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		else if(cmbReports.getSelectionModel().getSelectedItem().equals("Performance behind report"))
    		{
    			Stage stage= new Stage();
    			FXMLLoader loader=new FXMLLoader();
    			try {
    				loader.setLocation(PerformanceBehindReportPopupController.class.getResource("/boundary/guifiles/PerformanceBehindReportPopup.fxml"));
    				Pane root=loader.load();
    				Scene scene=new Scene(root);
    				stage.setTitle("Performance behind report");
    				stage.setScene(scene);
    				stage.show();
    			}catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
    
    @FXML
    void onSelectedItem(ActionEvent event) {
    	if(cmbReports.getSelectionModel().getSelectedItem().equals("Activity report")) {
    		duration.setVisible(true);
    	}else {
    		duration.setVisible(false);
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		duration.setVisible(false);
		List<String> reportsList= new ArrayList<String>();
		reportsList.add("Activity report");
		reportsList.add("Performance report");
		reportsList.add("Performance behind report");
		ObservableList<String> obList=FXCollections.observableList(reportsList);
		cmbReports.getItems().clear();
		cmbReports.setItems(obList);
		lblchoice.setTextFill(Color.RED);
		lblchoice.setVisible(false);
		lbldates.setTextFill(Color.RED);
		lbldates.setVisible(false);
	}
}
