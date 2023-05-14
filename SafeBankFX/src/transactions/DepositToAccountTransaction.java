package transactions;

import validations.TransactionValidations;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.DialogController;
import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.PaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.SavingsAccount;
import models.Transaction;
import models.User;
import notifications.EmailService;
import utils.TransactionUtils;

public class DepositToAccountTransaction {

	double amount;
	double accountBalance;
	String userId;
	String accountId;
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

	public final Deposit deposit = (double amount) -> {
		try {
			User user = UsersDAO.getUserById(userId);
			toEmail = user.getEmail();
			emailSubject = "OTP for Cash Deposit";
			emailMessage = "Your OTP for this transaction : ";
			int generatedOTP = TransactionUtils.generateOTP();
			Date generatedOTPTimestamp = new Date();
			EmailService.sendEmail(toEmail, emailSubject, emailMessage + generatedOTP);
			String OTP = DialogController.getEnteredOTPInputForTransaction(TransactionCategory.CASH_DEPOSIT);
			boolean isValidOTP = TransactionValidations.isOTPValid(OTP, generatedOTPTimestamp);
			System.out.println("Is OTP Valid : " + isValidOTP);
			if (isValidOTP) {
				int enteredOTP = Integer.parseInt(OTP);
				if (enteredOTP == generatedOTP) {
					accountBalance += amount;
					System.out.println("Updated Balance : USD " + accountBalance);
					SavingsAccountsDAO.updateAccountBalance(userId, accountId, accountBalance);
					SavingsAccount savingsAccount = SavingsAccountsDAO.getSavingsAccountById(accountId);
					Transaction transaction = new Transaction();
					transaction.setTransactionId(UUID.randomUUID());
					transaction.setTransactionCategory(TransactionCategory.CASH_DEPOSIT);
					transaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
					transaction.setTransactionMode(TransactionMode.CREDIT);
					transaction.setTransactionName("Deposit for Medical Purpose");
					transaction.setAmount(amount);
					transaction.setAccountNumber(savingsAccount.getAccountNumber());
					TransactionsDAO.createNewTransaction(userId, transaction);

					int creditScore = user.getCreditScore();
					user.setCreditScore(creditScore + 10);
					UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
					return PaymentStatus.SUCCESS;

				} else
					return PaymentStatus.INCORRECT_OTP;
			} else
				return PaymentStatus.INVALID_OTP;

		} catch (Exception exception) {
			// TODO: handle exception
			Logger.getLogger(DepositToAccountTransaction.class.getName()).log(Level.SEVERE, null, exception);
			return PaymentStatus.PAYMENT_EXCEPTION;
		}
	};
}
