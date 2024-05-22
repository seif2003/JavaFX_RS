package controllers.entreprise;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import application.Main;
import database.Connexion;

public class AcceeptedCandidat {
    @FXML
    private TableView<AcceptedCandidateData> acceptedTable;
    @FXML
    private TableColumn<AcceptedCandidateData, String> acceptedCandidatColumn;
    @FXML
    private TableColumn<AcceptedCandidateData, String> offreName;
    @FXML
    private TableColumn<AcceptedCandidateData, String> emailColumn;
    @FXML
    private TableColumn<AcceptedCandidateData, String> phoneColumn;
   
    @FXML
    private TableColumn<AcceptedCandidateData, Void> cvColumn;
    @FXML
    private Label list_of_offers;
    @FXML
    private Label create_offer;
    @FXML
    private Pane logoutPane;
    @FXML
    private Label Myprofile;

    private ObservableList<AcceptedCandidateData> acceptedCandidatesList = FXCollections.observableArrayList();

    public void initialize() {
    	Myprofile.setOnMouseClicked(event -> {
            Main.setRoot("/ressources/entreprise/MyProfileEntreprise");
        });
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

        String companyEmail = Main.getCurrentUser().getEmail();
        loadAcceptedRequestsByCompany(companyEmail);
        setupTable();
    }

    private void setupTable() {
        acceptedCandidatColumn.setCellValueFactory(new PropertyValueFactory<>("candidateName"));
        offreName.setCellValueFactory(new PropertyValueFactory<>("offerName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        cvColumn.setCellFactory(col -> new TableCell<AcceptedCandidateData, Void>() {
            private final Button btn = new Button("Consulter CV");

            {
                btn.setOnAction(event -> {
                    AcceptedCandidateData data = getTableView().getItems().get(getIndex());
                    System.out.println("Consulter CV de " + data.getCandidateName());
                    String resumeUrl = data.getResume();
                    if (resumeUrl != null && !resumeUrl.isEmpty()) {
                        try {
                            Desktop.getDesktop().browse(new URI(resumeUrl));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Le lien du CV est vide.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    AcceptedCandidateData data = getTableView().getItems().get(getIndex());
                    String resumeUrl = data.getResume();
                    if (resumeUrl == null || resumeUrl.isEmpty()) {
                        btn.setDisable(true);
                        btn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
                    } else {
                        btn.setDisable(false);
                        btn.setStyle(null); // Reset to default style
                    }
                    setGraphic(btn);
                }
            }
        });

        acceptedTable.setItems(acceptedCandidatesList);
    }

    private void loadAcceptedRequestsByCompany(String companyEmail) {
        String sql = "SELECT r.postulator, r.offer, p.fullName, o.job_title, p.email, p.phone, p.resume " +
                     "FROM request r " +
                     "JOIN postulator p ON r.postulator = p.email " +
                     "JOIN offer o ON r.offer = o.id " +
                     "WHERE o.company = ? AND r.etat = 'accepted'";

        try (Connection conn = Connexion.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, companyEmail);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String candidateName = rs.getString("fullName");
                String offerName = rs.getString("job_title");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String postulator = rs.getString("postulator");
                String resume = rs.getString("resume");
                acceptedCandidatesList.add(new AcceptedCandidateData(candidateName, offerName, email, phone, postulator, resume));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class AcceptedCandidateData {
        private String candidateName;
        private String offerName;
        private String email;
        private String phone;
        private String postulator;
        private String resume;

        public AcceptedCandidateData(String candidateName, String offerName, String email, String phone, String postulator, String resume) {
            this.candidateName = candidateName;
            this.offerName = offerName;
            this.email = email;
            this.phone = phone;
            this.postulator = postulator;
            this.resume = resume;
        }

        public String getCandidateName() {
            return candidateName;
        }

        public String getOfferName() {
            return offerName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getPostulator() {
            return postulator;
        }

        public String getResume() {
            return resume;
        }
    }
}
