package models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
   
    private UUID userId;
    private String name;
    private String email;
    private String password;
    private long phone;
    private List<SavingsAccount> accounts;
    private CreditCard creditCard;
    private List<Transaction> transactions;
    private List<BeneficiaryUser> beneficiaryUsers;
    private int creditScore;
    private Date createdAt;
    private Date updatedAt;
	
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public List<SavingsAccount> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<SavingsAccount> accounts) {
		this.accounts = accounts;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public List<BeneficiaryUser> getBeneficiaryUsers() {
		return beneficiaryUsers;
	}
	public void setBeneficiaryUsers(List<BeneficiaryUser> beneficiaryUsers) {
		this.beneficiaryUsers = beneficiaryUsers;
	}
	public int getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(int creditScore) {
		if(creditScore < 600)
			this.creditScore = 600;
		else if(creditScore > 850)
			this.creditScore = 850;
		else
			this.creditScore = creditScore;
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
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password="
				+ password + ", phone=" + phone + ", accounts=" + accounts + ", creditCard=" + creditCard
				+ ", transactions=" + transactions + ", beneficiaryUsers=" + beneficiaryUsers + ", creditScore="
				+ creditScore + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
