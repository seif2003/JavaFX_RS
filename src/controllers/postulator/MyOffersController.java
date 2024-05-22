package controllers.postulator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import database.Connexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import models.MyOffer;

public class MyOffersController implements Initializable {

    @FXML
    private Label myOffers;

    @FXML
    private Label myProfile;

    @FXML
    private Label listOfOffers;

    @FXML
    private Pane logoutPane;

    @FXML
    private TableView<MyOffer> refusedTable;

    @FXML
    private TableView<MyOffer> notProcessedTable;

    @FXML
    private TableView<MyOffer> acceptedTable;

    @FXML
    private TableColumn<MyOffer, String> refusedJobTitleColumn;

    @FXML
    private TableColumn<MyOffer, String> notProcessedJobTitleColumn;

    @FXML
    private TableColumn<MyOffer, String> acceptedJobTitleColumn;

    private ObservableList<MyOffer> refusedOffers = FXCollections.observableArrayList();
    private ObservableList<MyOffer> notProcessedOffers = FXCollections.observableArrayList();
    private ObservableList<MyOffer> acceptedOffers = FXCollections.observableArrayList();

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
        setupRowClickListeners();
    }

    private void loadOffers() {
        String sql = "SELECT o.id, o.job_title, r.etat FROM request r, offer o WHERE r.offer = o.id AND r.postulator = ?";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Main.getCurrentUser().getEmail());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String jobTitle = rs.getString("job_title");
                String etat = rs.getString("etat");
                int offerId = rs.getInt("id");

                MyOffer offer = new MyOffer(offerId, jobTitle, etat);

                switch (etat) {
                    case "rejected":
                        refusedOffers.add(offer);
                        break;
                    case "not processed":
                        notProcessedOffers.add(offer);
                        break;
                    case "accepted":
                        acceptedOffers.add(offer);
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        refusedJobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("job_title"));
        notProcessedJobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("job_title"));
        acceptedJobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("job_title"));

        refusedTable.setItems(refusedOffers);
        notProcessedTable.setItems(notProcessedOffers);
        acceptedTable.setItems(acceptedOffers);
    }

    private void setupRowClickListeners() {
        setupRowClickListener(refusedTable);
        setupRowClickListener(notProcessedTable);
        setupRowClickListener(acceptedTable);
    }

    private void setupRowClickListener(TableView<MyOffer> table) {
        table.setRowFactory(tv -> {
            TableRow<MyOffer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    MyOffer clickedOffer = row.getItem();
                    int id = clickedOffer.getOffer_id();
                    OfferDetailsController.setOfferId(id);
                    Main.setRoot("/ressources/postulator/OfferDetails");
                }
            });
            return row;
        });
    }
}
