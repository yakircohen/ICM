package common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TreeSet;

import controller.ServerController;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class IcmServer extends AbstractServer 
{	
	 private MsgHandler msgHandler;
	 private static Connection conn;
	 private static DBHandler dbHandler;
	 private static TreeSet<String> loggedUsers;	 
 
  /**
   * The default port to listen on.
   */
	 final public static int DEFAULT_PORT = 5555;
  
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
	  public IcmServer(int port) 
	  {
		  super(port);
		  dbHandler = new DBHandler();
		  
	      conn = dbHandler.databaseConnect();
	      
	      loggedUsers = new TreeSet<String>();  
	  }

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
	public void handleMessageFromClient(Object msg, ConnectionToClient client)
	{
		msgHandler = new MsgHandler();
		
		try {
			msgHandler.clientMsgHandler(msg, client, conn);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
  
	protected void serverStarted()
	{
		
	  System.out.println
	    ("Server listening for connections on port " + getPort());
	}
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * 
   */
  public static DBHandler getDBHandler() {
  	return dbHandler;
  }


  /**
   * 
   */
  public MsgHandler getMsgHandler() {
  	return msgHandler;
  }


  /**
   * 
   */
  public static void setDBHandler(DBHandler Handler) {
  	dbHandler = Handler;
  }
  
  public static boolean addToConnectedUsers(String userID) {
	  if(loggedUsers.contains(userID)) return false;
	  loggedUsers.add(userID);
	  System.out.println
      ("[Login] User ID: " +userID+ " Connected To The System.");
	  return true;
  }
  public static void removeUserConnected(String userID) {
	  System.out.println
      ("[Logout] User ID: " +userID+ " Disconnected From The System.");
	  loggedUsers.remove(userID);
	  
  }

  /**
   * 
   */
  public static void setConnection(Connection con) {
  	conn = con;
  }
  public Connection getConnection() {
	  	return conn;
  }
  


}
//End of EchoServer class
