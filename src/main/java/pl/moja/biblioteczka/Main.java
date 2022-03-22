package pl.moja.biblioteczka;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.utils.FxmlUtils;

import java.util.Locale;

public class Main extends Application {

    public static final String BORDER_PAIN_MAIN_FXML = "/fxml/BorderPainMain.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    DbManager dbManager = new DbManager();

    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(new Locale("en"));
        Pane borderPane = FxmlUtils.fxmlLoader(BORDER_PAIN_MAIN_FXML);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.application"));
        primaryStage.show();

    }
}
