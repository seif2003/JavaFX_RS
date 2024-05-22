package controllers.postulator;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import application.Main;
import database.Connexion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Offer;

public class OfferDetailsController implements Initializable {
	
	static int OFFER_ID;

    @FXML
    private Label jobTitleLabel;
    @FXML
    private TextFlow descriptionTextFlow;
    @FXML
    private Label dateLabel;
    @FXML
    private Label list_of_offers;
    @FXML
    private Label create_offer;
    @FXML
    private ImageView back;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label locationLabel;
	@FXML
	private Label myOffers;
	@FXML
	private Label myProfile;
	@FXML
	private Label listOfOffers;
	@FXML
	private Pane logoutPane;
	@FXML
	private Label skillsLabel;
	@FXML
	private Label categoryLabel;
	@FXML
	private ImageView image;
	@FXML
	private Label maxSalaryLabel;
	@FXML
	private Label minSalaryLabel;
	
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        if (back != null) {
        	back.setOnMouseClicked(event -> {
                Main.setRoot("/ressources/postulator/ListOfferPostulate");
        	});
        }
        
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
    	
    	
        loadOfferDetails(OFFER_ID);
    }

    private void loadOfferDetails(int offerId) {
        String sqlOffer = "SELECT * FROM offer WHERE id = ?";
        String sqlCompany = "SELECT * FROM company WHERE email = ?";

        try (Connection conn = Connexion.connect();
        	PreparedStatement pstmtOffer = conn.prepareStatement(sqlOffer);
        	PreparedStatement pstmtCompany = conn.prepareStatement(sqlCompany)) {

            pstmtOffer.setInt(1, offerId);
            ResultSet rsOffer = pstmtOffer.executeQuery();
            String email = "";
            if (rsOffer.next()) {
                String jobTitle = rsOffer.getString("job_title");
                String description = rsOffer.getString("description");
                LocalDateTime dateOfCreation = rsOffer.getTimestamp("date_of_creation").toLocalDateTime();
                String skills = rsOffer.getString("skills");
                String category = rsOffer.getString("category");
                String minS = rsOffer.getString("minSalary");
                String maxS = rsOffer.getString("maxSalary");
                
                email = rsOffer.getString("company");

                jobTitleLabel.setText(jobTitle);

                descriptionTextFlow.getChildren().clear();
                Text descriptionText = new Text(description);
                descriptionTextFlow.getChildren().add(descriptionText);

                dateLabel.setText(dateOfCreation.toString());
                skillsLabel.setText(skills);
                categoryLabel.setText(category);
                maxSalaryLabel.setText(maxS);
                minSalaryLabel.setText(minS);
            }

            pstmtCompany.setString(1, email);
            ResultSet rsRequest = pstmtCompany.executeQuery();

            while (rsRequest.next()) {
                String name = rsRequest.getString("name");
                String phone = rsRequest.getString("phone");
                String location = rsRequest.getString("location");
                String logoURL = rsRequest.getString("logo");
                
                nameLabel.setText(name);
                emailLabel.setText(email);
                phoneLabel.setText(phone);
                locationLabel.setText(location);
            	try {
                    URL url = new URL(logoURL);
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

	public static int getOfferId() {
		return OFFER_ID;
	}

	public static void setOfferId(int offerId) {
		OfferDetailsController.OFFER_ID = offerId;
	}
    
	@FXML
	private void postulate() {
	    Dialog<Offer> dialog = new Dialog<>();
	    dialog.setTitle("Postulate");
	    dialog.setHeaderText("Demande");

	    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

	    TextArea descriptionArea = new TextArea();
	    dialog.getDialogPane().setContent(new VBox(10, descriptionArea));

	    dialog.setResultConverter(dialogButton -> {
	        if (dialogButton == saveButtonType) {
	            boolean requestExists = false;
	            String checkQuery = "SELECT * FROM request WHERE postulator = ? AND offer = ?";

	            try (Connection conn = Connexion.connect();
	            	PreparedStatement checkPs = conn.prepareStatement(checkQuery)) {
	                checkPs.setString(1, Main.getCurrentUser().getEmail());
	                checkPs.setInt(2, OFFER_ID);

	                ResultSet rs = checkPs.executeQuery();
	                if (rs.next()) {
	                    requestExists = true;
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }

	            if (requestExists) {
	                Alert alert = new Alert(Alert.AlertType.WARNING);
	                alert.setTitle("Request Already Exists");
	                alert.setHeaderText("You have already requested this offer.");
	                alert.showAndWait();
	            } else {
	                String insertQuery = "INSERT INTO request (details, postulator, offer, date_of_creation, etat) VALUES (?, ?, ?, CURRENT_TIMESTAMP, 'not processed')";

	                try (Connection conn = Connexion.connect();
	                     PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
	                    insertPs.setString(1, descriptionArea.getText());
	                    insertPs.setString(2, Main.getCurrentUser().getEmail());
	                    insertPs.setInt(3, OFFER_ID);

	                    insertPs.executeUpdate();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return null;
	    });

	    dialog.showAndWait();
	}
}
