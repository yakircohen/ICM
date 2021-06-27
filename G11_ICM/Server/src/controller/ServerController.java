package controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import common.IcmServer;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ServerController {
	 
	@FXML private TextArea taLog;
	@FXML private Label lblAddress;
	@FXML private Button btnConnection;
	
	private PrintStream ps ;
	private Console console;
	private IcmServer sv;
	private boolean isConnected = false;
	final public static int DEFAULT_PORT = 5555;
	
	@FXML 
	public void connectServer(ActionEvent event) throws InterruptedException, IOException {
		if(isConnected) {
			disconnectServer();
			return;
		}
	    Service<Void> service = new Service<Void>(){
	    @Override
	    protected Task<Void> createTask() {
	       return new Task<Void>() {
	          @Override
	         protected Void call() throws Exception {
	            console = new Console(taLog);	            	   
	            ps = new PrintStream(console, true);	            	   
	            System.setOut(ps);
	            System.setErr(ps);

	            int port = 0; //Port to listen on
		       	try{port = 5555; } //Get port from command line
		       	catch(Throwable t) {port = DEFAULT_PORT;} //Set port to 5555
		       	
	    		sv = new IcmServer(port); //create the server with port
	    		Thread.sleep(1000);
		        try {sv.listen();} //Start listening for connections
		        catch (Exception ex) {System.out.println("ERROR - Could not listen for clients!");}
		        
	            return null;
	          }
	        };
	       }
	      }; service.setOnSucceeded(e -> {
	    	  isConnected = true;
	    	  setButtonConnectionState("Kill");
	    	  System.out.println("[Server Is ON]");
	    	  
	    	  //Run each day to reduce a day from each request.
		   	   Date date=new Date();
		   	   Timer timer = new Timer();

		   	   timer.schedule(new TimerTask(){
	   	        public void run(){
	   	          System.out.println("Background Task Starts Now: "+new Date());
	   	          String query = "SELECT * FROM request_handling WHERE idrequest NOT IN (SELECT idrequest FROM request_handling WHERE executionTime = '' OR currentStage = 'Closing')";
	   	          ResultSet rs = IcmServer.getDBHandler().executeQ(query);
	   	          ResultSet rs2, rs3;
	   	          try {
					while(rs.next() == true) {
						query = "SELECT * FROM request_handling WHERE idrequest = '"+rs.getString("idrequest")+"';";
						rs2 = IcmServer.getDBHandler().executeQ(query);
						if(rs2.next() == true) {
							if(rs2.getString("executionTime").equals("1")) { //if it is the last day, send message to charge
								insertMessage(rs2.getString("idCharge"), "One Day Left For Stage: "+rs2.getString("currentStage"), "One day left to finish stage for request id: "+rs2.getString("idrequest"), "ALERT");
							}
							if(rs2.getInt("executionTime") == 0) { //if it is exception
								insertMessage(rs2.getString("idCharge"), "Time Exception For Stage: "+rs2.getString("currentStage"), "Time exception in stage for request id: "+rs2.getString("idrequest"), "ALERT");
								query = "SELECT iduser FROM employee WHERE role = 'Manager';"; //gets the manager ID to inform him about the time exception.
								rs3 = IcmServer.getDBHandler().executeQ(query);
								if(rs3.next() == true)
									insertMessage(rs3.getString("iduser"), "Time Exception For Stage: "+rs2.getString("currentStage"), "Time exception in stage for request id: "+rs2.getString("idrequest"), "ALERT");
								query = "SELECT iduser FROM employee WHERE role = 'Inspector';"; //gets the Inspector ID to inform him about the time exception.
								rs3 = IcmServer.getDBHandler().executeQ(query);
								if(rs3.next() == true)
									insertMessage(rs3.getString("iduser"), "Time Exception For Stage: "+rs2.getString("currentStage"), "Time exception in stage for request id: "+rs2.getString("idrequest"), "ALERT");
							}else {
								query = "UPDATE request_handling SET executionTime = executionTime-'1' WHERE idrequest = '"+rs.getString("idrequest")+"';"; //reduce days
								IcmServer.getDBHandler().executeUpdate(query);
								query = "UPDATE request SET totalTime = totalTime+'1' WHERE idrequest = '"+rs.getString("idrequest")+"';"; //update total days for handling request
								IcmServer.getDBHandler().executeUpdate(query);
							}
							
						}
						
						
					  }
					} catch (SQLException e) {
						e.printStackTrace();
					}
	   	          System.out.println("Updated execution time for all processes");
	   	        }
	   	   },date, 24*60*60*1000);//24*60*60*1000 add 24 hours delay between job executions.
	      });
	      service.setOnRunning(e -> {
	    	  System.out.println("[Starting Server....]");
	      });
	      service.setOnFailed(e -> {
	    	  System.out.println("[Could Not Connect To Server]");
	      });
	   service.restart();
	   
	  
	}
	/**
	 * Adds message to user by his id
	 * @param iduser Id of user who will receive the message
	 * @param title Title of message
	 * @param content Content of message
	 * @param type The type of message (popup, text..)
	 */
	private void insertMessage(String iduser, String title, String content, String type) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		String date = dtf.format(localDate); //for message date
		String query = "INSERT INTO Messages (iduser,titleMessage,contentMessage,dateMessage,status,type) "+
				"VALUES ('"+iduser+"','"+title+"','"+content+"', '"+date+"', '', '"+type+"')";
		IcmServer.getDBHandler().executeUpdate(query);
	}
	public void initData(String address) {
    	lblAddress.setText(address); 
	}
	
	public void initialize(URL location, ResourceBundle resources) throws UnknownHostException {
		
	}
	
	/***
	 * Internal Class to redirect console output to TextArea
	 * 
	 */
	public class Console extends OutputStream {
		private TextArea console;
		private String b = "";
	    
		public Console(TextArea console) {
			this.console = console;
	    }

	    public void appendText(String valueOf) {
	    	Platform.runLater(() -> {
	    		console.appendText("<ICM>: " + valueOf);});   
	    }
	    /***
		 * Build string from bytes sent by prinln and passes it to appendText
		 * 
		 * @param i The byte
		 */
	    public void write(int i) throws IOException {
	    	b += (String.valueOf((char) i));
	    	if((String.valueOf((char) i)).equals("\n")) {
	    		appendText(b);
	    		b = "";
	    	}
	    }
	}
	
	/***
	 * Changes button name and icon by state (Start/Kill).
	 * 
	 * @param state The name of state to show on button
	 */
	private void setButtonConnectionState(String state) {
		Image iconState = new Image(getClass().getResourceAsStream("/boundary/guifiles/icons/" + state +  ".png"));
		ImageView iv = new ImageView(iconState);
		iv.setFitWidth(40);
		iv.setFitHeight(40);
		btnConnection.setText(state + " Server");
		btnConnection.setGraphic(iv);
	}
	/***
	 * Close db and server connections. Happens when button kill server is clicked 
	 * 
	 */
	private void disconnectServer() {
   		try {
   			sv.stopListening();
			sv.close();
			sv.getConnection().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
   		catch (SQLException e) {
			e.printStackTrace();
		}
   		isConnected = false;
   		setButtonConnectionState("Start");
   		System.out.println("[Server Is OFF]");
	}
}
