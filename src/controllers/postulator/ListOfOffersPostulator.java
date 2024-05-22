package controllers.postulator;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import application.Main;
import database.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Offer;


public class ListOfOffersPostulator implements Initializable {
	
	@FXML
	private TableView<Offer> offerTable;

	@FXML
	private TableColumn<Offer, String> offerNameColumn;

	@FXML
	private TableColumn<Offer, LocalDateTime> dateOfCreationColumn;

	@FXML
	private TableColumn<Offer, Void> viewColumn;
		
	@FXML
	private TableColumn<Offer,String> skillsColumn;
	
	@FXML
	private TableColumn<Offer, Void>  logoColumn;
	
	@FXML
	private Label myOffers;
	
	@FXML
	private Label myProfile;
	
	@FXML
	private Label listOfOffers;
	
	@FXML
	private Pane logoutPane;
	
	
	private ObservableList<Offer> offerList = FXCollections.observableArrayList();

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
    	
        loadOffers();
        setupTable();
        
	}

    private void loadOffers() {
        String sql = "SELECT * FROM offer,company where offer.company = company.email;";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String jobTitle = rs.getString("job_title");
                double maxSalary = rs.getDouble("maxSalary");
                double minSalary = rs.getDouble("minSalary");
                String description = rs.getString("description");
                String company = rs.getString("company");
                String category = rs.getString("category");
                String skills = rs.getString("skills");
                String logo = rs.getString("logo");
                LocalDateTime dateOfCreation = rs.getTimestamp("date_of_creation").toLocalDateTime();

                Offer offer = new Offer(id, jobTitle, maxSalary, minSalary, description, company, category, dateOfCreation,skills);
                offer.setImage(logo);
                offerList.add(offer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void setupTable() {
        offerNameColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        dateOfCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));
        skillsColumn.setCellValueFactory(new PropertyValueFactory<>("skills"));
        

        viewColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("View");

            {
                updateButton.setOnAction(event -> {
                    Offer offer = getTableView().getItems().get(getIndex());
                    OfferDetailsController.setOfferId(offer.getId());
                    Main.setRoot("/ressources/postulator/OfferDetails");
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
        
        logoColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            
            {
                imageView.setFitWidth(160);
                imageView.setFitHeight(160);
                imageView.setPreserveRatio(true);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Offer offer = getTableView().getItems().get(getIndex());
                    String logoUrl = offer.getImage();
                    if (logoUrl != null && !logoUrl.isEmpty()) {
                        try {
                            URL url = new URL(logoUrl);
                            Image img = new Image(url.openStream());
                            imageView.setImage(img);
                        } catch (IOException e) {
                            System.out.println("Error loading image: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    setGraphic(imageView);
                }
            }
        });
        

        offerTable.setItems(offerList);
    }   
    
    @FXML
    private void filter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ressources/postulator/FilterDialog.fxml"));
            AnchorPane filterPane = loader.load();

            // Get the controller for the filter dialog
            FilterDialogController filterDialogController = loader.getController();

            // Create a new stage for the filter dialog
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Filter Offers");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(filterPane));
            stage.showAndWait();

            // Get the selected skills from the filter dialog controller
            List<String> selectedSkills = filterDialogController.getSelectedSkills();

            // Apply the filter to the offer list
            applyFilter(selectedSkills);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyFilter(List<String> selectedSkills) {
        if (selectedSkills.isEmpty()) {
            offerTable.setItems(offerList); // Reset the table view if no filter is applied
            return;
        }

        ObservableList<Offer> filteredList = FXCollections.observableArrayList();

        for (Offer offer : offerList) {
            for (String skill : selectedSkills) {
                if (offer.getSkills().toLowerCase().contains(skill.toLowerCase())) {
                    filteredList.add(offer);
                    setupTable();
                    break;
                }
            }
        }

        offerTable.setItems(filteredList);
    }

    
}