package Windowing.front.controllers;

import Windowing.back.segmentfile.FormatException;
import Windowing.back.segmentfile.SegmentFileReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainSceneController extends Controller {
    @FXML
    Button readSegmentFileButton;


    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) {
        try {
            System.out.println(SegmentFileReader.readLines("segments1"));
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
    }
}
