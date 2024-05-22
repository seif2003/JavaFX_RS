package controllers.postulator;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import application.Main;
import controllers.auth.PasswordHasher;
import database.Connexion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Pair;


public class MyProfileController implements Initializable {

	@FXML
	private Label myOffers;
	@FXML
	private Label myProfile;
	@FXML
	private Label listOfOffers;
	@FXML
	private Pane logoutPane;
	@FXML
	private Button resumeButton;
	@FXML
	private TextField fullName;
	@FXML
	private TextField phone;
    @FXML
    private CheckBox Communication;
    @FXML
    private CheckBox ComputerSkills;
    @FXML
    private CheckBox ProblemSolving;
    @FXML
    private CheckBox Leadership;
    @FXML
    private CheckBox TimeManagement;
    @FXML
    private CheckBox Teamwork;
    @FXML
    private CheckBox ProjectManagement;
    @FXML
    private CheckBox DataAnalysis;
    @FXML
    private CheckBox Adaptability;
    @FXML
    private CheckBox CreativityAndInnovation;
    @FXML
    private CheckBox CustomerService;
    @FXML
    private CheckBox Negotiation;
    @FXML
    private CheckBox Autonomy;
    @FXML
    private CheckBox ChangeManagement;
    @FXML
    private CheckBox InterpersonalSkills;
    @FXML
    private CheckBox CriticalThinking;
    @FXML
    private CheckBox UIUXDesign;
    @FXML
    private CheckBox TechnicalWriting;
    @FXML
    private CheckBox AgileProjectManagement;
    @FXML
    private CheckBox SEOSEMSkills;
    @FXML
    private CheckBox GraphicDesign;
    @FXML
    private CheckBox ProgrammingSkills;
    @FXML
    private CheckBox SocialMediaManagement;
    @FXML
    private CheckBox ForeignLanguageProficiency;
    @FXML
    private CheckBox DatabaseDesign;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    	logoutPane.setOnMouseClicked(event -> {
            Main.logout();
    	});
    	
    	myOffers.setOnMouseClicked(event -> {
    		Main.setRoot("/ressources/postulator/MyOffers");
    	});
    	
    	myProfile.setOnMouseClicked(event -> {
    		Main.setRoot("/ressources/postulator/MyProfile");
    	});
    	
    	listOfOffers.setOnMouseClicked(event -> {
    		Main.setRoot("/ressources/postulator/ListOfferPostulate");
    	});
    	
    	initializeSkills();
    	initializeUserInfo();
    	
    }
    
    void initializeSkills() {
        String query = "SELECT skills FROM postulator WHERE email = ?";
        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, Main.getCurrentUser().getEmail());
            ResultSet rs = pstmt.executeQuery();
            
            // Assuming there's only one row returned in the result set
            if (rs.next()) {
                // Extract skills string from the result set
                String skillsString = rs.getString("skills");
                
                String[] skillsArray = skillsString.split(",");
                
                // Iterate through each skill and check if it exists
                for (String skill : skillsArray) {

                    String trimmedSkill = skill.trim(); // Trim to remove leading/trailing spaces
                    switch (trimmedSkill) {
                    case "Communication":
                        Communication.setSelected(true);
                        break;
                    case "Computer Skills":
                        ComputerSkills.setSelected(true);
                        break;
                    case "Problem Solving":
                        ProblemSolving.setSelected(true);
                        break;
                    case "Leadership":
                        Leadership.setSelected(true);
                        break;
                    case "Time Management":
                        TimeManagement.setSelected(true);
                        break;
                    case "Teamwork":
                        Teamwork.setSelected(true);
                        break;
                    case "Project Management":
                        ProjectManagement.setSelected(true);
                        break;
                    case "Data Analysis":
                        DataAnalysis.setSelected(true);
                        break;
                    case "Adaptability":
                        Adaptability.setSelected(true);
                        break;
                    case "Creativity and Innovation":
                        CreativityAndInnovation.setSelected(true);
                        break;
                    case "Customer Service":
                        CustomerService.setSelected(true);
                        break;
                    case "Negotiation":
                        Negotiation.setSelected(true);
                        break;
                    case "Autonomy":
                        Autonomy.setSelected(true);
                        break;
                    case "Change Management":
                        ChangeManagement.setSelected(true);
                        break;
                    case "Interpersonal Skills":
                        InterpersonalSkills.setSelected(true);
                        break;
                    case "Critical Thinking":
                        CriticalThinking.setSelected(true);
                        break;
                    case "UI/UX Design":
                        UIUXDesign.setSelected(true);
                        break;
                    case "Technical Writing":
                        TechnicalWriting.setSelected(true);
                        break;
                    case "Agile Project Management":
                        AgileProjectManagement.setSelected(true);
                        break;
                    case "SEO/SEM Skills":
                        SEOSEMSkills.setSelected(true);
                        break;
                    case "Graphic Design":
                        GraphicDesign.setSelected(true);
                        break;
                    case "Programming Skills":
                        ProgrammingSkills.setSelected(true);
                        break;
                    case "Social Media Management":
                        SocialMediaManagement.setSelected(true);
                        break;
                    case "Foreign Language Proficiency":
                        ForeignLanguageProficiency.setSelected(true);
                        break;
                    case "Database Design":
                        DatabaseDesign.setSelected(true);
                        break;
                    // Add more cases for other skills as needed
                    default:
                        // Do nothing or handle unrecognized skills
                        break;
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    void initializeUserInfo() {
        String query = "SELECT fullName, phone FROM postulator WHERE email = ?";
        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, Main.getCurrentUser().getEmail());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                fullName.setText(rs.getString("fullName"));
                phone.setText(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

            String query = "SELECT password FROM postulator WHERE email = ?";
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
        String updateQuery = "UPDATE postulator SET password = ? WHERE email = ?";
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
    void save() {

        // Concatenate all selected skills into a comma-separated string
        StringBuilder selectedSkillsBuilder = new StringBuilder();
        
        if (Communication.isSelected()) {
            selectedSkillsBuilder.append("Communication, ");
        }
        if (ComputerSkills.isSelected()) {
            selectedSkillsBuilder.append("Computer Skills, ");
        }
        if (ProblemSolving.isSelected()) {
            selectedSkillsBuilder.append("Problem Solving, ");
        }
        if (Leadership.isSelected()) {
            selectedSkillsBuilder.append("Leadership, ");
        }
        if (TimeManagement.isSelected()) {
            selectedSkillsBuilder.append("Time Management, ");
        }
        if (Teamwork.isSelected()) {
            selectedSkillsBuilder.append("Teamwork, ");
        }
        if (ProjectManagement.isSelected()) {
            selectedSkillsBuilder.append("Project Management, ");
        }
        if (DataAnalysis.isSelected()) {
            selectedSkillsBuilder.append("Data Analysis, ");
        }
        if (Adaptability.isSelected()) {
            selectedSkillsBuilder.append("Adaptability, ");
        }
        if (CreativityAndInnovation.isSelected()) {
            selectedSkillsBuilder.append("Creativity and Innovation, ");
        }
        if (CustomerService.isSelected()) {
            selectedSkillsBuilder.append("Customer Service, ");
        }
        if (Negotiation.isSelected()) {
            selectedSkillsBuilder.append("Negotiation, ");
        }
        if (Autonomy.isSelected()) {
            selectedSkillsBuilder.append("Autonomy, ");
        }
        if (ChangeManagement.isSelected()) {
            selectedSkillsBuilder.append("Change Management, ");
        }
        if (InterpersonalSkills.isSelected()) {
            selectedSkillsBuilder.append("Interpersonal Skills, ");
        }
        if (CriticalThinking.isSelected()) {
            selectedSkillsBuilder.append("Critical Thinking, ");
        }
        if (UIUXDesign.isSelected()) {
            selectedSkillsBuilder.append("UI/UX Design, ");
        }
        if (TechnicalWriting.isSelected()) {
            selectedSkillsBuilder.append("Technical Writing, ");
        }
        if (AgileProjectManagement.isSelected()) {
            selectedSkillsBuilder.append("Agile Project Management, ");
        }
        if (SEOSEMSkills.isSelected()) {
            selectedSkillsBuilder.append("SEO/SEM Skills, ");
        }
        if (GraphicDesign.isSelected()) {
            selectedSkillsBuilder.append("Graphic Design, ");
        }
        if (ProgrammingSkills.isSelected()) {
            selectedSkillsBuilder.append("Programming Skills, ");
        }
        if (SocialMediaManagement.isSelected()) {
            selectedSkillsBuilder.append("Social Media Management, ");
        }
        if (ForeignLanguageProficiency.isSelected()) {
            selectedSkillsBuilder.append("Foreign Language Proficiency, ");
        }
        if (DatabaseDesign.isSelected()) {
            selectedSkillsBuilder.append("Database Design, ");
        }
        
        // Remove trailing comma and space
        String selectedSkills = selectedSkillsBuilder.toString();
        if (!selectedSkills.isEmpty()) {
            selectedSkills = selectedSkills.substring(0, selectedSkills.length() - 2);
        }

        // Prepare update query
        String updateUserInfoQuery = "UPDATE postulator SET fullName = ?, phone = ?, skills = ? WHERE email = ?";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmtUserInfo = conn.prepareStatement(updateUserInfoQuery);) {

            // Set parameters for updating user info
            pstmtUserInfo.setString(1, fullName.getText());
            pstmtUserInfo.setString(2, phone.getText());
            pstmtUserInfo.setString(3, selectedSkills); // Set concatenated skills string
            pstmtUserInfo.setString(4, Main.getCurrentUser().getEmail());

            // Execute update for user info
            int userInfoUpdated = pstmtUserInfo.executeUpdate();
            
            if (userInfoUpdated > 0) {
                showAlert(AlertType.INFORMATION, "Update Successful", null, "Profile updated successfully");
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
	
	@FXML
	void uploadResume() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Select Resume PDF");
	    fileChooser.getExtensionFilters().addAll(
	        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
	    );

	    File selectedFile = fileChooser.showOpenDialog(resumeButton.getScene().getWindow());
	    if (selectedFile != null) {
	        try {
	            String resumeURL = selectedFile.toURI().toString(); // Get the file path
	            String query = "UPDATE postulator SET resume = ? WHERE email = ?";
	            try (Connection conn = Connexion.connect();
	                 PreparedStatement pstmt = conn.prepareStatement(query)) {
	                pstmt.setString(1, resumeURL); // Set the resume URL as a parameter
	                pstmt.setString(2, Main.getCurrentUser().getEmail());
	                
	                int rowsAffected = pstmt.executeUpdate();
	                if (rowsAffected > 0) {
	                    showAlert(AlertType.INFORMATION, "Resume Upload", null, "Resume uploaded successfully.");
	                } else {
	                    showAlert(AlertType.ERROR, "Resume Upload", null, "Failed to upload resume.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert(AlertType.ERROR, "Database Error", null, "Error uploading resume to the database.");
	        }
	    }
	}

}