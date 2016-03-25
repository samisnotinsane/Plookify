package account;

import java.util.ArrayList;
import java.util.Calendar;

public class User {

	private String firstName;
	private String lastName;
	private String addressLine;
	private String city;
	private String postCode;
	private boolean status;
	private boolean subscriptionType;
	private String email;
	private String password;

	public boolean getSubscriptionType() {
		return this.subscriptionType;
	}

	/**
	 * 
	 * @param subscriptionType
	 */
	protected void setSubscriptionType(boolean subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public boolean getStatus() {
		return this.status;
	}

	/**
	 * 
	 * @param status
	 */
	protected void setStatus(boolean status) {
		this.status = status;
	}

	public String getPostCode() {
		return this.postCode;
	}

	/**
	 * 
	 * @param postCode
	 */
	protected void setPostCode(String postCode) {
		if(postCode.length() < 8){
			this.postCode = postCode;
		}else{
			System.out.println("Could not set postcode");
		}
	}

	public String getCity() {
		return this.city;
	}

	/**
	 * 
	 * @param city
	 */
	protected void setCity(String city) {
		this.city = city;
	}

	public String getAddressLine() {
		return this.addressLine;
	}

	/**
	 * 
	 * @param addressLine
	 */
	protected void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getLastName() {
		return this.lastName;
	}

	/**
	 * 
	 * @param lastName
	 */
	protected void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * 
	 * @param firstName
	 */
	protected void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void getPaymentStatus() {
		// TODO - Get Payment Status
		//Payment payment = Utilities.getPayment(Authenticator.getUser().getEmail());
	}

	/**
	 * 
	 * @param paymentType
	 */

	public String getEmail() {
		return this.email;
	}

	/**
	 * 
	 * @param email
	 */
	protected void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @param oldPassword
	 * @param newPassword
	 */
	public void changePassword(String oldPassword, String newPassword) {
		// TODO - implement User.changePassword
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	protected boolean addDevice(String name) {
		// TODO - implement User.addDevice
		throw new UnsupportedOperationException();
	}

	public ArrayList<Device> getDevicesList() {
		// TODO - implement User.getDevicesList
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	protected boolean removeDevice(String name) {
		// TODO - implement User.removeDevice
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	protected boolean replaceDevice(String name) {
		// TODO - implement User.replaceDevice
		throw new UnsupportedOperationException();
	}
	protected void setPassword(String password){
		this.password = password;
	}
	protected String getPassword(){
		return this.password;
	}
}