package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {    
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			
			Screen screen = Screen.getPrimary();
	        double screenWidth = screen.getBounds().getWidth();
	        double screenHeight = screen.getBounds().getHeight();
	      
	        
	        double windowDecorationWidth = primaryStage.getWidth() - primaryStage.getScene().getWidth();
	        double windowDecorationHeight = primaryStage.getHeight() - primaryStage.getScene().getHeight();
	        root.setPrefSize(screenWidth - windowDecorationWidth, screenHeight - windowDecorationHeight);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
			
			primaryStage.show();	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
