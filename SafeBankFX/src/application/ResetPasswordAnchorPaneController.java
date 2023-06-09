package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.UsersDAO;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import utils.PasswordUtils;
import validations.UserValidations;

public class ResetPasswordAnchorPaneController extends Controller implements Initializable {
	@FXML
	private AnchorPane anchorPaneResetPassword;
	@FXML
	private PasswordField txtNewPassword;
	@FXML
	private PasswordField txtConfirmNewPassword;
	@FXML
	private Label lblResetPassword;
	@FXML
	private Button btnChangePassword;
	@FXML
	private Label lblResetPasswordEmail;
	@FXML
	private Hyperlink hyperlinkToLogin;
	@FXML
	private Button btnCancel;
	
	private static String emailVerified;

	// Event Listener on Button[#btnChangePassword].onAction
	@FXML
	public void handleChangeNewPasswordAction(ActionEvent event) throws IOException {
		// TODO Autogenerated
		String title = null;
		String headerText = null;
		String contentText = null;

		String newPassword = txtNewPassword.getText();
		String confirmNewPassword = txtConfirmNewPassword.getText();

		boolean isPasswordValid = UserValidations.isPasswordValid(newPassword);
		boolean isConfirmPasswordValid = UserValidations.isPasswordValid(newPassword);
		if (!isPasswordValid || !isConfirmPasswordValid) {
			if (!isPasswordValid) {
				headerText = "Invalid Password";
				AlertController.showError(title, headerText, contentText);
				return;
			}

			if (!isConfirmPasswordValid) {
				headerText = "Invalid Confirm Password";
				AlertController.showError(title, headerText, contentText);
				return;
			}
		} else {
			if (!newPassword.equals(confirmNewPassword)) {
				headerText = "Passwords do not match";
				AlertController.showError(title, headerText, contentText);
				return;
			} else {
				String hashedPassword = PasswordUtils.hashPassword(confirmNewPassword);
				boolean updatedPassword = UsersDAO.updateUserPassword(emailVerified, hashedPassword);

				if (updatedPassword) {
					emailVerified = null;
					headerText = "Changed Password Successfully. Login to Continue";
					AlertController.showSuccess(title, headerText, contentText);
				} else {
					headerText = "Failed to change password";
					AlertController.showError(title, headerText, contentText);

				}
				if(isSessionActive) {
					isSessionActive = false;
					user = null;
				}
				SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
				return;
			}
		}
	}
	@FXML
	public void handleCancelAction(ActionEvent event) throws IOException {
		if(isSessionActive && user != null) {
			anchorPaneResetPassword.getChildren().setAll(anchorPaneResetPassword.getChildren());
		}	
		else
			SwitchSceneController.invokeLayout(event, SceneFiles.FORGOT_PASSWORD_SCENE);
	}

	public void setResetPasswordEmail(String email) {
		lblResetPasswordEmail.setText(email);
		emailVerified = email;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		refreshState();
	}
}
