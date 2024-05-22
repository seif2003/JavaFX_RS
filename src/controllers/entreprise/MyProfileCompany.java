package controllers.entreprise;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import controllers.auth.PasswordHasher;
import database.Connexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class MyProfileCompany implements Initializable {

	@FXML
    private Label list_of_offers;
    @FXML
    private Label create_offer;
    @FXML
    private Label offerAccepted;
    @FXML
    private Pane logoutPane;
    @FXML
    private Label Myprofile;
    @FXML
    private TextField Name;
    @FXML
    private TextField phone;
    @FXML
    private TextField location;
    @FXML
    private Button save;
    @FXML
    private Button logobutton;
    @FXML
    private Button changePasswordButton;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logoutPane.setOnMouseClicked(event -> {
            Main.logout();
        });

        offerAccepted.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/candidatAccepted");
        });
    	
    	
        if (list_of_offers != null) {
            list_of_offers.setOnMouseClicked(event -> {
                Main.setRoot("/ressources/entreprise/entreprise");
            });
        }

        create_offer.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/create_offer");
        });
        Myprofile.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/MyProfile");
        });

        initializeCompanyInfo();
    }

    void initializeCompanyInfo() {
        String query = "SELECT name, phone, location,logo FROM company WHERE email = ?";
        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, Main.getCurrentUser().getEmail());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Name.setText(rs.getString("name"));
                phone.setText(rs.getString("phone"));
                location.setText(rs.getString("location"));
                try {
                    URL url = new URL(rs.getString("logo"));
                    Image img = new Image(url.openStream());
                    image.setImage(img);
                } catch (IOException e) {
                    System.out.println("Error loading image: " + e.getMessage());
                    e.printStackTrace();
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void save(ActionEvent event) {
        String updateCompanyInfoQuery = "UPDATE company SET name = ?, phone = ?, location = ? WHERE email = ?";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(updateCompanyInfoQuery)) {

            pstmt.setString(1, Name.getText());
            pstmt.setString(2, phone.getText());
            pstmt.setString(3, location.getText());
            pstmt.setString(4, Main.getCurrentUser().getEmail());

            int companyInfoUpdated = pstmt.executeUpdate();

            if (companyInfoUpdated > 0) {
                showAlert(AlertType.INFORMATION, "Update Successful", null, "Profile updated successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void uploadLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Logo Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(logobutton.getScene().getWindow());
        if (selectedFile != null) {
            try {
                String logoURL = selectedFile.toURI().toString(); // Get the file path
                String query = "UPDATE company SET logo = ? WHERE email = ?";
                try (Connection conn = Connexion.connect();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, logoURL); // Set the logo URL as a parameter
                    pstmt.setString(2, Main.getCurrentUser().getEmail());

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                    	try {
                            URL url = new URL(logoURL);
                            Image img = new Image(url.openStream());
                            image.setImage(img);
                        } catch (IOException e) {
                            System.out.println("Error loading image: " + e.getMessage());
                            e.printStackTrace();
                        }
                        showAlert(AlertType.INFORMATION, "Logo Upload", null, "Logo uploaded successfully.");
                    } else {
                        showAlert(AlertType.ERROR, "Logo Upload", null, "Failed to upload logo.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", null, "Error uploading logo to the database.");
            }
        }
    }

    @FXML
    void changePassword() {
        Dialog<Pair<String, String[]>> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your old password and new password");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Change", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Old Password");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New Password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm Password");

        grid.add(new Label("Old Password:"), 0, 0);
        grid.add(oldPassword, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(oldPassword.getText(), new String[]{newPassword.getText(), confirmPassword.getText()});
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String oldPass = result.getKey();
            String newPass = result.getValue()[0];
            String confirmPass = result.getValue()[1];

            if (newPass.length() < 6) {
                showAlert(AlertType.ERROR, "Password Error", null, "Password is too short. Please use at least 6 characters.");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                showAlert(AlertType.ERROR, "Password Error", null, "New password and confirm password do not match.");
                return;
            }

            String query = "SELECT password FROM company WHERE email = ?";
            try (Connection conn = Connexion.connect(); 
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, Main.getCurrentUser().getEmail());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getString("password").equals(PasswordHasher.hashPassword(oldPass))) {
                    updatePassword(newPass);
                } else {
                    showAlert(AlertType.ERROR, "Password Error", null, "Old password is incorrect.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updatePassword(String newPassword) {
        String updateQuery = "UPDATE company SET password = ? WHERE email = ?";
        try (Connection conn = Connexion.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
        	String hashedPassword = PasswordHasher.hashPassword(newPassword);
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, Main.getCurrentUser().getEmail());
            if (pstmt.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Password Changed", null, "Password changed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void deleteLogo() {
    	String updateQuery = "update company set logo = null where email = ?";
        try (Connection conn = Connexion.connect(); 
                PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
               pstmt.setString(1, Main.getCurrentUser().getEmail());
               image.setImage(null);
               if (pstmt.executeUpdate() > 0) {
                   showAlert(AlertType.INFORMATION, "Logo deleted", null, "Company Logo has been deleted successfully.");
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
    }

    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
