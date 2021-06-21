package entity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import common.MyFile;
/**
 * 
 * this class represents a request that was submitted to ICM
 *
 */
public class Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idReq;
	private String curState;
	private String requestedChange;
	private String purpose;
	private String comments;
	private String system;
	private String opentDate;
	private String idUser;
	private String status;
	private String currentStage;
	private String stageDueDate;
	private String totalTime;
	/**
	 * constructor with the following fields
	 * @param idReq
	 * @param curState
	 * @param requestedChange
	 * @param purpose
	 * @param comments
	 * @param system
	 * @param opentDate
	 * @param idUser
	 * @param status
	 * @param totalTime
	 */
	public Request(String idReq, String curState, String requestedChange, String purpose, String comments,
			String system, String opentDate, String idUser, String status, String totalTime) {
		this.idReq = idReq;
		this.curState = curState;
		this.requestedChange = requestedChange;
		this.purpose = purpose;
		this.comments = comments;
		this.system = system;
		this.opentDate = opentDate;
		this.idUser = idUser;
		this.status = status;
		this.totalTime = totalTime;
	}
	/**
	 * constructor with the following fields
	 * @param id
	 * @param duedate
	 * @param stage
	 */
	public Request(String id,String duedate, String stage) {
		idReq=id;
		
		stageDueDate = duedate;
		currentStage=stage;
	}
	/**
	 * empty constructor
	 */
	public Request() {
		
	}
	
	/**
	 * return the id of the request
	 * @return the id of the request
	 */
	public String getIdReq() {
		return idReq;
	}
	/**
	 * set the id of the request
	 * @param idReq - the id of the request
	 */
	public void setIdReq(String idReq) {
		this.idReq = idReq;
	}
	/**
	 * return the current stage of the request
	 * @return the current stage of the request
	 */
	public String getCurState() {
		return curState;
	}
	/**
	 * set the current stage of the request
	 * @param curState - the current stage of the request
	 */
	public void setCurState(String curState) {
		this.curState = curState;
	}
	/**
	 * return the change the user requested
	 * @return the change the user requested 
	 */
	public String getRequestedChange() {
		return requestedChange;
	}
	/**
	 * set the change the user requested
	 * @param requestedChange - the change the user requested
	 */
	public void setRequestedChange(String requestedChange) {
		this.requestedChange = requestedChange;
	}
	/**
	 * get the purpose of the change
	 * @return  the purpose of the change
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * set  the purpose of the change
	 * @param purpose -  the purpose of the change
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	/**
	 * get the comment the user submitted
	 * @return  the comment the user submitted
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * set get the comment the user submitted
	 * @param comments -  the comment the user submitted
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * the system the request belongs to 
	 * @return the system the request belongs to
	 */
	public String getSystem() {
		return system;
	}
	/**
	 * set the system the request belongs to
	 * @param system - the system the request belongs to
	 */
	public void setSystem(String system) {
		this.system = system;
	}
	/**
	 * set the totalTime the request belongs to
	 * @param totalTime - the totalTime the request belongs to
	 */
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	/**
	 * the date the request was submitted
	 * @return the date the request was submitted
	 */
	public String getOpentDate() {
		return opentDate;
	}
	/**
	 * set the date the request was submitted
	 * @param opentDate - the date the request was submitted
	 */
	public void setOpentDate(String opentDate) {
		this.opentDate = opentDate;
	}
	/**
	 * thd id of the user who submitted the request
	 * @return thd id of the user who submitted the request
	 */
	public String getIdUser() {
		return idUser;
	}
	/**
	 * set thd id of the user who submitted the request
	 * @param idUser - thd id of the user who submitted the request
	 */
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	/**
	 * the status of the request - opened/no started/frozen/closed
	 * @return the status of the request - opened/no started/frozen/closed
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * the status of the request
	 * @param status - the status of the request
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * the current stage the request is at
	 * @return - the current stage the request is at
	 */
	public String getCurrentStage() {
		return currentStage;
	}
	/**
	 * set the current stage the request is at
	 * @param currentStage - the current stage the request is at
	 */
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	/**
	 * the date the stage is supposed to end
	 * @return the date the stage is supposed to end
	 */
	public String getStageDueDate() {
		return stageDueDate;
	}
	/**
	 * set the date the stage is supposed to end
	 * @param stageDueDate - the date the stage is supposed to end
	 */
	public void setStageDueDate(String stageDueDate) {
		this.stageDueDate = stageDueDate;
	}
	/**
	 * returns a list containing all the request's information
	 * @return a list containing all the request's information
	 */
	public List<String> getAll(){
		List<String> list = new ArrayList<>();
		list.add(idReq);
		list.add(curState);
		list.add(requestedChange);
		list.add(purpose);
		list.add(comments);
		list.add(system);
		list.add(opentDate);
		list.add(idUser);
		list.add(status);
		list.add(totalTime);
		return list;
	}
	
	

}
