package entity;
/**
 * 
 * this class represents employee of ICM and its information 
 * extends User
 *
 */
public class Employee extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8670639212770164750L;
	
	private String empRole;
	/**
	 * An Employee constructor with the following parameters
	 * @param idUser - for super class
	 * @param firstName - for super class
	 * @param lastName - for super class
	 * @param email - for super class
	 * @param role - for super class
	 * @param department - for super class
	 * @param empRole - for this class
	 */
	public Employee(String idUser, String firstName, String lastName, String email, String role, String department, String empRole) {
		super(idUser, firstName, lastName, email, role, department);
		this.empRole = empRole;
	}
	/**
	 * set the role of the employee
	 * @param empRole - the role of the employee 
	 */
	public void setEmployeeRole(String empRole) {
		this.empRole = empRole;
	}
	/**
	 * get the role of the employee
	 * @return the role of the employee
	 */
	public String getEmploteeRole() {
		return empRole;
	}
	
}
