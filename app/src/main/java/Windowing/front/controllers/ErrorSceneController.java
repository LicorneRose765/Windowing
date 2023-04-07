package Windowing.front.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorSceneController extends Controller {
    public static String errorMessage;

    @FXML
    Label errorLabel;

    @FXML
    void initialize() {
        errorLabel.setText(errorMessage);
    }
}
