package account;

import java.util.Calendar;

public class Paypal extends Payment {

	private String email;
	private String password;

	/**
	 * 
	 * @param emailp
	 * @param passwordp
	 */
	public Paypal(String emailp, String passwordp) {
		setEmail(emailp);
		setPassword(passwordp);
	}

	/**
	 * @return
	 */
	public boolean makePayment() {
		String singleQuery;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, +1);
		int subtype = 0;
		super.setNextPaymentDate(calendar);
		int subID = Utilities.getSubscriptionID(Authenticator.getUser().getEmail());
		singleQuery = "INSERT INTO PAYMENT(SUBSID, PAYMENTMETHOD, NEXTPAYMENTDATE) VALUES('"
					+ subID
					+ "','" + 0 + "','" + super.getNextPaymentDate().getTimeInMillis() + "')";
		//System.out.println(singleQuery);
		Utilities.executeSingleQuery(singleQuery);
		singleQuery = "INSERT INTO PAYPAL(PAYMENTID, EMAIL, PASSWORD) VALUES('" + Utilities.getPaymentID(
				Authenticator.getUser().getEmail()
		)
						+ "','" + getEmail() + "','" + getPassword() + "')";
		//System.out.println(singleQuery);
		Utilities.executeSingleQuery(singleQuery);
		super.setPaymentStatus(true);
		if(super.getPaymentStatus()){
			subtype = 1;
		}
		singleQuery = "UPDATE USERSUBSCRIPTION SET SUBSTYPE='" + subtype + "' WHERE USERID='" +
				Utilities.getUserID(Authenticator.getUser().getEmail()) + "'";
		Utilities.executeSingleQuery(singleQuery);
		Authenticator.getUser().setSubscriptionType(true);
		return super.getPaymentStatus();
	}

	private String getEmail() {
		return this.email;
	}

	/**
	 * 
	 * @param email
	 */
	private void setEmail(String email) {
		this.email = email;
	}

	private String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param password
	 */
	private void setPassword(String password) {
		this.password = password;
	}

	public boolean removePaymentInfo() {
		// TODO - implement Paypal.removePaymentInfo
		throw new UnsupportedOperationException();
	}

}