package common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.*;
/**
 * The object we send to the server and communicate with.
 *
 */
public class ObjectManager implements Serializable {

	private static final long serialVersionUID = 261834733778535924L;
	private MsgEnum msgEnum;
	private String msgString;
	private boolean evFlag;
	private ResultSet rs;
	
	//Entities
	private User user;
	private Request req;
	private List<Document> listOfFiles;
	private Integer reqIDFromServer;
	private ArrayList<?> array,array2;
	private Map<?,?> map1,map2;
	private EvaluationReport evReport;
	private ActionsNeeded action;
	private RequestHandling selected;
	private Systems system;	
	public boolean isEvFlag() {
	return evFlag;
	}

	public void setEvFlag(boolean evFlag) {
	this.evFlag = evFlag;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public ObjectManager(ResultSet rs,MsgEnum msgEnum) {
		
		this.msgEnum = msgEnum;
		this.rs = rs;
	}
  public ObjectManager(EvaluationReport evReport, MsgEnum msgEnum) {
		this.msgEnum = msgEnum;
		this.evReport = evReport;
	}
	public ObjectManager(Integer reqIDFromServer, MsgEnum msgEnum) {
		
		this.msgEnum = msgEnum;
		this.reqIDFromServer = reqIDFromServer;
	}

	public Integer getReqIDFromServer() {
		return reqIDFromServer;
	}

	public void setReqIDFromServer(Integer reqIDFromServer) {
		this.reqIDFromServer = reqIDFromServer;
	}

	public ObjectManager(User user, MsgEnum msgEnum) { //user login request
		this.user = user;
		this.msgEnum = msgEnum;
	}
	public ObjectManager(String id, User user, MsgEnum msgEnum) {
		this.msgString = id;
		this.user = user;
		this.msgEnum = msgEnum;
	}
	public ObjectManager(String id, Systems system, MsgEnum msgEnum) {
		this.msgString = id;
		this.system = system;
		this.msgEnum = msgEnum;
	}
	public ObjectManager(String msgString, MsgEnum msgEnum) { //for any string handler
		this.msgString = msgString;
		this.msgEnum = msgEnum;
	}
	
	public ObjectManager(Request req, MsgEnum msgEnum) {
		this.msgEnum = msgEnum;
		this.req = req;
	}
	
	public ObjectManager(String msgString, ActionsNeeded action, MsgEnum msgEnum) {
		this.msgEnum = msgEnum;
		this.action = action;
		this.msgString = msgString;
	}
	
	public ObjectManager(List<Document> listOfFiles, MsgEnum msgEnum) {
		this.msgEnum = msgEnum;
		this.listOfFiles=listOfFiles;
		
	}
	public ObjectManager(ArrayList<?> array, MsgEnum msgEnum) { //for arraylists
		this.msgEnum = msgEnum;
		setArray(array);
	}

	public ObjectManager(RequestHandling selected, MsgEnum msgEnum) {
		this.selected = selected;
		this.msgEnum= msgEnum;	
	}

	public ObjectManager(ActionsNeeded action, MsgEnum msgEnum) {
		this.action = action;
		this.msgEnum= msgEnum;	
	}
	public ObjectManager(ArrayList<?> array, ActionsNeeded action, MsgEnum msgEnum) { //for time request action delete
		this.action = action;
		this.msgEnum= msgEnum;
		setArray(array);
	}
	public ObjectManager(ArrayList<?> array, ArrayList<?> array2, MsgEnum msgEnum) {
		this.array=array;
		this.array2=array2;
		this.msgEnum=msgEnum;
	}
	public ObjectManager(Map<?,?> map1, Map<?,?> map2,MsgEnum msgEnum) {
		this.map1=map1;
		this.map2=map2;
		this.msgEnum=msgEnum;
	}
	public Map<?, ?> getMap1() {
		return map1;
	}

	public Map<?, ?> getMap2() {
		return map2;
	}

	public List<Document> getListOfFiles(){
		return listOfFiles;
	}
	
	public ArrayList<?> getArray() {
		return array;
	}
	public void setArray(ArrayList<?> array) {
		this.array = array;
	}
	public User getUser() { //get user
		return user;
	}
	public MsgEnum getMsgEnum() { //get MsgEnum
		return msgEnum;
	}
	public ArrayList<?> getArray2() {
		return array2;
	}

	public String getMsgString() {
		return msgString;
	}
	
	public Request getReques() {
		return req;
	}
    public EvaluationReport getEvReport() {
		return evReport;
	}

	public void setEvReport(EvaluationReport evReport) {
		this.evReport = evReport;
	}
	public ActionsNeeded getAction() {
		return action;
	}

	public RequestHandling getSelected() {
		return selected;
	}

	public void setSelected(RequestHandling selected) {
		this.selected = selected;
	}
	public Systems getSystem() {
		return system;
	}
	

}
