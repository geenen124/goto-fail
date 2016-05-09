package control;

import data.ScriptingProject;
import gui.centerarea.ShotBlock;
import gui.root.RootPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Class wrapper for model management controllers.
 * @author alex
 */
@Log4j2
public class ControllerManager {

    @Getter
    private RootPane rootPane;

    @Getter
    private TimelineController timelineControl;

    @Getter
    private ToolViewController toolViewController;

    @Getter
    private DetailViewController detailViewController;

    @Getter
    private ShotBlock activeShotBlock;

    // Placeholder project in lieu of XML loading
    @Getter @Setter
    private ScriptingProject scriptingProject = new ScriptingProject("BOSS Project", 1.0);

    /**
     * Constructor.
     * @param rootPane Root Window
     */
    public ControllerManager(RootPane rootPane) {
        log.debug("Initializing new ControllerManager");

        this.rootPane = rootPane;
        initializeControllers();
    }

    /**
     * Overloaded constructor to directly pass controllers.
     * @param rootPane - the root window of the application
     * @param timelineController - the controller that controls the centerarea
     * @param detailViewController - the controller that controls the detailview
     * @param toolViewController  - the controller that controls the toolview
     */
    public ControllerManager(RootPane rootPane, TimelineController timelineController,
                             DetailViewController detailViewController,
                             ToolViewController toolViewController) {
        log.debug("Initializing new ControllerManager");

        this.rootPane = rootPane;
        this.timelineControl = timelineController;
        this.detailViewController = detailViewController;
        this.toolViewController = toolViewController;
    }

    /**
     * Initialize all necessary controllers.
     */
    private void initializeControllers() {
        timelineControl = new TimelineController(this);
        detailViewController = new DetailViewController(this);
        toolViewController = new ToolViewController(this);
    }

    /**
     * Sets the active ShotBlock and notifies necessary controllers.
     * @param block ShotBlock to set as active
     */
    public void setActiveShotBlock(ShotBlock block) {
        this.activeShotBlock = block;
        detailViewController.activeBlockChanged();
        toolViewController.activeBlockChanged();
    }
}