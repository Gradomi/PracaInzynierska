package pl.moja.biblioteczka.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.moja.biblioteczka.utils.DialogsUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    public HBox topMenuButtons;
    public ToggleGroup languageGroup;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TopMenuButtonsController topMenuButtonsController;
    @FXML
    private void initialize(){
        topMenuButtonsController.setMainController(this);
    }
    @FXML
    public void setCenter(String fxmlPath){

        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fxmlPath));
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages");
        loader.setResources(bundle);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setCenter(parent);
    }

    public void closeApplication() {
        Optional<ButtonType> result = DialogsUtils.confirmationDialog();
        if(result.get()==ButtonType.OK) {
            Platform.exit();
            System.out.println(0);
        }

    }

    public void setCaspian() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    }

    public void SetModena() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
    }

    public void alwaysOnTop(ActionEvent actionEvent) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        boolean value = ((CheckMenuItem) actionEvent.getSource()).selectedProperty().get();
        stage.setAlwaysOnTop(value);
    }

    public void about(){
        DialogsUtils.dialogAboutApplication();
    }
}
