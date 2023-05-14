package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import dao.CreditCardsDAO;
import dao.UsersDAO;
import enums.CardCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.CreditCard;
import models.SavingsAccount;
import notifications.EmailService;
import services.CreditCardsService;
import services.SavingsAccountService;
import utils.CreditCardUtils;
import validations.UserValidations;

public class ProfileInfoSceneController extends Controller implements Initializable {
	@FXML
	private Label lblHome;
	@FXML
	private Button btnLogout;
	@FXML
	private Label lblCurrentUserEmail;
	@FXML
	private Button btnGoBack;
	@FXML
	private Label lblName;
	@FXML
	private TextField txtName;
	@FXML
	private Label lblEmail;
	@FXML
	private TextField txtEmail;
	@FXML
	private Label lblPhone;
	@FXML
	private TextField txtPhone;
	@FXML
	private Button btnChangePassword;
	@FXML
	private Button btnChangeNumber;
	@FXML
	private Button btnChangeName;
	@FXML
	private Button btnEditName;
	@FXML
	private Button btnEditNumber;
	@FXML
	private Button btnRequestCreditCardUpgrade;
	@FXML
	private Button btnViewCreditScore;
	@FXML
	private AnchorPane anchorPaneChangePassword;
	@FXML
	private Button btnNewSavingsAccount;
	
	private static int incorrectCorrentPasswordAttempts;

	@FXML
	public void handleChangeNameAction(ActionEvent event) throws IOException, MessagingException {
		if (txtName.isDisabled()) {
			// change
			txtName.setDisable(false);
			txtName.setEditable(true);
			btnChangeName.setText("Save Changes");
		} else {
			// save change
			String title = null;
			String headerText = null;
			String contentText = null;

			String name = txtName.getText();
			boolean isNameValid = UserValidations.isNameValid(name);
			if (!isNameValid) {
				headerText = "Invalid Name Format";
				AlertController.showError(title, headerText, contentText);
				return;
			} else {
				UsersDAO.updateUserName(user.getUserId().toString(), name);
				txtName.setDisable(true);
				btnChangeName.setText("Change Name");
				headerText = "Updated User Name Successfully";

				AlertController.showSuccess(title, headerText, contentText);
				String toEmail = user.getEmail();
				String subject = "SafeBank Update Account Details";
				String message = "Your account name has been updated";

				EmailService.sendEmail(toEmail, subject, message);
				
				return;
			}
		}
	}

	@FXML
	public void handleChangeNumberAction(ActionEvent event) throws IOException, MessagingException {
		if (txtPhone.isDisabled()) {
			// change
			txtPhone.setDisable(false);
			txtPhone.setEditable(true);
			btnChangeNumber.setText("Save Changes");
		} else {
			// save change
			String title = null;
			String headerText = null;
			String contentText = null;

			String phone = txtPhone.getText();
			boolean isPhoneValid = UserValidations.isPhoneValid(phone);
			if (!isPhoneValid) {
				headerText = "Invalid Phone Number";
				AlertController.showError(title, headerText, contentText);
				return;
			} else {

				long phoneNumber = Long.parseLong(phone);
				boolean phoneExists = UsersDAO.userExistsByPhoneAndNotCurrentUser(phoneNumber, user.getUserId().toString());
				if (phoneExists) {
					headerText = "User exists with the given phone number";
					AlertController.showError(title, headerText, contentText);
					return;
				} else {
					UsersDAO.updateUserPhone(user.getUserId().toString(), phoneNumber);
					txtPhone.setDisable(true);
					btnChangeNumber.setText("Change Number");
					headerText = "Updated User Phone Number Successfully";

					AlertController.showSuccess(title, headerText, contentText);
					String toEmail = user.getEmail();
					String subject = "SafeBank Update Account Details";
					String message = "Your phone number has been updated";
					
					txtPhone.setDisable(true);
					EmailService.sendEmail(toEmail, subject, message);
					
					return;
				}
			}
		}
	}	
	
	@FXML 
	public void handleOpenNewSavingsAccountAction(ActionEvent event) throws IOException {
		
		String title = null;
		String headerText = null;
		String contentText = null;
		
		int numberOfSavingsAccounts = user.getAccounts().size();
		
		if(numberOfSavingsAccounts < 6) {
			boolean createdNewSavingsAccount = 
					SavingsAccountService.createSavingsAccount(user.getUserId().toString(), user);
			if(createdNewSavingsAccount) {
				title = "Create New Savings Account";
				headerText = "Created New Savings Account";
				AlertController.showSuccess(title, headerText, contentText);
				return;
			}
			else {
				title = "Account creation failed";
				headerText = "Some Error Occurred";
				AlertController.showError(title, headerText, contentText);
				return;
			}
		}
		else {
			title = "Account Creation";
			headerText = "Reached maximum limit for creating accounts";
			contentText = "If you still want to create an account, register with a new email address";
			AlertController.showWarning(title, headerText, contentText);
			return;
		}
	}

	@FXML
	public void handleChangePasswordAction(ActionEvent event) throws IOException {

		String title = null;
		String headerText = null;
		String contentText = null;
		boolean iterating = true;
		while (iterating) {
			String passwordsMatched = DialogController.enterCurrentPasswordDialog(incorrectCorrentPasswordAttempts);
			if(passwordsMatched == null) iterating = false;
			else if (passwordsMatched.equals("matched")) {
				incorrectCorrentPasswordAttempts = 0;
				AnchorPane transferOther = (AnchorPane) FXMLLoader
						.load(getClass().getResource(SceneFiles.RESET_PASSWORD_ANCHOR_PANE));
				anchorPaneChangePassword.getChildren().setAll(transferOther.getChildren());
				FXMLLoader loader = new FXMLLoader(getClass().getResource(SceneFiles.RESET_PASSWORD_ANCHOR_PANE));
				Parent root = loader.load();
				ResetPasswordAnchorPaneController resetPasswordController = loader.getController();
				resetPasswordController.setResetPasswordEmail(user.getEmail());
				iterating = false;
			} else {

				if (incorrectCorrentPasswordAttempts == 2) {
					iterating = false;
					Controller.user = null;
					Controller.isSessionActive = false;
					headerText = "Too many incorrect attempst. For security reasons, you will be signed out";
					AlertController.showError(title, headerText, contentText);
					incorrectCorrentPasswordAttempts = 0;
					SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
					return;
				}
				incorrectCorrentPasswordAttempts++;
			}
			
		}
	}

	@FXML
	public void handleRequestCreditCardUpgradeAction(ActionEvent event) throws Exception {
		
		refreshState();
		String title = null;
		String headerText = null;
		String contentText = null;

		String userId = user.getUserId().toString();
		CreditCard userCreditCard = user.getCreditCard();

		int creditScore = user.getCreditScore();
		if (userCreditCard.getCardCategory() == null) {
			refreshState();
			// CREATING NEW CARD
			if (creditScore < 720) {
				title = "SafeBank Credit Card Request";
				headerText = "Poor Credit Score. Boost up your credit score by adding more account transactions";
				AlertController.showError(title, headerText, contentText);
				return;
			} else {
				CardCategory cardCategory = CreditCardUtils.getEligibleCreditCard(creditScore, user);
				double totalcreditLimit = CreditCardUtils.getTotalCreditLimit(cardCategory);
				CreditCardsService.createNewCreditCard(userId, user, cardCategory);
				title = "SafeBank Credit Card Request";
				headerText = "Congratulations. We offered you a " + cardCategory + ", with a credit limit of "
						+ totalcreditLimit;
				AlertController.showSuccess(title, headerText, contentText);
				return;
			}
		} else {

			// UPDATING EXISTING CARD
			String cardId = userCreditCard.getCreditCardId().toString();
			CardCategory cardCategory = CreditCardUtils.getEligibleCreditCard(creditScore, user);
			double newTotalCreditLimit = CreditCardUtils.getTotalCreditLimit(cardCategory);
			double oldTotalCreditLimit = userCreditCard.getTotalCreditLimit();
			double oldRemainingCreditLimit = userCreditCard.getRemainingCreditLimit();
			double newRemainingCreditLimit = oldRemainingCreditLimit + (newTotalCreditLimit - oldTotalCreditLimit);
			if (cardCategory != userCreditCard.getCardCategory()) {
				// upgraded
				CreditCardsDAO.updateTotalCreditLimit(userId, cardId, newTotalCreditLimit, newRemainingCreditLimit, cardCategory);
				
				title = "SafeBank Credit Card Upgrade Request";
				headerText = "Congratulations. We offered you a " + cardCategory + "card, with a credit limit of "
						+ newTotalCreditLimit;
				AlertController.showSuccess(title, headerText, contentText);
				String toEmail = user.getEmail();
				String subject = "SafeBank Update Account Details";
				String message = "Your total credit limit has been upgraded";

				EmailService.sendEmail(toEmail, subject, message);
				
				return;
			} else {
				
				// same
				
				if(userCreditCard.getCardCategory() == CardCategory.PLATINUM) {
					title = "SafeBank Credit Card Upgrade Request";
					headerText = "Currently you have the most premium card. " + userCreditCard.getCardCategory()
							+ " is the maixum we can offer.";
					AlertController.showSuccess(title, headerText, contentText);
				}
				else {
					title = "SafeBank Credit Card Upgrade Request";
					headerText = "Currently you have " + userCreditCard.getCardCategory()
							+ " credit card. Boost your score further to upgrade your credit card and credit limit";
					AlertController.showSuccess(title, headerText, contentText);
				}
				return;
			}
			
		}

	}

	@FXML
	public void handleViewCreditScoreAction(ActionEvent event) throws IOException, MessagingException {
		int creditScore = user.getCreditScore();
		boolean poorScore = creditScore < 750;
		boolean averageScore = creditScore >= 750 && creditScore <= 799;
		boolean goodScore = creditScore >= 800;
		boolean bestScore = creditScore >= 830;
		String status = "";
		if (poorScore)
			status = "Poor";
		else if (averageScore)
			status = "Average";
		else if (goodScore)
			status = "Good";
		else if (bestScore)
			status = "Best";
		
		String title = "SafeBank Credit Score";
		String headerText = "Your Credit Score : " + creditScore + "\n";
		String contentText = "Status : " + status;
		AlertController.showSuccess(title, headerText, contentText);
		
		String toEmail = user.getEmail();
		String subject = "SafeBank Credit Score";
		String message = "Your Credit Score : " + creditScore + "\n" + "Status : " + status;

		EmailService.sendEmail(toEmail, subject, message);
		return;
	}

	@FXML
	public void handleLogoutAction(ActionEvent event) throws IOException {
		isSessionActive = false;
		user = null;
		SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
	}

	@FXML
	public void invokeHomeSceneLayout(ActionEvent event) throws IOException {
		SwitchSceneController.invokeLayout(event, SceneFiles.HOME_SCENE_LAYOUT);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		refreshState();
		// TODO Auto-generated method stub
		txtEmail.setDisable(true);
		txtName.setDisable(true);
		txtPhone.setDisable(true);
		txtEmail.setText(user.getEmail());
		txtName.setText(user.getName());
		txtPhone.setText(user.getPhone() + "");
		if(user.getCreditCard().getCreditCardId() != null)
			btnRequestCreditCardUpgrade.setText("Upgrade Existing Credit Card");
	}
}
