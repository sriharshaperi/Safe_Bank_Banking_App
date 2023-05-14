package utils;

public class SavingsAccountUtils {
	
	public static long generateAccountNumber() {
        long min = 10000000000L; // minimum 11 digit number
        long max = 99999999999L; // maximum 11 digit number
        long range = max - min + 1;
        return (long) (Math.random() * range) + min;
    }
	
	public static String getLastFourDigitsOf(long number) {
		String stringifiedNumber = number+"";
        char[] charDigits = stringifiedNumber.toCharArray();
        stringifiedNumber = "";
        for(int i = 0; i<charDigits.length; i++) {
        	if(i<charDigits.length-4) {
        		charDigits[i] = 'X';
            	stringifiedNumber += charDigits[i];
        	}
        	else stringifiedNumber += charDigits[i];
        }
        return stringifiedNumber;
    }
}
