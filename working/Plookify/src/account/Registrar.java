package account;

public class Registrar {

	private String firstName;
	private String lastName;
	private String addressLine;
	private String city;
	private String postCode;
	private String email;
	private String password;
	private boolean registrationStatus = false;
	private boolean discoverable = false;

	/**
	 * 
	 * @param firstName
	 */
	protected void setFirstName(String firstName){
		this.firstName = firstName;
	}

	/**
	 * 
	 * @param lastName
	 */
	protected void setLastName(String lastName)  {
		this.lastName = lastName;
	}

	/**
	 * 
	 * @param addressLine
	 */
	protected void setAddressLine(String addressLine)  {
		this.addressLine = addressLine;
	}

	/**
	 * 
	 * @param city
	 */
	protected void setCity(String city)  {
		this.city = city;
	}

	/**
	 * 
	 * @param postCode
	 */
	protected void setPostCode(String postCode)  {
		this.postCode = postCode;
	}

	/**
	 * 
	 * @param email
	 */
	protected void setEmail(String email)  {
		this.email = email;
	}

	/**
	 * 
	 * @param password
	 */
	protected void setPassword(String password) {
		this.password = password;
	}

	protected void setDiscoverable(boolean discoverable){
		this.discoverable = discoverable;
	}

	protected boolean registrate() {
		User user;
		user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setAddressLine(addressLine);
		user.setCity(city);
		user.setPostCode(postCode);
		user.setPassword(password);
		user.setSubscriptionType(false);
		user.setStatus(true);
		String singleQuery;
		int userStatus = 0;
		int substatus = 0;
		singleQuery = "INSERT INTO USER(EMAIL, PASSWORD) VALUES('" + user.getEmail() + "','" + user.getPassword()
				+ "')";
		Utilities.executeSingleQuery(singleQuery);
        /*
        User must be created First before executing other queries involving Foreign ID
        Couldn't use Utilities.getUserID before creating user
         */
		singleQuery = "INSERT INTO USERDETAILS(USERID, FIRSTNAME, LASTNAME) VALUES('" + Utilities.getUserID(user.getEmail())
				+ "','" + user.getFirstName() + "','" + user.getLastName() + "')";
		Utilities.executeSingleQuery(singleQuery);
		//checking is the substatus is true. Changing the substatus to 0 or 1 depending on that
		if(user.getSubscriptionType()){
			substatus = 1;
		}
		singleQuery = "INSERT INTO USERSUBSCRIPTION(USERID,SUBSTYPE) VALUES('" + Utilities.getUserID(user.getEmail())
				+ "','" + substatus + "')";
		Utilities.executeSingleQuery(singleQuery);
		//checking if the userstatus is true. Changing the userstatus variable to 0 or 1 depending on that
		if(user.getStatus()){
			userStatus = 1;
		}
		singleQuery = "INSERT INTO USERSTATUS(USERID, STATUS) VALUES('" + Utilities.getUserID(user.getEmail())
				+ "','" + userStatus + "')";
		//System.out.println(singleQuery);
		Utilities.executeSingleQuery(singleQuery);
		singleQuery = "INSERT INTO ADDRESS(USERID, ADDRESSLINE, CITY, POSTCODE) VALUES('"
				+ Utilities.getUserID(user.getEmail()) + "','"
				+ user.getAddressLine() + "','"
				+ user.getCity() + "','"
				+ user.getPostCode() + "')";
		Utilities.executeSingleQuery(singleQuery);
		singleQuery = "INSERT INTO disctbl(user_id, disc) VALUES('" +
				Utilities.getUserID(user.getEmail()) + "','" +
				0 + "')";
		Utilities.executeSingleQuery(singleQuery);
		Authenticator authenticator = new Authenticator(user.getEmail(), user.getPassword());
		if(authenticator.Authenticate()) {
			setRegistrationStatus(true);
		}
		return this.registrationStatus;
	}

	public boolean getRegistrationStatus() {
		return this.registrationStatus;
	}

	/**
	 * 
	 * @param registrationStatus
	 */
	private void setRegistrationStatus(boolean registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public Registrar() {
		setRegistrationStatus(false);
	}
}