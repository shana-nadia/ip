package duchess.ui;

import javafx.animation.PauseTransition;

import javafx.application.Platform;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javafx.util.Duration;

import duchess.Duchess;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Duchess duchess;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/Peasant.png"));
    private Image duchessImage = new Image(this.getClass().getResourceAsStream("/images/Duchess.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duchess instance */
    public void setDuchess(Duchess d) {
        duchess = d;

        dialogContainer.getChildren().add(
                DialogBox.getDuchessDialog(
                        duchess.getGreeting(),
                        duchessImage,
                        "DEFAULT"
                )
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duchess's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = duchess.getResponse(input);
        String commandType = duchess.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDuchessDialog(response, duchessImage, commandType)
        );
        userInput.clear();

        if ("EXIT".equals(commandType)) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
