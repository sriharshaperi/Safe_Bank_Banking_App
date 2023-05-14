package transactions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.DialogController;
import dao.CreditCardsDAO;
import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.CCBillPaymentStatus;
import enums.PaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.CreditCard;
import models.SavingsAccount;
import models.Transaction;
import models.User;
import notifications.EmailService;
import utils.TransactionUtils;
import validations.TransactionValidations;

public class PaymentFromAccountTransaction {

	private double amount;
	private double accountBalance;
	private String userId;
	private String accountId;
	private TransactionCategory transactionCategory;

	String toEmail = null;
	String emailSubject = null;
	String emailMessage = null;

	public PaymentFromAccountTransaction() {
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public TransactionCategory getTransactionCategory() {
		return transactionCategory;
	}

	public void setTransactionCategory(TransactionCategory transactionCategory) {
		this.transactionCategory = transactionCategory;
	}

	public Payment getAccountPayment() {
		return accountPayment;
	}

	public final Payment accountPayment = (double amount) -> {

		try {
			User user = UsersDAO.getUserById(userId);
			toEmail = user.getEmail();
			emailSubject = "OTP for Online Account Payment Transaction";
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
					if (amount <= accountBalance) {
						accountBalance -= amount;
						System.out.println("Updated Balance : USD " + accountBalance);
						SavingsAccountsDAO.updateAccountBalance(userId, accountId, accountBalance);
						SavingsAccount userAccount = SavingsAccountsDAO.getSavingsAccountById(accountId);
						Transaction transaction = new Transaction();
						transaction.setTransactionId(UUID.randomUUID());
						transaction.setTransactionCategory(transactionCategory);
						transaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
						transaction.setTransactionMode(TransactionMode.DEBIT);
						transaction.setAccountNumber(userAccount.getAccountNumber());
						transaction.setTransactionName("Account Transaction");
						transaction.setAmount(amount);
						transaction.setAccountNumber(userAccount.getAccountNumber());
						if (amount > 0)
							TransactionsDAO.createNewTransaction(userId, transaction);
						if(transactionCategory == TransactionCategory.CC_BILL_PAYMENT) {
							Date lastCCBillPaymentDate = TransactionsDAO.getLastPaymentDate(userId);
							Timestamp lastCCBillPaymentDateTimestamp = 
									new Timestamp(lastCCBillPaymentDate.getTime());
							CreditCard creditCard = CreditCardsDAO.getCreditCardByUserId(userId);
							String cardId = creditCard.getCreditCardId().toString();
							CreditCardsDAO.updateLastPaymentDate(userId, cardId, lastCCBillPaymentDateTimestamp);
						}
						int creditScore = user.getCreditScore();
						user.setCreditScore(creditScore + 10);
						UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
						return PaymentStatus.SUCCESS;

					} else
						return PaymentStatus.INSUFFICIENT_FUNDS;
				} else
					return PaymentStatus.INCORRECT_OTP;
			} else
				return PaymentStatus.INVALID_OTP;

		} catch (Exception exception) {
			// TODO: handle exception
			Logger.getLogger(PaymentFromAccountTransaction.class.getName()).log(Level.SEVERE, null, exception);
			return PaymentStatus.PAYMENT_EXCEPTION;
		}
	};
}