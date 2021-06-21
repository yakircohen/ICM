package common;

import java.io.IOException;

import ocsf.client.AbstractClient;

public class ClientConnector extends AbstractClient {
	/**
	   * Variables to store data.
	   * 
	   * @param clientUI holds Interface for messages.
	   */
	private ConnectorIF clientUI;
	
	/**
	   * Constructs an instance of the echo server.
	   *
	   * @param host The host address to connect.
	   * @param port The port number to connect on.
	   * @clientUI Interface for messages.
	   */
	 public ClientConnector(ConnectorIF clientUI) throws IOException 
	 {
	    super(ConnectionDetails.getHost(), ConnectionDetails.getPort()); //Call the superclass constructor
	    this.clientUI = clientUI;
		openConnection();
	 }
	
	 /**
	   * This method handles any messages received from the server.
	   *
	   * @param msg The message received from the server..
	   */
	 public void handleMessageFromServer(Object msg) 
	 {
		 try {
			msgReceivedHandler.msgHandler(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	  
	  /**
		 * Inner Class
		 *
		 */
		public static class ConnectionDetails {
			// Default server connection details.	
			private static int srvPort = 5555;
			private static String srvHostName = "localhost";
			
		 
		  public static void setServerDetails(String host, int port) {
			  srvPort = port;
			  srvHostName = host;
		  }
		  
		  /**
		   * Get the host address
		   * @return the host address
		   */
		  public static String getHost() {
			  return srvHostName;
		  }
		  
		  /**
		   * Get the port number
		   * @return the port number
		   */
		  public static int getPort() {
			  return srvPort;
		  }
		  
		}
}
