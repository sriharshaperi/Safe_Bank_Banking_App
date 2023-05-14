package application;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.UsersDAO;
import enums.TransactionCategory;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import models.User;
import utils.PasswordUtils;
import validations.TransactionValidations;
import validations.UserValidations;

public class DialogController extends Controller implements Initializable {

	public static boolean verifyEmailDialog(String email, int generatedOTP, Date generatedOTPTimestamp)
			throws Exception {

		try {
			String headerText = "Enter the verification code sent to your email";
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Email Verification Code");
			dialog.setHeaderText(headerText);
			dialog.setContentText(null);

			DialogPane dialogPane = dialog.getDialogPane();
			Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
			okButton.setText("Verify");
			okButton.setViewOrder(0);

			Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
			cancelButton.setViewOrder(1);

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				int enteredOTP = Integer.parseInt(result.get());
				boolean isOTPValid = TransactionValidations.isOTPValid(enteredOTP + "", generatedOTPTimestamp);
				if (isOTPValid && enteredOTP == generatedOTP) {
					System.out.println("Email Verified");
					return true;
				}
				System.out.println("Email not verified");
				return false;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public static void displayForgottenEmail() {
		String title = "Forgot Email";
		String headerText = "Enter your phone number to know your email";
		String contentText = null;
		User user = null;

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(null);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String enteredPhoneNumber = result.get();
			boolean isPhoneValid = UserValidations.isPhoneValid(enteredPhoneNumber);
			if (!isPhoneValid) {
				headerText = "Invalid Phone Number";
				AlertController.showError(title, headerText, contentText);
			} else {
				long phoneNumber = Long.parseLong(enteredPhoneNumber);
				boolean phoneExists = UsersDAO.userExistsByPhone(phoneNumber);
				if (!phoneExists) {
					headerText = "No record exists with the given phone number";
					AlertController.showError(title, headerText, contentText);
				} else {
					user = UsersDAO.getUserByPhone(phoneNumber);
					if (user == null) {
						headerText = "No record exists with the given phone number";
						AlertController.showError(title, headerText, contentText);
					} else {
						String email = user.getEmail();
						headerText = "Your registered email is : " + email;
						AlertController.showSuccess(title, headerText, contentText);
					}
				}
			}
		} else {
			headerText = "No Input Given";
			AlertController.showError(title, headerText, contentText);
			System.out.println("Could not retrieve forgotten user email");
		}
	}
	
	public static String enterCurrentPasswordDialog(int iterationCount) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Request for Change Password");
		dialog.setHeaderText("Current Password::Attempts Remaining : "+(3 - iterationCount));
		
		dialog.getDialogPane().setPrefWidth(400);
		dialog.getDialogPane().setPrefHeight(200);

		PasswordField passwordField = new PasswordField();
		dialog.getDialogPane().setContent(passwordField);

		passwordField.setPrefHeight(50);
		passwordField.setPrefWidth(200);
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
		    String password = passwordField.getText();
		    // Do something with the password...
		    boolean passwordsMatched = PasswordUtils.checkPassword(password, user.getPassword());
		    if(passwordsMatched)
		    	return "matched";
		    return "not_matched";
		}
		return null;
	}
	
	public static String getEnteredOTPInputForTransaction(TransactionCategory transactionCategory) {
		
		String title = null;
		String headerText = null;
		String contentText = null;
		
		headerText = "Enter the high security code sent to your email";
		if(transactionCategory == TransactionCategory.ONLINE_PAYMENT)
			title = "Online Payment";
		else if(transactionCategory == TransactionCategory.CASH_DEPOSIT)
			title = "Cash Deposit";
		else if(transactionCategory == TransactionCategory.CC_BILL_PAYMENT)
			title = "Credit Card Bill Payment";
		else if(transactionCategory == TransactionCategory.TRANSFER_TO_BENEFICIARY)
			title = "Transfer to Benificiary";
		else if(transactionCategory == TransactionCategory.TRANSFER_FROM_SELF)
			title = "Transfer to Self";
		
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    String enteredOTP = result.get();
		    return enteredOTP;
		}
		return null;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		refreshState();
	}
}
