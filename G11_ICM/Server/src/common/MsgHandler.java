package common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import entity.ActionsNeeded;
import entity.Document;
import entity.EvaluationReport;
import entity.Messages;
import entity.PerformanceReport;
import entity.Request;
import entity.RequestHandling;
import entity.Systems;
import entity.User;
import common.ObjectManager;
import ocsf.server.ConnectionToClient;
/**
 * Handles all requests sent from client
 * @author Ron
 *
 */
public class MsgHandler {
	DBHandler dbHandler = IcmServer.getDBHandler();
	ObjectManager objectManager;
	public String idOfRequestFromServer;

	@SuppressWarnings("unchecked")
	public void clientMsgHandler(Object msg, ConnectionToClient client, Connection conn)
			throws IOException, SQLException {
		objectManager = (ObjectManager) msg;
		User user = null;
		String query;
		String errorMsg;
		ResultSet rs;
		switch (objectManager.getMsgEnum()) {
		case LOGIN:
			query = "SELECT * FROM user WHERE iduser = '" + objectManager.getUser().getIdUser() + "'" + ";";
			rs = dbHandler.executeQ(query);
			if (rs.next() == true) {
				if (rs.getString("password").equals(objectManager.getUser().getPassword())) { // if user and password OK
					if (!IcmServer.addToConnectedUsers(rs.getString("iduser"))) { // if user already connected - show error
						errorMsg = "User ID: " + rs.getString("iduser") + " already connected to ICM.";
						objectManager = new ObjectManager(errorMsg, MsgEnum.LOGIN_ERROR);
					} else {
						if (rs.getString(6).equals("Information Technology")) {
							ResultSet rs2;
							query = "SELECT * FROM employee WHERE iduser = '" + objectManager.getUser().getIdUser()
									+ "'" + ";";
							rs2 = dbHandler.executeQ(query);
							if (rs2.next() == true) { // if user is an employee
								if (rs2.getString("role") == null || rs2.getString("role").equals("")) { // if employee does not have specific role
									user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
											rs2.getString(2), rs.getString(6), rs.getString(7)); // put profession role
								} else
									user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
											rs2.getString("role"), rs.getString(6), rs.getString(7)); // put role in
																										// system
							}
						} else {
							user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getString(5), rs.getString(6), rs.getString(7));
						}
						objectManager = new ObjectManager(user, MsgEnum.LOGIN);
					}
				} else {
					objectManager = new ObjectManager("Password invalid", MsgEnum.LOGIN_ERROR);
				}
			} else {
				objectManager = new ObjectManager("User does not exist", MsgEnum.LOGIN_ERROR);
			}
			client.sendToClient(objectManager);
			rs.close();
			break;

		case LOGOUT:
			IcmServer.removeUserConnected(objectManager.getUser().getIdUser()); // remove user from connected list
			break;

		case ADD_REQUEST:

			// getting max id to set the new id
			rs = dbHandler.executeQ("SELECT MAX(idrequest) FROM request");
			int rowcount;

			if (!rs.next())
				rowcount = 0;
			else
				rowcount = rs.getInt(1);

			// sets the request-object id
			objectManager.getReques().setIdReq(String.valueOf(rowcount + 1));

			idOfRequestFromServer = new Integer(rowcount).toString();
			// building the insert query
			StringBuilder qr = new StringBuilder("INSERT INTO request VALUES(");
			for (String fields : objectManager.getReques().getAll()) {

				qr.append("'" + fields + "',");
			}

			qr.setCharAt(qr.length() - 1, ')');

			// executing the query
			dbHandler.executeUpdate(qr.toString());
			
			//add an evaluator appoint request to actions table
			insertEvaluatorAppointAction(objectManager.getReques().getSystem(), objectManager.getReques().getIdReq());
			
			System.out.println("Request added to request table\n");
			client.sendToClient(new ObjectManager(new Integer(rowcount), MsgEnum.SEND_ID_OF_REQUEST_TO_CLIENT));
			// sending the id of the request to the client

			break;
			
		
		case ADD_FILES:

			// files part
			// adding the files to the documents table, and saving them on server side
			// creating the id for the files
			rs = dbHandler.executeQ("SELECT MAX(iddocument) FROM document");

			if (!rs.next())
				rowcount = 0;
			else
				rowcount = rs.getInt(1);

			// building the query
			int indexOfFile = 0;

			for (Document dcm : objectManager.getListOfFiles()) {

				StringBuilder filesQuery = new StringBuilder("INSERT INTO document VALUES(");
				// first thing we set it's id
				dcm.setIddocument(Integer.toString(++rowcount));

				// now we build the query
				filesQuery.append("'" + dcm.getIddocument() + "',");// ok

				filesQuery.append("'" + dcm.getIdrequest() + "_" + (++indexOfFile) + "',"); // format of naming:
																							// requestid_number
				filesQuery.append("'" + dcm.getFileType() + "',");// ok
				filesQuery.append("'" + "C:/ICM/Documents/" + dcm.getIdrequest() + "_" + indexOfFile + "."
						+ dcm.getFileType() + "/" + "',");
				filesQuery.append("'" + dcm.getIdrequest() + "')");

				// executing the query

				dbHandler.executeUpdate(filesQuery.toString());

				// saving the files in server side

				// create the path if it doesnt exist
				File dir = new File("C:/ICM/Documents/" + dcm.getIdrequest() + "/");
				if (!dir.exists())
					dir.mkdirs();

				// copying the file

				MyFile ff = dcm.getMyFile();

				try {
					String serverFilesPath = "C:/ICM/Documents/" + dcm.getIdrequest() + "/" + dcm.getIdrequest() + "_"
							+ (indexOfFile) + "." + dcm.getFileType();
					FileOutputStream fos = new FileOutputStream(serverFilesPath);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bos.write(ff.getMybytearray(), 0, ff.getSize());
					bos.close();
					System.out.println("File added sucessefuly to: " + serverFilesPath);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			break;
		case GET_REQUESTS_BY_ID_STARTED:

			// started requests query
			String requestStageQuery = "SELECT r.idrequest, rh.executionTime, rh.currentStage FROM "
					+ "request r INNER JOIN request_handling rh ON r.idrequest = rh.idrequest AND r.iduser =" + " '"
					+ objectManager.getMsgString() + "'";

			ResultSet stageRS = dbHandler.executeQ(requestStageQuery);

			ArrayList<Request> result1 = new ArrayList<>();

			while (stageRS.next()) {
				
				String id = stageRS.getString(1);
				String expectedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now().plusDays(stageRS.getLong(2)));
				String stage = stageRS.getString(3);
				Request temp = new Request(id, expectedDate,stage); 
				result1.add(temp);
			}

			//sending the result to the client
			ObjectManager obm1 = new ObjectManager(result1, MsgEnum.SEND_RS_STARTED_TO_CLIENT);
			client.sendToClient(obm1);
			
			break;
			
		
			
			
		case GET_REQUESTS_BY_ID_NOSTARTED:
			
			//------------------------------------------------------------------------------//
			
			// request that arent opened
			String NotStartedRequestQuery = "SELECT idrequest FROM request WHERE (RequestStatus = 'not started') AND iduser = "
					+ "'" + objectManager.getMsgString() + "'";
			ResultSet NotStartedRS = dbHandler.executeQ(NotStartedRequestQuery);

			
			ArrayList<Request> result2 = new ArrayList<>();

			while (NotStartedRS.next()) {
				String id = NotStartedRS.getString(1);
				String date = "unknown";
				String stage = "not started";
				Request temp = new Request(id,date,stage);
				result2.add(temp);
			}
			//sending the result to the client
			ObjectManager obm2 = new ObjectManager(result2, MsgEnum.SEND_RS_NOT_STARTED_TO_CLIENT);
			client.sendToClient(obm2);
			
			break;
			
		case ADD_EV_REPORT:
			EvaluationReport rpt = objectManager.getEvReport();
			String flag;
			
			if(dbHandler.executeQ("SELECT * FROM evaluation_report WHERE idreq = '"+
							rpt.getIdReq()+"'").next())

			{
				//UPDATE `evaluation_report` SET `location` = 'loc12', `description` = 'des12', `result` = 'res12', `risk` = 'risk1		risk3', `time` = 'time2' WHERE (`idreq` = '1');
				String existEv ="UPDATE `evaluation_report` SET `location` = '"+rpt.getLocation()+"',";
				existEv+="`description` = '"+rpt.getDescription()+"',";
				existEv+="`result` = '"+rpt.getResult()+"',";
				existEv+="`risk` = '"+rpt.getRisk()+"',";
				existEv+="`time` = '"+rpt.getTime()+"' WHERE (`idreq` = '"+rpt.getIdReq()+"')";
				dbHandler.executeUpdate(existEv);
			}
			else {
				
				String str1 = "INSERT INTO evaluation_report VALUES('";
				str1+=rpt.getIdReq()+"','";
				str1+=rpt.getLocation()+"','";
				str1+=rpt.getDescription()+"','";
				str1+=rpt.getResult()+"','";
				str1+=rpt.getRisk()+"','";
				str1+=rpt.getTime()+"')";				
				dbHandler.executeUpdate(str1);
			}
				
			System.out.println("Evaluation report for Request #"+rpt.getIdReq()+" added successfully!");
			
			query = "UPDATE request_handling SET executionTime = '7' , idCharge = '' , currentStage = 'Review' WHERE (`idrequest` = '"+rpt.getIdReq()+"')";
			dbHandler.executeUpdate(query);
			//
			System.out.println("Request #"+rpt.getIdReq()+" moved from: Evaluation > Review");
			//System.out.println("Stage updated: Evaluation > Review, in Request #"+rpt.getIdReq());
			break;

		case VIEW_MESSAGES:
			ArrayList<Messages> messagesArray = new ArrayList<Messages>();
			query = "SELECT * FROM Messages WHERE iduser = '" + objectManager.getUser().getIdUser() + "'" + ";";
			rs = dbHandler.executeQ(query);
			while (rs.next() == true) {
				messagesArray.add(new Messages(rs.getString("idMessage"), rs.getString("iduser"),
						rs.getString("titleMessage"), rs.getString("contentMessage"), rs.getString("status"),
						rs.getString("dateMessage"), rs.getString("type")));
			}
			client.sendToClient(new ObjectManager(messagesArray, MsgEnum.SEND_MESSAGES_TO_CLIENT));
			break;

		case VIEW_EMPLOYEES:
			ArrayList<User> employeesArray = new ArrayList<User>();
			query = "SELECT * FROM user WHERE role = 'Worker';"; // for users
			rs = dbHandler.executeQ(query);
			ResultSet rs2; // for employee
			while (rs.next() == true) {
				rs2 = dbHandler
						.executeQ("SELECT * FROM employee WHERE iduser = '" + rs.getString("iduser") + "'" + ";");
				if (rs2.next() == true) {
					employeesArray
							.add(new User(rs.getString("iduser"), rs.getString("firstName"), rs.getString("lastName"),
									rs.getString("email"), rs2.getString("role"), rs.getString("department")));
				} else {
					employeesArray
							.add(new User(rs.getString("iduser"), rs.getString("firstName"), rs.getString("lastName"),
									rs.getString("email"), rs.getString("role"), rs.getString("department")));
				}
			}
			if (employeesArray.isEmpty())
				System.out.println("No Employees"); // change this to send error OR handle this in client side.
			else
				client.sendToClient(new ObjectManager(employeesArray, MsgEnum.VIEW_EMPLOYEES));
			break;
			
		case VIEW_ACTIONS: //for process waiting time approval and employee approval
			ArrayList<ActionsNeeded> actionsArray = new ArrayList<ActionsNeeded>();
			query = "SELECT * FROM actions_needed;"; // for users
			rs = dbHandler.executeQ(query);
			while(rs.next() == true) {
				actionsArray.add(new ActionsNeeded(rs.getString("idrequest"), rs.getString("idCharge"), rs.getString("stage"),rs.getString("actionsNeeded")));
			}
			client.sendToClient(new ObjectManager(actionsArray, MsgEnum.VIEW_ACTIONS));
			break;

		case VIEW_PROCESSES:
			ArrayList<RequestHandling> processesArray = new ArrayList<RequestHandling>();
			query = "SELECT * FROM request_handling;"; // for process handling
			rs = dbHandler.executeQ(query);

			while(rs.next() == true) {
				String expectedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now().plusDays(rs.getLong("executionTime")));
				RequestHandling temp = new RequestHandling();
				//(rs.getString("idrequest"), rs.getString("idCharge"), expectedDate, rs.getString("currentStage"), rs.getString("status"));
				temp.setIdrequest(rs.getString("idrequest"));
				temp.setCurrentStage(rs.getString("currentStage"));
				if(rs.getString("executionTime").isEmpty()){
					temp.setExecutionTime("");
				}
				else {
					temp.setExecutionTime(expectedDate);
				}
				temp.setStatus(rs.getString("status"));
				temp.setIdCharge(rs.getString("idCharge"));
				temp.setTimeNum(rs.getString("executionTime"));
				processesArray.add(temp);
			}
			client.sendToClient(new ObjectManager(processesArray, MsgEnum.VIEW_PROCESSES));
			break;	
			
			
		//need to delete - checking
		case VIEW_PROCESSES_TO_BE_DETERMINED:
			ArrayList<RequestHandling> processesTimeArray = new ArrayList<RequestHandling>();
			query = "SELECT * FROM request_handling WHERE executionTime is NULL or executionTime ='';"; // for process handling
			rs = dbHandler.executeQ(query);
			while(rs.next() == true) {
				processesTimeArray.add(new RequestHandling(rs.getString("idrequest"), rs.getString("idCharge"), rs.getString("executionTime"), rs.getString("currentStage"), rs.getString("status")));
			}
			client.sendToClient(new ObjectManager(processesTimeArray, MsgEnum.VIEW_PROCESSES_TO_BE_DETERMINED));
			break;	
			
		case VIEW_REQUEST:
			Request req = null;
			query = "SELECT * FROM request WHERE idrequest = '" + objectManager.getMsgString() + "'" + ";";
			rs = dbHandler.executeQ(query);
			if (rs.next() == true) {
				req = new Request(rs.getString("idrequest"), rs.getString("currentState"), rs.getString("changeRequested"), rs.getString("requestPurpose"), rs.getString("comments"),
						rs.getString("ITSystem"), rs.getString("submissionDate"), rs.getString("iduser"), rs.getString("RequestStatus"), rs.getString("totalTime"));
			}
			if (req == null)
				System.out.println("No such request for id"); // change this to send error OR handle this in client side.
			else
				client.sendToClient(new ObjectManager(req, MsgEnum.VIEW_REQUEST));
			break;
			
		case CHECK_PRE_EV:
			String str3 = "SELECT * FROM evaluation_report WHERE idreq = '"+
							objectManager.getMsgString()+"'";
			ResultSet rs3 = dbHandler.executeQ(str3);
			
			if(rs3.next()) {
				EvaluationReport er3 = new EvaluationReport();
				er3.setLocation(rs3.getString(2));
				er3.setDescription(rs3.getString(3));
				er3.setResult(rs3.getString(4));
				er3.setRisk(rs3.getString(5));
				er3.setTime(rs3.getString(6));
				
				client.sendToClient(new ObjectManager(er3, MsgEnum.CLIENT_EV_REP));	
			}
			else {
				EvaluationReport erNull = null;
				client.sendToClient(new ObjectManager(erNull,MsgEnum.CLIENT_EV_REP));
			} 

			break;
			
		case GET_EV_REPORT:
			query = "SELECT * FROM evaluation_report WHERE idreq = '"+objectManager.getMsgString()+"'";
			rs = dbHandler.executeQ(query);
			
			EvaluationReport report = new EvaluationReport();
			rs.next();
			report.setLocation(rs.getString(2));
			report.setDescription(rs.getString(3));
			report.setResult(rs.getString(4));
			report.setRisk(rs.getString(5));
			report.setTime(rs.getString(6));
			
			objectManager = new ObjectManager(report, MsgEnum.SET_EV_IN_REVIEW);
			client.sendToClient(objectManager);	
			break;
		
		case MORE_INFO_REVIEW:
			query = "UPDATE request_handling SET currentStage = 'Evaluation' , idCharge = '', executionTime = '' WHERE idrequest = '"+objectManager.getMsgString()+"'";
			dbHandler.executeUpdate(query);
			
			//Get the relevant system the request is about
			query = "SELECT ITSystem FROM request WHERE idrequest = '" + objectManager.getMsgString() + "'" + ";";
			rs = dbHandler.executeQ(query);
			if(!rs.next()) {
				System.out.println("System for request did not found");
			}
			//Add an evaluator appoint request to actions table
			insertEvaluatorAppointAction(rs.getString("ITSystem"), objectManager.getMsgString());
			System.out.println("Request #"+objectManager.getMsgString()+" moved from: Review > Evaluation");
			break;
			
		case GET_CHANGE_INFO:
			query = "SELECT description FROM evaluation_report WHERE idreq = '"+objectManager.getMsgString()+"'";
			rs = dbHandler.executeQ(query);
			rs.next();
			String  infoANDfailure = rs.getString(1);
			query = "SELECT failure FROM checking_failure WHERE idrequest = '"+objectManager.getMsgString()+"'";
			rs = dbHandler.executeQ(query);
			if(rs.next())
				infoANDfailure+="\n\nFailure: "+rs.getString(1);
			
			objectManager = new ObjectManager(infoANDfailure, MsgEnum.SET_INFO_EXECUATION);
			client.sendToClient(objectManager);
			break;
		
		case EXECUTION_DONE:
			query = "UPDATE request_handling SET currentStage = 'Checking' , executionTime = '7' , idCharge = '123' WHERE idrequest = '"+objectManager.getMsgString()+"'";
			dbHandler.executeUpdate(query);
			System.out.println("Request #"+objectManager.getMsgString()+" moved from: Execution > Checking");	
			break;

			
		case APPROVE_CHECKING:
			query = "UPDATE request_handling SET currentStage = 'Closing', executionTime = '' , idCharge = '' WHERE idrequest = '"+objectManager.getMsgString()+"'";
			dbHandler.executeUpdate(query);
			query = "SELECT iduser FROM employee WHERE role = 'Inspector'";
			rs = dbHandler.executeQ(query);
			if(rs.next() == true)
				query = "INSERT INTO actions_needed VALUES ("+Integer.valueOf(objectManager.getMsgString())+", '"+rs.getString("iduser")+"', 'Closing', 'Close Request');";
			
			dbHandler.executeUpdate(query); //execute the insert query
			System.out.println("Request #"+objectManager.getMsgString()+" moved from: Checking > Closing");		
			break;
			
		case REJECT_CHECKING:
			String idstr = objectManager.getMsgString().split("_")[0];
			String failurestr = objectManager.getMsgString().split("_")[1];
			query = "SELECT * FROM checking_failure WHERE idrequest = '"+idstr+"'";
			
			
			if(dbHandler.executeQ(query).next()) {
				query = "UPDATE checking_failure SET failure = '"+failurestr+"'"+" WHERE idrequest = '"+idstr+"'";
				dbHandler.executeUpdate(query);				
			}
			else {
				query = "INSERT INTO checking_failure VALUES ('"+idstr+"','"+failurestr+"')";
				dbHandler.executeUpdate(query);
			}
			
			query = "UPDATE request_handling SET currentStage = 'Execution', executionTime = '' , idCharge = '' WHERE idrequest = '"+idstr+"'";
			dbHandler.executeUpdate(query);
			insertExecutorAppointAction(idstr); //add appoint executor request
			System.out.println("Request #"+idstr+" moved from: Checking > Execution");
			
			break;

		//Show employees [information engineers in Information Technology department] without role who can be appointed to stages charge
		case VIEW_EMPLOYEES_TO_APPOINT:
			ArrayList<User> employeeArray = new ArrayList<>();
			query = "SELECT e.iduser, u.firstName, u.lastName, e.role FROM employee e, user u WHERE e.iduser=u.iduser AND e.role = '';"; //Select only employees who have no roles.
			rs = dbHandler.executeQ(query);
			query = "SELECT e.iduser, u.firstName, u.lastName, e.role FROM employee e, user u WHERE e.iduser=u.iduser AND e.role IS NULL;"; //for null role
			rs2 = dbHandler.executeQ(query);
			while (rs.next() == true) {
				employeeArray.add(new User(rs.getString("iduser"), rs.getString("firstName"), rs.getString("lastName")));
			}
			while (rs2.next() == true) {
				employeeArray.add(new User(rs2.getString("iduser"), rs2.getString("firstName"), rs2.getString("lastName")));
			}
			if (employeeArray.isEmpty())
				System.out.println("No Information Engineers To Appoint"); // change this to send error OR handle this in client side.
			else
				client.sendToClient(new ObjectManager(employeeArray, MsgEnum.VIEW_EMPLOYEES_TO_APPOINT));
			break;
		
		case APPOINT_STAGE_CHARGE:
			query = "SELECT idrequest FROM request_handling WHERE idrequest = '"+objectManager.getAction().getIdrequest()+"';";
			rs = dbHandler.executeQ(query);
			if(!rs.next()) { //if request does not exist in request_handling
				query = "INSERT INTO request_handling VALUES ("+Integer.valueOf(objectManager.getAction().getIdrequest())+
						", '"+ objectManager.getMsgString() +"', ''"+
						", '"+ objectManager.getAction().getStage() +"', '');";
			}else {
				query = "UPDATE request_handling SET currentStage = '"+ objectManager.getAction().getStage() +"', executionTime = '' , idCharge = '"+ objectManager.getMsgString() +"' WHERE idrequest = '"+objectManager.getAction().getIdrequest()+"';";		
			}
			dbHandler.executeUpdate(query); //update stage charge and currentStage
			deleteAction(); //delete the action from DB
			break;
			
		case CHECK_REVIEW_EXIST:
			query = "SELECT * FROM review_decision WHERE idrequest = '"+objectManager.getMsgString()+"'";
			rs = dbHandler.executeQ(query);
			
			if(rs.next()) {
				objectManager = new ObjectManager(rs.getString(2), MsgEnum.SET_REVIEW_EXIST);
				
				
			}
			else {
				objectManager = new ObjectManager("", MsgEnum.SET_REVIEW_EXIST);
			}
			
			client.sendToClient(objectManager);
			
			break;
		case ADD_REVIEW:
			String reviewArr[] = objectManager.getMsgString().split("_");
			query = "INSERT INTO review_decision  VALUES ('"+reviewArr[0]+"','"+reviewArr[1]+"')";
			dbHandler.executeUpdate(query);
			break;
			
		case REVIEW_LEADER_APPROVE:
			query = "SELECT * FROM review_decision WHERE idrequest = '"+objectManager.getMsgString()+"'";
			rs = dbHandler.executeQ(query);
			rs.next();
			if(rs.getString(2).contentEquals("Approve")) {
				query = "UPDATE request_handling SET currentStage = 'Execution' , executionTime = '' , idCharge = '' WHERE idrequest = '"+objectManager.getMsgString()+"'";
				dbHandler.executeUpdate(query);
				insertExecutorAppointAction(objectManager.getMsgString()); //add appoint executor request
				System.out.println("Request #"+objectManager.getMsgString()+" moved from: Review > Execution");
			}
			else {
				query = "SELECT iduser, idrequest FROM request WHERE idrequest = '"+objectManager.getMsgString()+"';"; //gets the user issued the request
				rs = dbHandler.executeQ(query);
				//sending finished messages for users
				if(rs.next() == true) {
					insertMessage(rs.getString("iduser"), "Rejected Your Request [ID: "+rs.getString("idrequest")+"]",
							"Hi, We rejected your request. Thank you.", "FINISH");
					query = "UPDATE request SET status = 'rejected' WHERE idrequest = "+Integer.valueOf(objectManager.getMsgString());
					dbHandler.executeUpdate(query);
					query = "DELETE FROM request_handling WHERE idrequest = "+Integer.valueOf(objectManager.getMsgString());
					dbHandler.executeUpdate(query);
				}
				
				System.out.println("Request #"+objectManager.getMsgString()+" moved from: Review > Closing");
			}
			break;
			
		case UPDADE_DEPARTMENT:
			ArrayList<?> depUpdate = objectManager.getArray();
			for(Object u : depUpdate) {
				User us = (User)u;
				if(us.getDepartment().equals("Information Technology")) { //Add to department
					query = "INSERT INTO employee VALUES ('"+us.getIdUser()+"' , 'Information Engineer', '');"; //add to employee table with role empty
					dbHandler.executeUpdate(query);
					query = "UPDATE user SET department = 'Information Technology' WHERE iduser = '"+us.getIdUser()+"';"; //update user's department
					dbHandler.executeUpdate(query);
				}else { //Remove from department
					query = "DELETE FROM employee WHERE iduser = '" +us.getIdUser()+ "';"; //delete employee from employee department
					dbHandler.executeUpdate(query);
					query = "UPDATE user SET department = '"+us.getDepartment()+"' WHERE iduser = '"+us.getIdUser()+"';"; //update user's department
					dbHandler.executeUpdate(query);
				}
			}
			break;
			
		case VIEW_EMPLOYEES_WITH_ROLES:
			ArrayList<User> empWithRoleArray = new ArrayList<User>();
			query = "SELECT e.iduser, u.firstName, u.lastName, u.email, e.role FROM employee e, user u WHERE e.iduser=u.iduser AND e.iduser NOT IN "
					+ "(SELECT iduser FROM employee WHERE role = '' OR role IS NULL);";
			rs = dbHandler.executeQ(query);
			while (rs.next() == true) {
				empWithRoleArray.add(new User(rs.getString("iduser"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("role")));
			}
			if (empWithRoleArray.isEmpty())
				System.out.println("No Employees with roles"); // change this to send error OR handle this in client side.
			else
				client.sendToClient(new ObjectManager(empWithRoleArray, MsgEnum.VIEW_EMPLOYEES_WITH_ROLES));
			break;
		case VIEW_SYSTEMS: //shows systems and their charge
			ArrayList<Systems> sys = new ArrayList<Systems>();
			query = "SELECT * FROM systems";
			rs = dbHandler.executeQ(query);
			ResultSet rst;
			while (rs.next() == true) {
				if(rs.getString("iduser").equals(""))
					sys.add(new Systems(rs.getString("systemName"),"None", "None", "None"));
				else {
					query = "SELECT firstName, lastName FROM user WHERE iduser = '"+rs.getString("iduser")+"';";
					rst = dbHandler.executeQ(query);
					if(!rst.next()) System.out.println("No such user ID");
					else {
						sys.add(new Systems(rs.getString("systemName"),rs.getString("iduser"), rst.getString("firstName"), rst.getString("lastName")));
					}
				}
			}
			client.sendToClient(new ObjectManager(sys, MsgEnum.VIEW_SYSTEMS));
			break;
		case CHECK_TIME_SET:
			query = "SELECT * FROM time_requests WHERE idRequest = '"+objectManager.getSelected().getIdrequest()+"'"+
					"AND type = 'Time' AND currentStage = '"+objectManager.getSelected().getCurrentStage()+"'";
			if(dbHandler.executeQ(query).next()) {
				objectManager= new ObjectManager("True", MsgEnum.SET_FLAG_TIMEC);	
			}
			else {
				objectManager= new ObjectManager("False", MsgEnum.SET_FLAG_TIMEC);
			}
			client.sendToClient(objectManager);
			break;
		
		case ADD_TIME_REQ:
			//INSERT INTO `q3t6Vm6bTC`.`time_requests` (`currentStage`, `idRequest`, `idCharge`, `timeRequested`, `type`) VALUES ('Evalution', '1', '123', '4', 'Time');

			query = "INSERT INTO time_requests (`currentStage`, `idRequest`, `idCharge`, `timeRequested`, `type`) VALUES ('"+
					objectManager.getSelected().getCurrentStage()+"','"+
					objectManager.getSelected().getIdrequest()+"','"+
					objectManager.getSelected().getIdCharge()+"','"+
					objectManager.getSelected().getTimeNeeded()+"','Time')";
			dbHandler.executeUpdate(query);
			insertStageTimeAction(objectManager.getSelected().getIdrequest(), objectManager.getSelected().getIdCharge(),
					"Time", objectManager.getSelected().getCurrentStage()); //Adds the time request for Inspector's approval
			break;
			
		case CHECK_EXTEND:
			query = "SELECT * FROM time_requests WHERE idRequest = '"+objectManager.getSelected().getIdrequest()+"'"+
					" AND type = 'Extend' AND currentStage = '"+objectManager.getSelected().getCurrentStage()+"'";
			if(dbHandler.executeQ(query).next()) {
				objectManager= new ObjectManager("True", MsgEnum.SET_EXTEND_PROCESSES);
				
			}
			else {
				objectManager= new ObjectManager("False", MsgEnum.SET_EXTEND_PROCESSES);
			}
			client.sendToClient(objectManager);
			break;
			
		case INSERT_EXTEND:
			query = "INSERT INTO time_requests (`currentStage`, `idRequest`, `idCharge`, `timeRequested`, `type`, `reason`) VALUES ('"+
					objectManager.getSelected().getCurrentStage()+"','"+
					objectManager.getSelected().getIdrequest()+"','"+
					objectManager.getSelected().getIdCharge()+"','"+
					objectManager.getSelected().getTimeNeeded()+"','Extend','"+
					objectManager.getSelected().getExtendReason()+"')";
			System.out.println(query);
			dbHandler.executeUpdate(query);
			insertStageTimeAction(objectManager.getSelected().getIdrequest(), objectManager.getSelected().getIdCharge(),
					"Extend", objectManager.getSelected().getCurrentStage()); //Adds the time request for Inspector's approval
			break;
		case VIEW_TIME: //view the time requested for approval
			String timeType = objectManager.getAction().getActionsNeeded(); //gets the action for time (Time Approval or Extend Approval)
			String arr[] = timeType.split(" ", 2); //arr[0]=Time or Extend, arr[1]=Approval
			query = "SELECT * FROM time_requests WHERE idRequest = '"+objectManager.getAction().getIdrequest()+"'"
					+" AND idCharge = '"+ objectManager.getAction().getIdCharge() +"'"
					+" AND type = '"+ arr[0] +"' AND currentStage = '"+objectManager.getAction().getStage()+"'";
			rs = dbHandler.executeQ(query);
			if(!rs.next())
				System.out.println("Error, No such time request!");
			else {
				ArrayList<String> timeReq = new ArrayList<String>();
				timeReq.add(rs.getString("idTimeRequest"));
				timeReq.add(rs.getString("timeRequested"));
				timeReq.add(rs.getString("reason"));
				timeReq.add(rs.getString("type"));
				objectManager = new ObjectManager(timeReq, MsgEnum.VIEW_TIME);
				client.sendToClient(objectManager);
			}
			break;
		case CREATE_PERFORMANCE_REPORT:
			query= "SELECT timeRequested FROM time_requests WHERE status='Approved'";
			rs=dbHandler.executeQ(query);
			ArrayList<Integer> approvedExtensions=new ArrayList<Integer>();
			ArrayList<Integer> addedActivityTime= new ArrayList<Integer>();
			while(rs.next())
			{
				approvedExtensions.add(rs.getInt("timeRequested"));
			}
			query= "SELECT e.time, r.totalTime FROM evaluation_report e, request r WHERE e.idreq=r.idrequest AND r.totalTime>e.time";
			rs=dbHandler.executeQ(query);
			while(rs.next())
			{
				addedActivityTime.add(Math.subtractExact(rs.getInt("totalTime"), rs.getInt("time")));
			}
			objectManager= new ObjectManager(approvedExtensions,addedActivityTime,MsgEnum.CREATE_PERFORMANCE_REPORT);
			client.sendToClient(objectManager);
			break;
		case APPROVE_TIME:
			ArrayList<String> timeReq = new ArrayList<>((ArrayList<String>)objectManager.getArray());
			if(timeReq.get(3).equals("Time")) { //if it is just a time request for a stage to start
				query = "UPDATE request_handling SET executionTime = '"+timeReq.get(1)+"' WHERE idrequest = '"+objectManager.getAction().getIdrequest()+"';"; //set stage time
				dbHandler.executeUpdate(query);
				deleteTimeRequest(timeReq.get(0)); //delete time request row by id of time request
				//send message to stage charge about time approval for stage
				insertMessage(objectManager.getAction().getIdCharge(), "Time Approved For Request ID: ["+objectManager.getAction().getIdrequest()+"]",
						"Time has been approved by Inspector for "+objectManager.getAction().getStage()+" stage.", "INFORM");
			}else { //if its a time extend request
				query = "UPDATE request_handling SET executionTime = executionTime+'"+timeReq.get(1)+"' WHERE idrequest = '"+objectManager.getAction().getIdrequest()+"';"; //add time to stage
				dbHandler.executeUpdate(query);
				query = "UPDATE time_requests SET status = 'Approved' WHERE idTimeRequest = "+Integer.valueOf(timeReq.get(0))+";"; //update status to approved
				dbHandler.executeUpdate(query);
				query = "SELECT iduser FROM employee WHERE role = 'Manager';"; //gets the manager ID to inform him about the time approval for extend.
				rs = dbHandler.executeQ(query);
				if(!rs.next()) System.out.println("No Manager for ICM");
				else {
					//send message about extend to Manager
					insertMessage(rs.getString("iduser"), "Time Extend Approved For Request ID: ["+objectManager.getAction().getIdrequest()+"]",
							"Time extend has been approved by Inspector for "+objectManager.getAction().getStage()+" stage." +" Days added: "+timeReq.get(1), "INFORM");
				}
				//send to stage charge a time extend approve message
				insertMessage(objectManager.getAction().getIdCharge(), "Time Extend Approved For Request ID: ["+objectManager.getAction().getIdrequest()+"]",
						"Time extend has been approved by Inspector for "+objectManager.getAction().getStage()+" stage." +" Days added: "+timeReq.get(1), "INFORM");
			}
			deleteAction(); //delete the action row for Inspector
			break;
		case REJECT_TIME:
			timeReq = new ArrayList<>((ArrayList<String>)objectManager.getArray());
			if(timeReq.get(3).equals("Time")) { //if it is just a time request for a stage to start
				//send message to stage charge about time rejected for stage
				insertMessage(objectManager.getAction().getIdCharge(), "Time Rejected For Request ID: ["+objectManager.getAction().getIdrequest()+"]",
						"Time has been rejected by Inspector for "+objectManager.getAction().getStage()+" stage. You may send a new time request.", "INFORM");
			}else { //if its a time extend request
				//send to stage charge a time extend rejected message
				insertMessage(objectManager.getAction().getIdCharge(), "Time Extend Rejected For Request ID: ["+objectManager.getAction().getIdrequest()+"]",
						"Time extend has been rejected by Inspector for "+objectManager.getAction().getStage()+" stage. You may send a new time request.", "INFORM");
			}
			deleteTimeRequest(timeReq.get(0)); //delete time request row by id of time request
			deleteAction(); //delete the action row for Inspector
			break;
		case VIEW_EMPLOYEES_TO_APPOINT_MANAGER:
			ArrayList<User> empTo = new ArrayList<>();
			query = "SELECT e.iduser, u.firstName, u.lastName, e.role FROM employee e, user u WHERE e.iduser=u.iduser AND e.role = '';"; //Select only employees who have no roles.
			rs = dbHandler.executeQ(query);
			query = "SELECT e.iduser, u.firstName, u.lastName, e.role FROM employee e, user u WHERE e.iduser=u.iduser AND e.role IS NULL;"; //for null role
			rs2 = dbHandler.executeQ(query);
			while (rs.next() == true) {
				empTo.add(new User(rs.getString("iduser"), rs.getString("firstName"), rs.getString("lastName")));
			}
			while (rs2.next() == true) {
				empTo.add(new User(rs2.getString("iduser"), rs2.getString("firstName"), rs2.getString("lastName")));
			}
			if (empTo.isEmpty())
				System.out.println("No Information Engineers To Appoint"); // change this to send error OR handle this in client side.
			else
				client.sendToClient(new ObjectManager(empTo, MsgEnum.VIEW_EMPLOYEES_TO_APPOINT_MANAGER));
			break;
		case REQUEST_FOR_INSPECTOR:
			query = "SELECT r.idrequest, r.RequestStatus, rh.currentStage FROM "
					+ "request r INNER JOIN request_handling rh ON r.idrequest = rh.idrequest";
			rs = dbHandler.executeQ(query);
			ArrayList<Request> listForInspector = new ArrayList<Request>();
			while(rs.next()) {
				Request temp = new Request();
				temp.setIdReq(rs.getString(1));
				temp.setStatus(rs.getString(2));
				
				temp.setCurrentStage(rs.getString(3));
				listForInspector.add(temp);
			}
			
			objectManager = new ObjectManager(listForInspector, MsgEnum.SET_PROCESSES_INSPECTOR);
			client.sendToClient(objectManager);
			
			break;
		case FREEZE_REQUEST:
			query = "UPDATE `request_handling` SET `status` = 'frozen' WHERE (`idrequest` = '"+objectManager.getMsgString()+"')";
			dbHandler.executeUpdate(query);
			query = "UPDATE `request` SET `RequestStatus` = 'frozen' WHERE (`idrequest` = '"+objectManager.getMsgString()+"')";
			dbHandler.executeUpdate(query);
			break;
		case UNFREEZE_REQUEST:
			query = "UPDATE `request_handling` SET `status` = '' WHERE (`idrequest` = '"+objectManager.getMsgString()+"')";
			dbHandler.executeUpdate(query);
			query = "UPDATE `request` SET `RequestStatus` = 'opened' WHERE (`idrequest` = '"+objectManager.getMsgString()+"')";
			dbHandler.executeUpdate(query);
			break;
		case REPLACE_REVIEW_LEADER:
			query = "UPDATE employee SET role = '' WHERE iduser = '"+objectManager.getUser().getIdUser()+"';"; //remove role from user
			dbHandler.executeUpdate(query);
			query = "UPDATE employee SET role = 'Review Leader' WHERE iduser = '"+objectManager.getMsgString()+"';"; //set role for user
			dbHandler.executeUpdate(query);
			break;
		case REPLACE_REVIEW_MEMBER:
			query = "UPDATE employee SET role = '' WHERE iduser = '"+objectManager.getUser().getIdUser()+"';"; //remove role from user
			dbHandler.executeUpdate(query);
			query = "UPDATE employee SET role = 'Review Member' WHERE iduser = '"+objectManager.getMsgString()+"';"; //set role for user
			dbHandler.executeUpdate(query);
			break;
		case REPLACE_INSPECTOR:
			query = "UPDATE employee SET role = '' WHERE iduser = '"+objectManager.getUser().getIdUser()+"';"; //remove role from user
			dbHandler.executeUpdate(query);
			query = "UPDATE employee SET role = 'Inspector' WHERE iduser = '"+objectManager.getMsgString()+"';"; //set role for user
			dbHandler.executeUpdate(query);
			break;
		case REPLACE_SYSTEM_CHARGE:
			query = "UPDATE systems SET iduser = '"+ objectManager.getMsgString() +"' WHERE systemName = '"+objectManager.getSystem().getSystemName()+"';"; //remove role from user
			dbHandler.executeUpdate(query);
			query = "SELECT iduser FROM systems WHERE iduser = '" + objectManager.getSystem().getIduser() + "';"; //check if he has more systems
			rs = dbHandler.executeQ(query);
			if(!rs.next()) {
				query = "UPDATE employee SET role = '' WHERE iduser = '"+objectManager.getSystem().getIduser()+"';"; //remove role from user
				dbHandler.executeUpdate(query);
			}
			query = "UPDATE employee SET role = 'Support and Maintenance' WHERE iduser = '"+objectManager.getMsgString()+"';"; //update role for user
			dbHandler.executeUpdate(query);
			break;
		case CLOSE_REQUEST:
			query = "UPDATE request SET status = 'closed' WHERE idrequest = '"+objectManager.getAction().getIdrequest()+"'";;
			dbHandler.executeUpdate(query);
			query = "DELETE FROM request_handling WHERE idrequest = "+Integer.valueOf(objectManager.getAction().getIdrequest());
			dbHandler.executeUpdate(query);
			query = "SELECT iduser, idrequest FROM request WHERE idrequest = '"+objectManager.getAction().getIdrequest()+"';"; //remove role from user
			rs = dbHandler.executeQ(query);
			if(rs.next() == true) {
				insertMessage(rs.getString("iduser"), "Finished handeling Your Request [ID: "+rs.getString("idrequest")+"]",
						"Hi, We finished handeling your request. Thank you.", "FINISH");
			}
			insertMessage(objectManager.getAction().getIdCharge(), "Finished handeling Request [ID: "+rs.getString("idrequest")+"]",
					"Hi, We finished handeling the request. Thank you.", "FINISH");
			deleteAction();
			break;
		case CREATE_PERFORMANCE_BEHIND_REPORT:
			int countNumofDelays=0;
			Map<String,String> requestsAndSystems=new HashMap<String, String>();
			Map<String,Integer> requestsAndNumOfDelays=new HashMap<String, Integer>();
			Map<String,Integer> requestsAndDelayTime=new HashMap<String, Integer>();
			query = "SELECT idrequest, ITSystem FROM request";
			rs=dbHandler.executeQ(query);
			while(rs.next())
			{
				requestsAndSystems.put(rs.getString("idrequest"), rs.getString("ITSystem"));
			}
			for(Map.Entry<String, String> entry: requestsAndSystems.entrySet())
			{
				String strForQuery="Time exception in stage for request id: " + entry.getKey();
				query= "SELECT contentMessage FROM Messages WHERE contentMessage= '"+strForQuery+"';";
				rs=dbHandler.executeQ(query);
				while(rs.next())
				{
					countNumofDelays++;
				}
				requestsAndNumOfDelays.put(entry.getValue(), countNumofDelays);
				countNumofDelays=0;
			}
			query ="SELECT r.ITSystem, e.time, r.totalTime FROM evaluation_report e, request r WHERE e.idreq=r.idrequest AND r.totalTime>e.time";
			rs=dbHandler.executeQ(query);
			while(rs.next())
			{
				requestsAndDelayTime.put(rs.getString("ITSystem"), Math.subtractExact(rs.getInt("totalTime"), rs.getInt("time")));
			}
			objectManager=new ObjectManager(requestsAndNumOfDelays, requestsAndDelayTime, MsgEnum.CREATE_PERFORMANCE_BEHIND_REPORT);
			client.sendToClient(objectManager);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Adds Evaluator appoint request to actions_needed for Inspector treat.
	 * 
	 * @param sysName The relevant system the request is about
	 * @param requestId The id of issued request
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	private void insertEvaluatorAppointAction(String sysName, String requestId) throws NumberFormatException, SQLException {
		String query;
		ResultSet rs;
		query = "SELECT iduser FROM systems WHERE systemName = '" + sysName + "'" + " AND iduser NOT IN (SELECT iduser FROM systems WHERE iduser = '');"; //Prepare the Evaluator appoint by the Inspector
		rs = dbHandler.executeQ(query);
		if(!rs.next()) //in case there is no system charge, just set charge to null
			query = "INSERT INTO actions_needed VALUES ("+Integer.valueOf(requestId)+", 'None', 'Evaluation', 'Evaluator Appointment');";
		else{ //if system has charge, appoint him as default.
			query = "INSERT INTO actions_needed VALUES ("+Integer.valueOf(requestId)+", '" 
					+ rs.getString("iduser") + "', 'Evaluation', 'Evaluator Appointment');";
		}
		dbHandler.executeUpdate(query); //execute the insert query
	}
	/**
	 * Adds Executor appoint request to actions_needed for Inspector treat.
	 * 
	 * @param requestId The id of issued request
	 */
	private void insertExecutorAppointAction(String requestId) {
		String query;
		query = "INSERT INTO actions_needed VALUES ("+Integer.valueOf(requestId)+", 'None'" 
					 + ", 'Execution', 'Executor Appointment');";
		
		dbHandler.executeUpdate(query); //execute the insert query
	}
	/**
	 * Adds time request to actions_needed table for Inspector approval (requests such as time for stage and time extend)
	 * 
	 * @param requestId The id of issued request
	 * @param idCharge The stage charge sent the time request
	 * @param timeReqType The time type (time for stage or time extend for stage)
	 * @param stage The stage who needs the time approval
	 */
	private void insertStageTimeAction(String requestId, String idCharge, String timeReqType, String stage) {
		String query;
		query = "INSERT INTO actions_needed VALUES ("+Integer.valueOf(requestId)+", '"+ idCharge +"', '" 
				+ stage+ "', '"+ timeReqType +" Approval');";
		dbHandler.executeUpdate(query); //execute the insert query
	}
	/**
	 * Deletes the row of action from actions_needed table  
	 */
	private void deleteAction() {
		String query = "DELETE FROM actions_needed WHERE stage = '"+objectManager.getAction().getStage()+
				"' AND idCharge = '"+ objectManager.getAction().getIdCharge() +
				"' AND actionsNeeded = '"+objectManager.getAction().getActionsNeeded()+
				"' AND idrequest = '"+objectManager.getAction().getIdrequest()+"';";
		dbHandler.executeUpdate(query); //delete the action from table
	}
	/**
	 * Deletes the row of time requested for stage from time_requests table by the id inserted
	 * @param idTimeRequest The time request id to be deleted from table
	 */
	private void deleteTimeRequest(String idTimeRequest) {
		String query = "DELETE FROM time_requests WHERE idTimeRequest = "+Integer.valueOf(idTimeRequest)+";";
		dbHandler.executeUpdate(query); //delete the action from table
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
		dbHandler.executeUpdate(query);
	}
}
