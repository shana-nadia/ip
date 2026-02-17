package duchess.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import duchess.Duchess;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Duchess duchess = new Duchess();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setTitle("Duchess");
            fxmlLoader.<MainWindow>getController().setDuchess(duchess);  // inject the Duchess instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
