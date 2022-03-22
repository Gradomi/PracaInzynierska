package pl.moja.biblioteczka.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.moja.biblioteczka.database.dao.MaterialDao;
import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.models.Material;

import java.math.BigDecimal;
import java.sql.SQLException;

public class FavoriteController {
    @FXML
    private TableView<Material> favouriteTableView;

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

    private DbManager dbManager = new DbManager();

    private MaterialDao materialDao = new MaterialDao(dbManager);

    @FXML
    public void initialize() throws SQLException {

        materialIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        materialNameCol.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        normCol.setCellValueFactory(new PropertyValueFactory<>("norm"));
        yieldStrengthMinCol.setCellValueFactory(new PropertyValueFactory<>("yieldStrenghtMin"));
        yieldStrengthMaxCol.setCellValueFactory(new PropertyValueFactory<>("yieldStrenghtMax"));
        tensileStrengthMinCol.setCellValueFactory(new PropertyValueFactory<>("tensileStrengthMin"));
        tensileStrengthMaxCol.setCellValueFactory(new PropertyValueFactory<>("tensileStrengthMax"));
        percentageElongationMinCol.setCellValueFactory(new PropertyValueFactory<>("percentageElongationMin"));
        percentageElongationMaxCol.setCellValueFactory(new PropertyValueFactory<>("percentageElongationMax"));
        brinellHardnessMinCol.setCellValueFactory(new PropertyValueFactory<>("brinellHardnessMin"));
        brinellHardnessMaxCol.setCellValueFactory(new PropertyValueFactory<>("brinellHardnessMax"));
        impactStrengthMinCol.setCellValueFactory(new PropertyValueFactory<>("impactStrengthMin"));
        impactStrengthMaxCol.setCellValueFactory(new PropertyValueFactory<>("impactStrengthMax"));
        materialCompositionCol.setCellValueFactory(new PropertyValueFactory<>("materialComposition"));

        try {
            favouriteTableView.setItems(FXCollections.observableArrayList(materialDao.get()
                    .stream()
                    .filter(Material::isFavourite)
                    .toList()));

        }catch(SQLException sqlException){
            DbManager.printSQLException(sqlException);
        }


    }

    public void deleteFavoriteButtonOnAction(ActionEvent actionEvent){

    }



}
