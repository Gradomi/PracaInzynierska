package pl.moja.biblioteczka.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pl.moja.biblioteczka.database.dao.AbstractDao;
import pl.moja.biblioteczka.database.dao.CompositionDao;
import pl.moja.biblioteczka.database.dao.MaterialCompositionDao;
import pl.moja.biblioteczka.database.dao.MaterialDao;
import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.models.Composition;
import pl.moja.biblioteczka.models.Material;
import pl.moja.biblioteczka.models.MaterialComposition;
import pl.moja.biblioteczka.utils.DialogsUtils;
import pl.moja.biblioteczka.utils.validators.NumericFieldValidator;
import pl.moja.biblioteczka.utils.validators.TextFieldValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMaterialController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField normTextField;
    @FXML
    private TextField yieldStrenghtMinTextField;
    @FXML
    private TextField yieldStrenghtMaxTextField;
    @FXML
    private TextField tensileStrengthMinTextField;
    @FXML
    private TextField tensileStrengthMaxTextField;
    @FXML
    private TextField percentageElongationMinTextField;
    @FXML
    private TextField percentageElongationMaxTextField;
    @FXML
    private TextField brinellHardnessMinTextField;
    @FXML
    private TextField brinellHardnessMaxTextField;
    @FXML
    private TextField impactStrengthMinTextField;
    @FXML
    private TextField impactStrengthMaxTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private ListView<Object> addedElementsListView;
    @FXML
    private ComboBox<Composition> addMaterialComboBox;
    @FXML
    private TextField minValueTextField;
    @FXML
    private TextField maxValueTextField;
    @FXML
    private Button addMaterialButton;
    List<MaterialComposition> materialCompositions = new ArrayList<>();
    List<Composition> compositions = new ArrayList<>();

    private DbManager dbManager = new DbManager();
    private MaterialCompositionDao materialCompositionDao = new MaterialCompositionDao(dbManager);
    private MaterialDao materialDao = new MaterialDao(dbManager);
    private CompositionDao compositionDao = new CompositionDao(dbManager);

    private Material newMaterial;


    public void validateTextFieldInput(TextField textfield) {
        if (TextFieldValidator.validate(textfield)) {
            textfield.getStyleClass().remove("error");
        } else {
            textfield.getStyleClass().add("error");
        }
    }

    public void validateNumericFieldInput(TextField textField) {
        if (NumericFieldValidator.validate(textField)) {
            textField.getStyleClass().remove("error");
        } else {
            textField.getStyleClass().add("error");
        }
    }


    @FXML
    public void initialize() {

        nameTextField.textProperty().addListener((observableValue, s, t1) -> validateTextFieldInput(nameTextField));
        countryTextField.textProperty().addListener((observableValue, s, t1) -> validateTextFieldInput(countryTextField));
        normTextField.textProperty().addListener((observableValue, s, t1) -> validateTextFieldInput(normTextField));

        yieldStrenghtMinTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(yieldStrenghtMinTextField));
        yieldStrenghtMaxTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(yieldStrenghtMaxTextField));
        tensileStrengthMinTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(tensileStrengthMinTextField));
        tensileStrengthMaxTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(tensileStrengthMaxTextField));
        percentageElongationMinTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(percentageElongationMinTextField));
        percentageElongationMaxTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(percentageElongationMaxTextField));
        brinellHardnessMinTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(brinellHardnessMinTextField));
        brinellHardnessMaxTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(brinellHardnessMaxTextField));
        impactStrengthMinTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(impactStrengthMinTextField));
        impactStrengthMaxTextField.textProperty().addListener((observableValue, s, t1) -> validateNumericFieldInput(impactStrengthMaxTextField));
        
        compositions = compositionDao.get();
        addMaterialComboBox.setItems(FXCollections.observableArrayList(compositions));

        addMaterialComboBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(Composition composition) {
                if (composition != null) {

                    return composition.getSymbolName();
                }
                return null;
            }

            @Override
            public Composition fromString(String string) {
                return addMaterialComboBox.getItems().stream().filter(composition ->
                        composition.getSymbolId() == Integer.parseInt(string)).findFirst().orElse(null);
            }
        });
    }

    public void saveButtonOnAction() {
        materialCompositions.add(MaterialComposition.builder()
                .chemicalCompositionId(addMaterialComboBox.getSelectionModel().getSelectedItem().getSymbolId())
                .compositionRatioMin(Float.parseFloat(minValueTextField.getText()))
                .compositionRatioMax(Float.parseFloat(maxValueTextField.getText()))
                .build());
        addedElementsListView.setItems(FXCollections.observableArrayList(materialCompositions));
    }

    public void clearButtonOnAction() {

    }

    public void addMaterialButtonOnAction() {
        newMaterial = Material.builder()
                .materialName(nameTextField.getText())
                .country(countryTextField.getText())
                .norm(normTextField.getText())
                .yieldStrenghtMin(new BigDecimal(yieldStrenghtMinTextField.getText()))
                .yieldStrenghtMax(new BigDecimal(yieldStrenghtMaxTextField.getText()))
                .percentageElongationMin(new BigDecimal(percentageElongationMinTextField.getText()))
                .percentageElongationMax(new BigDecimal(percentageElongationMaxTextField.getText()))
                .brinellHardnessMin(new BigDecimal(brinellHardnessMinTextField.getText()))
                .brinellHardnessMax(new BigDecimal(brinellHardnessMaxTextField.getText()))
                .impactStrengthMin(new BigDecimal(impactStrengthMinTextField.getText()))
                .impactStrengthMax(new BigDecimal(impactStrengthMaxTextField.getText()))
                .tensileStrengthMin(new BigDecimal(tensileStrengthMinTextField.getText()))
                .tensileStrengthMax(new BigDecimal(tensileStrengthMaxTextField.getText()))
                .build();

        try {
            int newId = materialDao.save(newMaterial);
            if (newId != 0) {
                materialCompositions.forEach(composition -> composition.setMaterialId(newId));
                materialCompositionDao.save(materialCompositions);
            }

        } catch (SQLException sqlException) {
            DialogsUtils.erroorDialog(ResourceBundle.getBundle("messages").getString("sql.error"));
            AbstractDao.printSQLException(sqlException);
        }
    }


}
