package controllers.entreprise;

import database.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Request;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import application.Main;

public class DetailsController implements Initializable {
	public static String selectedRequestEmail;
    @FXML
    private TableView<Request> requestTable;
    @FXML
    private TableColumn<Request, String> fullNameColumn;
    @FXML
    private TableColumn<Request, LocalDateTime> creationDateColumn;
    @FXML
    private TableColumn<Request, Void> acceptColumn;  // Ensure this matches the ID in the FXML file
    @FXML
    private TableColumn<Request, Void> rejectColumn;  // Ensure this matches the ID in the FXML file
    @FXML
    private TableColumn<Request, Void> detailsColumn;  // New column for details

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
    private Label CandidatsAccepted;
    @FXML
    private Label Myprofile;

    
    public static Request getRequestSelected() {
		return requestSelected;
	}


	@FXML
	private Pane logoutPane;
    @FXML
    private ImageView back;
    public static Request requestSelected;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	Myprofile.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/MyProfileEntreprise");
        });
    	CandidatsAccepted.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/candidatAccepted");
        });
    	
    	if (back != null) {
        	back.setOnMouseClicked(event -> {
                Main.setRoot("/ressources/entreprise/entreprise");
        	});
        }
    	logoutPane.setOnMouseClicked(event -> {
            Main.logout();
    	});
        if (list_of_offers != null) {
            list_of_offers.setOnMouseClicked(event -> {
                Main.setRoot("/ressources/entreprise/entreprise");
            });
        }

        create_offer.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/create_offer");
        });

        // Initialize table columns
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("postulator"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));

        // Configure accept and reject buttons
        addButtonToTable();

        // Ajout de la colonne "Details"
        detailsColumn.setCellFactory(param -> new TableCell<Request, Void>() {
            private final Button detailsButton = new Button("Details");

            {
                detailsButton.setOnAction(event -> {
                    Request request = getTableView().getItems().get(getIndex());
                    showRequestDetails(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }

            private void showRequestDetails(Request request) {
            	
            	selectedRequestEmail = request.getPostulator();
            	requestSelected = request;
                
                Main.setRoot("/ressources/entreprise/details_candidat");
            }
        });

        // Load offer details
        loadOfferDetails(List_of_offers.selectedOfferId);
    }

    private void loadOfferDetails(int offerId) {
        String sqlOffer = "SELECT * FROM offer WHERE id = ?";
        String sqlRequest = "SELECT * FROM request WHERE offer = ? AND etat = 'not processed'";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmtOffer = conn.prepareStatement(sqlOffer);
             PreparedStatement pstmtRequest = conn.prepareStatement(sqlRequest)) {

            pstmtOffer.setInt(1, offerId);
            ResultSet rsOffer = pstmtOffer.executeQuery();

            if (rsOffer.next()) {
                String jobTitle = rsOffer.getString("job_title");
                String description = rsOffer.getString("description");
                LocalDateTime dateOfCreation = rsOffer.getTimestamp("date_of_creation").toLocalDateTime();

                jobTitleLabel.setText(jobTitle);

                descriptionTextFlow.getChildren().clear();
                Text descriptionText = new Text(description);
                descriptionTextFlow.getChildren().add(descriptionText);

                dateLabel.setText(dateOfCreation.toString());
            }

            // Create an ObservableList to hold the requests
            ObservableList<Request> requestList = FXCollections.observableArrayList();

            pstmtRequest.setInt(1, offerId);
            ResultSet rsRequest = pstmtRequest.executeQuery();

            while (rsRequest.next()) {
                String details = rsRequest.getString("details");
                String postulator = rsRequest.getString("postulator");
                LocalDateTime dateOfCreation = rsRequest.getTimestamp("date_of_creation").toLocalDateTime();

                Request request = new Request(details, postulator, offerId, dateOfCreation);
                requestList.add(request);  // Add the request to the ObservableList
            }

            // Set the items of the TableView to the request list
            requestTable.setItems(requestList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        // Cell factory for the accept column
        acceptColumn.setCellFactory(column -> new TableCell<Request, Void>() {
            private final Button btnAccept = new Button("Accept");

            {
            	btnAccept.setStyle("-fx-background-color:#32CD32; -fx-text-fill: white;");
                btnAccept.setOnAction(event -> {
                    Request request = getTableView().getItems().get(getIndex());
                    changeRequestStatus(request, "accepted");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAccept);
                }
            }
        });

        // Cell factory for the reject column
        rejectColumn.setCellFactory(column -> new TableCell<Request, Void>() {
            private final Button btnReject = new Button("Reject");

            {
            	btnReject.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
            	btnReject.setOnAction(event -> {
                    Request request = getTableView().getItems().get(getIndex());
                    changeRequestStatus(request, "rejected");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnReject);
                }
            }
        });
    }

    private void changeRequestStatus(Request request, String status) {
        String sqlUpdate = "UPDATE request SET etat = ? WHERE details = ? AND postulator = ? AND date_of_creation = ?";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            pstmt.setString(1, status);
            pstmt.setString(2, request.getDetails());
            pstmt.setString(3, request.getPostulator());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(request.getDateOfCreation()));
            pstmt.executeUpdate();

            // Supprimer l'élément de la liste observable
            requestTable.getItems().remove(request);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
