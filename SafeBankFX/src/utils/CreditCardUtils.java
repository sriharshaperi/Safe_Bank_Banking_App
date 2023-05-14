package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import enums.CardCategory;
import enums.CardProvider;
import models.CreditCard;
import models.User;

public class CreditCardUtils {

	public static double getTotalCreditLimit(CardCategory cardCategory) {
		double totalCreditLimit = 0;
		if (cardCategory.equals(CardCategory.BURGUNDY))
			totalCreditLimit = 10000;
		else if (cardCategory.equals(CardCategory.GOLD))
			totalCreditLimit = 20000;
		else if (cardCategory.equals(CardCategory.PLATINUM))
			totalCreditLimit = 30000;
		return totalCreditLimit;
	}

	public static long generateCardNumber() {
		long min = 1000000000000000L; // minimum 16 digit number
		long max = 9999999999999999L; // maximum 16 digit number
		long range = max - min + 1;
		long randomNum = (long) (Math.random() * range) + min;
		String randNumStr = randomNum + "";
		int[] cardPrefixes = { 4, 5, 6, 34, 37 };
		if (!randNumStr.startsWith("4") || !randNumStr.startsWith("5") || !randNumStr.startsWith("6")
				|| !randNumStr.startsWith("34") || !randNumStr.startsWith("37")) {
			int prefix = cardPrefixes[(int) Math.ceil(Math.random() * 4)];

			if (prefix == 34 || prefix == 37) {
				randNumStr = prefix + randNumStr.substring(2);
			} else {
				randNumStr = prefix + randNumStr.substring(1);
			}
			return Long.parseLong(randNumStr);
		} else {
			return randomNum;
		}
	}

	public static int generatePin() {
		int min = 1000; // minimum 4 digit number
		int max = 9999; // maximum 4 digit number
		return (int) (Math.random() * (max - min + 1)) + min;
	}

	public static int generateSecurityCode() {
		int min = 100; // minimum 3 digit number
		int max = 999; // maximum 3 digit number
		return (int) (Math.random() * (max - min + 1)) + min;
	}

	public static CardProvider getCardProvider(double cardNumber) {
		String cardNumberString = cardNumber + "";
		if (cardNumberString.startsWith("4"))
			return CardProvider.VISA;
		else if (cardNumberString.startsWith("5"))
			return CardProvider.MASTER_CARD;
		else if (cardNumberString.startsWith("6"))
			return CardProvider.AMERICAN_EXPRESS;
		return CardProvider.DISCOVER;
	}

	public static Date getValidThru(Date inputDate) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inputDate);
		calendar.add(Calendar.MINUTE, 15);
		return calendar.getTime();
	}

	public static Date createPaymentDueDate(Date date, int days) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	public static CardCategory getCardCategory(User user) {
		int creditScore = user.getCreditScore();

		boolean burgundyCardRange = creditScore >= 720 && creditScore <= 759;
		boolean goldCardRange = creditScore >= 760 && creditScore <= 819;
		boolean platinumCardRange = creditScore >= 820;

		System.out.println(user.getCreditCard());
		if (user.getCreditCard() == null || burgundyCardRange)
			return CardCategory.BURGUNDY;
		else if (goldCardRange)
			return CardCategory.GOLD;
		else if (platinumCardRange)
			return CardCategory.PLATINUM;
		return CardCategory.BURGUNDY;
	}

	public static CardCategory getEligibleCreditCard(int creditScore, User user) {

		boolean burgundyCardRange = creditScore >= 720 && creditScore <= 759;
		boolean goldCardRange = creditScore >= 760 && creditScore <= 819;
		CardCategory cardCategory = user.getCreditCard().getCardCategory();
		if (burgundyCardRange)
			return CardCategory.BURGUNDY;
		else if (goldCardRange) {
			if(cardCategory == null)
				return CardCategory.BURGUNDY;
			return CardCategory.GOLD;
		}
		else {
			if(cardCategory == null)
				return CardCategory.BURGUNDY;
			else if(cardCategory == CardCategory.BURGUNDY)
				return CardCategory.GOLD;
			else
				return CardCategory.PLATINUM;	
		}
	}

	public static Date generateCCDueDateAfterTwoMin(Date inputDate) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inputDate);
		calendar.add(Calendar.MINUTE, 2);
		return calendar.getTime();
	}

	/**
	 * depends on getSeventhOfNextMonth() depends on addOneMonthToDate(String
	 * inputDateStr)
	 */
	public static Date generateDueDateForCreditCard() {
		LocalDate currentDate = LocalDate.now(); // get current date
		int currentMonth = currentDate.getMonthValue(); // get current month value
		int currentYear = currentDate.getYear(); // get current year value

		// create a LocalDate object for the 18th day of current month
		LocalDate eighteenthDayOfCurrentMonth = LocalDate.of(currentYear, currentMonth, 18);

		// create a LocalDate object for the input date
		LocalDate inputDate = LocalDate.of(2023, 3, 20); // replace with your input date

		// compare input date with 18th day of current month
		try {
			if (inputDate.isBefore(eighteenthDayOfCurrentMonth) || inputDate.isEqual(eighteenthDayOfCurrentMonth)) {
				Date seventhOfNextMonth = getSeventhOfNextMonth();
				return seventhOfNextMonth;
			} else {
				Date seventhOfNextToNextMonth = getSeventhOfNextMonth();
				Date dateAfterAddingOneMonth = addOneMonthToDate(seventhOfNextToNextMonth.toString());
				return dateAfterAddingOneMonth;
			}
		} catch (ParseException e) {
			return new Date();
		}
	}

	/**
	 * depends on getFullFormattedDate(String inputDate)
	 */
	public static Date getSeventhOfNextMonth() throws ParseException {
		LocalDate currentDate = LocalDate.now(); // get current date
		LocalDate nextMonth = currentDate.plusMonths(1); // get date of next month
		LocalDate seventhOfNextMonth = LocalDate.of(nextMonth.getYear(), nextMonth.getMonthValue(), 8); // create date
																										// for 7th of
																										// next month
		return getFullFormattedDate(seventhOfNextMonth.toString());
	}

	public static Date getFullFormattedDate(String inputDate) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = inputFormat.parse(inputDate); // convert input string to Date object

		SimpleDateFormat outputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		outputFormat.setTimeZone(TimeZone.getTimeZone("America/New_York")); // set time zone to Eastern Time (EDT)
		String outputDate = outputFormat.format(date); // format the date to desired string

		System.out.println(outputDate); // print the formatted date

		// convert formatted string to Date object
		Date outputDateObj = outputFormat.parse(outputDate);
		return outputDateObj;
	}

	public static Date addOneMonthToDate(String inputDateStr) {
		// parse input string to LocalDateTime object
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
		LocalDateTime inputDate;
		try {
			inputDate = LocalDateTime.parse(inputDateStr, inputFormat);
		} catch (DateTimeParseException e) {
			System.err.println("Invalid input date string: " + inputDateStr);
			return null;
		}

		// add one month to input date
		LocalDateTime outputDate = inputDate.plusMonths(1).minusSeconds(1);

		// convert LocalDateTime to Date object
		Date date = java.util.Date.from(outputDate.atZone(java.time.ZoneId.systemDefault()).toInstant());
		return date;
	}
	
	public static double getPayableAmount(CreditCard creditCard) {
		
		double totalCreditLimit = creditCard.getTotalCreditLimit();
		double remainingCreditLimit = creditCard.getRemainingCreditLimit();
		double payableAmount = totalCreditLimit - remainingCreditLimit;
		return payableAmount;
	}
}
