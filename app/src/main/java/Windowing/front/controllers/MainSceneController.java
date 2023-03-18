package Windowing.front.controllers;

import Windowing.back.geometry.CoordinateConverter;
import Windowing.back.segmentfile.*;
import Windowing.datastructure.Window;
import Windowing.front.scenes.SceneLoader;
import Windowing.front.scenes.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Group;
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
    StackPane segmentsContainer;
    @FXML
    Button readSegmentFileButton;
    private static final double MAX_WIDTH = 1260, MAX_HEIGHT = 650, CONTAINER_WIDTH = 1280, CONTAINER_HEIGHT = 670;

    public static Stage popup; // TODO : this code sucks
    public static File chosenFile;
    /**
     * 0 : the chosen file is the chosenFile object of type File
     * 1 : the chosen file is example 1
     * 2 : the chosen file is example 2
     * 3 : the chosen file is example 3
     */
    public static int chosenFileInt;

    public void initialize() {

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

        // updateSegmentsPane(-100, 300, -400, 100);

        drawSegments(fileData, fileData.getWindow());
    }

    /**
     * Basically a bunch of geometry tricks along these lines :
     *  1. Define the function that converts a coordinate on the segments plane into a coordinate on the displayed pane
     *  2. Resize the segments pane in order to utilize as much area of the screen as possible
     *  3. Move the segments pane to the center of the screen for *beauty*
     * @param xMin The x coordinate of the leftmost point of the segments plane (the lowest x coordinate among all points)
     * @param xMax The x coordinate of the rightmost point of the segments plane (the highest x coordinate among all points)
     * @param yMin The y coordinate of the uppermost point of the segments plane (the lowest y coordinate among all points)
     * @param yMax The y coordinate of the lowermost point of the segments plane (the highest y coordinate among all points)
     // TODO WARNING : THIS ASSUMES THAT THE SEGMENTS PLANE'S AXIS ARE DEFINED AS THE DISPLAY'S AXIS (HIGHER = LOWER Y)
     */
    private void updateSegmentsPane(double xMin, double xMax, double yMin, double yMax) {
        double absoluteWidth = xMax - xMin, absoluteHeight = yMax - yMin;
        CoordinateConverter converter = new CoordinateConverter(xMin, xMax, yMin, yMax, MAX_WIDTH, MAX_HEIGHT);
        System.out.println("converter.isHeightRestricting() = " + converter.isHeightRestricting()); // TODO ?????
        if (converter.isHeightRestricting()) {
            // pane has size (width/height * maxheight) x maxheight (width/height < 0, a fraction of maxheight)
            System.out.println("absoluteWidth / absoluteHeight = " + absoluteWidth / absoluteHeight);
            segmentsContainer.setPrefSize(absoluteWidth / absoluteHeight * MAX_HEIGHT, MAX_HEIGHT);
            // move the pane on the x-axis
            segmentsContainer.setLayoutX(MAX_WIDTH / 2 - segmentsContainer.getPrefWidth() / 2);
        } else {
            // pane has size maxwidth x (height/width * maxwidth) (height/width < 0, a fraction of maxwidth
            segmentsContainer.setPrefSize(MAX_WIDTH, absoluteHeight / absoluteWidth * MAX_WIDTH);
            // move the pane on the y-axis
            // not really working ?
            segmentsContainer.setLayoutY(MAX_HEIGHT / 2 - segmentsContainer.getPrefHeight() / 2);
        }

        // TODO : this should be working but it is not, I think xCompute and yCompute do not return the right coordinates
        System.out.println("segmentsPane.getPrefWidth() = " + segmentsContainer.getPrefWidth());
        System.out.println("segmentsPane.getPrefHeight() = " + segmentsContainer.getPrefHeight());
        Line line = new Line(converter.xConvert(-100), converter.yConvert(300), converter.xConvert(-400), converter.yConvert(100));
        line.setStrokeWidth(5);
        System.out.println("line = " + line);
        segmentsContainer.getChildren().add(line);
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

        segmentsContainer.getChildren().add(segmentsGroup);
    }

    private void updateSegmentsPanePosition() {
        segmentsContainer.setLayoutX(MAX_WIDTH / 2 - segmentsContainer.getPrefWidth() / 2);
        segmentsContainer.setLayoutY(MAX_HEIGHT / 2 - segmentsContainer.getPrefHeight() / 2);
    }

    private void openFileLoaderPopup() {
        popup = new Stage();
        Scenes.FileLoaderPopup = SceneLoader.load("FileLoaderPopup"); // TODO : maybe move this to main so we can load all fxml in the beginning
        popup.setScene(Scenes.FileLoaderPopup);
        popup.showAndWait();
    }
}
