package main.java.app;

import java.sql.SQLException;
import main.java.controller.Controller;
import javafx.application.Application;
import main.java.model.Database;
import main.java.view.UserInterface;

public class RunApp {

	public static void main(String[] args) throws SQLException {

		Database database = new Database();
		UserInterface loginPage = new UserInterface();

		Controller controller = new Controller(loginPage, database);

		Application.launch(UserInterface.class, args);

		database.closeConnection();
		System.out.println("Connection closed");
	}
}