package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dao.BeneficiaryUsersDAO;
import dao.CreditCardsDAO;
import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import models.BeneficiaryUser;
import models.CreditCard;
import models.SavingsAccount;
import models.Transaction;
import models.User;

public class UserService {
	
	public static User findOrCreateUser(Map<String, Object> newUserData) {
		
		//textfield name - rajesh
		//textfield age - 23
		//textfield phone - 8689797897
		
		User newUser = new User();
		newUser.setUserId(UUID.randomUUID());
		newUser.setName((String) newUserData.get("name"));
		newUser.setEmail((String) newUserData.get("email"));
		newUser.setPassword((String) newUserData.get("password"));
		newUser.setPhone((long) Long.parseLong((String) newUserData.get("phone")));
		newUser.setAccounts(new ArrayList<SavingsAccount>());
		newUser.setCreditCard(new CreditCard());
		newUser.setTransactions(new ArrayList<Transaction>());
		newUser.setBeneficiaryUsers(new ArrayList<BeneficiaryUser>());
		
		boolean existingUser = UsersDAO.userExistsByEmail(newUser.getEmail());
		User user = null;
		if(!existingUser) {
			boolean created = UsersDAO.createNewUser(newUser);
			if(created) {
				user = UsersDAO.getUserByEmail(newUser.getEmail());
				System.out.println("Created Account for User "+newUser.getName());
				return user;
			}
			else {
				System.out.println("User Already Exists / Some Error Occurred");
				return null;
			}
		}
		else {
			user = UsersDAO.getUserByEmail(newUser.getEmail());
//			String userId = user.getUserId().toString();
//			List<SavingsAccount> accounts = 
//					SavingsAccountsDAO.getUserSavingsAccounts(userId);
//			CreditCard creditCard = 
//					CreditCardsDAO.getCreditCardByUserId(userId);
//			List<Transaction> transactions = 
//					TransactionsDAO.getUserTransactions(userId);
//			List<BeneficiaryUser> beneficiaries = 
//					BeneficiaryUsersDAO.getBeneficiaries(userId);
//			
//			if(accounts == null)
//				user.setAccounts(new ArrayList<>());
//			else
//				user.setAccounts(accounts);
//			
//			if(beneficiaries == null)
//				user.setBeneficiaryUsers(new ArrayList<>());
//			else
//				user.setBeneficiaryUsers(beneficiaries);
//			
//			if(creditCard == null)
//				user.setCreditCard(new CreditCard());
//			else
//				user.setCreditCard(creditCard);
//			
//			if(transactions == null)
//				user.setTransactions(new ArrayList<>());
//			else
//				user.setTransactions(transactions);

			return user;
		}
	}
}
