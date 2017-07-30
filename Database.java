package main.java.model;


import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.mysql.jdbc.Connection;


public class Database {

	private Connection conn;
	private String currentUserID;
	private String currentUsername;
	
	private ObservableList<PantryData> pantryData;
	private ObservableList<String> foodNames;
	

	// to be called by controller to establish connection to database before
	// launching javafx application
	public void initializeDB() throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver"); // load Driver
			System.out.println("Driver loaded");

			this.conn = (Connection) DriverManager.getConnection(
					"jdbc:mysql://localhost/GroceryApp", "root", "admin"); // establishes
																			// connection
																			// to
																			// DB
			System.out.println("DB connected");

			// Statement statement = conn.createStatement(); //create statement

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	// to be called when the "Sign In" button is clicked on LoginPage
	public boolean verifyLogin(String username, String password)
			throws SQLException {

		// sets up a prepared statement and resultset
		String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?;";
		PreparedStatement pLogin = conn.prepareStatement(query); // creates the
																	// preparedstatement
																	// by
																	// calling
																	// connection()
		pLogin.setString(1, username); // sets parameter 1 to username (inputted
										// into verifyLogin from a TextField)
		pLogin.setString(2, password); // sets parameter 2 to password
		ResultSet resultSet = pLogin.executeQuery(); // declares a resultSet to
														// hold the result of
														// the query

		// tests if the resultset has a query
		if (resultSet.next()) {
			System.out.println("Valid User!");
			currentUserID = resultSet.getString(1);
			currentUsername = resultSet.getString(2);
			return true;
		} else {
			System.out.println("Invalid!");
			return false;
		}
	}

	// to be called when the client clicks the "new"
	public boolean newUserAccount(String username, String password) {

		// sets up a prepared statement and resultset to insert a new user into
		// table
		String query = "INSERT INTO Users (Username, Password) VALUES (?, ?);";
		PreparedStatement pNewUser;
		try {
			pNewUser = conn.prepareStatement(query);
			pNewUser.setString(1, username);
			pNewUser.setString(2, password);
			pNewUser.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return false;
		}

		/*
		 * TEST to see if new user was inserted into table correctly. REPLACE
		 * WITH JUNIT TEST String query2 = "SELECT * FROM Users;"; Statement
		 * statement = conn.createStatement(); ResultSet resultSet =
		 * statement.executeQuery(query2); while (resultSet.next()){
		 * System.out.println(resultSet.getString(1) + resultSet.getString(2) +
		 * resultSet.getString(3)); }
		 */
	}
	
	public ObservableList<PantryData> getPantryTableData(){
		pantryData = FXCollections.observableArrayList();
		
		try{
			String query = "SELECT fs.Name, fs.Brand, u.Quantity, u.QuantityUnit, u.Expiration, u.CostPerOz, fs.Category FROM UsersFood u, Foodstuff fs WHERE u.FoodID=fs.FoodID AND u.UserID=?;";
			PreparedStatement pstatement = conn.prepareStatement(query);
			pstatement.setString(1, currentUserID);
			
			ResultSet rs = pstatement.executeQuery();
					
			while (rs.next()){
				String stock = rs.getString(3) + " " + rs.getString(4);
				PantryData pd = new PantryData(rs.getString(1), rs.getString(2), stock, rs.getString(5), rs.getString(6), rs.getString(7));
				pantryData.add(pd);
				System.out.println("Added " + pd.getStock() + "to Database.pantryData observable list");
				
			}
			

		}
		catch (SQLException e){
			System.out.println(e.getMessage());
		}
		
		return pantryData;
	}
	
	public void addFoodToPantry(String name, String vegan, String vegetarian, String brand, String category){
		
		try{
		String query = "INSERT INTO Foodstuff (Name, Vegan, Vegetarian, Brand, Category) VALUES (?, ?, ?, ?, ?);";
		PreparedStatement pstatement = conn.prepareStatement(query);
		pstatement.setString(1,  name);
		pstatement.setString(2, vegan);
		pstatement.setString(3, vegetarian);
		pstatement.setString(4, brand);
		pstatement.setString(5, category);
		
		pstatement.executeUpdate();
		
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public ObservableList<String> getFoodNames(String currentUserInput){
		foodNames = FXCollections.observableArrayList();
		
		try{
			String query = "SELECT Name FROM Foodstuff WHERE Name LIKE ?;";
			PreparedStatement pstatement = conn.prepareStatement(query);
			pstatement.setString(1,  currentUserInput + "%");
			
			ResultSet rs = pstatement.executeQuery();
					
			while (rs.next()){
				foodNames.add(rs.getString(1));
			}
			

		}
		catch (SQLException e){
			System.out.println(e.getMessage());
		}
		
		return foodNames;
	}
	
	

	// to be called after UI application closes
	public void closeConnection() throws SQLException {
		conn.close();
	}
	
	public String getUserID(){
		return currentUserID;
	}
	
	public void setUserID(String userID){
		currentUserID = userID;
	}
	
	public String getUsername(){
		return currentUsername;
	}
	
	public void setUsername(String username){
		currentUsername = username;
	}
	
	

}