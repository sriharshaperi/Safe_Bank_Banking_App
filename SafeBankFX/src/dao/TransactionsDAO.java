package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import enums.CCBillPaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.CreditCard;
import models.Transaction;
import models.User;
import utils.CreditCardUtils;
import utils.TransactionUtils;

public class TransactionsDAO extends DatabaseConnectionFactory {

	public static boolean createNewTransaction(String userId, Transaction transaction) {

		String transactionId = transaction.getTransactionId().toString();
		String transactionName = transaction.getTransactionName();
		String transactionCategory = transaction.getTransactionCategory().name();
		String transactionType = transaction.getTransactionType().name();
		String transactionMode = transaction.getTransactionMode().name();
		double amount = transaction.getAmount();
		Timestamp dueDate = null;
		String paymentStatus = null;
		
		if (transaction.getDueDate() != null) {
			dueDate = new Timestamp(transaction.getDueDate().getTime());
			paymentStatus = transaction.getPaymentStatus().toString();
		}
		
		boolean isCardTransaction = transactionType.equals("CARD_TRANSACTION");
		long cardNumber = 0;
		long accountNumber = 0;
		if (isCardTransaction) {
			cardNumber = transaction.getCardNumber();
		} else
			accountNumber = transaction.getAccountNumber();

		final String CREATE_NEW_TRANSACTION_QUERY;

		if (isCardTransaction) {
			CREATE_NEW_TRANSACTION_QUERY = "INSERT INTO " + "safebankdb.transactions(" + "transaction_id, "
					+ "transaction_name, " + "transaction_category, " + "transaction_type, " + "transaction_mode, "
					+ "amount, " + "user_id, " + "due_date," + "card_number," + "payment_status) "

					+ "VALUES (" + "'" + transactionId + "'," + "'" + transactionName + "'," + "'" + transactionCategory
					+ "', " + "'" + transactionType + "', " + "'" + transactionMode + "', " + "'" + amount + "', " + "'"
					+ userId + "', " + "'" + dueDate + "', " + "'" + cardNumber + "', " + "'" + paymentStatus + "');";
		} else {
			CREATE_NEW_TRANSACTION_QUERY = "INSERT INTO " + "safebankdb.transactions(" + "transaction_id, "
					+ "transaction_name, " + "transaction_category, " + "transaction_type, " + "transaction_mode, "
					+ "amount, " + "account_number, " + "user_id) "

					+ "VALUES (" + "'" + transactionId + "'," + "'" + transactionName + "'," + "'" + transactionCategory
					+ "', " + "'" + transactionType + "', " + "'" + transactionMode + "', " + "'" + amount + "', " + "'"
					+ accountNumber + "', " + "'" + userId + "');";
		}

		boolean createdTransaction = executeUpdate(CREATE_NEW_TRANSACTION_QUERY);
		if (createdTransaction)
			return true;
		return false;
	}

	public static List<Transaction> getUserTransactions(String userId) {
		ResultSet results = null;
		List<Transaction> retrievedTransactions = null;
		try {
			final String GET_TRANSACTIONS_QUERY = "SELECT * " + "FROM safebankdb.transactions " + "WHERE user_id = '"
					+ userId + "';";
			
			System.out.println("Running this query :::: "+ GET_TRANSACTIONS_QUERY);

			results = (ResultSet) executeQuery(GET_TRANSACTIONS_QUERY);
			retrievedTransactions = new ArrayList<>();
			while (results.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(UUID.fromString(results.getString("transaction_id")));
				transaction.setTransactionName(results.getString("transaction_name"));
				transaction
						.setTransactionCategory(TransactionCategory.valueOf(results.getString("transaction_category")));
				transaction.setTransactionType(TransactionType.valueOf(results.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(results.getString("transaction_mode")));
				transaction.setAmount(results.getDouble("amount"));
				transaction.setCreatedAt(results.getTimestamp("created_at"));
				transaction.setDueDate(results.getTimestamp("due_date"));
				transaction.setAccountNumber(results.getLong("account_number"));
				transaction.setCardNumber(results.getLong("card_number"));
				String status = results.getString("payment_status");
				if(status == null)
					status = CCBillPaymentStatus.NO_TRANSACTIONS.toString();
				transaction.setPaymentStatus(CCBillPaymentStatus.valueOf(status));
				retrievedTransactions.add(transaction);
				
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return retrievedTransactions;
	}
	
	public static List<Transaction> getTransactionsByCardNumber(long cardNumber) {

		final String GET_TRANSACTIONS_QUERY = "SELECT * " + "FROM safebankdb.transactions " + "WHERE card_number = "
				+ cardNumber + " order by created_at desc;";
		
		return getTransactions(GET_TRANSACTIONS_QUERY);
	}

	public static List<Transaction> getTransactionsByAccountNumber(long accountNumber) {

		final String GET_TRANSACTIONS_QUERY = "SELECT * " + "FROM safebankdb.transactions " + "WHERE account_number = "
				+ accountNumber + " order by created_at desc;";
		
		System.out.println("Running this query :::: "+ GET_TRANSACTIONS_QUERY);
		return getTransactions(GET_TRANSACTIONS_QUERY);

	}

	public static List<Transaction> getTransactions(String GET_TRANSACTIONS_QUERY) {

		ResultSet results = null;
		List<Transaction> retrievedTransactions = null;
		try {
			results = (ResultSet) executeQuery(GET_TRANSACTIONS_QUERY);			
			retrievedTransactions = new ArrayList<>();
			
			while (results.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(UUID.fromString(results.getString("transaction_id")));
				transaction.setTransactionName(results.getString("transaction_name"));
				transaction.setTransactionCategory(TransactionCategory.valueOf(results.getString("transaction_category")));
				transaction.setTransactionType(TransactionType.valueOf(results.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(results.getString("transaction_mode")));
				transaction.setAmount(results.getDouble("amount"));
				transaction.setCreatedAt(results.getTimestamp("created_at"));
				transaction.setDueDate(results.getTimestamp("due_date"));
				transaction.setAccountNumber(results.getLong("account_number"));
				transaction.setCardNumber(results.getLong("card_number"));
				retrievedTransactions.add(transaction);
			}
			results.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return retrievedTransactions;
	}

	public static Transaction getTransaction(String transactionId) {
		ResultSet transactions = null;
		Transaction transaction = null;
		try {
			final String GET_TRANSACTION_QUERY = "SELECT * " + "FROM safebankdb.transactions "
					+ "WHERE transaction_id = '" + transactionId + "';";

			transactions = executeQuery(GET_TRANSACTION_QUERY);
			transaction = new Transaction();
			while (transactions.next()) {
				transaction.setTransactionId(UUID.fromString(transactions.getString("transaction_id")));
				transaction.setTransactionName(transactions.getString("transaction_name"));
				transaction.setTransactionType(TransactionType.valueOf(transactions.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(transactions.getString("transaction_mode")));
				transaction.setTransactionCategory(
						TransactionCategory.valueOf(transactions.getString("transaction_category")));
				transaction.setAmount(transactions.getDouble("amount"));
				transaction.setCreatedAt(transactions.getTimestamp("created_at"));
				transaction.setDueDate(transactions.getTimestamp("due_date"));
				transaction.setAccountNumber(transactions.getLong("account_number"));
				transaction.setCardNumber(transactions.getLong("card_number"));
			}
			transactions.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		} finally {
			closeConnection();
		}
		return transaction;
	}

	public static Transaction getTransactionByDate(String userId, Timestamp transactionTimestamp) {
		ResultSet transactions = null;
		Transaction transaction = null;
		try {
			final String GET_TRANSACTION_QUERY = "SELECT * " + "FROM safebankdb.transactions " + "WHERE user_id = '"
					+ userId + "' " + "AND created_at = '" + transactionTimestamp + "';";

			transactions = executeQuery(GET_TRANSACTION_QUERY);
			transaction = new Transaction();
			while (transactions.next()) {
				transaction.setTransactionId(UUID.fromString(transactions.getString("transaction_id")));
				transaction.setTransactionName(transactions.getString("transaction_name"));
				transaction.setTransactionType(TransactionType.valueOf(transactions.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(transactions.getString("transaction_mode")));
				transaction.setTransactionCategory(
						TransactionCategory.valueOf(transactions.getString("transaction_category")));
				transaction.setAmount(transactions.getDouble("amount"));
				transaction.setCreatedAt(transactions.getTimestamp("created_at"));
				transaction.setDueDate(transactions.getTimestamp("due_date"));
				transaction.setAccountNumber(transactions.getLong("account_number"));
				transaction.setCardNumber(transactions.getLong("card_number"));
			}
			transactions.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		} finally {
			closeConnection();
		}
		return transaction;
	}

	public static Date getLastDueDate(String userId) {
		ResultSet transactions = null;
		Transaction transaction = null;
		try {
			final String GET_TRANSACTION_QUERY = "SELECT due_date " + "FROM safebankdb.transactions "
					+ "WHERE user_id = '" + userId + "' " + "AND transaction_type = 'CARD_TRANSACTION'"
					+ " ORDER BY due_date desc LIMIT 1;";

			transactions = executeQuery(GET_TRANSACTION_QUERY);
			transaction = new Transaction();
			while (transactions.next()) {
				transaction.setDueDate(transactions.getTimestamp("due_date"));
			}
			transactions.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		} finally {
			closeConnection();
		}
		return transaction.getDueDate();
	}

//	public static boolean 
//	updatePaymentStatusOfSameDueDateTransactions(
//			String userId, Date dueDate, PaymentStatus status) {
//
//		Timestamp dueDateTimestamp = new Timestamp(dueDate.getTime());
//		String paymentStatus = status.toString();
//		
//		final String UPDATE_PAYMENT_STATUS_QUERY = "UPDATE safebankdb.transactions " 
//		+ "SET payment_status = " + "'" + paymentStatus
//		+ "'" + " WHERE user_id = " + "'" + userId 
//		+ "'" + " AND due_date = " + "'" + dueDateTimestamp + "'";
//
//		boolean updatedPaymentStatus = executeUpdate(UPDATE_PAYMENT_STATUS_QUERY);
//		if (updatedPaymentStatus)
//			return true;
//		return false;
//	}

	public static boolean updateTransactionsPaymentStatus(String userId) {
		ResultSet results = null;
		List<Transaction> unpaidTransactions = null;
		try {

			final String GET_UNPAID_TRANSACTIONS_QUERY = "SELECT * " + "FROM safebankdb.transactions "
					+ "WHERE transaction_type = '" + TransactionType.CARD_TRANSACTION + "' " + "AND user_id = '"
					+ userId + "'" + "AND payment_status IN " + "('" + CCBillPaymentStatus.PENDING + "'," + "'"
					+ CCBillPaymentStatus.LATE_YET_PENDING + "')" + " ORDER BY due_date desc;";

			results = (ResultSet) executeQuery(GET_UNPAID_TRANSACTIONS_QUERY);
			unpaidTransactions = new ArrayList<>();
			while (results.next()) {
				Transaction transaction = new Transaction();
				transaction.setTransactionId(UUID.fromString(results.getString("transaction_id")));
				transaction.setTransactionName(results.getString("transaction_name"));
				transaction
						.setTransactionCategory(TransactionCategory.valueOf(results.getString("transaction_category")));
				transaction.setTransactionType(TransactionType.valueOf(results.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(results.getString("transaction_mode")));
				transaction.setAmount(results.getDouble("amount"));
				transaction.setCreatedAt(results.getTimestamp("created_at"));
				transaction.setDueDate(results.getTimestamp("due_date"));
				transaction.setAccountNumber(results.getLong("account_number"));
				transaction.setCardNumber(results.getLong("card_number"));
				unpaidTransactions.add(transaction);
			}

			User currentUser = UsersDAO.getUserById(userId);
			CreditCard currentUserCreditCard = CreditCardsDAO.getCreditCardByUserId(currentUser.getUserId().toString());
			System.out.println(currentUserCreditCard);
			double payableAmount = CreditCardUtils.getPayableAmount(currentUserCreditCard);
			
			Date lastPaymentDate = currentUserCreditCard.getLastPaymentDate();
			unpaidTransactions.forEach(currentTransaction -> {
				Date currentTransactionDueDate = currentTransaction.getDueDate();
				
//				if(lastPaymentDate != null && currentTransactionDueDate != null) {
					int compareValue = new Date().compareTo(currentTransactionDueDate);
					
					if (compareValue > 0) {
						if (payableAmount > 0) {
							currentTransaction.setPaymentStatus(CCBillPaymentStatus.LATE_YET_PENDING);
						} else {
							currentTransaction.setPaymentStatus(CCBillPaymentStatus.LATE);
						}
					} else {
						if (payableAmount > 0) {
							currentTransaction.setPaymentStatus(CCBillPaymentStatus.PENDING);
						} else {
							currentTransaction.setPaymentStatus(CCBillPaymentStatus.IN_TIME);
						}
					}
					System.out.println("Last Payment Date :::: "+lastPaymentDate);
					System.out.println("Current Transaction Due Date :::: "+currentTransactionDueDate);
					System.out.println("compare value in updateTransactionsPaymentStatus :::: "+compareValue+ " :::: Status :::: "+currentTransaction.getPaymentStatus());
					updateTransactionPaymentStatus(userId, currentTransaction.getTransactionId().toString(),
							currentTransaction.getPaymentStatus().toString());

//				}
			});

		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return true;
	}

	public static boolean updateTransactionPaymentStatus(String userId, String transactionId, String paymentStatus) {

		final String UPDATE_PAYMENT_STATUS_QUERY = "UPDATE safebankdb.transactions " + "SET payment_status = '"
				+ paymentStatus + "' " + "WHERE user_id = '" + userId + "' " + "AND transaction_id = '" + transactionId
				+ "';";

		boolean updatedPaymentStatus = executeUpdate(UPDATE_PAYMENT_STATUS_QUERY);
		if (updatedPaymentStatus)
			return true;
		return false;
	}

	public static Date getLastPaymentDate(String userId) throws SQLException {
		Date lastPaymentDate = null;
		ResultSet result = null;
		final String GET_LAST_PAYMENT_DATE_QUERY = "SELECT created_at " + "FROM safebankdb.transactions "
				+ "WHERE transaction_category = '" + TransactionCategory.CC_BILL_PAYMENT + "' " + "AND user_id = '"
				+ userId + "' " + "ORDER BY created_at desc;";
		result = (ResultSet) executeQuery(GET_LAST_PAYMENT_DATE_QUERY);
		while (result.next()) {
			lastPaymentDate = result.getTimestamp("created_at");
		}
		return lastPaymentDate;
	}

	public static boolean transactionExists(String transactionId) {
		Transaction transaction = getTransaction(transactionId);
		if (transaction != null)
			return true;
		return false;
	}
}
