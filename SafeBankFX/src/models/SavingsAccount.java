package models;

import java.util.Date;
import java.util.UUID;

public class SavingsAccount {
	private UUID accountId;
	private long accountNumber;
	private double accountBalance;
	private Date createdAt;
	private Date updatedAt;
	
	public UUID getAccountId() {
		return accountId;
	}
	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "SavingsAccount [accountId=" + accountId + ", accountNumber=" + accountNumber
				+ ", accountBalance=" + accountBalance + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
