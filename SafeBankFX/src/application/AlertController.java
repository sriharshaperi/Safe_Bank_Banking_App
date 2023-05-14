package application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertController extends Controller {

	
	public static boolean showConfirmation(String title, String headerText, String contentText) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
		return alert.getResult() == ButtonType.OK;
	}

	public static void showError(String title, String headerText, String contentText) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void showSuccess(String title, String headerText, String contentText) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void showWarning(String title, String headerText, String contentText) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
}
