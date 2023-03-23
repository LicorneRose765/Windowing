package Windowing.front.controllers;

import Windowing.back.segment.*;
import Windowing.datastructure.Window;
import Windowing.front.scenes.SceneLoader;
import Windowing.front.scenes.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainSceneController extends Controller {
    @FXML
    Group segmentsGroup;
    @FXML
    StackPane lowerContainer, limiter, background, segmentsContainer;
    @FXML
    Button readSegmentFileButton;
    private static final double MAX_WIDTH = 1260, MAX_HEIGHT = 660;

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
    void initialize() {
        // omg dynamic javafx sucks
        background.prefWidthProperty().bind(limiter.prefWidthProperty());
        background.prefHeightProperty().bind(limiter.prefHeightProperty());
        segmentsContainer.prefWidthProperty().bind(background.prefWidthProperty());
        segmentsContainer.prefHeightProperty().bind(background.prefHeightProperty());
    }

    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) throws IOException, URISyntaxException, FormatException {
        SegmentFileData fileData = null;
        openFileLoaderPopup();

        switch (chosenFileInt) {
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
                fileData = SegmentFileReader.readLines("segments1.seg");
                break;
            case 2:
                fileData = SegmentFileReader.readLines("segments2.seg");
                break;
            case 3:
                fileData = SegmentFileReader.readLines("segments3.seg");
                break;
        }
        assert fileData != null;

        drawSegments(fileData, fileData.getWindow());
    }

    private void drawSegments(SegmentFileData fileData, Window window) {
        segmentsGroup.getChildren().clear();
        segmentsContainer.getChildren().clear();

        Line l = new Line(-200, -200, 200, -200);
        l.setStroke(Color.RED);
        segmentsGroup.getChildren().add(l);
        l = new Line(-200, -100, 200, -100);
        l.setStroke(Color.GREEN);
        segmentsGroup.getChildren().add(l);
        l = new Line(-200, 0, 200, 0);
        l.setStroke(Color.BLUE);
        segmentsGroup.getChildren().add(l);
        l = new Line(-200, 100, 200, 100);
        l.setStroke(Color.GOLD);
        segmentsGroup.getChildren().add(l);
        l = new Line(-200, 200, 200, 200);
        l.setStroke(Color.ORANGE);
        segmentsGroup.getChildren().add(l);
        l = new Line(-100, -200, -100, 200);
        l.setStroke(Color.CYAN);
        segmentsGroup.getChildren().add(l);
        l = new Line(100, -200, 100, 200);
        l.setStroke(Color.DARKGREEN);
        segmentsGroup.getChildren().add(l);

        // for (Point point : fileData.getPST().query(window)) {
        //     segmentsGroup.getChildren().add(point.toLine());
        // }

        updateScaling();

        segmentsContainer.getChildren().add(segmentsGroup);
    }

    private void updateScaling() {
        double scaleFactor = computeScaleFactor();

        segmentsGroup.setScaleX(scaleFactor);
        segmentsGroup.setScaleY(scaleFactor);
    }

    private double computeScaleFactor() {
        double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
        for (Node node : segmentsGroup.getChildren()) {
            Line line = (Line) node;
            if (line.getStartX() < xMin) xMin = line.getStartX();
            if (line.getEndX() > xMax) xMax = line.getEndX();
            if (line.getStartY() < yMin) yMin = line.getStartY();
            if (line.getEndY() > yMax) yMax = line.getEndY();
        }
        double absoluteWidth = xMax - xMin, absoluteHeight = yMax - yMin;

        double scaleFactor = 1;
        if (absoluteWidth / MAX_WIDTH > absoluteHeight / MAX_HEIGHT) {
            scaleFactor = MAX_WIDTH / absoluteWidth;
        } else {
            scaleFactor = MAX_HEIGHT / absoluteHeight;
        }
        return scaleFactor;
    }

    private void openFileLoaderPopup() {
        popup = new Stage();
        Scenes.FileLoaderPopup = SceneLoader.load("FileLoaderPopup"); // TODO : maybe move this to main so we can load all fxml in the beginning
        popup.setScene(Scenes.FileLoaderPopup);
        popup.showAndWait();
    }
}
