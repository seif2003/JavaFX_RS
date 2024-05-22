package controllers.entreprise;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import application.Main;
import database.Connexion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Company;

public class CreateOfferContoller {

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

    @FXML
    private TextField category;

    @FXML
    private TextArea description;

    @FXML
    private TextField job_title;

    @FXML
    private TextField max_sal;

    @FXML
    private TextField min_sal;

    // Expression régulière pour valider un montant (max 2 décimales, au moins un chiffre)
    private static final String AMOUNT_PATTERN = "\\d+(\\.\\d{1,2})?";
    
    @FXML
    private Label list_of_offers;
    
    @FXML
    private Label create_offer;
    
	@FXML
	private Pane logoutPane;
    @FXML
    private Label CandidatsAccepted;
    @FXML
    private Label Myprofile;
    
    @FXML
    private void initialize() {
    	Myprofile.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/MyProfileEntreprise");
        });
    	CandidatsAccepted.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/candidatAccepted");
        });
    	logoutPane.setOnMouseClicked(event -> {
            Main.logout();
    	});
    	
        list_of_offers.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/entreprise");
        });
        
        create_offer.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/create_offer");
        });
    }

    @FXML
    void createOffer() {
        String categoryText = category.getText();
        String descriptionText = description.getText();
        String jobTitleText = job_title.getText();
        String maxSalaryText = max_sal.getText();
        String minSalaryText = min_sal.getText();
        
        List<String> skillsList = new ArrayList<>();
        if (Communication.isSelected()) {
            skillsList.add("Communication");
        }
        if (ComputerSkills.isSelected()) {
            skillsList.add("Computer Skills");
        }
        if (ProblemSolving.isSelected()) {
            skillsList.add("Problem Solving");
        }
        if (Leadership.isSelected()) {
            skillsList.add("Leadership");
        }
        if (TimeManagement.isSelected()) {
            skillsList.add("Time Management");
        }
        if (Teamwork.isSelected()) {
            skillsList.add("Teamwork");
        }
        if (ProjectManagement.isSelected()) {
            skillsList.add("Project Management");
        }
        if (DataAnalysis.isSelected()) {
            skillsList.add("Data Analysis");
        }
        if (Adaptability.isSelected()) {
            skillsList.add("Adaptability");
        }
        if (CreativityAndInnovation.isSelected()) {
            skillsList.add("Creativity and Innovation");
        }
        if (CustomerService.isSelected()) {
            skillsList.add("Customer Service");
        }
        if (Negotiation.isSelected()) {
            skillsList.add("Negotiation");
        }
        if (Autonomy.isSelected()) {
            skillsList.add("Autonomy");
        }
        if (ChangeManagement.isSelected()) {
            skillsList.add("Change Management");
        }
        if (InterpersonalSkills.isSelected()) {
            skillsList.add("Interpersonal Skills");
        }
        if (CriticalThinking.isSelected()) {
            skillsList.add("Critical Thinking");
        }
        if (UIUXDesign.isSelected()) {
            skillsList.add("UI/UX Design");
        }
        if (TechnicalWriting.isSelected()) {
            skillsList.add("Technical Writing");
        }
        if (AgileProjectManagement.isSelected()) {
            skillsList.add("Agile Project Management");
        }
        if (SEOSEMSkills.isSelected()) {
            skillsList.add("SEO/SEM Skills");
        }
        if (GraphicDesign.isSelected()) {
            skillsList.add("Graphic Design");
        }
        if (ProgrammingSkills.isSelected()) {
            skillsList.add("Programming Skills");
        }
        if (SocialMediaManagement.isSelected()) {
            skillsList.add("Social Media Management");
        }
        if (ForeignLanguageProficiency.isSelected()) {
            skillsList.add("Foreign Language Proficiency");
        }
        if (DatabaseDesign.isSelected()) {
            skillsList.add("Database Design");
        }


        // Concaténation des compétences en une chaîne de caractères séparée par des virgules
        String skills = String.join(",", skillsList);
        
        // Vérification de la validité des montants saisis
        if (!isValidAmount(maxSalaryText) || !isValidAmount(minSalaryText)) {
            showAlert(AlertType.ERROR, "Invalid Input", "Invalid salary amount", "Please enter a valid salary amount.");
            return;
        }

        BigDecimal maxSalary = new BigDecimal(maxSalaryText);
        BigDecimal minSalary = new BigDecimal(minSalaryText);

        // Vérification de l'utilisateur actuel
        Object currentUser = Main.getCurrentUser();
        if (currentUser instanceof Company) {
            String companyEmail = ((Company) currentUser).getEmail();

            Connection connection = Connexion.connect();
            try {
                String query = "INSERT INTO offer (job_title, maxSalary, minSalary, description, company, category, skills) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, jobTitleText);
                preparedStatement.setBigDecimal(2, maxSalary);
                preparedStatement.setBigDecimal(3, minSalary);
                preparedStatement.setString(4, descriptionText);
                preparedStatement.setString(5, companyEmail);
                preparedStatement.setString(6, categoryText);
                preparedStatement.setString(7, skills);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Offer created", "The offer was successfully created.");
                    clearForm();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to create offer", "An error occurred while creating the offer.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Database Error", "Error accessing database", "Please try again later.");
            }
        } else {
            // Gérer le cas où l'utilisateur actuel n'est pas une entreprise
            showAlert(AlertType.ERROR, "Access Denied", "Only companies can create offers", "Please log in as a company to create an offer.");
        }
    }

    private void clearForm() {
        job_title.clear();
        max_sal.clear();
        min_sal.clear();
        description.clear();
        category.clear();
        Communication.setSelected(false);
        ComputerSkills.setSelected(false);
        ProblemSolving.setSelected(false);
        Leadership.setSelected(false);
        TimeManagement.setSelected(false);
        Teamwork.setSelected(false);
        ProjectManagement.setSelected(false);
        DataAnalysis.setSelected(false);
        Adaptability.setSelected(false);
        CreativityAndInnovation.setSelected(false);
        CustomerService.setSelected(false);
        Negotiation.setSelected(false);
        Autonomy.setSelected(false);
        ChangeManagement.setSelected(false);
        InterpersonalSkills.setSelected(false);
        CriticalThinking.setSelected(false);
        UIUXDesign.setSelected(false);
        TechnicalWriting.setSelected(false);
        AgileProjectManagement.setSelected(false);
        SEOSEMSkills.setSelected(false);
        GraphicDesign.setSelected(false);
        ProgrammingSkills.setSelected(false);
        SocialMediaManagement.setSelected(false);
        ForeignLanguageProficiency.setSelected(false);
        DatabaseDesign.setSelected(false);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Vérifie si une chaîne correspond au motif de montant
    private boolean isValidAmount(String amount) {
        return Pattern.matches(AMOUNT_PATTERN, amount);
    }
}
