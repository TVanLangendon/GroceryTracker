package main.java.controller;

import main.java.model.*;
import main.java.view.UserInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Controller {

	// ERROR: I get an invocation error if these are not static. 
	//This is because the FXMLLoader will instantiate a new Controller object each time. This instantiation will use a non-parameterized constructor
	//so I cannot pass variables to assign the view and model. Instead I use static view and model so that each controller instance can access them
	private static UserInterface theUserInterface;
	private static Database theDatabase;
	
	private static int userID;
	private static ObservableList<String> foodNames;
	private static String foodNameBoxInput;

	@FXML private PasswordField passwordField;
	@FXML private TextField usernameField;
	@FXML private TextField newUsernameField;
	@FXML private PasswordField newPasswordField;
	@FXML private Text welcomeText;
	@FXML private Label pantryWelcome;	//must NOT be static. Why?: Maybe loader does not make variables accessible to the Class (Controller), but instead to the instantiated object
	@FXML private TableView<PantryData> pantryTableView;
	@FXML private TableColumn<PantryData, String> nameCol;
	@FXML private TableColumn<PantryData, String> brandCol;
	@FXML private TableColumn<PantryData, String> stockCol;
	@FXML private TableColumn<PantryData, String> expirationCol;
	@FXML private TableColumn<PantryData, String> costCol;
	@FXML private TableColumn<PantryData, String> categoryCol;
	@FXML private ComboBox<String> foodNameBox;
	@FXML private ComboBox<String> brandBox;
	@FXML private ComboBox<String> quantityBox;
	@FXML private ComboBox<String> quantitySizeBox;
	

	// constructor called by the FXML file when declaring fx:controller
	public Controller(){
		
	}
	

	//constructor called by RunApp so that I can assign the static UserInterface and Database
	public Controller(UserInterface userInterface, Database database)
			throws SQLException {

		theUserInterface = userInterface;
		theDatabase = database;

		theDatabase.initializeDB();
	}

	@FXML
	private void handleSubmitButton(ActionEvent event) throws SQLException,
			IOException {
		if (theDatabase.verifyLogin(usernameField.getText(),
				passwordField.getText())) {
			theUserInterface.showHubPage();
		} else
			theUserInterface.loginFailure(welcomeText);
	}

	@FXML
	private void handleShowNewAccountPage(ActionEvent event)
			throws SQLException, IOException {
		theUserInterface.showNewUserAccountPage();
	}

	@FXML
	private void handleCreateNewAccount(ActionEvent event)
			throws SQLException {
		if (theDatabase.newUserAccount(newUsernameField.getText(),
				newPasswordField.getText()))
			theUserInterface.closeNewUserStage();

	}

	@FXML
	private void handleShowPantryPage(ActionEvent event) throws IOException {
		theUserInterface.showPantryPage();
	}
	
	@FXML
	private void handleShowAddFoodToPantryPage(ActionEvent event){
		theUserInterface.showAddFoodPage();
	}
	
	@FXML
	private void handleSubmitFoodToPantry(ActionEvent event){
		theDatabase.addFoodToPantry(foodNameBox.getValue().toString(), "1", "1", brandBox.getValue().toString(), "Fruit");
		theUserInterface.closeAddFoodStage();
	}

	//called by the UserInterface to fill the tableview with SQL data before .show() the page 
	public void setupPantryPage(){
		pantryWelcome.setText("Welcome, " + theDatabase.getUsername());
		
		ObservableList<PantryData> pantryData = theDatabase.getPantryTableData();
		pantryTableView.setItems(pantryData);
		
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		brandCol.setCellValueFactory(new PropertyValueFactory("brand"));
		stockCol.setCellValueFactory(new PropertyValueFactory("stock"));
		expirationCol.setCellValueFactory(new PropertyValueFactory("expiration"));
		costCol.setCellValueFactory(new PropertyValueFactory("cost"));
		categoryCol.setCellValueFactory(new PropertyValueFactory("category"));
	}
	

	
	//REFACTOR: Instead of making an SQL query after each keystroke by calling theDatabase.getFoodNames(), 
	//Use a formatted string to check through the full list of foodNames already generated and stored as items
	public void setupAddFoodToPantryPage(){
		
		foodNames = theDatabase.getFoodNames("%");
		foodNameBox.setItems(foodNames);
		foodNameBoxInput = "";
		
		foodNameBox.getEditor().setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				System.out.println("MouseClicked - show");
				foodNameBox.show();
			}
		});
		
		foodNameBox.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>(){
			
			@Override
			public void handle(KeyEvent e){
					
				//Checks to see if user typed a Backspace (/u0008) or a DEL (/u0127)
				//If so, sets the String equal to the input minus 1 character (.getEditor().getText())
				if (e.getCharacter().codePointAt(0) == 8 || e.getCharacter().codePointAt(0) == 127){
					foodNameBoxInput = foodNameBox.getEditor().getText();
				}
				
				//Otherwise adds the new character to the string. 
				//PROBLEM, concat only adds to the end of the String
				//Solution: Use StringBuilder to insert instead. 
				else{
				foodNameBoxInput = foodNameBoxInput.concat(e.getCharacter());
				}
				
				System.out.println("foodNameBoxInput: " + foodNameBoxInput);
				foodNames = theDatabase.getFoodNames(foodNameBoxInput);
				foodNameBox.setItems(foodNames);
				foodNameBox.show();
			}
		});
			

	}
	
	
	
	public int getUserID(){
		return userID;
	}
	
	public void setUserID(int iD){
		userID = iD;
	}
	
	//Method to optionally implement some of the TableView without the FXML file
/*	public void setupPantryPage() {
		
		pantryWelcome.setText("Welcome, " + theDatabase.getUsername());
		ObservableList<PantryData> pantryData = theDatabase.getPantryTableData();

		
		TableColumn<PantryData, String> nameCol = new TableColumn<PantryData, String>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		
		TableColumn<PantryData, String> brandCol = new TableColumn<PantryData, String>("Brand");
		brandCol.setCellValueFactory(new PropertyValueFactory("brand"));
		
		TableColumn<PantryData, String> stockCol = new TableColumn<PantryData, String>("Stock");
		stockCol.setCellValueFactory(new PropertyValueFactory("stock"));
		
		TableColumn<PantryData, String> expirationCol = new TableColumn<PantryData, String>("Expiration");
		expirationCol.setCellValueFactory(new PropertyValueFactory("expiration"));
		
		TableColumn<PantryData, String> costCol = new TableColumn<PantryData, String>("Cost");
		costCol.setCellValueFactory(new PropertyValueFactory("cost"));
		
		TableColumn<PantryData, String> categoryCol = new TableColumn<PantryData, String>("Category");
		categoryCol.setCellValueFactory(new PropertyValueFactory("category"));
		
		pantryTableView.setItems(pantryData);
		
		pantryTableView.getColumns().setAll(nameCol, brandCol, stockCol, expirationCol, costCol, categoryCol);
		
	}*/


}
