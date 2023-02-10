package Windowing.front.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
    }

    @FXML
    void handleOnConfirmButtonMouseClicked(MouseEvent mouseEvent) {
        MainSceneController.popup.close();
        // TODO : set the file in a static var or something
        //  also we can prevent closing via "confirm" if no file is selected (only allow closing via closing button)
    }
}
