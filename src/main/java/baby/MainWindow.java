package baby;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controller for the JavaFX chat window.
 */
public class MainWindow {
    private static final String LINE = "____________________________________________________________";

    private Baby baby = new Baby();

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    /**
     * Sets up behaviour after the FXML controls have been loaded.
     */
    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));
    }

    /**
     * Handles both pressing Enter and clicking the Send button.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = baby.getResponse(input);
        boolean isError = isErrorResponse(response);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getBabyDialog(formatBabyResponse(response, isError), isError)
        );

        userInput.clear();
    }

    /**
     * Checks whether Baby's response should use the error style.
     *
     * @param response The raw response from Baby.
     * @return True if the response is an error response.
     */
    private boolean isErrorResponse(String response) {
        return response.contains("OOPS!") || response.contains("Sorry");
    }

    /**
     * Makes the console response text pleasant to read inside a chat bubble.
     *
     * @param response The raw response from Baby.
     * @param isError Whether the response is an error response.
     * @return The text to display in the GUI.
     */
    private String formatBabyResponse(String response, boolean isError) {
        String message = stripConsoleLines(response);
        if (isError) {
            return "Oh snow, " + message.replace("OOPS!", "").trim();
        }

        return "Your Highness,\n" + message;
    }

    /**
     * Removes console separator lines before showing text in the GUI.
     *
     * @param response The raw response from Baby.
     * @return The response without console separators.
     */
    private String stripConsoleLines(String response) {
        StringBuilder message = new StringBuilder();
        String[] lines = response.split("\\R");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty() || trimmedLine.equals(LINE)) {
                continue;
            }

            if (message.length() > 0) {
                message.append(System.lineSeparator());
            }
            message.append(trimmedLine);
        }

        return message.toString();
    }
}
