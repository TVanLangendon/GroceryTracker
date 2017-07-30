package main.java.view;

import java.io.IOException;

import main.java.model.PantryData;
import main.java.controller.Controller;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UserInterface extends Application {

	private Stage newUserStage;
	private Stage hubStage;
	private Stage loginStage;
	private Stage pantryStage;
	private Stage addFoodStage;
	@Override
	public void start(Stage primaryStage) throws IOException {

		try{
		//instantiates FXMLLoader and sets the location to look for files
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/resources/LoginPage.fxml"));

		// establishes a GridPane as the root node
		GridPane root = loader.load();

		// Creates the scene and shows the primary stage
		loginStage = primaryStage;
		loginStage.setScene(new Scene(root, 300, 275));
		loginStage.setTitle("YourFoodTracker");
		loginStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void showNewUserAccountPage() throws IOException {

		GridPane root = FXMLLoader.load(getClass().getResource(
				"/resources/AccountCreationPage.fxml"));

		newUserStage = new Stage();
		newUserStage.setTitle("Create New Account");
		newUserStage.setScene(new Scene(root, 300, 300));
		newUserStage.initModality(Modality.APPLICATION_MODAL);
		newUserStage.show();
	}

	public void showHubPage() throws IOException {

		GridPane root = FXMLLoader
				.load(getClass().getResource("/resources/HubPage.fxml"));

		hubStage = new Stage();
		hubStage.setTitle("Welcome to YourFoodTracker");
		hubStage.setScene(new Scene(root, 500, 500));
		hubStage.initModality(Modality.APPLICATION_MODAL);
		hubStage.show();

	}

	public void showPantryPage() {

		try{
		
		//creates a new FXMLoader and assigns its resourcebundle to /resources/PantryPage.fxml
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"/resources/PantryPage.fxml"));

		//loads the FXML object graph into a VBox variable (root)
		VBox root = loader.load();
		

		//gets the controller object that was instantiated by FXMLLoader.load()
		Controller controller = loader.<Controller>getController();
		
		//calls a method in the controller object which sets the Label text and fills the TableView!
		controller.setupPantryPage();
		
		pantryStage = new Stage();
		pantryStage.setScene(new Scene(root, 500, 500));
		pantryStage.initModality(Modality.APPLICATION_MODAL);
		pantryStage.setTitle("Your Pantry");

		pantryStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void showAddFoodPage(){
		
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/AddFoodToPantryPage.fxml"));
			
			VBox root = loader.load();
			
			Controller controller = loader.<Controller>getController();
			controller.setupAddFoodToPantryPage();
			
			addFoodStage = new Stage();
			addFoodStage.setScene(new Scene(root));
			addFoodStage.initModality(Modality.APPLICATION_MODAL);
			addFoodStage.setTitle("Add Food to Your Pantry");
			
			addFoodStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void loginFailure(Text text) {
		text.setFill(Color.RED);
		text.setText("Invalid Username and/or Password!");
	}

	public void closeNewUserStage() {
		newUserStage.close();
	}

	public void closeLoginStage() {
		loginStage.close();
	}
	
	public void closeAddFoodStage(){
		addFoodStage.close();
	}

}
