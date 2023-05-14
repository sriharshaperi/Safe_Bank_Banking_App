package utils;

import java.util.Date;

import javax.mail.MessagingException;

import dao.UsersDAO;
import javafx.scene.control.TextField;
import models.User;
import notifications.EmailService;
import validations.TransactionValidations;

public class UserUtils {

	public static boolean isValidEmail(TextField textField) {
		return true;
	}

	public static boolean isPasswordValid(TextField textField) {
		return true;
	}

	public static boolean isEmailPasswordValid(String email, String password) {
		boolean userExists = UsersDAO.userExistsByEmail(email);
		if (!userExists) {
			return false;
		}
		User user = UsersDAO.getUserByEmail(email);
		String hashedPasswordFromDB = user.getPassword();
		boolean passwordHashMached = PasswordUtils.checkPassword(password, hashedPasswordFromDB);
		if (passwordHashMached)
			return true;
		return false;
	}

	public static boolean sendEmailVerificationOTP(String email, int generatedOTP) {

		String subject = "SafeBank Email Verification";
		String message = "Enter this verifiction code to verify your email\n" + "Verification Code : "+generatedOTP;
		return EmailService.sendEmail(email, subject, message);
	}
}
