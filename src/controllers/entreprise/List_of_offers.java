package controllers.entreprise;

import application.Main;
import database.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Company;
import models.Offer;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class List_of_offers implements Initializable {

    @FXML
    private TableView<Offer> offerTable;

    @FXML
    private TableColumn<Offer, String> offerNameColumn;

    @FXML
    private TableColumn<Offer, LocalDateTime> creationDateColumn;

    @FXML
    private TableColumn<Offer, Void> updateColumn;

    @FXML
    private TableColumn<Offer, Void> deleteColumn;

    private TableColumn<Offer, Void> detailsColumn; // Retirer @FXML

    @FXML
    private Label list_of_offers;

    @FXML
    private Label create_offer;
    @FXML
	private Pane logoutPane;
    @FXML
    private Label Myprofile;

    public static int selectedOfferId;
    @FXML
    private Label CandidatsAccepted;

    private ObservableList<Offer> offerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	Myprofile.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/MyProfileEntreprise");
        });
    	CandidatsAccepted.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/candidatAccepted");
        });
    	
    	
    	logoutPane.setOnMouseClicked(event -> {
            Main.logout();
    	});
    	if (list_of_offers != null) {
            // Ajoutez l'événement de clic à list_of_offers
            list_of_offers.setOnMouseClicked(event -> {
                // Redirigez vers la page list_of_offers lorsque list_of_offers est cliqué
				Main.setRoot("/ressources/entreprise");
            });}

        create_offer.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/create_offer");
        });

        Object currentUser = Main.getCurrentUser();

        if (currentUser instanceof Company) {
            String companyEmail = ((Company) currentUser).getEmail();
            loadOffersByCompany(companyEmail);
            setupTable();
        } else {
            System.out.println("Company email not found. Exiting initialize method.");
        }
    }

    private void loadOffersByCompany(String companyEmail) {
        String sql = "SELECT * FROM offer WHERE company = ?";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, companyEmail);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String jobTitle = rs.getString("job_title");
                double maxSalary = rs.getDouble("maxSalary");
                double minSalary = rs.getDouble("minSalary");
                String description = rs.getString("description");
                String company = rs.getString("company");
                String category = rs.getString("category");
                LocalDateTime dateOfCreation = rs.getTimestamp("date_of_creation").toLocalDateTime();
                String skills = rs.getString("skills");

                Offer offer = new Offer(id, jobTitle, maxSalary, minSalary, description, company, category, dateOfCreation,skills);
                offerList.add(offer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        offerNameColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));

        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
           

            {
            	updateButton.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white;");
                updateButton.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    
                    editOffer(offer);
                });
               
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    deleteOffer(offer);
                });
            
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        detailsColumn = new TableColumn<>("Details");
        detailsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button detailsButton = new Button("Details");

            {
            	detailsButton.setStyle("-fx-text-fill: black;");
            	detailsButton.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    showOfferDetails(offer);
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
        });

        // Ajouter la colonne detailsColumn à la table
        offerTable.getColumns().add(detailsColumn);

        offerTable.setItems(offerList);
    }

    private void editOffer(Offer offer) {
        Dialog<Offer> dialog = new Dialog<>();
        dialog.setTitle("Edit Offer");
        dialog.setHeaderText("Edit Offer: " + offer.getJobTitle());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField jobTitleField = new TextField(offer.getJobTitle());
        TextField maxSalaryField = new TextField(String.valueOf(offer.getMaxSalary()));
        TextField minSalaryField = new TextField(String.valueOf(offer.getMinSalary()));
        TextArea descriptionArea = new TextArea(offer.getDescription());

        dialog.getDialogPane().setContent(new VBox(10, jobTitleField, maxSalaryField, minSalaryField, descriptionArea));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                offer.setJobTitle(jobTitleField.getText());
                offer.setMaxSalary(Double.parseDouble(maxSalaryField.getText()));
                offer.setMinSalary(Double.parseDouble(minSalaryField.getText()));
                offer.setDescription(descriptionArea.getText());
                saveChanges(offer);
                return offer;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteOffer(Offer offer) {
        offerList.remove(offer);
        try (Connection connection = Connexion.connect()) {
            String query = "DELETE FROM offer WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, offer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveChanges(Offer offer) {
        try (Connection connection = Connexion.connect()) {
            String query = "UPDATE offer SET job_title=?, maxSalary=?, minSalary=?, description=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, offer.getJobTitle());
            statement.setDouble(2, offer.getMaxSalary());
            statement.setDouble(3, offer.getMinSalary());
            statement.setString(4, offer.getDescription());
            statement.setInt(5, offer.getId());
            statement.executeUpdate();
            offerTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showOfferDetails(Offer offer) {
        selectedOfferId = offer.getId();
        Main.setRoot("/ressources/entreprise/Offre_name");
    }
}
