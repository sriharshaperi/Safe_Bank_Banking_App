package backendmain;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.DatabaseConnectionFactory;
import dao.SavingsAccountsDAO;
import enums.PaymentStatus;
import enums.TransactionCategory;
import models.CreditCard;
import models.SavingsAccount;
import models.User;
import notifications.EmailService;
import notifications.OTPService;
import services.CreditCardsService;
import services.SavingsAccountService;
import services.UserService;
import transactions.DepositToAccountTransaction;
import transactions.PaymentFromAccountTransaction;
import transactions.PaymentFromCardTransaction;
import transactions.SelfTransferTransaction;
import transactions.BeneficiaryTransferTransaction;
import transactions.CCBillPaymentTransaction;

public class SafeBankBackendMain {
	
	public static void backendMain() {
		Map<String, Object> newUserData = new HashMap<>();
		newUserData.put("name", "peri");
		newUserData.put("email", "sriharshaperi@gmail.com");
		newUserData.put("password", "peri");
		newUserData.put("phone", "8888888888");
		
		User user = UserService.findOrCreateUser(newUserData);
		
//		String userId = user.getUserId().toString();
//		SavingsAccountService.createSavingsAccount(userId, user);
		
//		String toEmail = (String) newUserData.get("email");
//		String message = "Testing Email SafeBank";
//		String subject = "Hi. Good Luck";
		
		try {
			
//		CreditCardsService.createNewCreditCard(userId, user);
//		BeneficiaryService.addNewBeneficaryService("perisriharsha@gmail.com", user);
		
//		OTPService.sendSMS("+18552723703");
//		EmailService.sendEmail(toEmail, subject, message);
//			MailgunEmailService.sendSimpleMessage();
		
		
		double amount = 10000;
		
		SavingsAccount account = user.getAccounts().get(0);
		String accountId = account.getAccountId().toString();
		double accountBalance = account.getAccountBalance();
		
		CreditCard creditCard = user.getCreditCard();
		String cardId = creditCard.getCreditCardId().toString();
		double remainingCreditLimit = creditCard.getRemainingCreditLimit();
		double totalCreditLimit = creditCard.getTotalCreditLimit();
		
		double payableAmount = totalCreditLimit - remainingCreditLimit;
		
//		DepositToAccountTransaction depositTransaction = 
//				new DepositToAccountTransaction(userId, accountId, accountBalance, amount);
//		
//		depositTransaction.start();
		
//		PaymentFromAccountTransaction accountPaymentTransaction = 
//				new PaymentFromAccountTransaction(
//						userId, 
//						accountId, 
//						accountBalance, 
//						amount,
//						TransactionCategory.ONLINE_PAYMENT);
//
//		accountPaymentTransaction.start();
		
//		PaymentFromCardTransaction cardPaymentTransaction = 
//				new PaymentFromCardTransaction(
//						userId, 
//						cardId, 
//						remainingCreditLimit, 
//						1000);
//
//		cardPaymentTransaction.start();
		
//		CCBillPaymentTransaction creditCardBillPayment = 
//				new CCBillPaymentTransaction(
//						userId, 
//						accountId, 
//						cardId, 
//						accountBalance, 
//						remainingCreditLimit, 
//						100);
//		
//		creditCardBillPayment.start();
		
//		BeneficiaryTransferTransaction beneficiaryTransferTransaction = 
//				new BeneficiaryTransferTransaction(userId, userId, accountId, accountId, minAmount);
//		
//		beneficiaryTransferTransaction.start();
		
		
//		SelfTransferTransaction selfTransferTransaction = 
//				new SelfTransferTransaction(userId, accountId, accountId, minAmount);
//		
//		selfTransferTransaction.start();
				
			
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName())
			.log(Level.SEVERE, null, exception);
		}
	}
}
