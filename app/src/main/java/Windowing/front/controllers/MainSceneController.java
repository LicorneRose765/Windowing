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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

/**
 * Controller used for the `MainScene` scene defined in the `MainScene.fxml` file.
 */
public class MainSceneController extends Controller {

    /*================================================================================================================*
     *                                                                                                                *
     *                                                 FXML VARIABLES                                                 *
     *                                                                                                                *
     *================================================================================================================*/


    private static final double MAX_WIDTH = 1250, MAX_HEIGHT = 650;
    public static Stage popup;
    public static File chosenFile;

    public static boolean modifiedChosenFile = false;
    /**
     * 0 : the chosen file is the {@link #chosenFile} object of type {@link File}
     * 1 : the chosen file is example 1
     * 2 : the chosen file is example 2
     * 3 : the chosen file is example 3
     */
    public static int chosenFileValue;
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


    /*================================================================================================================*
     *                                                                                                                *
     *                                                    VARIABLES                                                 *
     *                                                                                                                *
     *================================================================================================================*/
    @FXML
    Group segmentsGroup;
    @FXML
    StackPane lowerContainer, limiter, background, segmentsContainer;
    @FXML
    Button readSegmentFileButton, linuxButton, tooltipButton;
    @FXML
    AnchorPane header;
    UnaryOperator<TextFormatter.Change> formatter = MainSceneController::apply;
    @FXML
    TextFormatter<String> xMinIntegerTextFormatter = new TextFormatter<>(formatter),
            xMaxIntegerTextFormatter = new TextFormatter<>(formatter),
            yMinIntegerTextFormatter = new TextFormatter<>(formatter),
            yMaxIntegerTextFormatter = new TextFormatter<>(formatter);
    EventHandlers eventHandlers = new EventHandlers();
    private SegmentFileData currentFileData;
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

    private static String validInputRegex = "-?((\\d*)|i|in|inf)", validInputResultRegex = "-?((\\d*)|inf)";


    /*================================================================================================================*
     *                                                                                                                *
     *                                                     CLASSES                                                    *
     *                                                                                                                *
     *================================================================================================================*/

    @FXML
    private static TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.matches(validInputRegex)) {
            return change;
        } else {
            return null;
        }
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


        segmentsContainer.setClip(new Rectangle(1250, 650));

        tooltipLabel.setText("""
                Buttons :
                      o   Load : load a segments file (must be properly formatted !)
                      o   Apply : apply the given window (see below) to the currently loaded segments

                Window input :
                      o   xMin, xMax, yMin, yMax = coordinates of the window
                      Accepted values :     o   positive or negative integers,
                                            o   -inf or inf for unbounded windows
                      Don't forget that conditions xMin < xMax and yMin < yMax must be met.

                Controls :
                      o   Drag with left click : move the segments
                      o   Right click : center the segments""");
    }

    @FXML
    void handleReadSegmentFileButtonMouseClicked(MouseEvent mouseEvent) {
        openFileLoaderPopup();
        if (!modifiedChosenFile) {
            // If the user cancelled the file selection, we don't want to do anything
            return;
        }


        switch (chosenFileValue) {
            case 0:
                try {
                    currentFileData = SegmentFileReader.readLines(chosenFile.toURI());
                } catch (IOException ioException) {
                    ErrorSceneController.errorMessage = "File not found or not readable";
                    openErrorPopup();
                } catch (FormatException formatException) {
                    ErrorSceneController.errorMessage = "Error while parsing the file \n "
                            + formatException.getMessage();
                    openErrorPopup();
                } catch (Exception e) {
                    ErrorSceneController.errorMessage = "Error while loading the file";
                    System.out.println("[ERROR] Couldn't load "+ chosenFile +" : " + e.getMessage());
                    openErrorPopup();
                }
                break;
            case 1:
                try {
                    currentFileData = SegmentFileReader.readLines("segments1.seg");
                } catch (Exception e) {
                    ErrorSceneController.errorMessage = "Error while loading segments1.seg";
                    System.out.println("[ERROR] Couldn't load segments1.seg : " + e.getMessage());
                    openErrorPopup();
                }
                break;
            case 2:
                try {
                    currentFileData = SegmentFileReader.readLines("segments2.seg");
                } catch (Exception e) {
                    ErrorSceneController.errorMessage = "Error while loading segments2.seg";
                    System.out.println("[ERROR] Couldn't load segments2.seg : " + e.getMessage());
                    openErrorPopup();
                }
                break;
            case 3:
                try {
                    currentFileData = SegmentFileReader.readLines("segments3.seg");
                } catch (Exception e) {
                    ErrorSceneController.errorMessage = "Error while loading segments3.seg";
                    System.out.println("[ERROR] Couldn't load segments3.seg : " + e.getMessage());
                    openErrorPopup();
                }
                break;
        }

        if (currentFileData == null) {
            return;
        }

        xMinTextField.setPromptText("xMin (" + (int) currentFileData.getWindow().getXMin() + ")");
        xMaxTextField.setPromptText("xMax (" + (int) currentFileData.getWindow().getXMax() + ")");
        yMinTextField.setPromptText("yMin (" + (int) currentFileData.getWindow().getYMin() + ")");
        yMaxTextField.setPromptText("yMax (" + (int) currentFileData.getWindow().getYMax() + ")");
        windowing = new Windowing(currentFileData.getSegments());

        drawSegmentsAndWindow(windowing, currentFileData.getWindow(), false);

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
    void handleTextFieldOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLinuxButtonMouseClicked(null);
        }
    }

    /**
     * Computes the coordinates of the fours corners of the displayed window. Adapts these coordinates if the window is
     * unbounded so that the displaying is intuitive and shows that well. Draws four lines representing the window,
     * following the previously computed coordinates. If the window is unbounded on one side, do not show the line
     * corresponding to that side to emphasize on the unbounded side.
     *
     * @param window    The window to display
     * @param windowing The windowing object containing the least and greatest X and Y coordinates among all segments
     *                  retrieved from the query (corresponding to the given window)
     */
    private void updateWindowRectangle(Window window, Windowing windowing) {
        Point2D lowerLeftCorner = new Point2D(window.getXMin() - 1.5, -window.getYMin() + 1.5),
                upperLeftCorner = new Point2D(window.getXMin() - 1.5, -window.getYMax() - 1.5),
                upperRightCorner = new Point2D(window.getXMax() + 1.5, -window.getYMax() - 1.5),
                lowerRightCorner = new Point2D(window.getXMax() + 1.5, -window.getYMin() + 1.5);

        if (window.getXMin() == Double.NEGATIVE_INFINITY) {
            upperLeftCorner = new Point2D(windowing.leastX - 1.5, upperLeftCorner.getY());
            lowerLeftCorner = new Point2D(windowing.leastX - 1.5, lowerLeftCorner.getY());
        }

        if (window.getXMax() == Double.POSITIVE_INFINITY) {
            upperRightCorner = new Point2D(windowing.greatestX + 1.5, upperRightCorner.getY());
            lowerRightCorner = new Point2D(windowing.greatestX + 1.5, lowerRightCorner.getY());
        }

        if (window.getYMin() == Double.NEGATIVE_INFINITY) {
            lowerLeftCorner = new Point2D(lowerLeftCorner.getX(), -(windowing.leastY - 1.5));
            lowerRightCorner = new Point2D(lowerRightCorner.getX(), -(windowing.leastY - 1.5));
        }

        if (window.getYMax() == Double.POSITIVE_INFINITY) {
            upperLeftCorner = new Point2D(upperLeftCorner.getX(), -(windowing.greatestY + 1.5));
            upperRightCorner = new Point2D(upperRightCorner.getX(), -(windowing.greatestY + 1.5));
        }

        windowLeftLine.setStartX(lowerLeftCorner.getX());
        windowLeftLine.setEndX(upperLeftCorner.getX());
        windowLeftLine.setStartY(lowerLeftCorner.getY());
        windowLeftLine.setEndY(upperLeftCorner.getY());

        windowUpLine.setStartX(upperLeftCorner.getX());
        windowUpLine.setEndX(upperRightCorner.getX());
        windowUpLine.setStartY(upperLeftCorner.getY());
        windowUpLine.setEndY(upperRightCorner.getY());

        windowRightLine.setStartX(upperRightCorner.getX());
        windowRightLine.setEndX(lowerRightCorner.getX());
        windowRightLine.setStartY(upperRightCorner.getY());
        windowRightLine.setEndY(lowerRightCorner.getY());

        windowDownLine.setStartX(lowerRightCorner.getX());
        windowDownLine.setEndX(lowerLeftCorner.getX());
        windowDownLine.setStartY(lowerRightCorner.getY());
        windowDownLine.setEndY(lowerLeftCorner.getY());


        if (window.getXMin() != Double.NEGATIVE_INFINITY) segmentsGroup.getChildren().add(windowLeftLine);

        if (window.getXMax() != Double.POSITIVE_INFINITY) segmentsGroup.getChildren().add(windowRightLine);

        if (window.getYMin() != Double.NEGATIVE_INFINITY) segmentsGroup.getChildren().add(windowDownLine);

        if (window.getYMax() != Double.POSITIVE_INFINITY) segmentsGroup.getChildren().add(windowUpLine);
    }


    /*================================================================================================================*
     *                                                                                                                *
     *                                                     METHODS                                                    *
     *                                                                                                                *
     *================================================================================================================*/

    /**
     * Valid inputs must match the following regex : <code>-?(\d*|i|n|f)</code>. If the input is empty, fires an {@link InvalidInputEvent}
     * to the given <code>userFeedbackNodes</code>. This serves as user feedback and indicates which field should be modified.
     *
     * @param s                 The input to extract the Double value from
     * @param userFeedbackNodes Nodes which will receive an {@link InvalidInputEvent} if the input is invalid.
     * @return The value of the input as a Double
     */
    private Double extractValue(String s, Node... userFeedbackNodes) {
        if (s.equals("") || !(s.matches(validInputResultRegex))) {
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
     * @param windowing     The windowing object containing the segments.
     * @param window        The window to query with.
     * @param addWithStream Whether to add the data via streams or not. Improves performance in some cases.
     */
    private void drawSegmentsAndWindow(Windowing windowing, Window window, boolean addWithStream) {
        // Initially, the button is disabled, so you can't apply the window before reading a file
        // This method will be called when the user loads a file, which is when we should enable the linux button (apply button)
        linuxButton.setDisable(false);

        segmentsGroup = new Group();

        segmentsContainer.getChildren().clear();

        ArrayList<Segment> segments = windowing.query(window);

        if (addWithStream) {
            segmentsGroup.getChildren().addAll(segments.stream().map(Segment::toLine).toList());
        } else {
            for (Segment segment : segments) {
                segmentsGroup.getChildren().add(segment.toLine());
            }
        }

        updateWindowRectangle(window, windowing);
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
        modifiedChosenFile = false;
        popup = new Stage();
        popup.setScene(Scenes.FileLoaderPopup);
        popup.setHeight(450);
        popup.setWidth(600);
        popup.showAndWait();
    }

    private void openErrorPopup() {
        Scenes.ErrorScene = SceneLoader.load("ErrorScene");
        popup = new Stage();
        popup.setScene(Scenes.ErrorScene);
        popup.setWidth(600);
        popup.setHeight(450);
        popup.showAndWait();
    }

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
}
