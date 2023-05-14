package transactions;

import java.sql.Timestamp;
import java.util.Date;
import dao.CreditCardsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.CCBillPaymentStatus;
import enums.PaymentStatus;
import enums.TransactionCategory;
import models.CreditCard;
import models.User;
import utils.CreditCardUtils;
import utils.TransactionUtils;

public class CCBillPaymentTransaction {

	private String userId;
	private String accountId;
	private String cardId;
	private double accountBalance;
	private double remainingCreditLimit;
	private double amount;

	
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

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getRemainingCreditLimit() {
		return remainingCreditLimit;
	}

	public void setRemainingCreditLimit(double remainingCreditLimit) {
		this.remainingCreditLimit = remainingCreditLimit;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public PaymentStatus payCCBillAmount() throws Exception {
		
        PaymentFromAccountTransaction accountPaymentTransaction = null;
        
		accountPaymentTransaction = new PaymentFromAccountTransaction();
		accountPaymentTransaction.setUserId(userId);
		accountPaymentTransaction.setAccountId(accountId);
		accountPaymentTransaction.setAccountBalance(accountBalance);
		accountPaymentTransaction.setAmount(amount);
		accountPaymentTransaction.setTransactionCategory(TransactionCategory.ONLINE_PAYMENT);

		PaymentStatus paymentStatus = 
				accountPaymentTransaction.accountPayment.payment(amount);
		return paymentStatus;
    }

    public static CCBillPaymentStatus getCCBillPaymentStatus(
    		User user, CreditCard creditCard) throws Exception {
        double totalPayableAmount = CreditCardUtils.getPayableAmount(creditCard);
        Date lastDueDate = TransactionsDAO.getLastDueDate(user.getUserId().toString());
        Date lastPaymentDate = creditCard.getLastPaymentDate();
        CCBillPaymentStatus paymentStatus;
        if(lastPaymentDate == null) {
        	if (lastDueDate == null)
            	paymentStatus = CCBillPaymentStatus.NO_TRANSACTIONS;
        	else {
        		int compareValue = new Date().compareTo(lastDueDate);
        		if(compareValue > 0)
        			paymentStatus = CCBillPaymentStatus.LATE_YET_PENDING;
        		else
        			paymentStatus = CCBillPaymentStatus.PENDING;
        	}
        }
        else {
        	int comparedDates = lastPaymentDate.compareTo(lastDueDate);
            
            if (comparedDates > 0) {
            	//late payment
            	if (totalPayableAmount > 0)
                	paymentStatus = CCBillPaymentStatus.LATE_YET_PENDING;
                else
                	paymentStatus = CCBillPaymentStatus.LATE;
            }
            else {
            	
            	//in time
                if (totalPayableAmount > 0)
                	paymentStatus = CCBillPaymentStatus.PENDING;
                else
                	paymentStatus = CCBillPaymentStatus.IN_TIME;
            }
        }
        return paymentStatus;
    }
    
    public static void updateRemainingCreditLimit(
    		String userId, 
    		String cardId, 
    		double remainingCreditLimit, 
    		double amount) throws Exception {
    	
        remainingCreditLimit += amount;
        CreditCardsDAO.updateRemainingCreditLimit(userId, cardId, remainingCreditLimit);
        
        Timestamp lastPaymentDate = new Timestamp(new Date().getTime());
        CreditCardsDAO.updateLastPaymentDate(userId, cardId, lastPaymentDate);
		
		User user = UsersDAO.getUserById(userId);
		CreditCard creditCard = CreditCardsDAO.getCreditCardByCardId(cardId);
		int creditScore = user.getCreditScore();
		CCBillPaymentStatus paymentStatus = 
				CCBillPaymentTransaction.getCCBillPaymentStatus(user, creditCard);
		TransactionsDAO.updateTransactionsPaymentStatus(userId);
		
		if(paymentStatus == CCBillPaymentStatus.LATE)
			user.setCreditScore(creditScore - 20);
		else if(paymentStatus == CCBillPaymentStatus.IN_TIME)
			user.setCreditScore(creditScore + 40);
		UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
    }
}
