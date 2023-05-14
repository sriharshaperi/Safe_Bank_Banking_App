package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.BeneficiaryUsersDAO;
import dao.UsersDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.BeneficiaryUser;
import models.User;
import validations.UserValidations;

public class AddNewBenAnchorPaneController extends Controller implements Initializable {
	@FXML
	private TextField benEmail;
	@FXML
	private Button btnAddBen;
	
	// Event Listener on Button[#btnAddBen].onAction
	@FXML
	public void handeAddBenAction(ActionEvent event) throws IOException {
		// TODO Autogenerated
		
		String title = null;
		String headerText = null;
		String contentText = null;
		
		String email = benEmail.getText();
		boolean isEmailValid = UserValidations.isEmailValid(email);
		boolean emailExists = UsersDAO.userExistsByEmail(email);
		title = "Add New Beneficiary";
		if (!isEmailValid) {
			headerText = "Invalid Email";
			AlertController.showError(title, headerText, contentText);
			return;
		}
		else if (!emailExists) {
			headerText = "User does not exist with the given email";
			AlertController.showError(title, headerText, contentText);
			return;
		}
		else if (user.getEmail().equals(email)){
			headerText = "You cannot add yourself as a beneficiary user";
			AlertController.showError(title, headerText, contentText);
			return;
		}
		else {
			User toBeBeneficiaryUser = UsersDAO.getUserByEmail(email);
			String beneficiaryUserId = toBeBeneficiaryUser.getUserId().toString();
			String userId = user.getUserId().toString();
			boolean addedBeneficiary = BeneficiaryUsersDAO.addNewBeneficiary(userId, beneficiaryUserId);
			if(addedBeneficiary) {
				headerText = "Successfully added "+ user.getName() +" as a beneficiary user";
				AlertController.showConfirmation(title, headerText, contentText);
				SwitchSceneController.invokeLayout(event, SceneFiles.TRANSACTIONS_SCENE_LAYOUT);
				return;
			}
			else {
				BeneficiaryUser beneficiaryUser = BeneficiaryUsersDAO.getBeneficiary(beneficiaryUserId);
				User userData = UsersDAO.getUserById(beneficiaryUser.getBeneficiaryUserId().toString());
				headerText = "Beneficiary user "+userData.getName()+" already exists";
				AlertController.showError(title, headerText, contentText);
				return;
			}
		}
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		refreshState();
	}
}
