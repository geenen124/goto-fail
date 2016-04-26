package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class that represents the whole top of the gui application.
 */
public class RootPane extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create a BorderPane (a layout with 5 areas: top, bottom, left, right and center)
        // and add our views to it.

        BorderPane topLevelPane = new BorderPane();

        TopPane topPane = new TopPane();
        topLevelPane.setTop(topPane);

        BottomPane bottomPane = new BottomPane();
        topLevelPane.setBottom(bottomPane);

        // Create scene and set the stage. This is where the window is basically
        // created. Also has some useful settings.

        primaryStage.setScene(new Scene(topLevelPane)); // start resolution of application (keep low?)
        primaryStage.setTitle("Hoi ben een titel lol.");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
