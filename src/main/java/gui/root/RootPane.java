package gui.root;

import control.ControllerManager;

import gui.modal.StartupModalView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Class that represents the whole top of the gui application.
 */
@Log4j2
public class RootPane extends Application {

    private int minimumResolutionX = 640;
    private int minimumResolutionY = 480;
    private int startingResolutionX = 1280;
    private int startingResolutionY = 800;
    
    @Getter
    private static final String CONFIG_FILEPATH = "config.ini";

    @Getter
    Stage primaryStage;
    @Getter
    private BorderPane topLevelPane;
    @Getter
    private RootHeaderArea rootHeaderArea;
    @Getter
    private RootFooterArea rootFooterArea;
    @Getter
    private RootCenterArea rootCenterArea;
    @Getter
    private ControllerManager controllerManager;
    @Getter
    private StartupModalView startupModalView;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting RootPane.");
        this.primaryStage = primaryStage;
        // Create a BorderPane, a layout with 5 areas: top, bottom, left, right and center,
        // and add our views to it.
        topLevelPane = new BorderPane();
        // Create scene and set the stage. This is where the window is basically
        // created. Also has some useful settings.
        Scene scene = new Scene(topLevelPane);
        initStylesheets(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("New Project");
        primaryStage.setMinHeight(minimumResolutionY);
        primaryStage.setMinWidth(minimumResolutionX);
        primaryStage.setHeight(startingResolutionY);
        primaryStage.setWidth(startingResolutionX);
        primaryStage.centerOnScreen();

        // represents center of ui
        rootCenterArea = new RootCenterArea(this, 0, true);
        topLevelPane.setCenter(rootCenterArea);
        // represents file-view-help bar and button bars at top of gui.
        rootHeaderArea = new RootHeaderArea(this);
        topLevelPane.setTop(rootHeaderArea);
        // represents simple bar at bottom of gui.
        rootFooterArea = new RootFooterArea();
        topLevelPane.setBottom(rootFooterArea);
        // startup modal view.
        startupModalView = new StartupModalView(this);
        
        controllerManager = new ControllerManager(this);

        String recentProjectPath = readPathFromConfig();
        if (recentProjectPath != null) {
            controllerManager.getFileMenuController().load(new File(recentProjectPath));
            primaryStage.setTitle(controllerManager.getScriptingProject().getName());
        } else {
            initStartupScreen(false);
        }
    }

    /**
     * Load all main stylesheets for this scene.
     * @param scene scene to load.
     */
    private void initStylesheets(Scene scene) {
        scene.getStylesheets().add("Stylesheets/Misc.css");
        scene.getStylesheets().add("Stylesheets/StyledButton.css");
        scene.getStylesheets().add("Stylesheets/StyledCheckbox.css");
        scene.getStylesheets().add("Stylesheets/StyledTextfield.css");
        scene.getStylesheets().add("Stylesheets/StyledListview.css");
        scene.getStylesheets().add("Stylesheets/StyledMenu.css");
    }

    /**
     * Read the most recent project filepath from the config file, for auto load.
     * @return the filepath if one is found, null otherwise
     */
    private String readPathFromConfig() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(CONFIG_FILEPATH));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Forces reload of root center area.
     * @param area the rootcenterarea to load.
     */
    public void reInitRootCenterArea(RootCenterArea area) {
        topLevelPane.setCenter(area);
        rootCenterArea = area;
        primaryStage.show();
    }

    /**
     * Forces load of startup screen.
     * @param loadFailed whether to display a load failure message or not.
     */
    public void initStartupScreen(boolean loadFailed) {
        if (loadFailed) {
            startupModalView.setLoadFailed();
        }
        startupModalView.displayModal();
    }

    /**
     * Removes startup screen.
     */
    public void closeStartupScreen() {
        startupModalView.hideModal();
    }
}
