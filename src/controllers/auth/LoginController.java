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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Company;
import models.Postulator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    void login() throws IOException {
        String emailText = email.getText();
        String passwordText = password.getText();
        String hashedPassword = PasswordHasher.hashPassword(passwordText);

        // Check if any field is empty
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            showAlert(AlertType.ERROR, "Field Empty", "Email and password are required", "Please fill in both email and password fields.");
            return;
        }

        // Validate email format
        if (!isValidEmail(emailText)) {
            showAlert(AlertType.ERROR, "Email Format Error", "Invalid email format", "Please enter a valid email address.");
            return;
        }

        // Validate password length
        if (passwordText.length() < 6) {
            showAlert(AlertType.ERROR, "Password Error", "Password too short", "Password must be at least 6 characters long.");
            return;
        }

        Connection connection = Connexion.connect();
        try {
            String query = "SELECT * FROM postulator WHERE email=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, emailText);
            preparedStatement.setString(2, hashedPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Postulator postulator = new Postulator(resultSet.getString("fullName"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("phone"));
				redirectToPostulatorPage();

                Main.setCurrentUser(postulator);
            } else {
                query = "SELECT * FROM company WHERE email=? AND password=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, emailText);
                preparedStatement.setString(2, hashedPassword);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Company comp = new Company(resultSet.getString("name"), resultSet.getString("location"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("phone"));
                   	Main.setCurrentUser(comp);
                    redirectToCompanyPage();
                } else {
                    showAlert(AlertType.ERROR, "Login Error", "Invalid credentials", "Please check your email and password.");
                }
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

    @FXML
    void redirect() {
        Main.setRoot("/ressources/auth/SignUp");
    }

    private void redirectToPostulatorPage() {
        Main.setRoot("/ressources/postulator/ListOfferPostulate");
    }

    private void redirectToCompanyPage() {
        Main.setRoot("/ressources/entreprise/entreprise");
    }
    
}


