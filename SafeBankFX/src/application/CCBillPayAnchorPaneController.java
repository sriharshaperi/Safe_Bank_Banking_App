package application;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import dao.CreditCardsDAO;
import dao.SavingsAccountsDAO;
import enums.PaymentStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;
import models.CreditCard;
import models.SavingsAccount;
import transactions.CCBillPaymentTransaction;
import utils.CreditCardUtils;
import utils.SavingsAccountUtils;
import validations.TransactionValidations;

public class CCBillPayAnchorPaneController extends Controller implements Initializable {

	@FXML
	private ToggleGroup ChangeAmountLimit;

	@FXML
	private Button btnReset;

	@FXML
	private Button btnCCBillPay;

	@FXML
	private ComboBox<String> cbAccounts;

	@FXML
	private RadioButton radioBtnCustom;

	@FXML
	private RadioButton radioBtnFull;

	@FXML
	private RadioButton radioBtnMin;

	@FXML
	private TextField txtAmount;


	private Map<String, Long> displayAccountNumbersMapping;
	private Map<String, Long> displayCardNumbersMapping;
	private String selectedDisplayAccountNumber;
	private String selectedDisplayCardNumber;
	private SavingsAccount currentSelectedAccount;
	private Double payableAmount;

	private int incorrectOTPAttempts;
	private int invalidOTPAttemptsCount;
	private int insufficientFundsAttemptsCount;

	@FXML
	public void handleCCBillPayAction(ActionEvent event) throws Exception {

		String title = null;
		String headerText = null;
		String contentText = null;
		
		if(currentSelectedAccount == null) {
			title = "Credit Card Bill Payment";
			headerText = "No Account Selected";
			contentText = "Please select an account to pay from";
			AlertController.showError(title, headerText, contentText);
			return;
		}

		selectedDisplayAccountNumber = SavingsAccountUtils
				.getLastFourDigitsOf(currentSelectedAccount.getAccountNumber());

		String userId = user.getUserId().toString();
		String accountId = currentSelectedAccount.getAccountId().toString();
		double accountBalance = currentSelectedAccount.getAccountBalance();

		CreditCard userCreditCard = user.getCreditCard();
		String cardId = userCreditCard.getCreditCardId().toString();
		double remainingCreditLimit = userCreditCard.getRemainingCreditLimit();
		double totalCreditLimit = userCreditCard.getTotalCreditLimit();

		selectedDisplayCardNumber = SavingsAccountUtils.getLastFourDigitsOf(userCreditCard.getCardNumber());

		String amountTextFieldValue = txtAmount.getText();
		boolean isAmountValid = TransactionValidations.isAmountValidForCCBillPayment(amountTextFieldValue);

		
		if (!isAmountValid) {
			title = "Invalid Amount";
			headerText = "Amount is invalid for transaction";
			contentText = "Enter a valid numerical value";
			AlertController.showError(title, headerText, contentText);
			return;
		} else {

			double amount = Double.parseDouble(amountTextFieldValue);
			if (amount > 1.1 * totalCreditLimit) {
				title = "Invalid Amount";
				headerText = "Cannot exceed the payable amount over 10 percent of total credit limit";
				contentText = "Enter a valid numerical value upto " + (totalCreditLimit + 0.1 * totalCreditLimit);
				AlertController.showError(title, headerText, contentText);
				return;
			} else {
				CCBillPaymentTransaction ccBillPaymentTransaction = new CCBillPaymentTransaction();
				ccBillPaymentTransaction.setUserId(userId);
				ccBillPaymentTransaction.setAccountId(accountId);
				ccBillPaymentTransaction.setCardId(cardId);
				ccBillPaymentTransaction.setAccountBalance(accountBalance);
				ccBillPaymentTransaction.setRemainingCreditLimit(remainingCreditLimit);
				ccBillPaymentTransaction.setAmount(amount);
				PaymentStatus paymentStatus = ccBillPaymentTransaction.payCCBillAmount();

				if (paymentStatus == PaymentStatus.SUCCESS) {
					title = "Transaction Successful";
					headerText = "*** Successfully Paid USD " + amount;
					contentText = "towards card number : " + selectedDisplayCardNumber + "\n" + "from account number : "
							+ selectedDisplayAccountNumber + " ***";
					CCBillPaymentTransaction.updateRemainingCreditLimit(userId, cardId, remainingCreditLimit, amount);
					AlertController.showSuccess(title, headerText, contentText);
					SwitchSceneController.invokeLayout(event, SceneFiles.TRANSACTIONS_SCENE_LAYOUT);
					return;
				} else {
					if (insufficientFundsAttemptsCount == 2 || invalidOTPAttemptsCount == 2
							|| incorrectOTPAttempts == 2) {

						title = "Transaction Blocked";
						headerText = "*** You have reached maximum allowed attempts ***";
						contentText = "For security reasons, you will be signed out";
						if (isSessionActive) {
							isSessionActive = false;
							user = null;
						}
						AlertController.showError(title, headerText, contentText);
						SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
					} else {
						if (paymentStatus == PaymentStatus.INSUFFICIENT_FUNDS) {
							title = "Insufficient Funds";
							headerText = "*** Insufficient Funds ***";
							contentText = "Try again with different ammount or account." + " Attempts Remaining : "
									+ (2 - insufficientFundsAttemptsCount) + " ***";
							insufficientFundsAttemptsCount += 1;
						} else if (paymentStatus == PaymentStatus.INCORRECT_OTP) {
							title = "Incorrect OTP";
							headerText = "*** Incorrect OTP. Transaction Declined. " + "Attempts Remaining : "
									+ (2 - incorrectOTPAttempts) + "***";
							incorrectOTPAttempts += 1;
						} else if (paymentStatus == PaymentStatus.INVALID_OTP) {
							title = "Invalid OTP";
							headerText = "*** Invalid OTP. " + "Transaction Declined. Attempts Remaining : "
									+ (2 - invalidOTPAttemptsCount) + " ***";
							invalidOTPAttemptsCount += 1;
						} else if (paymentStatus == PaymentStatus.PAYMENT_EXCEPTION) {
							title = "Transaction Exception";
							headerText = "*** Transaction Failed .Try Again Later ***";
						}
						AlertController.showError(title, headerText, contentText);
						return;
					}
				}
			}

		}
	}

	@FXML
	public void displayAccounts(ActionEvent event) throws IOException {
		String selectedAccountNumber = cbAccounts.getSelectionModel().getSelectedItem();
		if (selectedAccountNumber != null) {
			txtAmount.setDisable(true);
			btnReset.setVisible(true);

			Long accountNumber = displayAccountNumbersMapping.get(selectedAccountNumber);
			currentSelectedAccount = SavingsAccountsDAO.getSavingsAccountByAccountNumber(accountNumber);
			if(radioBtnCustom.isSelected() && txtAmount.isDisable()) {
				txtAmount.setText("");
				txtAmount.setDisable(false);
			}
		}
	}

	@FXML
	public void handleChangeAmountLimit(ActionEvent event) throws IOException {

		payableAmount = CreditCardUtils.getPayableAmount(user.getCreditCard());

		if (radioBtnFull.isSelected()) {
			txtAmount.setDisable(true);
			txtAmount.setText(payableAmount.toString());
		} else if (radioBtnMin.isSelected()) {
			payableAmount = 0.1 * payableAmount;
			txtAmount.setDisable(true);
			txtAmount.setText(payableAmount.toString());
		} else {
			txtAmount.setText("");
			txtAmount.setDisable(false);
		}

	}

	@FXML
	public void handleReset(ActionEvent event) throws IOException {
		cbAccounts.getSelectionModel().clearSelection();
		cbAccounts.setButtonCell(new PromptButtonCell<>(cbAccounts.getPromptText()));
		btnReset.setVisible(false);
		radioBtnFull.setSelected(true);
		txtAmount.setText("");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		refreshState();
		displayAccountNumbersMapping = new HashMap<>();
		displayCardNumbersMapping = new HashMap<>();

		List<String> accountNumbers = user.getAccounts().stream().map(account -> {
			long accountNumber = account.getAccountNumber();
			String displayAccountNumber = SavingsAccountUtils.getLastFourDigitsOf(accountNumber);
			displayAccountNumbersMapping.put(displayAccountNumber, accountNumber);
			return displayAccountNumber;
		}).collect(Collectors.toList());

		ObservableList<String> accountNumbersList = FXCollections.observableArrayList(accountNumbers);
		cbAccounts.setItems(accountNumbersList);
		cbAccounts.setStyle("-fx-font-size: 20px;");

		CreditCard userCreditCard = CreditCardsDAO.getCreditCardByUserId(user.getUserId().toString());

		String displayCardNumber = SavingsAccountUtils.getLastFourDigitsOf(userCreditCard.getCardNumber());
		displayCardNumbersMapping.put(displayCardNumber, userCreditCard.getCardNumber());

		Double totalPayableAmount = CreditCardUtils.getPayableAmount(userCreditCard);
		
		btnReset.setVisible(false);
		txtAmount.setText(totalPayableAmount.toString());
		txtAmount.setDisable(true);
	}
}