package pl.moja.biblioteczka.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import pl.moja.biblioteczka.database.dao.MaterialDao;
import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.models.Material;
import pl.moja.biblioteczka.models.SearchParameter;
import pl.moja.biblioteczka.utils.validators.NumericFieldValidator;
import pl.moja.biblioteczka.utils.validators.TextFieldValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoryController {

    @FXML
    private Button searchMaterialButton;

    @FXML
    private Button addParameterButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Material> tableView;
    DbManager dbManager;
    MaterialDao materialDao;
    List<Material> materials;
    @FXML
    private TableColumn<Material, Integer> materialIdCol;
    @FXML
    private TableColumn<Material, Object> materialCompositionCol;
    @FXML
    private TableColumn<Material, String> materialNameCol;
    @FXML
    private TableColumn<Material, String> countryCol;
    @FXML
    private TableColumn<Material, String> normCol;
    @FXML
    private TableColumn<Material, BigDecimal> yieldStrengthMinCol;
    @FXML
    private TableColumn<Material, BigDecimal> yieldStrengthMaxCol;
    @FXML
    private TableColumn<Material, BigDecimal> tensileStrengthMinCol;
    @FXML
    private TableColumn<Material, BigDecimal> tensileStrengthMaxCol;
    @FXML
    private TableColumn<Material, BigDecimal> percentageElongationMinCol;
    @FXML
    private TableColumn<Material, BigDecimal> percentageElongationMaxCol;
    @FXML
    private TableColumn<Material, BigDecimal> brinellHardnessMinCol;
    @FXML
    private TableColumn<Material, BigDecimal> brinellHardnessMaxCol;
    @FXML
    private TableColumn<Material, BigDecimal> impactStrengthMinCol;
    @FXML
    private TableColumn<Material, BigDecimal> impactStrengthMaxCol;
    @FXML
    private ListView<SearchParameter> searchListView;
    @FXML
    private ComboBox<String> advancedSearchComboBox;
    @FXML
    private TextField advancedSearchTextField1;
    @FXML
    private List<SearchParameter> searchParameter = new ArrayList<>();
    @FXML
    List<String> tableColumnsNames;

    @FXML
    private Button saveButton;

    private Material searchMaterial = new Material();

    public void initialize() throws SQLException {

        initBindings();
        dbManager = new DbManager();
        materialDao = new MaterialDao(dbManager);
        materials = materialDao.get();
        initTable();

         tableView.setItems(FXCollections.observableArrayList(materials));

        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) ->
                searchMaterialButton.disableProperty().bind(searchTextField.textProperty().isEmpty()));

        tableColumnsNames = tableView.getColumns().stream().map(TableColumn::getText).toList();
        advancedSearchComboBox.setItems(FXCollections.observableArrayList(tableColumnsNames.stream()
                .map(name -> name.trim().replace(" ", "_")).toList()));

        setSearchButtonState();

    }

    private void initTableFields() {
        materialIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        materialCompositionCol.setCellValueFactory(new PropertyValueFactory<>("materialComposition"));
        setUpMaterialNameCol();
        setupCountryCol();
        setupNormCol();
        setupYieldStrengthMinCol();
        setupYieldStrengthMaxCol();
        setupTensileStrengthMinCol();
        setupTensileStrengthMaxCol();
        setupPercentageElongationMinCol();
        setupPercentageElongationMaxCol();
        setupBrinellHardnessMinCol();
        setupBrinellHardnessMaxCol();
        setupImpactStrengthMinCol();
        setupImpactStrengthMaxCol();
    }

    private void initTable() {
        initTableFields();
        tableView.setEditable(true);
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);

    }

    private void validateTableTextColumn(TableColumn<Material, String> tableColumn) {
        if (TextFieldValidator.validate(tableColumn.getText())) {
            tableColumn.getStyleClass().remove("error");
        } else {
            tableColumn.getStyleClass().add("error");
        }
    }

    private void setUpMaterialNameCol() {
        materialNameCol.setCellValueFactory(new PropertyValueFactory<>("materialName"));

        materialNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        materialNameCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setMaterialName(tableColumn.getNewValue())
        );
        materialNameCol.textProperty().addListener((observableValue, s, t1) -> validateTableTextColumn(materialNameCol));
    }

    @FXML
    private void saveOnAction() throws SQLException {
        materialDao.update(tableView.getItems().stream().toList());
        tableView.refresh();
    }

    private void setupCountryCol() {
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        countryCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setCountry(tableColumn.getNewValue())
        );
        countryCol.textProperty().addListener((observableValue, s, t1) -> validateTableTextColumn(countryCol));
    }

    private void setupNormCol() {
        normCol.setCellValueFactory(new PropertyValueFactory<>("norm"));
        normCol.setCellFactory(TextFieldTableCell.forTableColumn());
        normCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setNorm(tableColumn.getNewValue())
        );
    }

    private void setupYieldStrengthMinCol() {
        yieldStrengthMinCol.setCellValueFactory(new PropertyValueFactory<>("yieldStrenghtMin"));
        yieldStrengthMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        yieldStrengthMinCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setYieldStrenghtMin(tableColumn.getNewValue()));

        yieldStrengthMinCol.textProperty().addListener((observableValue, oldV, newV) -> {


        });
    }

    private void setupYieldStrengthMaxCol() {
        yieldStrengthMaxCol.setCellValueFactory(new PropertyValueFactory<>("yieldStrenghtMax"));
        yieldStrengthMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        yieldStrengthMaxCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setYieldStrenghtMax(tableColumn.getNewValue())
        );
    }

    private void setupTensileStrengthMinCol() {
        tensileStrengthMinCol.setCellValueFactory(new PropertyValueFactory<>("tensileStrengthMin"));
        tensileStrengthMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        tensileStrengthMinCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setTensileStrengthMin(tableColumn.getNewValue())
        );
    }

    private void setupTensileStrengthMaxCol() {
        tensileStrengthMaxCol.setCellValueFactory(new PropertyValueFactory<>("tensileStrengthMax"));
        tensileStrengthMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        tensileStrengthMaxCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setTensileStrengthMax(tableColumn.getNewValue())
        );
    }


    private void setupPercentageElongationMinCol() {
        percentageElongationMinCol.setCellValueFactory(new PropertyValueFactory<>("percentageElongationMin"));
        percentageElongationMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        percentageElongationMinCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setPercentageElongationMin(tableColumn.getNewValue())
        );
    }

    private void setupPercentageElongationMaxCol() {
        percentageElongationMaxCol.setCellValueFactory(new PropertyValueFactory<>("percentageElongationMax"));
        percentageElongationMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        percentageElongationMaxCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setPercentageElongationMax(tableColumn.getNewValue())
        );
    }

    private void setupBrinellHardnessMinCol() {
        brinellHardnessMinCol.setCellValueFactory(new PropertyValueFactory<>("brinellHardnessMin"));
        brinellHardnessMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        brinellHardnessMinCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setBrinellHardnessMin(tableColumn.getNewValue()));
    }

    private void setupBrinellHardnessMaxCol() {
        brinellHardnessMaxCol.setCellValueFactory(new PropertyValueFactory<>("brinellHardnessMax"));
        brinellHardnessMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        brinellHardnessMaxCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setBrinellHardnessMax(tableColumn.getNewValue()));
    }


    private void setupImpactStrengthMinCol() {
        impactStrengthMinCol.setCellValueFactory(new PropertyValueFactory<>("impactStrengthMin"));
        impactStrengthMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        impactStrengthMinCol.setOnEditCommit(
                tableColumn -> (tableColumn.getTableView().getItems().get(
                        tableColumn.getTablePosition().getRow())
                ).setImpactStrengthMin(tableColumn.getNewValue()));
    }

    private void setupImpactStrengthMaxCol() {
        impactStrengthMaxCol.setCellValueFactory(new PropertyValueFactory<>("impactStrengthMax"));
        impactStrengthMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        impactStrengthMaxCol.setOnEditCommit(tableColumn -> tableColumn.getTableView().getItems()
                .get(tableColumn.getTablePosition().getRow())
                .setImpactStrengthMax(tableColumn.getNewValue()));
    }

    private void initBindings() {
        this.searchMaterialButton.disableProperty().bind(searchTextField.textProperty().isEmpty());

    }

    public void searchMaterialOnAction(ActionEvent actionEvent) throws SQLException {

        if (searchParameter.isEmpty()) {
            if (searchTextField.getText().isEmpty()) {
                materials = materialDao.get();
            } else {
                materials = materialDao.get().stream().filter(material -> material.getMaterialName().contains(searchTextField.getText())).toList();
            }
        } else {
            if (searchTextField.getText().isEmpty()) {
                materials = materialDao.getByParameters(searchParameter);
            } else {
                materials = materialDao.getByParameters(searchParameter).stream().filter(material -> material.getMaterialName().contains(searchTextField.getText())).toList();
            }
        }

        tableView.setItems(FXCollections.observableArrayList(materials));
        searchParameter = new ArrayList<>();
        searchListView.setItems(FXCollections.observableArrayList());
        setSearchButtonState();
        searchTextField.setText(null);
        advancedSearchTextField1.setText(null);
        advancedSearchComboBox.getSelectionModel().clearSelection();
    }
    

    public void searchTextFieldOnAction(ActionEvent actionEvent) throws SQLException {
        if (searchTextField.getText().isEmpty() && searchParameter.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList(materials));
        }
    }

    public void favoriteButtonOnAction(ActionEvent actionEvent) throws SQLException {

        Material material = tableView.getSelectionModel().getSelectedItem();
        material.setFavourite(!material.isFavourite());
        materialDao.update(material);
    }

    public void advancedSearchComboBoxOnAction(ActionEvent actionEvent) {

    }


    public void addParameterOnAction(ActionEvent actionEvent) {
        SearchParameter parameter = new SearchParameter();
        parameter.setColumnName(advancedSearchComboBox.getSelectionModel().getSelectedItem());

        advancedSearchComboBox.setItems(FXCollections.observableArrayList(advancedSearchComboBox
                .getItems()
                .stream()
                .filter(x -> !x.equals(advancedSearchComboBox.getSelectionModel().getSelectedItem()))
                .toList()));
        parameter.setValue(advancedSearchTextField1.getText());
        searchParameter.add(parameter);
        searchParameter.forEach(System.out::println);
        searchListView.setItems(FXCollections.observableArrayList(searchParameter));
        setSearchButtonState();
    }

    private void setSearchButtonState() {
        searchMaterialButton.disableProperty().bind(searchTextField.textProperty().isEmpty().and(Bindings.isEmpty(searchListView.getItems())));
    }
}
