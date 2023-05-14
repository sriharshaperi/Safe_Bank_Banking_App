package services;

import java.util.List;
import java.util.UUID;

import dao.SavingsAccountsDAO;
import dao.UsersDAO;
import models.SavingsAccount;
import models.User;
import utils.SavingsAccountUtils;

public class SavingsAccountService {
	
	public static boolean createSavingsAccount(String userId, User user) {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(0);
		long accountnumber = SavingsAccountUtils.generateAccountNumber();
		savingsAccount.setAccountNumber(accountnumber);
		savingsAccount.setAccountId(UUID.randomUUID());
		boolean accountCreated = 
				SavingsAccountsDAO.createNewAccount(userId, savingsAccount);
		if(accountCreated) {
			int creditScore = user.getCreditScore();
			user.setCreditScore(creditScore + 20);
			UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
			System.out.println("Created Savings Account for User "+user.getName());
			List<SavingsAccount> userAccounts = SavingsAccountsDAO.getUserSavingsAccounts(userId);
			user.setAccounts(userAccounts);
			return true;
		}
		else {
			System.out.println("Failed Creating Savings Account for "+user.getName());
			return false;
		}
	}
}
