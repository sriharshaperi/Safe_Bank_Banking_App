package utils;

public class TransactionUtils {
	
	public static int generateOTP() {
        int min = 100000; // minimum 6 digit number
        int max = 999999; // maximum 6 digit number
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }
}
