package pl.moja.biblioteczka.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TopMenuButtonsController {


    private MainController mainController;


    @FXML
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }



    @FXML
    public void addCategory() {
        mainController.setCenter("fxml/AddCategory.fxml");
    }

    public void favoriteActions() {
        mainController.setCenter("fxml/Favorite.fxml");
    }

    public void addMaterialsAction() {
        mainController.setCenter("fxml/AddMaterial.fxml");
    }

    public void editMaterialAction() {
        mainController.setCenter("fxml/EditMaterial.fxml");
    }


}
