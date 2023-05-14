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

public class BeneficiaryTransferTransaction {
	
	private String senderUserId;
	private String receiverUserId;
	private String senderAccountId;
	private String receiverAccountId;
	private double amount;
	
	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
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

	public void transferToBeneficiary() throws Exception {
        
		SavingsAccount senderAccount = SavingsAccountsDAO.getSavingsAccountById(senderAccountId);
		SavingsAccountsDAO.processTransfer(senderAccountId, receiverAccountId, amount);
        Transaction senderTransaction = new Transaction();
        senderTransaction.setTransactionId(UUID.randomUUID());
        senderTransaction.setTransactionCategory(TransactionCategory.TRANSFER_TO_BENEFICIARY);
        senderTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
        senderTransaction.setTransactionMode(TransactionMode.DEBIT);
        senderTransaction.setTransactionName("Transfer to Beneficiary");
        senderTransaction.setAmount(amount);
        senderTransaction.setAccountNumber(senderAccount.getAccountNumber());
      	TransactionsDAO.createNewTransaction(senderUserId, senderTransaction);
      	
      	User sender = UsersDAO.getUserById(senderUserId);
      	int creditScore = sender.getCreditScore();
		sender.setCreditScore(creditScore + 10);
		
		UsersDAO.updateUserCreditScore(senderUserId, sender.getCreditScore());
      	
		SavingsAccount recieverAccount = SavingsAccountsDAO.getSavingsAccountById(receiverAccountId);
      	Transaction receiverTransaction = new Transaction();
      	receiverTransaction.setTransactionId(UUID.randomUUID());
      	receiverTransaction.setTransactionCategory(TransactionCategory.TRANSFER_FROM_BENEFICIARY);
      	receiverTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
      	receiverTransaction.setTransactionMode(TransactionMode.CREDIT);
      	receiverTransaction.setTransactionName("Transfer from Beneficiary");
      	receiverTransaction.setAmount(amount);
        receiverTransaction.setAccountNumber(recieverAccount.getAccountNumber());
      	TransactionsDAO.createNewTransaction(receiverUserId, receiverTransaction);
    }
}
