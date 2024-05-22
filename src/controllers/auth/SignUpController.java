package controllers.auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import database.Connexion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {
    @FXML
    private TextField fullName;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private PasswordField conPassword;
    
	@FXML
	void redirect() throws IOException {
		Main.setRoot("/ressources/auth/Login");
	}
	
	@FXML
	void forentreprise() throws IOException {
		Main.setRoot("/ressources/auth/SignUp2");
	}
	
	@FXML
	void signup() {
	    String nameText = fullName.getText();
	    String emailText = email.getText();
	    String phoneText = phone.getText();
	    String passwordText = password.getText();
	    String conPasswordText = conPassword.getText();

	    // Check if any field is empty
	    if (nameText.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || passwordText.isEmpty() || conPasswordText.isEmpty()) {
	        showAlert(AlertType.ERROR, "Field Empty", "All fields are required", "Please fill in all the fields.");
	        return;
	    }

	    // Validate password length
	    if (passwordText.length() < 6) {
	        showAlert(AlertType.ERROR, "Password Error", "Password too short", "Password must be at least 6 characters long.");
	        return;
	    }

	    // Validate password and confirm password
	    if (!passwordText.equals(conPasswordText)) {
	        showAlert(AlertType.ERROR, "Password Error", "Passwords do not match", "Please re-enter matching passwords.");
	        return;
	    }

	    // Validate email format
	    if (!isValidEmail(emailText)) {
	        showAlert(AlertType.ERROR, "Email Format Error", "Invalid email format", "Please enter a valid email address.");
	        return;
	    }

	    // Check if email already exists in either company or postulator table
	    Connection connection = Connexion.connect(); // Assuming Connexion is a class for establishing DB connection
	    try {
	        String query = "SELECT email FROM company WHERE email = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, emailText);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            showAlert(AlertType.ERROR, "Email Already Exists", "Email already registered", "Please use a different email address.");
	            return;
	        }

	        query = "SELECT email FROM postulator WHERE email = ?";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, emailText);
	        resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            showAlert(AlertType.ERROR, "Email Already Exists", "Email already registered", "Please use a different email address.");
	            return;
	        }
	        
	        // If email is unique, insert new user data into the database
	        String hashedPassword = PasswordHasher.hashPassword(passwordText);
	        query = "INSERT INTO postulator (fullName, email, phone, password) VALUES (?, ?, ?, ?)";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, nameText);
	        preparedStatement.setString(2, emailText);
	        preparedStatement.setString(3, phoneText);
	        preparedStatement.setString(4, hashedPassword);

	        int rowsInserted = preparedStatement.executeUpdate();
	        if (rowsInserted > 0) {
	            showAlert(AlertType.INFORMATION, "Sign Up Success", "Account created", "You have successfully signed up.");
	            // Redirect to login page
	            Main.setRoot("/ressources/auth/Login");
	        } else {
	            showAlert(AlertType.ERROR, "Sign Up Error", "Failed to create account", "Something went wrong. Please try again later.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert(AlertType.ERROR, "Database Error", "Error accessing database", "Please try again later.");
	    }
	}


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
	
	private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
	
}
