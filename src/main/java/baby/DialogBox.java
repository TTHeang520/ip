package baby;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents one row in the chat conversation.
 */
public class DialogBox extends HBox {
    private static final Image QUEEN_IMAGE =
            new Image(DialogBox.class.getResourceAsStream("/images/queen.png"));
    private static final Image SNOWMAN_IMAGE =
            new Image(DialogBox.class.getResourceAsStream("/images/snowman.png"));

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image, String labelStyleClass) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        dialog.getStyleClass().add("dialog-label");
        dialog.getStyleClass().add(labelStyleClass);

        if (image == null) {
            displayPicture.setVisible(false);
            displayPicture.setManaged(false);
        } else {
            displayPicture.setImage(image);
        }
    }

    /**
     * Flips the dialog box so the avatar is on the left.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a right-aligned dialog row for the user's input.
     *
     * @param text The user's message.
     * @return The user's dialog row.
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox db = new DialogBox(text, null, "user-label");
        db.getStyleClass().add("user-row");
        return db;
    }

    /**
     * Creates a left-aligned dialog row for Baby's reply.
     *
     * @param text Baby's response.
     * @param isError True if the response should use the error style.
     * @return Baby's dialog row.
     */
    public static DialogBox getBabyDialog(String text, boolean isError) {
        Image image = isError ? SNOWMAN_IMAGE : QUEEN_IMAGE;
        String styleClass = isError ? "error-label" : "queen-label";
        DialogBox db = new DialogBox(text, image, styleClass);
        db.getStyleClass().add("baby-row");
        db.flip();
        return db;
    }
}
