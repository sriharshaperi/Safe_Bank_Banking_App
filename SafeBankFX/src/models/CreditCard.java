package models;

import java.util.Date;
import java.util.UUID;
import enums.CardCategory;
import enums.CardProvider;

public class CreditCard {
    private UUID creditCardId;
    private long cardNumber;
    private int securityCode;
    private int pinNumber;
    private double totalCreditLimit;
    private double remainingCreditLimit;
    private Date validThru;
    private Date lastPaymentDate;
    private Date createdAt;
    private Date updatedAt;
    private CardCategory cardCategory;
    private CardProvider cardProvider;
	
	public UUID getCreditCardId() {
		return creditCardId;
	}
	public void setCreditCardId(UUID creditCardId) {
		this.creditCardId = creditCardId;
	}
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(int securityCode) {
		this.securityCode = securityCode;
	}
	public int getPinNumber() {
		return pinNumber;
	}
	public void setPinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
	}
	public double getTotalCreditLimit() {
		return totalCreditLimit;
	}
	public void setTotalCreditLimit(double totalCreditLimit) {
		this.totalCreditLimit = totalCreditLimit;
	}
	public double getRemainingCreditLimit() {
		return remainingCreditLimit;
	}
	public void setRemainingCreditLimit(double remainingCreditLimit) {
		this.remainingCreditLimit = remainingCreditLimit;
	}
	public Date getValidThru() {
		return validThru;
	}
	public void setValidThru(Date validThru) {
		this.validThru = validThru;
	}
	public Date getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(Date lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
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
	public CardCategory getCardCategory() {
		return cardCategory;
	}
	public void setCardCategory(CardCategory cardCategory) {
		this.cardCategory = cardCategory;
	}
	public CardProvider getCardProvider() {
		return cardProvider;
	}
	public void setCardProvider(CardProvider cardProvider) {
		this.cardProvider = cardProvider;
	}
	@Override
	public String toString() {
		return "CreditCard [creditCardId=" + creditCardId + ", cardNumber=" + cardNumber + ", securityCode="
				+ securityCode + ", pinNumber=" + pinNumber + ", totalCreditLimit=" + totalCreditLimit
				+ ", remainingCreditLimit=" + remainingCreditLimit + ", validThru=" + validThru + ", lastPaymentDate="
				+ lastPaymentDate + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", cardCategory="
				+ cardCategory + ", cardProvider=" + cardProvider + "]";
	}
}
