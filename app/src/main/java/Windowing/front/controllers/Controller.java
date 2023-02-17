package Windowing.front.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This mother Controller class gives access to a few useful methods to all children classes.
 */
public abstract class Controller {
    /**
     * Returns a new style line with the darker background color to use when the mouse enters a button.
     *
     * @param button The entered button
     * @return The CSS line
     */
    public static String formatNewCSSLineMouseEntered(Button button) {
        String CSSLine = "";
        if (button.getPrefWidth() == 350) {
            // Big buttons
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -2.5; -fx-border-radius: 10;";
        } else if (button.getPrefWidth() == 120) {
            // Language & Back buttons
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 296) {
            // Change password button
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -2.5; -fx-border-radius: 10;";
        } else if (button.getPrefWidth() == 50) {
            // Square buttons for PIN
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -1.5; -fx-border-radius: 4;";
        } else if (button.getPrefWidth() == 200) {
            if (button.getText().toLowerCase().contains("path")) {
                CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
            } else {
                // Confirm button for PIN
                CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -2.5; -fx-border-radius: 10;";
            }
        } else if (button.getPrefWidth() == 250) {
            // Export buttons
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 170) {
            // Right side buttons
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 75) {
            // Add button for language
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 135) {
            // Add/remove account for visualisation
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 100) {
            // Search button
            CSSLine = "-fx-background-color: rgb(190, 185, 180); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        }
        return CSSLine;
    }

    /**
     * Returns a new style line with the original background color to use when the mouse exits a button.
     *
     * @param button The exited button
     * @return The CSS line
     */
    public static String formatNewCSSLineMouseExited(Button button) {
        String CSSLine = "";
        if (button.getPrefWidth() == 350) {
            // Big buttons
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -2.5; -fx-border-radius: 10;";
        } else if (button.getPrefWidth() == 120) {
            // Language & Back buttons
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 296) {
            // Change password button
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -2.5; -fx-border-radius: 10;";
        } else if (button.getPrefWidth() == 50) {
            // Square buttons for PIN
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -1.5; -fx-border-radius: 4;";
        } else if (button.getPrefWidth() == 200) {
            if (button.getText().toLowerCase().contains("path")) {
                CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
            } else {
                // Confirm button for PIN
                CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 2.5; -fx-border-insets: -2.5; -fx-border-radius: 10;";
            }
        } else if (button.getPrefWidth() == 250) {
            // Export buttons
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 170) {
            // Right side buttons
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 75) {
            // Add button for language
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 135) {
            // Add/remove account for visualisation
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        } else if (button.getPrefWidth() == 100) {
            // Search button
            CSSLine = "-fx-background-color: rgb(210, 205, 200); -fx-border-color: rgb(10, 10, 20); -fx-border-width: 1.5; -fx-border-insets: -1.5; -fx-border-radius: 5;";
        }
        return CSSLine;
    }

    @FXML
    void handleButtonMouseEntered(MouseEvent event) {
        Button buttonSource = (Button) event.getSource();
        buttonSource.setStyle(formatNewCSSLineMouseEntered(buttonSource));
    }

    @FXML
    void handleButtonMouseExited(MouseEvent event) {
        Button buttonSource = (Button) event.getSource();
        buttonSource.setStyle(formatNewCSSLineMouseExited(buttonSource));
    }
}