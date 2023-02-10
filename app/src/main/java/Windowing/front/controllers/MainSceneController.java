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

import java.io.IOException;
import java.net.URISyntaxException;

public class MainSceneController extends Controller {
    @FXML
    Pane segmentsPane;
    @FXML
    Button readSegmentFileButton;

    public static Stage popup; // TODO : this code sucks

    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) {
        SegmentFileData fileData = null;
        openFileLoaderPopup();
        try {
            fileData = SegmentFileReader.readLines("segments1");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            // TODO : show feedback to the user
            e.printStackTrace();
            System.out.println("File is not properly formatted.");
        } catch (URISyntaxException e) {
            // TODO : show feedback to the user ?
            e.printStackTrace();
            System.out.println("File path is not properly formatted and cannot be converted from URL to URI.");
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
    //  If from resources is selected, activate three radio buttons : example1, example2, example3 to load either
    //  example file located in the resources
    //  If from file system is selected, activate a button that opens a file chooser
}
