package common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import controller.EmployeesController;
import controller.EvaluationController;
import controller.ExecutionController;
import controller.LoginController;

import controller.MessagesController;

import controller.MyRequestsController;

import controller.NewRequestController;
import controller.PerformanceBehindReportPopupController;
import controller.PerformanceReportPopupController;
import controller.ProcessInspectorController;
import controller.ProcessesController;
import controller.RequestViewController;
import controller.ReviewController;
import controller.TimeController;
import entity.ActionsNeeded;
import entity.Messages;
import entity.Request;
import entity.RequestHandling;
import entity.Systems;
import entity.User;
import sun.reflect.generics.visitor.Reifier;

//hanler for messages received from server
public class msgReceivedHandler {	
	
	@SuppressWarnings("unchecked")
	public static void msgHandler(Object msg) throws InterruptedException {
		ObjectManager objectManager = (ObjectManager)msg;
		switch(objectManager.getMsgEnum()) 
		{
		case LOGIN:
			LoginController.setUserReceived(objectManager.getUser());
			break;
		case LOGIN_ERROR:
			LoginController.errorMessage = objectManager.getMsgString();
			break;
		case SEND_ID_OF_REQUEST_TO_CLIENT:
		
			NewRequestController.setNewID(objectManager.getReqIDFromServer());
			break;

		case SEND_MESSAGES_TO_CLIENT:
			MessagesController.setListOfMessages((ArrayList<Messages>)objectManager.getArray());
			break;
		
		case SEND_RS_NOT_STARTED_TO_CLIENT:		
			
			MyRequestsController.setRsNotStarted( (ArrayList<Request>) objectManager.getArray());
			break;
		
		case SEND_RS_STARTED_TO_CLIENT:
			MyRequestsController.setRsStarted( (ArrayList<Request>) objectManager.getArray());
			break;
			
		case VIEW_EMPLOYEES:
			EmployeesController.setListOfEmployees((ArrayList<User>)objectManager.getArray());
			break;
		case VIEW_ACTIONS:
			ProcessInspectorController.setListOfActions((ArrayList<ActionsNeeded>)objectManager.getArray());
			break;
		case VIEW_PROCESSES:
			ProcessesController.setListOfProcesses((ArrayList<RequestHandling>)objectManager.getArray());
			break;
		case VIEW_REQUEST: //Gets Request by the idrequest send by user
			RequestViewController.setRequest((Request)objectManager.getReques());
			break;
		case CLIENT_EV_REP:
			EvaluationController.setReport(objectManager.getEvReport());
			break;
			
		case SET_EV_IN_REVIEW:
			ReviewController.setReport(objectManager.getEvReport());
			break;
		case SET_INFO_EXECUATION:
			ExecutionController.setInfo(objectManager.getMsgString());
			break;
		case VIEW_EMPLOYEES_TO_APPOINT:
			ProcessInspectorController.setListOfEmployees((ArrayList<User>)objectManager.getArray());
			break;
		case SET_REVIEW_EXIST:
			ReviewController.setReviewExist(objectManager.getMsgString());
		case CREATE_PERFORMANCE_REPORT:
			PerformanceReportPopupController.setApprovedExtensions((ArrayList<Integer>)objectManager.getArray());
			PerformanceReportPopupController.setAddedActivityTime((ArrayList<Integer>)objectManager.getArray2());
			break;
		case VIEW_EMPLOYEES_WITH_ROLES:
			EmployeesController.setListOfEmployees((ArrayList<User>)objectManager.getArray());
		  break;
		case SET_FLAG_TIMEC:
			TimeController.setFlag(objectManager.getMsgString());
			break;
		case SET_EXTEND_PROCESSES:
			ProcessesController.setFlag(objectManager.getMsgString());
			break;
		case VIEW_TIME:
			ProcessInspectorController.setTimeReq((ArrayList<String>)objectManager.getArray());
			break;
		case VIEW_SYSTEMS:
			EmployeesController.setListOfSystems((ArrayList<Systems>)objectManager.getArray());
			break;
		case VIEW_EMPLOYEES_TO_APPOINT_MANAGER:
			EmployeesController.setListOfEmpTo((ArrayList<User>)objectManager.getArray());
			break;
		case SET_PROCESSES_INSPECTOR:
				ProcessInspectorController.setRequestList((ArrayList<Request>) objectManager.getArray());
					break;
		case CREATE_PERFORMANCE_BEHIND_REPORT:
			PerformanceBehindReportPopupController.setDaysOfDelay((Map<String,Integer>)objectManager.getMap1());
			PerformanceBehindReportPopupController.setTimeOfDelay((Map<String,Integer>)objectManager.getMap2());
			break;
		default:
			break;
		}
	}
}
