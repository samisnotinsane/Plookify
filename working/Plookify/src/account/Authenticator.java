package account;

/*
This method has reached version 1. It is working perfectly now.
This method is responsible for authenticating a user
 */
import account.PopupBoxes.PaymentPopUp;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.util.resources.cldr.ebu.CalendarData_ebu_KE;

import java.sql.*;
import java.util.Calendar;

public class Authenticator {
	private static boolean AuthenticateStatus = false;
	private static User user = null;
	private String email;
	private String password;

	/**
	 * This method is static to let other package get the current user
	 * Will return null if there's no user is logged in. Exception must be caught
	 * @return User
     */
	public static User getUser() {
		return user;
	}

	/**
	 * sets the user to the logged in user.
	 * can be only set by this class
	 * @param userp
	 */
	private void setUser(User userp) {
		user = userp;
	}

	/**
	 * this method will return the authentication status.
	 * Other packages can check if any user is logged in via this method
	 * static so other packges can find it
	 * @return boolean
     */
	public static boolean getAuthenticateStatus() {
		return AuthenticateStatus;
	}

	/**
	 *
	 * @param AuthenticateStatusp
	 */
	private void setAuthenticateStatus(boolean AuthenticateStatusp) {
		AuthenticateStatus = AuthenticateStatusp;
	}

	/**
	 * Constructor for Authenticator, takes email and password
	 * @param emailp
	 * @param passwordp
	 */
	protected Authenticator(String emailp, String passwordp) {
		setEmail(emailp);
		setPassword(passwordp);
	}

	/**
	 * This method authenticates the user. It uses getUserExists() to check if the user exists first
	 * then it checks for the password using checkPassword()
	 * @return AuthenticateStatus
	 */
	protected boolean Authenticate() {
		if(getUserExists()){
			if(checkPassword()){
				user = Utilities.getUser(email);

				/**
				 * Below I am checking if the user has a premium account. If he is premium then it checks if the user's
				 * payment is due. If today is before or after 2 days of payment due date then it shows a popup saying
				 * if the don't change it it will revert to free. If they don't do it within those days then after two
				 * days of payment due date it will revert to free
				 */

				Calendar today = Calendar.getInstance();
				Calendar twoDayBefore = Calendar.getInstance();
				Calendar twoDayAfter = Calendar.getInstance();
				Calendar payment = Calendar.getInstance();
				Calendar expire = Calendar.getInstance();
				if(user.getSubscriptionType()){
					try {
						payment.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
						twoDayAfter.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
						twoDayBefore.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
						expire.setTimeInMillis(Utilities.getNextPaymentDate(Authenticator.getUser().getEmail()));
						expire.add(Calendar.DATE, 2);
						twoDayBefore.add(Calendar.DATE, -2);
						twoDayAfter.add(Calendar.DATE, 2);
						System.out.println("TwoDayBefore: " + twoDayBefore.getTime());
						System.out.println("TwoDayAfter: " +twoDayAfter.getTime());
						if (today.after(twoDayBefore) && today.before(twoDayAfter)) {
							new PaymentPopUp();
						}else if(today.after(expire)){
							String singleQuery = "UPDATE USERSUBSCRIPTION SET SUBSTYPE='" + 0 + "' WHERE USERID='" +
									Utilities.getUserID(Authenticator.getUser().getEmail()) + "'";
							//System.out.println(Utilities.getUserID(Authenticator.getUser().getEmail()));
							Utilities.executeSingleQuery(singleQuery);
							Authenticator.getUser().setSubscriptionType(false);
							if(Utilities.paymentExists()){
								Utilities.removePayment();
							}
						}
					}catch (Exception e){
						//
					}
				}
				/**
				 * If everything is fine then set the authentication status to true
				 */
				AuthenticateStatus = true;
			}
		}
		/**
		 * If everything is not fine then set the authentication status to false
		 */
		return AuthenticateStatus;
	}

	/**
	 * This method checks if the user exists by checking the current email with database
	 * @return exist
	 */
	private boolean getUserExists(){
		Connection connection = null;
		boolean exist = false;
		try{
			connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			String query = "SELECT COUNT(EMAIL) AS TOTAL FROM USER WHERE EMAIL='" + email + "'";
			ResultSet rs = statement.executeQuery(query);
			if(rs.getInt("TOTAL") != 0){
				exist = true;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try{
				if(connection != null)
					connection.close();
			}catch (SQLException e){
				System.err.println(e);
			}
		}
		return exist;
	}

	/**
	 * This method checks if the password is correct by checking it with database
	 * @return boolean
	 */
	private boolean checkPassword(){
		Connection connection = null;
		boolean exist = false;
		try{
			connection = DriverManager.getConnection("jdbc:sqlite:bin/test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			String query = "SELECT PASSWORD FROM USER WHERE EMAIL='" + email + "'";
			ResultSet rs = statement.executeQuery(query);
			if(rs.getString("PASSWORD").equals(password)){
				exist = true;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try{
				if(connection != null)
					connection.close();
			}catch (SQLException e){
				System.err.println(e);
			}
		}
		return exist;
	}
	/**
	 * 
	 * @param email
	 */
	private void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @param password
	 */
	private void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method will set the user to null and authenticationStatus to false
	 * @return boolean
	 */
	public static boolean logout() {
		user = null;
		AuthenticateStatus = false;
		return true;
	}

}