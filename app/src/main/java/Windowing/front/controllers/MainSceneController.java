package Windowing.front.controllers;

import Windowing.back.segmentfile.FormatException;
import Windowing.back.segmentfile.Segment;
import Windowing.back.segmentfile.SegmentFileData;
import Windowing.back.segmentfile.SegmentFileReader;
import Windowing.front.scenes.SceneLoader;
import Windowing.front.scenes.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class MainSceneController extends Controller {
    @FXML
    Pane segmentsPane;
    @FXML
    Button readSegmentFileButton;

    public static Stage popup; // TODO : this code sucks
    public static File chosenFile;
    /**
     * 0 : the chosen file is the chosenFile object of type File
     * 1 : the chosen file is example 1
     * 2 : the chosen file is example 2
     * 3 : the chosen file is example 3
     */
    public static int chosenFileInt;

    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) throws IOException, URISyntaxException, FormatException {
        SegmentFileData fileData = null;
        openFileLoaderPopup();

        switch (chosenFileInt) {
            // throws exception in cases 1 2 and 3 because we know that the example are properly formatted
            case 0:
                try {
                    fileData = SegmentFileReader.readLines(chosenFile.toURI());
                } catch (FormatException e) {
                    // TODO : show feedback to the user
                    e.printStackTrace();
                    System.out.println("File is not properly formatted.");
                }
                break;
            case 1:
                fileData = SegmentFileReader.readLines("segments1");
                break;
            case 2:
                fileData = SegmentFileReader.readLines("segments2");
                break;
            case 3:
                fileData = SegmentFileReader.readLines("segments3");
                break;
        }
        assert fileData != null;
        segmentsPane.getChildren().clear();
        for (Segment segment : fileData.getSegments()) {
            System.out.println(segment);
            segmentsPane.getChildren().add(segment.toLine());
        }
    }

    private void openFileLoaderPopup() {
        popup = new Stage();
        Scenes.FileLoaderPopup = SceneLoader.load("FileLoaderPopup"); // TODO : maybe move this to main so we can load all fxml in the beginning
        popup.setScene(Scenes.FileLoaderPopup);
        popup.showAndWait();
    }

    // TODO : to choose a file to load :
    //  2 radio buttons :
    //     * From resources
    //     * From file system
    //  If from resources is selected, activate three radio buttons : example1, segments2, example3 to load either
    //  example file located in the resources
    //  If from file system is selected, activate a button that opens a file chooser
}
