package Windowing.front.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class FileLoaderPopupController extends Controller {
    @FXML
    RadioButton examplesRadioButton, fileSystemRadioButton, example1RadioButton, example2RadioButton, example3RadioButton;
    @FXML
    Button fileChooserButton, closeButton, confirmButton;
    @FXML
    Label filenameLabel, fileChosenLabel;

    File chosenFile;

    @FXML
    void handleOnCloseButtonMouseClicked() {
        MainSceneController.popup.close();
    }

    @FXML
    void handleExamplesOnMouseClicked(MouseEvent mouseEvent) {
        fileChooserButton.setDisable(true);
        example1RadioButton.setDisable(false);
        example2RadioButton.setDisable(false);
        example3RadioButton.setDisable(false);

        fileChosenLabel.setVisible(false);
        filenameLabel.setVisible(false);

        if (!example1RadioButton.isSelected() && !example2RadioButton.isSelected() && !example3RadioButton.isSelected())
            confirmButton.setDisable(true);
    }

    @FXML
    void handleFileSystemOnMouseClicked(MouseEvent mouseEvent) {
        fileChooserButton.setDisable(false);
        example1RadioButton.setDisable(true);
        example1RadioButton.setSelected(false);
        example2RadioButton.setDisable(true);
        example2RadioButton.setSelected(false);
        example3RadioButton.setDisable(true);
        example3RadioButton.setSelected(false);

        fileChosenLabel.setVisible(true);
        filenameLabel.setVisible(true);

        confirmButton.setDisable(chosenFile == null);
    }

    @FXML
    void handleOnOpenFileChooserButtonMouseClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a segment file");
        chosenFile = fileChooser.showOpenDialog(MainSceneController.popup);

        if (chosenFile == null) {
            fileChosenLabel.setText("None");
            return;
        }
        filenameLabel.setText(chosenFile.getName());
        confirmButton.setDisable(false);
    }

    @FXML
    void handleOnConfirmButtonMouseClicked(MouseEvent mouseEvent) {
        if (examplesRadioButton.isSelected()) {
            if (example1RadioButton.isSelected()) MainSceneController.chosenFileValue = 1;
            if (example2RadioButton.isSelected()) MainSceneController.chosenFileValue = 2;
            if (example3RadioButton.isSelected()) MainSceneController.chosenFileValue = 3;
        } else {
            // The filesystem radio button MUST be selected then
            MainSceneController.chosenFileValue = 0;
            MainSceneController.chosenFile = chosenFile;
        }
        MainSceneController.popup.close();
    }

    @FXML
    void handleOnExample1ButtonMouseClicked(MouseEvent mouseEvent) {
        confirmButton.setDisable(false);
    }

    @FXML
    void handleOnExample2ButtonMouseClicked(MouseEvent mouseEvent) {
        confirmButton.setDisable(false);
    }

    @FXML
    void handleOnExample3ButtonMouseClicked(MouseEvent mouseEvent) {
        confirmButton.setDisable(false);
    }
}
