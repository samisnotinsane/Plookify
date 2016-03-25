package account;

import java.util.Calendar;

public abstract class Payment {

	private Calendar nextPaymentDate;
	private double amount;
	private boolean PaymentStatus = false;

	protected Calendar getNextPaymentDate() {
		return this.nextPaymentDate;
	}

	/**
	 * 
	 * @param nextPaymentDate
	 */
	protected void setNextPaymentDate(Calendar nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	protected double getAmount() {
		return this.amount;
	}

	/**
	 * 
	 * @param amount
	 */
	private void setAmount(float amount) {
		this.amount = 9.99;
	}

	protected abstract boolean makePayment();

	protected boolean getPaymentStatus() {
		return this.PaymentStatus;
	}

	/**
	 * 
	 * @param PaymentStatus
	 */
	protected void setPaymentStatus(boolean PaymentStatus) {
		this.PaymentStatus = PaymentStatus;
	}

}