package entity;

import java.io.Serializable;
/**
 * 
 * a user of ICM and his information such as: id, name, password, email,role, department
 *
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2938298699894499558L;
	private String idUser;
	private String firstName;
	private String lastName;
	private String email;
	private String role;
	private String department;
	private String password;
	/**
	 * constructor with the following fields
	 * @param idUser
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param role
	 * @param department
	 * @param password
	 */
	public User(String idUser, String firstName, String lastName, String email, String role, String department, String password) {
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.department = department;
		this.password = password;
	}
	/**
	 * constructor with the following fields
	 * @param idUser
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param role
	 * @param department
	 */
	public User(String idUser, String firstName, String lastName, String email, String role, String department) { //for employee
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.department = department;
	}
	/**
	 * constructor with the following fields
	 * @param idUser
	 * @param password
	 */
	public User(String idUser, String password) { //for user login
		this.idUser = idUser;
		this.password = password;
	}
	/**
	 * constructor with the following fields
	 * @param idUser
	 * @param firstName
	 * @param lastName
	 */
	public User(String idUser, String firstName, String lastName) { //to show only specific data
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * constructor with the following fields
	 * @param idUser
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param role
	 * @param department
	 */
	public User(String idUser, String firstName, String lastName, String email, String role) { //for employee with role
		this.idUser = idUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
	}
	/**
	 * the id of the user
	 * @return the id of the user
	 */
	public String getIdUser() {
		return idUser;
	}
/**
 * the first name of the user
 * @return the first name of the user
 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * the last name of the user
	 * @return the last name of the user
	 */

	public String getLastName() {
		return lastName;
	}
/**
 * the email of the user
 * @return the email of the user
 */
	public String getEmail() {
		return email;
	}
/**
 * the role of the user
 * @return the role of the user
 */
	public String getRole() {
		return role;
	}
/**
 * the department of the user
 * @return the department of the user
 */
	public String getDepartment() {
		return department;
	}
/**
 * the password of the user
 * @return the password of the user
 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * set the department for user
	 * @param department the department for user
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
}
