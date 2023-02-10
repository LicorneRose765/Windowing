package Windowing.front.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;

public class FileLoaderPopupController extends Controller {
    @FXML
    RadioButton examplesRadioButton, fileSystemRadioButton, example1RadioButton, example2RadioButton, example3RadioButton;
    @FXML
    Button fileChooserButton, closeButton;
    @FXML
    Label filenameLabel, fileChosenLabel;

    @FXML
    void handleOnCloseButtonMouseClicked() {
        MainSceneController.popup.close();
    }

    public void handleExamplesOnMouseClicked(MouseEvent mouseEvent) {
        fileChooserButton.setDisable(true);
        example1RadioButton.setDisable(false);
        example2RadioButton.setDisable(false);
        example3RadioButton.setDisable(false);
        fileChosenLabel.setVisible(false);
        filenameLabel.setVisible(false);
    }

    public void handleFileSystemOnMouseClicked(MouseEvent mouseEvent) {
        fileChooserButton.setDisable(false);
        example1RadioButton.setDisable(true);
        example1RadioButton.setSelected(false);
        example2RadioButton.setDisable(true);
        example2RadioButton.setSelected(false);
        example3RadioButton.setDisable(true);
        example3RadioButton.setSelected(false);
        fileChosenLabel.setVisible(true);
        filenameLabel.setVisible(true);
    }
}
