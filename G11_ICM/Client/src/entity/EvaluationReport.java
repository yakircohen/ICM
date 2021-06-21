package entity;

import java.io.Serializable;
/**
 * 
 * this class represents Evaluation Report submitted by the evaluator 
 *
 */

public class EvaluationReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2814323715285014273L;
	
	private String location;
	private String description;
	private String result;
	private String risk;
	private String time;
	private String idReq;
	
	/**
	 * empty constructor for creating JavaBean class
	 */
	public EvaluationReport() {
		
	}
	
	/**
	 * this getter return the id of the request this report belongs to
	 * @return the id of the request this report belongs to
	 */
	public String getIdReq() {
		return idReq;
	}
/**
 * this setter set the id of the request this report belongs to
 * @param idReq - the id of the request this report belongs to
 */
	public void setIdReq(String idReq) {
		this.idReq = idReq;
	}
/**
 *  this getter return the location of the change
 * @return the location of the change 
 */
	public String getLocation() {
		return location;
	}
	/**
	 * this setter set the location of the change
	 * @param location - the location of the change
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * this getter return the description of the change
	 * @return the description of the change
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * this setter set the description of the change
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * this getter return the result of the change
	 * @return the result of the change
	 */
	public String getResult() {
		return result;
	}
	/**
	 * this setter set the result of the change
	 * @param result - the result of the change
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * this getter return the risk of the change
	 * @return the risk of the change
	 */
	public String getRisk() {
		return risk;
	}
	/**
	 * this setter set the risk of the change
	 * @param risk - the risk of the change
	 */
	public void setRisk(String risk) {
		this.risk = risk;
	}
	/**
	 * this getter return time evaluation it should take to finish this change
	 * @return time evaluation of the change
	 */
	public String getTime() {
		return time;
	}
	/**
	 * this setter set time evaluation of the change
	 * @param time - time evaluation of the change
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	

}
