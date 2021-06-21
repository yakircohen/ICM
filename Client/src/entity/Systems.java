package entity;

import java.io.Serializable;

public class Systems  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3842062925716052805L;
	private String systemName;
	private String iduser;
	private String firstName;
	private String lastName;
	
	public Systems(String systemName, String iduser, String firstName, String lastName) {
		this.systemName = systemName;
		this.iduser = iduser;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getSystemName() {
		return systemName;
	}

	public String getIduser() {
		return iduser;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
}
