package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.BeneficiaryUser;
import models.CreditCard;
import models.SavingsAccount;
import models.Transaction;
import models.User;

public class UsersDAO extends DatabaseConnectionFactory {

	public static boolean createNewUser(User newUser) {
		String uuid = newUser.getUserId().toString();
		String email = newUser.getEmail();
		String password = newUser.getPassword();
		Long phone = newUser.getPhone();
		String name = newUser.getName();

		final String CREATE_NEW_USER_QUERY = "INSERT INTO safebankdb.users "
				+ "(user_id, name, email, password, phone) " + "VALUES('" + uuid + "', '" + name + "', '" + email
				+ "', '" + password + "', '" + phone + "')";

		boolean createdUser = executeUpdate(CREATE_NEW_USER_QUERY);
		if (createdUser)
			return true;
		return false;
	}

	public static boolean updateUserPassword(String email, String encryptedPassword) {
		
		System.out.println(email);
		System.out.println(encryptedPassword);
		
		final String UPDATE_USER_PASSWORD_QUERY = "UPDATE safebankdb.users " 
				+ "SET password = '" + encryptedPassword + "'"
				+ " WHERE email = '" + email + "'";
		boolean updatedUserPassword = executeUpdate(UPDATE_USER_PASSWORD_QUERY);
		System.out.println("Updated User password status ==> " + updatedUserPassword);
		if (updatedUserPassword)
			return true;
		return false;
	}

	public static boolean updateUserPhone(String userId, Long phone) {
		final String UPDATE_USER_PHONE_QUERY = "UPDATE safebankdb.users " 
				+ "SET phone = '" + phone.longValue() + "'"
				+ " WHERE user_id = '" + userId + "'";
		boolean updatedUserPhone = executeUpdate(UPDATE_USER_PHONE_QUERY);
		if (updatedUserPhone)
			return true;
		return false;
	}

	public static boolean updateUserName(String userId, String name) {
		final String UPDATE_USER_NAME = "UPDATE safebankdb.users " 
				+ "SET name = '" + name + "'"
				+ " WHERE user_id = '" + userId + "'";
		boolean updatedUserName = executeUpdate(UPDATE_USER_NAME);
		if (updatedUserName)
			return true;
		return false;
	}

	public static User getUserByEmail(String email) {
		final String GET_USER_QUERY = "SELECT * " + 
				"FROM safebankdb.users " + "WHERE "
				+ "email = '" + email + "';";
		return getUserData(GET_USER_QUERY);
	}

	public static User getUserById(String userId) {
		final String GET_USER_QUERY = "SELECT * " + 
				"FROM safebankdb.users " + "WHERE "
				+ "user_id = '" + userId + "';";
		return getUserData(GET_USER_QUERY);
	}
	
	public static User getUserByPhone(long phone) {
		final String GET_USER_QUERY = "SELECT * " + 
				"FROM safebankdb.users " + "WHERE "
				+ "phone = '" + phone + "';";
		return getUserData(GET_USER_QUERY);
	}

	private static User getUserData(String GET_USER_QUERY) {
		
		ResultSet users = null;
		List<User> retrievedUser = null;
		try {
			users = executeQuery(GET_USER_QUERY);
			retrievedUser = new ArrayList<>();
			User user = new User();
			while (users.next()) {
				user.setName(users.getString("name"));
				user.setEmail(users.getString("email"));
				user.setPassword(users.getString("password"));
				user.setPhone(users.getLong("phone"));
				user.setUserId(UUID.fromString(users.getString("user_id")));
				user.setCreatedAt(users.getTimestamp("created_at"));
				user.setUpdatedAt(users.getTimestamp("updated_at"));
				user.setCreditScore(users.getInt("credit_score"));
				retrievedUser.add(user);
			}
			users.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		} finally {
			closeConnection();
		}
		
		User currentUser = null;
		if(retrievedUser !=null && !retrievedUser.isEmpty()) {
			
			currentUser = retrievedUser.get(0);
			String userId = currentUser.getUserId().toString();
			List<SavingsAccount> accounts = 
					SavingsAccountsDAO.getUserSavingsAccounts(userId);
			CreditCard creditCard = 
					CreditCardsDAO.getCreditCardByUserId(userId);
			List<Transaction> transactions = 
					TransactionsDAO.getUserTransactions(userId);
			List<BeneficiaryUser> beneficiaries = 
					BeneficiaryUsersDAO.getBeneficiaries(userId);
			
	
			if(accounts == null)
				currentUser.setAccounts(new ArrayList<>());
			else
				currentUser.setAccounts(accounts);
			
			if(beneficiaries == null)
				currentUser.setBeneficiaryUsers(new ArrayList<>());
			else
				currentUser.setBeneficiaryUsers(beneficiaries);
			
			if(creditCard == null)
				currentUser.setCreditCard(new CreditCard());
			else
				currentUser.setCreditCard(creditCard);
			
			if(transactions == null)
				currentUser.setTransactions(new ArrayList<>());
			else
				currentUser.setTransactions(transactions);

		}
		
		return currentUser;
	}

	public static boolean updateUserCreditScore(String userId, int creditScore) {

		final String UPDATE_USER_CREDIT_SCORE_QUERY = "UPDATE safebankdb.users " + "SET credit_score = '" + creditScore + "'"
				+ " WHERE user_id = '" + userId + "'";

		boolean updatedRemainingCreditLimit = executeUpdate(UPDATE_USER_CREDIT_SCORE_QUERY);
		if (updatedRemainingCreditLimit)
			return true;
		return false;
	}

	public static boolean userExistsByEmail(String email) {
		User user = getUserByEmail(email);
		if (user != null)
			return true;
		return false;
	}
	
	public static boolean userExistsByPhone(long phone) {
		User user = getUserByPhone(phone);
		//to ensure that the existing user is not the only current user
		if (user != null)
			return true;
		return false;
	}
	
	public static boolean userExistsByPhoneAndNotCurrentUser(long phone, String userId) {
		User user = getUserByPhone(phone);
		//to ensure that the existing user is not the only current user
		if (user != null && !user.getUserId().toString().equals(userId))
			return true;
		return false;
	}
	
	public static boolean userExistsById(String userId) {
		User user = getUserById(userId);
		if (user != null)
			return true;
		return false;
	}
}
