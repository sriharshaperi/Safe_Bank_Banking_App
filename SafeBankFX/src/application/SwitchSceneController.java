package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SwitchSceneController extends Controller {
	private void setSceneLayout(ActionEvent event, String fileNameWithExt) throws IOException {
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource(fileNameWithExt));
		Scene scene = new Scene(root,root.getMinWidth(),root.getMinHeight());
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void invokeLayout(ActionEvent event, String fileNameWithExt) throws IOException {
		new SwitchSceneController().setSceneLayout(event, fileNameWithExt);
	}
}
