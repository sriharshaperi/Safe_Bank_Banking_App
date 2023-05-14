package transactions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.DialogController;
import dao.CreditCardsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.CCBillPaymentStatus;
import enums.PaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.CreditCard;
import models.Transaction;
import models.User;
import notifications.EmailService;
import utils.CreditCardUtils;
import utils.TransactionUtils;
import validations.TransactionValidations;

public class PaymentFromCardTransaction {

	private double amount;
	private double remainingCreditLimit;
	private String userId;
	private String cardId;
	private TransactionCategory transactionCategory;
	
	String toEmail = null;
	String emailSubject = null;
	String emailMessage = null;
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getRemainingCreditLimit() {
		return remainingCreditLimit;
	}

	public void setRemainingCreditLimit(double remainingCreditLimit) {
		this.remainingCreditLimit = remainingCreditLimit;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public TransactionCategory getTransactionCategory() {
		return transactionCategory;
	}

	public void setTransactionCategory(TransactionCategory transactionCategory) {
		this.transactionCategory = transactionCategory;
	}

	public final Payment cardPayment = (double amount) -> {

		try {
			User user = UsersDAO.getUserById(userId);
			toEmail = user.getEmail();
			emailSubject = "OTP for Online Card Payment Transaction";
			emailMessage = "Your OTP for this transaction : ";
			int generatedOTP = TransactionUtils.generateOTP();
			Date generatedOTPTimestamp = new Date();
			EmailService.sendEmail(toEmail, emailSubject, emailMessage + generatedOTP);
			String OTP = DialogController
					.getEnteredOTPInputForTransaction(TransactionCategory.ONLINE_PAYMENT);
			boolean isValidOTP = TransactionValidations.isOTPValid(OTP, generatedOTPTimestamp);
			System.out.println("Is OTP Valid : " + isValidOTP);
				if (isValidOTP) {
					int enteredOTP = Integer.parseInt(OTP);
					if (enteredOTP == generatedOTP) {
						if (amount <= remainingCreditLimit) {
							remainingCreditLimit -= amount;
							System.out.println("Updated Balance : USD " + remainingCreditLimit);
							Date dueDate = CreditCardUtils.generateCCDueDateAfterTwoMin(new Date());
//									CreditCardUtils.generateDueDateForCreditCard();
							CreditCardsDAO.updateRemainingCreditLimit(userId, cardId, remainingCreditLimit);
							CreditCard userCreditCard = CreditCardsDAO.getCreditCardByCardId(cardId);
							Transaction transaction = new Transaction();
							transaction.setTransactionId(UUID.randomUUID());
							transaction.setTransactionCategory(TransactionCategory.ONLINE_PAYMENT);
							transaction.setTransactionType(TransactionType.CARD_TRANSACTION);
							transaction.setTransactionMode(TransactionMode.DEBIT);
							transaction.setTransactionName("Online Payment for Shopping");
							transaction.setAmount(amount);
							transaction.setDueDate(dueDate);
							double payableAmount = CreditCardUtils.getPayableAmount(userCreditCard);
							if(payableAmount > 0)
							transaction.setPaymentStatus(CCBillPaymentStatus.PENDING);
							else
							transaction.setPaymentStatus(CCBillPaymentStatus.IN_TIME);
							transaction.setCardNumber(userCreditCard.getCardNumber());
							TransactionsDAO.createNewTransaction(userId, transaction);
							
							int creditScore = user.getCreditScore();
							// CreditCard creditCard = CreditCardsDAO.getCreditCardByCardId(cardId);
							CCBillPaymentStatus paymentStatus = 
									CCBillPaymentTransaction.getCCBillPaymentStatus(user, userCreditCard);
							if(paymentStatus == CCBillPaymentStatus.LATE)
								user.setCreditScore(creditScore - 20);
							else if(paymentStatus == CCBillPaymentStatus.LATE_YET_PENDING) {
								double totCreditLimit = userCreditCard.getTotalCreditLimit();
								double remCreditlimit = userCreditCard.getRemainingCreditLimit();
								Timestamp lastPaymentDateTimestamp = null;
								Transaction lastCCBillPaymentTransaction = null;
								lastPaymentDateTimestamp = new Timestamp(userCreditCard.getLastPaymentDate().getTime());
								if(lastPaymentDateTimestamp != null) {
									CreditCardsDAO.updateLastPaymentDate(userId, cardId, lastPaymentDateTimestamp);
								}
								
								lastCCBillPaymentTransaction = 
										TransactionsDAO.getTransactionByDate(userId, lastPaymentDateTimestamp);
								if(lastCCBillPaymentTransaction != null) {
									double amountPaid = lastCCBillPaymentTransaction.getAmount();
									System.out.println("Last CC Bill Payment Amount :::: "+amount);
									if(amountPaid <= 0.1 * (totCreditLimit - (remCreditlimit - amount))) {
										user.setCreditScore(creditScore - 30);
									}
									else
										user.setCreditScore(creditScore - 20);
								}
							}
								
							if(amount > 0.33 * remainingCreditLimit)
								user.setCreditScore(creditScore - 20);
							else
								user.setCreditScore(creditScore + 20);
							
							UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
							
							return PaymentStatus.SUCCESS;

						} else return PaymentStatus.INSUFFICIENT_FUNDS;
					} else return PaymentStatus.INCORRECT_OTP;
				} else return PaymentStatus.INVALID_OTP;

		} catch (Exception exception) {
			// TODO: handle exception
			Logger.getLogger(PaymentFromCardTransaction.class.getName()).log(Level.SEVERE, null, exception);
			return PaymentStatus.PAYMENT_EXCEPTION;
		}
	};
}
