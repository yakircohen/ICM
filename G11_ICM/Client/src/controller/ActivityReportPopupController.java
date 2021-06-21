package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import entity.ActivityReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ActivityReportPopupController implements Initializable {
	 @FXML
	    private Label lblmedian;

	    @FXML
	    private Label lblStandardDiviation;

	    @FXML
	    private TableView<String> tblFrequencyDistribution;

	    @FXML
	    private TableColumn<String, String> colRange;

	    @FXML
	    private TableColumn<String, Integer> colTotalnRange;
	    
	    @FXML
	    private Label lblStartDate;

	    @FXML
	    private Label lblEndDate;

	    @FXML
	    private Button btnAddReport;
	    
	    private static LocalDate startDate;
	    
	    private static LocalDate endDate;
	    @FXML
	    void addReportToTable(ActionEvent event) {

	    }

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			
		}

		public static void setStartDate(LocalDate startDate) {
			ActivityReportPopupController.startDate = startDate;
		}

		public static void setEndDate(LocalDate endDate) {
			ActivityReportPopupController.endDate = endDate;
		}
}
