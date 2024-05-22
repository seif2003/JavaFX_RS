package controllers.postulator;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FilterDialogController {

    @FXML
    private VBox filterVBox;

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
    private CheckBox Teamwork1;
    @FXML
    private CheckBox ProjectManagement1;
    @FXML
    private CheckBox DataAnalysis1;
    @FXML
    private CheckBox Adaptability1;
    @FXML
    private CheckBox CreativityAndInnovation1;
    @FXML
    private CheckBox CustomerService1;
    @FXML
    private CheckBox Negotiation1;
    @FXML
    private CheckBox Autonomy1;
    @FXML
    private CheckBox ChangeManagement1;
    @FXML
    private CheckBox InterpersonalSkills1;
    @FXML
    private CheckBox CriticalThinking1;
    @FXML
    private CheckBox UIUXDesign1;
    @FXML
    private CheckBox TechnicalWriting1;
    @FXML
    private CheckBox AgileProjectManagement1;
    @FXML
    private CheckBox SEOSEMSkills1;
    @FXML
    private CheckBox GraphicDesign1;
    @FXML
    private CheckBox ProgrammingSkills1;
    @FXML
    private CheckBox SocialMediaManagement1;
    @FXML
    private CheckBox ForeignLanguageProficiency1;
    @FXML
    private CheckBox DatabaseDesign1;

    private List<String> selectedSkills = new ArrayList<>();

    @FXML
    private void initialize() {
        // Add listeners to checkboxes to collect selected skills
        addCheckBoxListener(Communication);
        addCheckBoxListener(ComputerSkills);
        addCheckBoxListener(ProblemSolving);
        addCheckBoxListener(Leadership);
        addCheckBoxListener(TimeManagement);
        addCheckBoxListener(Teamwork1);
        addCheckBoxListener(ProjectManagement1);
        addCheckBoxListener(DataAnalysis1);
        addCheckBoxListener(Adaptability1);
        addCheckBoxListener(CreativityAndInnovation1);
        addCheckBoxListener(CustomerService1);
        addCheckBoxListener(Negotiation1);
        addCheckBoxListener(Autonomy1);
        addCheckBoxListener(ChangeManagement1);
        addCheckBoxListener(InterpersonalSkills1);
        addCheckBoxListener(CriticalThinking1);
        addCheckBoxListener(UIUXDesign1);
        addCheckBoxListener(TechnicalWriting1);
        addCheckBoxListener(AgileProjectManagement1);
        addCheckBoxListener(SEOSEMSkills1);
        addCheckBoxListener(GraphicDesign1);
        addCheckBoxListener(ProgrammingSkills1);
        addCheckBoxListener(SocialMediaManagement1);
        addCheckBoxListener(ForeignLanguageProficiency1);
        addCheckBoxListener(DatabaseDesign1);
    }

    private void addCheckBoxListener(CheckBox checkBox) {
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectedSkills.add(checkBox.getText());
            } else {
                selectedSkills.remove(checkBox.getText());
            }
        });
    }

    public List<String> getSelectedSkills() {
        return selectedSkills;
    }

    @FXML
    private void search() {
        // Close the filter dialog when search is clicked
        ((Stage) filterVBox.getScene().getWindow()).close();
    }
}
