package transactions;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.SavingsAccount;
import models.Transaction;
import models.User;

public class SelfTransferTransaction {
	
	private String userId;
	private String senderAccountId;
	private String receiverAccountId;
	private double amount;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSenderAccountId() {
		return senderAccountId;
	}

	public void setSenderAccountId(String senderAccountId) {
		this.senderAccountId = senderAccountId;
	}

	public String getReceiverAccountId() {
		return receiverAccountId;
	}

	public void setReceiverAccountId(String receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void transferToSelf() throws Exception {
		SavingsAccount senderAccount = SavingsAccountsDAO.getSavingsAccountById(senderAccountId);
        SavingsAccountsDAO.processTransfer(senderAccountId, receiverAccountId, amount);
        Transaction senderTransaction = new Transaction();
        senderTransaction.setTransactionId(UUID.randomUUID());
        senderTransaction.setTransactionCategory(TransactionCategory.TRANSFER_TO_SELF);
        senderTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
        senderTransaction.setTransactionMode(TransactionMode.DEBIT);
        senderTransaction.setTransactionName("Transfer to Self");
        senderTransaction.setAmount(amount);
        senderTransaction.setAccountNumber(senderAccount.getAccountNumber());
      	TransactionsDAO.createNewTransaction(userId, senderTransaction);
   
      	SavingsAccount receiverAccount = SavingsAccountsDAO.getSavingsAccountById(receiverAccountId);
      	Transaction receiverTransaction = new Transaction();
      	receiverTransaction.setTransactionId(UUID.randomUUID());
      	receiverTransaction.setTransactionCategory(TransactionCategory.TRANSFER_FROM_SELF);
      	receiverTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
      	receiverTransaction.setTransactionMode(TransactionMode.CREDIT);
      	receiverTransaction.setTransactionName("Transfer from Self");
      	receiverTransaction.setAmount(amount);
      	receiverTransaction.setAccountNumber(receiverAccount.getAccountNumber());
      	TransactionsDAO.createNewTransaction(userId, receiverTransaction);
      	
      	User user = UsersDAO.getUserById(userId);
      	int creditScore = user.getCreditScore();
		user.setCreditScore(creditScore + 10);
		
		UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
    }
}
