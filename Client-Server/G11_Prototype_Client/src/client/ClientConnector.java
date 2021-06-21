package client;

import java.io.IOException;
import java.util.ArrayList;
import common.clientConnectionIF;
import ocsf.client.*;

public class ClientConnector extends AbstractClient {
	/**
	   * Variables to store data.
	   * 
	   * @param aList holds the data returned from server.
	   */
	private clientConnectionIF clientUI;
	private ArrayList<ArrayList<String>> aList;
	
	/**
	   * Constructs an instance of the echo server.
	   *
	   * @param host The host address to connect.
	   * @param port The port number to connect on.
	   * @clientUI Interface for messages.
	   */
	 public ClientConnector(String host, int port, clientConnectionIF clientUI) throws IOException 
	 {
	    super(host, port); //Call the superclass constructor
	    this.clientUI = clientUI;
	    this.aList = new ArrayList<ArrayList<String>>();
		openConnection();
	 }
	
	 /**
	   * This method handles any messages received from the server.
	   *
	   * @param msg The message received from the server..
	   */
	 @SuppressWarnings("unchecked")
	 public void handleMessageFromServer(Object msg) 
	 {
		this.aList = (ArrayList<ArrayList<String>>)msg;
	 }
	 
	 /**
	   * This method sends any data from user to the server.
	   *
	   * @param msg The message from user to be sent to the server.
	   */
	public void handleMessageFromClientUI(Object msg)  
	{
	   try
	   {
	    sendToServer(msg);
	   }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
	  }
	 
	 /**
	   * This method returns a list of arraylists contains
	   */
	  public ArrayList<ArrayList<String>> getListj() {
		  return aList;
	  }
	  
	  /**
	   * This method quits system if error connecting to server
	   */
	  public void quit()
	  {
	    try
	    {
	      closeConnection();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	  }
}
