package account;

/**
 * This class is responsible for making credit card payment
 */

import java.util.Calendar;

public class CreditCard extends Payment {

	private long longDigit;
	private int CSV;
	private int Expirydate;

	private long getLongDigit() {
		return this.longDigit;
	}

	/**
	 * 
	 * @param longDigit
	 */
	private void setLongDigit(long longDigit) {
		this.longDigit = longDigit;
	}

	private int getCSV() {
		return this.CSV;
	}

	/**
	 * 
	 * @param CSV
	 */
	private void setCSV(int CSV) {
		this.CSV = CSV;
	}

	private int getExpirydate() {
		return this.Expirydate;
	}

	/**
	 * 
	 * @param Expirydate
	 */
	private void setExpirydate(int Expirydate) {
		this.Expirydate = Expirydate;
	}

	/**
	 * This constructor takes the details of Credit Card
	 * @param longDigitp
	 * @param CSVp
	 * @param ExpiryDatep
	 */
	public CreditCard(long longDigitp, int CSVp, int ExpiryDatep) {
		setCSV(CSVp);
		setLongDigit(longDigitp);
		setExpirydate(ExpiryDatep);
	}

	/**
	 * This method is a overridden from the Superclass Payment. This inserts the credit card data into the database
	 * if everything is okay then it returns true
	 * @return Payment Status
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
				+ "','" + 1 + "','" + super.getNextPaymentDate().getTimeInMillis() + "')";
		//System.out.println(singleQuery);
		Utilities.executeSingleQuery(singleQuery);
		singleQuery = "INSERT INTO CREDITCARD(PAYMENTID, CSV, LONGDIGIT, EXPIRYDATE) VALUES('" + Utilities.getPaymentID(
				Authenticator.getUser().getEmail()
		)
				+ "','" + getCSV() + "','" + getLongDigit() + "','" + getExpirydate() + "')";
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
}