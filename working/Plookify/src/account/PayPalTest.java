package account;

import account.Payment;
import account.Paypal;

/**
 * Created by tahnik on 15/02/2016.
 */
public class PayPalTest {
    protected static void PayPalObjectTest(){
        Payment payment = new Paypal("tahnik@live.co.ijo", "268ds");
        System.out.println(payment.makePayment());
    }
}