package controllers.entreprise;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import controllers.entreprise.AcceeptedCandidat.AcceptedCandidateData;
import database.Connexion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Postulator;
import models.Request;

public class CandidatDetailsController {

    @FXML
    private Label fullNameLabel;
    @FXML
    private Label emailText;
    @FXML
    private Label phoneText;
    @FXML
    private TextFlow detailsText; // Champ pour les détails de la demande
    @FXML
    private Label competenceList; // Champ pour les compétences
    @FXML
    private Label list_of_offers;
    @FXML
    private Label create_offer;
    @FXML
    private Pane logoutPane;
    @FXML
    private ImageView back;
    @FXML
    private Label CandidatsAccepted;
    @FXML
    private Label Myprofile;
    
    private String resume;

    public void initialize() {
    	Myprofile.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/MyProfileEntreprise");
        });
    	CandidatsAccepted.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/candidatAccepted");
        });
        if (back != null) {
            back.setOnMouseClicked(event -> {
                Main.setRoot("/ressources/entreprise/Offre_name");
            });
        }
        // Initialisation des données du candidat
        logoutPane.setOnMouseClicked(event -> {
            Main.logout();
        });
        if (list_of_offers != null) {
            list_of_offers.setOnMouseClicked(event -> {
                Main.setRoot("/ressources/entreprise");
            });
        }

        create_offer.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/create_offer");
        });

        String selectedEmail = DetailsController.selectedRequestEmail;
        Request selectedRequest = DetailsController.getRequestSelected();
        Postulator candidate = getSelectedCandidate(selectedEmail);

        if (candidate != null) {
            fullNameLabel.setText(candidate.getFullName());
            emailText.setText(candidate.getEmail());
            phoneText.setText(candidate.getPhone());
            competenceList.setText(candidate.getSkills()); // Afficher les compétences du candidat
        }

        if (selectedRequest != null) {
            Text requestDetails = new Text(selectedRequest.getDetails()); // Créer un Text à partir des détails de la demande
            detailsText.getChildren().add(requestDetails); // Ajouter les détails au TextFlow
        }
    }

    public Postulator getSelectedCandidate(String email) {
        String sql = "SELECT * FROM postulator WHERE email = ?";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("fullName");
                String phone = rs.getString("phone");
                String skills = rs.getString("skills"); 
                String resume = rs.getString("resume");
                this.resume = resume; 
                
                return new Postulator(email, null, phone, fullName,skills,resume);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching candidate details: " + e.getMessage());
        }
        return null;
    }
    
    @FXML
    public void resume() {
    	if (resume == null) {
    		showAlert(Alert.AlertType.ERROR, "Resume Error", "Cannot open resume", "This user doesn't have a resume yet or the resume cannot be found.");
    		return;
    	}
    	try {
    		Desktop.getDesktop().browse(new URI(resume));
    	} catch (IOException | URISyntaxException e) {
    		showAlert(Alert.AlertType.ERROR, "Resume Error", "Cannot open resume", "This user doesn't have a resume yet or the resume cannot be found.");
    	}
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
