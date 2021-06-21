package entity;

import java.io.Serializable;

public class ActionsNeeded  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1806774890242293575L;
	private String idrequest;
	private String idCharge;
	private String stage;
	private String actionsNeeded;
	
	public ActionsNeeded(String idrequest, String idCharge, String stage, String actionsNeeded) {
		this.idrequest = idrequest;
		this.idCharge = idCharge;
		this.stage = stage;
		this.actionsNeeded = actionsNeeded;
	}
	public String getIdrequest() {
		return idrequest;
	}

	public String getIdCharge() {
		return idCharge;
	}

	public String getStage() {
		return stage;
	}

	public String getActionsNeeded() {
		return actionsNeeded;
	}

	
}
