package icm.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import ocsf.server.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
public class EchoServer extends AbstractServer 
{
 
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private Connection conn;
  private boolean isFirstRun = true;
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  	@SuppressWarnings("unchecked") //avoid warnings
	public void handleMessageFromClient(Object msg, ConnectionToClient client)
	{
	   connectToDB();
	   if(isFirstRun) //if msg is arraylist of arraylists
	   {
		  createAList(msg, client); //create arraylist of data from sql, and sends to client
		  isFirstRun = false;
	   }
	   else{
			PreparedStatement ps;
			   try {
			    	ps = conn.prepareStatement("UPDATE requirement " + "SET Status= ? WHERE requirement.BoardNumber= ?");
				  	ps.setString(1,((ArrayList<String>)msg).get(4).toString());
				  	ps.setString(2,((ArrayList<String>)msg).get(1).toString());
				  	ps.executeUpdate();
				  	createAList(new ArrayList<String>(), client);
			    	}catch (SQLException e) {
			    		e.printStackTrace();
			    	}  
		   }
	 }
  /**
   * This method creates a connection with mysql database.  Called
   * before any commands who use DB.
   */
  private void connectToDB() {
	  try 
		{
          Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
          System.out.println("Driver definition succeed");
      } catch (Exception ex) {
      	/* handle the error*/
      	 System.out.println("Driver definition failed");
      	 }
      
      try 
      {
          conn = DriverManager.getConnection("jdbc:mysql://localhost/icm?serverTimezone=IST","root","Aa123456");
          System.out.println("SQL connection succeed"); 
   		} catch (SQLException ex) 
   	    {/* handle any errors*/
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
          }
  }
  @SuppressWarnings("unchecked")
  /**
   * This method creates an list of arraylists that holds the data from DB.  Called
   * each time the clients asks to watch requests list.
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  private void createAList(Object msg, ConnectionToClient client){
	  Statement stmt;
	  
	  ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();
	  if(isFirstRun) aList = (ArrayList<ArrayList<String>>)msg;
	  try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM requirement;");
			while(rs.next()){
				ArrayList<String> data = new ArrayList<String>();
				data.add(rs.getString(1));
				data.add(rs.getString(2));
				data.add(rs.getString(3));
				data.add(rs.getString(4));
				data.add(rs.getString(5));
				data.add(rs.getString(6));
				aList.add(data);
			}
			try {
				client.sendToClient(aList);
			}catch(IOException e) {
				e.printStackTrace();
			}
			rs.close();
			conn.close();

		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
 }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */ 
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
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
