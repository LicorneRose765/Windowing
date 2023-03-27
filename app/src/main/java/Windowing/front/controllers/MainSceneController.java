package Windowing.front.controllers;

import Windowing.back.segment.*;
import Windowing.datastructure.Window;
import Windowing.front.scenes.SceneLoader;
import Windowing.front.scenes.Scenes;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.UnaryOperator;

public class MainSceneController extends Controller {
    @FXML
    TextField xMinTextField, xMaxTextField, yMinTextField, yMaxTextField;
    @FXML
    TextFormatter<String> xMinIntegerTextFormatter, xMaxIntegerTextFormatter, yMinIntegerTextFormatter, yMaxIntegerTextFormatter; // not sure about <String>
    @FXML
    Group segmentsGroup;
    @FXML
    StackPane lowerContainer, limiter, background, segmentsContainer;
    @FXML
    Button readSegmentFileButton, linuxButton;

    private static final double MAX_WIDTH = 1250, MAX_HEIGHT = 650;

    public static Stage popup; // TODO : this code sucks
    public static File chosenFile;
    /**
     * 0 : the chosen file is the chosenFile object of type File
     * 1 : the chosen file is example 1
     * 2 : the chosen file is example 2
     * 3 : the chosen file is example 3
     */
    public static int chosenFileValue;

    private SegmentFileData currentFileData;
    private Windowing windowing;

    @FXML
    void initialize() {
        // omg dynamic javafx sucks
        background.prefWidthProperty().bind(limiter.prefWidthProperty());
        background.prefHeightProperty().bind(limiter.prefHeightProperty());
        segmentsContainer.prefWidthProperty().bind(background.prefWidthProperty());
        segmentsContainer.prefHeightProperty().bind(background.prefHeightProperty());

        UnaryOperator<TextFormatter.Change> formatter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*")) {
                return change;
            } else {
                return null;
            }
        };
        xMinIntegerTextFormatter = new TextFormatter<>(formatter);
        xMaxIntegerTextFormatter = new TextFormatter<>(formatter);
        yMinIntegerTextFormatter = new TextFormatter<>(formatter);
        yMaxIntegerTextFormatter = new TextFormatter<>(formatter);

        xMinTextField.setTextFormatter(xMinIntegerTextFormatter);
        xMaxTextField.setTextFormatter(xMaxIntegerTextFormatter);
        yMinTextField.setTextFormatter(yMinIntegerTextFormatter);
        yMaxTextField.setTextFormatter(yMaxIntegerTextFormatter);
    }

    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) throws IOException, URISyntaxException, FormatException {
        openFileLoaderPopup();

        switch (chosenFileValue) {
            case 0:
                try {
                    currentFileData = SegmentFileReader.readLines(chosenFile.toURI());
                } catch (FormatException e) {
                    // TODO : show feedback to the user
                    e.printStackTrace();
                }
                break;
            case 1:
                currentFileData = SegmentFileReader.readLines("segments1.seg");
                break;
            case 2:
                currentFileData = SegmentFileReader.readLines("segments2.seg");
                break;
            case 3:
                currentFileData = SegmentFileReader.readLines("segments3.seg");
                break;
        }
        assert currentFileData != null;

        xMinTextField.setPromptText("xMin (" + (int) currentFileData.getWindow().getXMin() + ")");
        xMaxTextField.setPromptText("xMax (" + (int) currentFileData.getWindow().getXMax() + ")");
        yMinTextField.setPromptText("yMin (" + (int) currentFileData.getWindow().getYMin() + ")");
        yMaxTextField.setPromptText("yMax (" + (int) currentFileData.getWindow().getYMax() + ")");
        drawSegments(new Windowing(currentFileData.getSegments()), currentFileData.getWindow());
    }

    @FXML
    void handleLinuxButtonMouseClicked(MouseEvent mouseEvent) {
        drawSegments(new Windowing(currentFileData.getSegments()), extractWindow());
        xMinTextField.setPromptText("xMin (" + xMinTextField.getText() + ")");
        xMinTextField.setText("");
        xMaxTextField.setPromptText("xMax (" + xMaxTextField.getText() + ")");
        xMaxTextField.setText("");
        yMinTextField.setPromptText("yMin (" + yMinTextField.getText() + ")");
        yMinTextField.setText("");
        yMaxTextField.setPromptText("yMax (" + yMaxTextField.getText() + ")");
        yMaxTextField.setText("");
    }

    /**
     * @return A <code>Window</code> object with the values of the text fields.
     */
    private Window extractWindow() {
        String xMinText = xMinTextField.getText();
        String xMaxText = xMaxTextField.getText();
        String yMinText = yMinTextField.getText();
        String yMaxText = yMaxTextField.getText();

        // TODO : make this unbounded window
        if (xMinText.equals("")) xMinText = "0";
        if (xMaxText.equals("")) xMaxText = "0";
        if (yMinText.equals("")) yMinText = "0";
        if (yMaxText.equals("")) yMaxText = "0";

        return new Window(
                Integer.parseInt(xMinText),
                Integer.parseInt(xMaxText),
                Integer.parseInt(yMinText),
                Integer.parseInt(yMaxText));
    }

    /**
     * Removes any existing segments and draws the segments from the given file data after querying it with the given window.
     * @param windowing The windowing object containing the segments.
     * @param window The window to query with.
     */
    private void drawSegments(Windowing windowing, Window window) {
        // Initially, the button is disabled, so you can't apply the window before reading a file
        // This method will be called when the user loads a file, which is when we should enable the linuxing button
        linuxButton.setDisable(false);

        segmentsGroup.getChildren().clear();
        segmentsContainer.getChildren().clear();

        for (Segment segment : windowing.query(window)) {
            segmentsGroup.getChildren().add(segment.toLine());
        }

        updateScaling();

        segmentsContainer.getChildren().add(segmentsGroup);
    }

    /**
     * Updates the scaling of the segments group to fit the display size constraints.
     */
    private void updateScaling() {
        double scaleFactor = computeScaleFactor();

        segmentsGroup.setScaleX(scaleFactor);
        segmentsGroup.setScaleY(scaleFactor);
    }

    /**
     * Computes the scale factor to apply to the segments group to fit the display size constraints.
     * @return The scale factor
     */
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

        double scaleFactor;
        if (absoluteWidth / MAX_WIDTH > absoluteHeight / MAX_HEIGHT) {
            scaleFactor = MAX_WIDTH / absoluteWidth;
        } else {
            scaleFactor = MAX_HEIGHT / absoluteHeight;
        }
        return scaleFactor;
    }

    /**
     * Opens the file loader popup so that the user can choose a file to load segments from.
     */
    private void openFileLoaderPopup() {
        popup = new Stage();
        Scenes.FileLoaderPopup = SceneLoader.load("FileLoaderPopup"); // TODO : maybe move this to main so we can load all fxml in the beginning
        popup.setScene(Scenes.FileLoaderPopup);
        popup.showAndWait();
    }
}
