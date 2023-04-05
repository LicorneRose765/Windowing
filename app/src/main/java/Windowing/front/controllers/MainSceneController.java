package Windowing.front.controllers;

import Windowing.back.segment.*;
import Windowing.datastructure.Window;
import Windowing.front.events.AbstractInvalidInputEvent;
import Windowing.front.events.EventsManager;
import Windowing.front.events.InvalidInputEvent;
import Windowing.front.scenes.SceneLoader;
import Windowing.front.scenes.Scenes;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

// TODO : grille

/**
 * Controller used for the `MainScene` scene defined in the `MainScene.fxml` file.
 */
public class MainSceneController extends Controller {


    /*================================================================================================================*
     *                                                                                                                *
     *                                                 FXML VARIABLES                                                 *
     *                                                                                                                *
     *================================================================================================================*/


    @FXML
    Label xMinWarningLabel, xMaxWarningLabel, yMinWarningLabel, yMaxWarningLabel;
    @FXML
    Label tooltipLabel;
    @FXML
    TextField xMinTextField, xMaxTextField, yMinTextField, yMaxTextField;
    @FXML
    Line xMinLine, xMaxLine, yMinLine, yMaxLine, windowLeftLine, windowUpLine, windowRightLine, windowDownLine;
    @FXML
    Rectangle xMinRectangle, xMaxRectangle, yMinRectangle, yMaxRectangle, windowRectangle, tooltipBackgroundRectangle;
    @FXML
    Group segmentsGroup;
    @FXML
    StackPane lowerContainer, limiter, background, segmentsContainer;
    @FXML
    Button readSegmentFileButton, linuxButton, tooltipButton;

    UnaryOperator<TextFormatter.Change> formatter = MainSceneController::apply;
    @FXML
    TextFormatter<String> xMinIntegerTextFormatter = new TextFormatter<>(formatter),
            xMaxIntegerTextFormatter = new TextFormatter<>(formatter),
            yMinIntegerTextFormatter = new TextFormatter<>(formatter),
            yMaxIntegerTextFormatter = new TextFormatter<>(formatter);


    /*================================================================================================================*
     *                                                                                                                *
     *                                                    VARIABLES                                                 *
     *                                                                                                                *
     *================================================================================================================*/


    private static final double MAX_WIDTH = 1250, MAX_HEIGHT = 650;
    public static Stage popup; // TODO : this code sucks
    public static File chosenFile;
    /**
     * 0 : the chosen file is the {@link #chosenFile} object of type {@link File}
     * 1 : the chosen file is example 1
     * 2 : the chosen file is example 2
     * 3 : the chosen file is example 3
     */
    public static int chosenFileValue;
    private SegmentFileData currentFileData;
    /**
     * This value represents how much the user is actually able to zoom in or out. A value of 4 means that the user can
     * zoom out 4 times less than the initial scale. Reminder : the user cannot zoom in more than the initial scale.
     */
    private final double SCALE_LIMIT = 10;
    /**
     * The step used for zooming. A value of 1.05 means that the user will zoom in or out by 5% each time.
     * We multiply the current scaling of the segments by this value for zooming in, and divide for zooming out.
     */
    private final double SCALE_STEP = 1.05;

    private Windowing windowing;
    /**
     * Represents the initial scale of the loaded segments. Depending on their actual coordinates, they will need to be
     * scaled to fit the area. This initial scaling value is used to determine the new maximum and minimum scale factor
     * that can be applied to the segments. For comfort, we keep this initialScale in mind as being the maximum scale :
     * the most zoomed in we could be. Then, we divide this value by 1.5 so that the segments are not instantly occupying
     * the entire area.
     */
    private double maxScale;

    private double mouseDragStartX, mouseDragStartY;
    private double initialTranslateX, initialTranslateY;

    EventHandlers eventHandlers = new EventHandlers();


    /*================================================================================================================*
     *                                                                                                                *
     *                                                     CLASSES                                                    *
     *                                                                                                                *
     *================================================================================================================*/


    private class EventHandlers {
        /**
         * Anonymous class defining the handler used when the <code>xMinLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> xMinLineInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                xMinLine.setStroke(Color.rgb(200, 0, 0));
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>xMaxLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> xMaxLineInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                xMaxLine.setStroke(Color.rgb(200, 0, 0));
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>yMinLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> yMinLineInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                yMinLine.setStroke(Color.rgb(200, 0, 0));
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>yMaxLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> yMaxLineInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                yMaxLine.setStroke(Color.rgb(200, 0, 0));
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>xMinLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> xMinWarningLabelInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                xMinWarningLabel.setVisible(true);
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>xMaxLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> xMaxWarningLabelInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                xMaxWarningLabel.setVisible(true);
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>yMinLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> yMinWarningLabelInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                yMinWarningLabel.setVisible(true);
            }
        };
        /**
         * Anonymous class defining the handler used when the <code>yMaxLine</code> object receives an {@link InvalidInputEvent}.
         */
        EventHandler<AbstractInvalidInputEvent> yMaxWarningLabelInvalidInputEventHandler = new EventHandler<>() {
            @Override
            public void handle(AbstractInvalidInputEvent event) {
                yMaxWarningLabel.setVisible(true);
            }
        };
    }


    /*================================================================================================================*
     *                                                                                                                *
     *                                                   FXML METHODS                                                 *
     *                                                                                                                *
     *================================================================================================================*/


    @FXML
    void initialize() {
        // omg dynamic javafx sucks
        xMinTextField.setTextFormatter(xMinIntegerTextFormatter);
        xMaxTextField.setTextFormatter(xMaxIntegerTextFormatter);
        yMinTextField.setTextFormatter(yMinIntegerTextFormatter);
        yMaxTextField.setTextFormatter(yMaxIntegerTextFormatter);

        xMinLine.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.xMinLineInvalidInputEventHandler);
        xMaxLine.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.xMaxLineInvalidInputEventHandler);
        yMinLine.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.yMinLineInvalidInputEventHandler);
        yMaxLine.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.yMaxLineInvalidInputEventHandler);

        xMinWarningLabel.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.xMinWarningLabelInvalidInputEventHandler);
        xMaxWarningLabel.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.xMaxWarningLabelInvalidInputEventHandler);
        yMinWarningLabel.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.yMinWarningLabelInvalidInputEventHandler);
        yMaxWarningLabel.addEventHandler(InvalidInputEvent.INVALID_VALUE, eventHandlers.yMaxWarningLabelInvalidInputEventHandler);

        tooltipLabel.setText("""
                Buttons :
                      o   Load : load a segments file (must be properly formatted !)
                      o   Linux : apply the given window (see below) to the currently loaded segments

                Window input :
                      o   xMin, xMax, yMin, yMax = coordinates of the window
                      Accepted values :     o   positive or negative integers,
                                            o   -inf or inf for unbounded windows
                      Don't forget that conditions xMin < xMax and yMin < yMax must be met.

                Controls :
                      o   Drag with left click : move the segments
                      o   Right click : center the segments""");

        // TODO : remove this as well (it's for speed load)
        try {
            handleReadSegmentFileButtonMouseClicked(null);
        } catch (Exception ignored) {}
    }

    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) throws IOException, URISyntaxException, FormatException {
        // TODO : uncomment this and remove the last line : currentFileData = SegmentFileReader.readLines("segments1.seg");
        //  it's only there to speed load the segments1
        /*
        openFileLoaderPopup();

        switch (chosenFileValue) {
            case 0:
                try {
                    // TIME LOGGER
                    long startl = System.currentTimeMillis();
                    currentFileData = SegmentFileReader.readLines(chosenFile.toURI());
                    long endl = System.currentTimeMillis();
                    System.out.println("file read in" + " [" + (endl - startl) + "]");
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
        */
        currentFileData = SegmentFileReader.readLines("segments1.seg");

        assert currentFileData != null;

        xMinTextField.setPromptText("xMin (" + (int) currentFileData.getWindow().getXMin() + ")");
        xMaxTextField.setPromptText("xMax (" + (int) currentFileData.getWindow().getXMax() + ")");
        yMinTextField.setPromptText("yMin (" + (int) currentFileData.getWindow().getYMin() + ")");
        yMaxTextField.setPromptText("yMax (" + (int) currentFileData.getWindow().getYMax() + ")");
        windowing = new Windowing(currentFileData.getSegments());

        // TIME LOGGER
        long startl = System.currentTimeMillis();
        drawSegmentsAndWindow(windowing, currentFileData.getWindow(), false);
        long endl = System.currentTimeMillis();
        // System.out.println("drawn in" + " [" + (endl - startl) + "]");

        initialTranslateX = segmentsGroup.getLayoutX();
        initialTranslateY = segmentsGroup.getLayoutY();
    }

    @FXML
    void handleLinuxButtonMouseClicked(MouseEvent mouseEvent) {
        // Reset in case they are already red because of previous bad input
        xMinLine.setStroke(Color.rgb(0, 0, 0));
        xMaxLine.setStroke(Color.rgb(0, 0, 0));
        yMinLine.setStroke(Color.rgb(0, 0, 0));
        yMaxLine.setStroke(Color.rgb(0, 0, 0));

        xMinWarningLabel.setVisible(false);
        xMaxWarningLabel.setVisible(false);
        yMinWarningLabel.setVisible(false);
        yMaxWarningLabel.setVisible(false);

        xMinRectangle.setVisible(false);
        xMaxRectangle.setVisible(false);
        yMinRectangle.setVisible(false);
        yMaxRectangle.setVisible(false);

        Window extractedWindow = extractWindow();
        if (extractedWindow == null)
            return; // happens if any of the component is null which happens in case of bad input (and an InvalidInputEvent is fired)
        drawSegmentsAndWindow(windowing, extractedWindow, true);
        xMinTextField.setPromptText("xMin (" + xMinTextField.getText() + ")");
        xMinTextField.setText("");
        xMaxTextField.setPromptText("xMax (" + xMaxTextField.getText() + ")");
        xMaxTextField.setText("");
        yMinTextField.setPromptText("yMin (" + yMinTextField.getText() + ")");
        yMinTextField.setText("");
        yMaxTextField.setPromptText("yMax (" + yMaxTextField.getText() + ")");
        yMaxTextField.setText("");
    }

    @FXML
    void handleSegmentsContainerOnScroll(ScrollEvent scrollEvent) {
        if (scrollEvent.getDeltaY() > 0) {
            double resultingScale = segmentsGroup.getScaleX() * SCALE_STEP;
            // condition to be able to zoom in : we must not have a scale factor greater than the initial scale factor
            // (otherwise, the segments will be bigger than the window)
            // if (resultingScale > maxScale * 0.98) return; // * 0.98 so that the drawn window remains in the bounds
            segmentsGroup.setScaleX(resultingScale);
            segmentsGroup.setScaleY(resultingScale);
        } else if (scrollEvent.getDeltaY() < 0) {
            double resultingScale = segmentsGroup.getScaleX() / SCALE_STEP;
            // condition to be able to zoom out : we must not have a scale factor smaller than the initial scale factor
            // divided by the max scale factor. This restriction is set in the SCALE_LIMIT value
            // if (resultingScale < maxScale / SCALE_LIMIT) return;
            segmentsGroup.setScaleX(resultingScale);
            segmentsGroup.setScaleY(resultingScale);
        }
    }


    @FXML
    void handleSegmentsContainerOnMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            mouseDragStartX = mouseEvent.getSceneX();
            mouseDragStartY = mouseEvent.getSceneY();
            mouseEvent.consume();
        }
    }

    @FXML
    void handleSegmentsContainerOnMouseReleased(MouseEvent mouseEvent) {
        // TODO : implement a minX/Y & maxX/Y when querying, return it somehow because we need it a lot (here for ex)
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            double mouseDragEndX = mouseEvent.getSceneX();
            double mouseDragEndY = mouseEvent.getSceneY();
            mouseEvent.consume();

            double deltaX = mouseDragEndX - mouseDragStartX;
            double deltaY = mouseDragEndY - mouseDragStartY;

            segmentsGroup.setTranslateX(segmentsGroup.getTranslateX() + deltaX);
            segmentsGroup.setTranslateY(segmentsGroup.getTranslateY() + deltaY);
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            segmentsGroup.setTranslateX(initialTranslateX);
            segmentsGroup.setTranslateY(initialTranslateY);
        }
    }

    @FXML
    void handleTooltipButtonOnMouseEntered(MouseEvent mouseEvent) {
        tooltipBackgroundRectangle.setWidth(tooltipLabel.getWidth());
        tooltipBackgroundRectangle.setHeight(tooltipLabel.getHeight());
        tooltipLabel.setVisible(true);
        tooltipBackgroundRectangle.setVisible(true);

    }

    @FXML
    void handleTooltipButtonOnMouseExited(MouseEvent mouseEvent) {
        tooltipLabel.setVisible(false);
        tooltipBackgroundRectangle.setVisible(false);
    }

    @FXML
    private static TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.matches("-?((\\d*)|i|in|inf)")) {
            return change;
        } else {
            return null;
        }
    }


    /*================================================================================================================*
     *                                                                                                                *
     *                                                     METHODS                                                    *
     *                                                                                                                *
     *================================================================================================================*/


    private void updateWindowRectangle(Window window) {
        Point2D lowerLeftCorner = new Point2D(window.getXMin() - 1.5, - window.getYMin() + 1.5),
                upperLeftCorner = new Point2D(window.getXMin() - 1.5, - window.getYMax() - 1.5),
                upperRightCorner = new Point2D(window.getXMax() + 1.5, - window.getYMax() - 1.5),
                lowerRightCorner = new Point2D(window.getXMax() + 1.5, - window.getYMin() + 1.5);

        if (window.getXMin() != Double.NEGATIVE_INFINITY) {
            windowLeftLine.setStartX(lowerLeftCorner.getX());
            windowLeftLine.setStartY(lowerLeftCorner.getY());
            windowLeftLine.setEndX(upperLeftCorner.getX());
            windowLeftLine.setEndY(upperLeftCorner.getY());
        } else {
            windowLeftLine = null;
        }

        if (window.getXMax() != Double.POSITIVE_INFINITY) {
            windowRightLine.setStartX(upperRightCorner.getX());
            windowRightLine.setStartY(upperRightCorner.getY());
            windowRightLine.setEndX(lowerRightCorner.getX());
            windowRightLine.setEndY(lowerRightCorner.getY());
        } else {
            windowRightLine = null;
        }

        if (window.getYMin() != Double.NEGATIVE_INFINITY) {
            windowDownLine.setStartX(lowerRightCorner.getX());
            windowDownLine.setStartY(lowerRightCorner.getY());
            windowDownLine.setEndX(lowerLeftCorner.getX());
            windowDownLine.setEndY(lowerLeftCorner.getY());
        } else {
            windowDownLine = null;
        }

        if (window.getYMax() != Double.NEGATIVE_INFINITY) {
            windowUpLine.setStartX(upperLeftCorner.getX());
            windowUpLine.setStartY(upperLeftCorner.getY());
            windowUpLine.setEndX(upperRightCorner.getX());
            windowUpLine.setEndY(upperRightCorner.getY());
        } else {
            windowUpLine = null;
        }

        if (windowLeftLine != null) {
            segmentsGroup.getChildren().add(windowLeftLine);
            System.out.println("a");
        }
        if (windowUpLine != null) {
            segmentsGroup.getChildren().add(windowUpLine);
            System.out.println("b");
        }
        if (windowRightLine != null) {
            segmentsGroup.getChildren().add(windowRightLine);
            System.out.println("c");
        }
        if (windowDownLine != null) {
            segmentsGroup.getChildren().add(windowDownLine);
            System.out.println("d");
        }

        /*
        windowRectangle.setX(window.getXMin() - 1.5);
        windowRectangle.setY(-window.getYMax() - 1.5);
        windowRectangle.setWidth(window.getXMax() - window.getXMin() + 3);
        windowRectangle.setHeight(window.getYMax() - window.getYMin() + 3);
         */
    }

    /**
     * Valid inputs must match the following regex : <code>-?(\d*|inf)</code>. If the input is empty, fires an {@link InvalidInputEvent}
     * to the given <code>userFeedbackNodes</code>. This serves as user feedback and indicates which field should be modified.
     *
     * @param s The input to extract the Double value from
     * @param userFeedbackNodes Nodes which will receive an {@link InvalidInputEvent} if the input is invalid.
     * @return The value of the input as a Double
     */
    private Double extractValue(String s, Node... userFeedbackNodes) {
        if (s.equals("")) {
            for (Node node : userFeedbackNodes) EventsManager.sendEventTo(new InvalidInputEvent(), node);
            return null;
        }
        if (s.startsWith("-")) {
            if (s.substring(1).equals("inf")) return Double.NEGATIVE_INFINITY;
        }
        if (s.equals("inf")) return Double.POSITIVE_INFINITY;
        return Double.parseDouble(s);
    }

    /**
     * @return A {@link Window} object with the values of the text fields.
     */
    private Window extractWindow() {
        boolean isInputWindowValid = true;

        String xMinText = xMinTextField.getText();
        String xMaxText = xMaxTextField.getText();
        String yMinText = yMinTextField.getText();
        String yMaxText = yMaxTextField.getText();

        // Calling extractValue() will fire events to the given feedback node in case of empty input
        Double xMin = extractValue(xMinText, xMinLine, xMinWarningLabel);
        Double xMax = extractValue(xMaxText, xMaxLine, xMaxWarningLabel);
        Double yMin = extractValue(yMinText, yMinLine, yMinWarningLabel);
        Double yMax = extractValue(yMaxText, yMaxLine, yMaxWarningLabel);

        if (xMin == null) isInputWindowValid = false;
        if (xMax == null) isInputWindowValid = false;
        if (yMin == null) isInputWindowValid = false;
        if (yMax == null) isInputWindowValid = false;

        // Make sure that xMin < xMax
        if (xMin != null && xMax != null) {
            if (xMin >= xMax) {
                EventsManager.sendEventToAll(new InvalidInputEvent(), xMinLine, xMaxLine, xMinWarningLabel, xMaxWarningLabel);
                isInputWindowValid = false;
            }
        }
        // Make sure that yMin < yMax
        if (yMin != null && yMax != null) {
            if (yMin >= yMax) {
                EventsManager.sendEventToAll(new InvalidInputEvent(), yMinLine, yMaxLine, yMinWarningLabel, yMaxWarningLabel);
                isInputWindowValid = false;
            }
        }
        if (!isInputWindowValid) return null;

        return new Window(xMin, xMax, yMin, yMax);
    }

    /**
     * Removes any existing segments and draws the segments from the given file data after querying it with the given <code>window</code>.
     *
     * @param windowing The windowing object containing the segments.
     * @param window    The window to query with.
     */
    private void drawSegmentsAndWindow(Windowing windowing, Window window, boolean addWithStream) {
        // Initially, the button is disabled, so you can't apply the window before reading a file
        // This method will be called when the user loads a file, which is when we should enable the linuxing button
        linuxButton.setDisable(false);

        // TIME LOGGER
        long startl = System.currentTimeMillis();
        segmentsGroup = new Group();
        long endl = System.currentTimeMillis();
        // System.out.println("segments cleared in" + " [" + (endl - startl) + "]");

        // TIME LOGGER
        startl = System.currentTimeMillis();
        segmentsContainer.getChildren().clear();
        endl = System.currentTimeMillis();
        // System.out.println("segments container cleared in" + " [" + (endl - startl) + "]");

        // TIME LOGGER
        startl = System.currentTimeMillis();
        ArrayList<Segment> segments = windowing.query(window);
        endl = System.currentTimeMillis();
        // System.out.println("query finished in " + " [" + (endl - startl) + "]");

        // TIME LOGGER
        startl = System.currentTimeMillis();
        if (addWithStream) {
            // System.out.println("adding with stream");
            segmentsGroup.getChildren().addAll(segments.stream().map(Segment::toLine).toList());
        } else {
            // System.out.println("adding without stream");
            for (Segment segment : segments) {
                segmentsGroup.getChildren().add(segment.toLine());
            }
        }
        endl = System.currentTimeMillis();
        // System.out.println("segments added in" + " [" + (endl - startl) + "]");

        // TODO : pour la window infinie : ajouter que les bords du rectangle nécessaires (infinie en x négatif = ne pas
        //  ajouter le bord gauche)
        updateWindowRectangle(window);

        // segmentsGroup.getChildren().addAll(windowLeftLine, windowDownLine, windowRightLine, windowUpLine);

        updateScaling();

        segmentsContainer.getChildren().add(segmentsGroup);
    }

    /**
     * Updates the scaling of the segments group to fit the display size constraints.
     */
    private void updateScaling() {
        double scaleFactor = computeScaleFactor();
        maxScale = scaleFactor;

        // Divide by 1.5 as explained in the javadoc of the maxScale field for visual comfort
        segmentsGroup.setScaleX(scaleFactor / 1.5);
        segmentsGroup.setScaleY(scaleFactor / 1.5);
    }

    /**
     * Computes the scale factor to apply to the segments group to fit the display size constraints.
     *
     * @return The scale factor
     */
    private double computeScaleFactor() {
        double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
        for (Node node : segmentsGroup.getChildren()) {
            if (node instanceof Rectangle) continue;

            Line line = (Line) node;
            // Exclude window bounds in case because if they are -inf or inf, the scale factor will be 0
            if (line.getStartX() == Double.NEGATIVE_INFINITY) continue;
            if (line.getEndX() == Double.POSITIVE_INFINITY) continue;
            if (line.getStartY() == Double.NEGATIVE_INFINITY) continue;
            if (line.getEndY() == Double.POSITIVE_INFINITY) continue;

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
