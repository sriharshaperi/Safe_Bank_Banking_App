package application;

import dao.TransactionsDAO;
import dao.UsersDAO;
import models.User;

public class Controller {
	protected static User user;
	protected static boolean isSessionActive;
	protected static void refreshState() {
		user = UsersDAO.getUserById(user.getUserId().toString());
		if(user.getCreditCard().getLastPaymentDate() != null)
		TransactionsDAO.updateTransactionsPaymentStatus(user.getUserId().toString());
	}
	protected static String verfifiedEmailForPasswordReset;
}
