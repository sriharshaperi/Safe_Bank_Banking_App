package models;

import java.util.Date;
import java.util.UUID;

import enums.CCBillPaymentStatus;
import enums.PaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;

public class Transaction {
	private UUID transactionId;
	private String transactionName;
	private TransactionCategory transactionCategory;
	private TransactionType transactionType;
	private TransactionMode transactionMode;
	private double amount;
	private Date createdAt;
	private Date dueDate;
	private CCBillPaymentStatus paymentStatus;
	private long accountNumber;
	private long cardNumber;
	
	public UUID getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public TransactionCategory getTransactionCategory() {
		return transactionCategory;
	}
	public void setTransactionCategory(TransactionCategory transactionCategory) {
		this.transactionCategory = transactionCategory;
	}
	public TransactionMode getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(TransactionMode transactionMode) {
		this.transactionMode = transactionMode;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public CCBillPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(CCBillPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", transactionName=" + transactionName
				+ ", transactionCategory=" + transactionCategory + ", transactionType=" + transactionType
				+ ", transactionMode=" + transactionMode + ", amount=" + amount + ", createdAt=" + createdAt
				+ ", dueDate=" + dueDate + ", paymentStatus=" + paymentStatus + ", accountNumber=" + accountNumber
				+ ", cardNumber=" + cardNumber + "]";
	}
}
