package controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import common.MsgEnum;
import common.ObjectManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
public class PerformanceBehindReportPopupController implements Initializable {
	@FXML
    private Label lblMedianNumDelays;
    @FXML
    private Label lblstandarddeviationNumDelays; 
    @FXML
    private Label lblMedianDelayTime;
    @FXML
    private Label lblstandarddeviationDelayTime;   
    @FXML
    private ComboBox<String> cmbSystem;   
    @FXML
    private TableView<FrequencyDistribution> tblFrequencyDistributionnumdelays;    
    @FXML
    private TableColumn<FrequencyDistribution, Object> colRangenumdelays;
    @FXML
    private TableColumn<FrequencyDistribution, Integer> colTotalnRangenumdelays;   
    @FXML
    private TableView<FrequencyDistribution> tblFrequencyDistributiontimedelay;
    @FXML
    private TableColumn<FrequencyDistribution, Object> colRangetimedelay;
    @FXML
    private TableColumn<FrequencyDistribution, Integer> colTotalnRangetimedelay;    
    @FXML
    private Button btnData;    
    @FXML
    private Label lblsytstemerr;    
    private static Map<String, Integer> daysOfDelay=new HashMap<String, Integer>();    
    private static Map<String,Integer> timeOfDelay =new HashMap<String, Integer>();    
    private static ObjectManager generatePerformanceBehindReport;
    ArrayList<Integer> numofDelays= new ArrayList<Integer>();
	  ArrayList<Integer> timeofDelays= new ArrayList<Integer>();

	  Integer countDelayTime=1;
	  Integer sumofNumDelays=0,sumofTimeDelays=0,countNumDelays=1,countTimeDelays=1,avgNumofDelays,avgTimeofDelays;
	  Object[] arr;
	  Double standardDiviationNumDelays, standardDiviationTimeDelays;
	public static void setDaysOfDelay(Map<String, Integer> daysOfDelay) {
		PerformanceBehindReportPopupController.daysOfDelay = daysOfDelay;
	}

	public static void setTimeOfDelay(Map<String, Integer> timeOfDelay) {
		PerformanceBehindReportPopupController.timeOfDelay = timeOfDelay;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generatePerformanceBehindReport= new ObjectManager(daysOfDelay, timeOfDelay, MsgEnum.CREATE_PERFORMANCE_BEHIND_REPORT);
		ConnectionController.getClient().handleMessageFromClientUI(generatePerformanceBehindReport);
		try {
			Thread.sleep(2000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		List<String> ITSystemsList =new ArrayList<String>();
		ITSystemsList.add("Braude Website");
		ITSystemsList.add("Info Braude");
		ITSystemsList.add("Library");
		ITSystemsList.add("Moodle");
		ITSystemsList.add("Office");
		ObservableList<String> obList=FXCollections.observableList(ITSystemsList);
		cmbSystem.getItems().clear();
		cmbSystem.setItems(obList);
		lblsytstemerr.setTextFill(Color.RED);
		lblsytstemerr.setVisible(false);
	}
	
	  @FXML
	    void displayData(ActionEvent event) {
		  
		  //Integer arr[];
		  if(cmbSystem.getSelectionModel().isEmpty())
			  lblsytstemerr.setVisible(true);
		  else {
			  lblsytstemerr.setVisible(false);
			  arrangeDetails(cmbSystem.getSelectionModel().getSelectedItem());
		  }
	  }
	  public void arrangeDetails(String ITSystem) {
		  
		  for(Map.Entry<String, Integer> entry : daysOfDelay.entrySet())
			  if(entry.getKey().equals(ITSystem))
				  numofDelays.add(entry.getValue());
		  Collections.sort(numofDelays);
		  if((numofDelays.size()%2)==0)
		  {
			  avgNumofDelays=Math.addExact(numofDelays.get((numofDelays.size()/2)+1),numofDelays.get(numofDelays.size()/2));
			  avgNumofDelays/=2;
			  lblMedianNumDelays.setText(avgNumofDelays.toString());
		  }
		  else lblMedianNumDelays.setText(numofDelays.get(numofDelays.size()/2).toString());
		  for(Integer val : numofDelays)
			  sumofNumDelays+=val;
		  standardDiviationNumDelays=(double)(sumofNumDelays/numofDelays.size());
		  lblstandarddeviationNumDelays.setText(standardDiviationNumDelays.toString());
		  arr= new Object[numofDelays.size()];
		  arr=numofDelays.toArray();
		  for(int i=1;i<arr.length;i++)
		  {
			  if(arr[i-1].equals(arr[i]))
				  countNumDelays++;
			  else
			  {
				  tblFrequencyDistributionnumdelays.getItems().add(new FrequencyDistribution(arr[i-1], countNumDelays));
				  countNumDelays=1;
			  Integer sumofDelayTime = null;
			switch(cmbSystem.getSelectionModel().getSelectedItem()) {
			  case "Braude Website":
				  for(Map.Entry<String, Integer> entry : daysOfDelay.entrySet())
					  if(entry.getKey().equals("Braude Website"))
						  numofDelays.add(entry.getValue());
				  Collections.sort(numofDelays);
					  if(((numofDelays.size())%2)==0)
					  {
						  Integer avg1=(Math.addExact(numofDelays.get(numofDelays.size()/2-1), numofDelays.size()/2)/2);
						  lblMedianNumDelays.setText(avg1.toString());
					  }
					  else
						  lblMedianNumDelays.setText(numofDelays.get(numofDelays.size()/2).toString());
					  for(Integer val : numofDelays)
						  sumofNumDelays+=val;
					  Double standardDiviation=(double)(sumofNumDelays/numofDelays.size());
					  lblstandarddeviationNumDelays.setText(standardDiviation.toString());
					  Integer[] arr1=new Integer[numofDelays.size()];
					  i=0;
					  for(Integer j : numofDelays) {
						  arr1[i]=j;
						  i++;
					  }
					  for(i=1; i<arr1.length;i++)
					  {
						  if(arr1[i-1]==arr1[i])
							  countNumDelays++;
						  else
						  {
							  tblFrequencyDistributionnumdelays.getItems().add(new FrequencyDistribution(arr1[i-1], countNumDelays));
							  countNumDelays=1;
						  }
					  }
					  for(Map.Entry<String, Integer> entry: timeOfDelay.entrySet())
						  if(entry.getKey().equals("Braude Website"))
							  timeofDelays.add(entry.getValue());
					  Collections.sort(timeofDelays);
					  if(((timeofDelays.size())%2)==0)
					  {
						  Integer avg2=(Math.addExact(timeofDelays.get(timeofDelays.size()/2-1), timeofDelays.size()/2)/2);
						  lblMedianDelayTime.setText(avg2.toString());
					  }
					  else
						  lblMedianDelayTime.setText(timeofDelays.get(timeofDelays.size()/2).toString());
					  for(Integer val : timeofDelays)
						  sumofDelayTime+=val;
					  Double standardDiviation2=(double)(sumofDelayTime/timeofDelays.size());
					  lblstandarddeviationDelayTime.setText(standardDiviation2.toString());
					  arr1=new Integer[timeofDelays.size()];
					  arr1=(Integer[])timeofDelays.toArray();
					  for(i=1; i<arr1.length;i++)
					  {
						  if(arr1[i-1]==arr1[i])
							  countDelayTime++;
						  else
						  {
							  tblFrequencyDistributiontimedelay.getItems().add(new FrequencyDistribution(arr1[i-1], countDelayTime));
							  countDelayTime=1;
						  }
					  }
					  break;
				  }
			  }
		  }
		  for(Map.Entry<String, Integer> entry : timeOfDelay.entrySet())
			  if(entry.getKey().equals(ITSystem))
					 timeofDelays.add(entry.getValue());
		  Collections.sort(timeofDelays);
		  if((timeofDelays.size()%2)==0)
		  {
			  avgTimeofDelays=Math.addExact(timeofDelays.get((timeofDelays.size()/2)+1),timeofDelays.get(timeofDelays.size()/2));
			  lblMedianDelayTime.setText(avgTimeofDelays.toString());
		  }
		  else lblMedianDelayTime.setText(timeofDelays.get(timeofDelays.size()/2).toString());
		  for(Integer val : timeofDelays)
			  sumofTimeDelays+=val;
		  standardDiviationTimeDelays=(double)(sumofTimeDelays/timeofDelays.size());
		  lblstandarddeviationDelayTime.setText(standardDiviationTimeDelays.toString());
		  arr= new Object[timeofDelays.size()];
		  arr=timeofDelays.toArray();
		  for(int i=1;i<arr.length;i++)
		  {
			  if(arr[i-1].equals(arr[i]))
				  countTimeDelays++;
			  else
			  {
				  tblFrequencyDistributionnumdelays.getItems().add(new FrequencyDistribution(arr[i-1], countTimeDelays));
				  countTimeDelays=1;
			  }
		  }
	  }
	  private class FrequencyDistribution {
		  private Object range;
		private Integer totalInRange;
		  public FrequencyDistribution(Object range,Integer totalInRange) {
			  this.range=range;
			  this.totalInRange=totalInRange;
			  }
		  }
}
