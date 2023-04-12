/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Windowing;

import Windowing.front.navigation.Flow;
import Windowing.front.scenes.SceneLoader;
import Windowing.front.scenes.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestMe extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage_) {
        Scenes.MainScene = SceneLoader.load("MainScene");
        Scenes.FileLoaderPopup = SceneLoader.load("FileLoaderPopup");

        Flow.add(Scenes.MainScene);

        stage_.setTitle("Windowing app");
        stage_.setResizable(false);
        stage_.setWidth(1280);
        stage_.setHeight(750);
        stage_.setScene(Scenes.MainScene);
        stage_.show();
    }
}
