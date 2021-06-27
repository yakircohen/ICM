package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Request;

public class DBHandler {
	private Connection conn;

	public Connection databaseConnect()
	{
		try
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}

        try
        {
        	//on develop
        	conn = DriverManager.getConnection("jdbc:mysql://remotemysql.com/q3t6Vm6bTC?autoReconnect=true&useSSL=true","q3t6Vm6bTC","hLwAfZ78Fx");
        	//For Local:
            //conn = DriverManager.getConnection("jdbc:mysql://localhost/icm?serverTimezone=IST","root","Aa123456");
            System.out.println("SQL Connection Succeed.");
     	} catch (SQLException ex)
     	    {	/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
        return conn;
   	}

	public DBHandler() {
		this.conn = databaseConnect();
	}

	public ResultSet executeQ(String query){
		Statement stmt;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public void executeUpdate(String query) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insertRequest(Request toInsert) {
		Statement stmt;
		ResultSet rs = null;
		String query;
		int rowcount = 0;
		try {
			//getting max value to decide the id
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT MAX(idrequest) FROM request");
		if (!rs.next())
			rowcount = 0;
		else
			rowcount = rs.getInt(1);

		toInsert.setIdReq(String.valueOf(rowcount+1));

		query = "INSERT INTO `request` (`idrequest`, `currentState`, `changeRequested`, `requestPurpose`, `ITSystem`, `submissionDate`, `iduser`,`RequestStatus`) VALUES ('"+
		toInsert.getIdReq()+"','"+
		toInsert.getCurState()+"','"+
		toInsert.getRequestedChange()+"','"+
		toInsert.getPurpose()+"','"+
		toInsert.getSystem()+"','"+
		toInsert.getOpentDate()+"','"+
		toInsert.getIdUser()+"','opened')";


		stmt.executeUpdate(query);
		return rowcount+1;

		}
		catch (SQLException e) {
			e.printStackTrace();
			//case of failing to insert
			return -1;
		}


		//return rowcount;
	}

	public String getUserID(int requestID) {
		String query = "SELECT `iduser` FROM request WHERE idrequest = '"+String.valueOf(requestID)+"'";
		ResultSet rs = executeQ(query);
		try {
			if(!rs.next())
				return "No such user";

			return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}



	}


}
