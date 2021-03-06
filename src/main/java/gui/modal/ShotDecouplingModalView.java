package gui.modal;

import data.CameraShot;
import gui.root.RootPane;
import gui.styling.StyledButton;
import lombok.Getter;

/**
 * Class responsible for displaying a popup to confirm that a CameraShot should
 * separate from it's DirectorShot.
 */
public class ShotDecouplingModalView extends ButtonsOnlyModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 350 work very, very well.
    private static final int width = 650;
    private static final int height = 200;

    /*
     * Other variables
     */

    private CameraShot cameraShot;
    @Getter
    private StyledButton cancelButton;
    @Getter
    private StyledButton confirmButton;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param shot shot to be separated
     */
    public ShotDecouplingModalView(RootPane rootPane, CameraShot shot) {
        this(rootPane, width, height, shot);
    }

    /**
     * Constructor.
     *
     * @param rootPane root pane on top of which to display the modal.
     * @param width    width of the modal window.
     * @param height   height of the modal window.
     * @param shot     shot to be separated
     */
    public ShotDecouplingModalView(RootPane rootPane,
                                   int width,
                                   int height,
                                   CameraShot shot) {
        super(rootPane, width, height);
        this.cameraShot = shot;
        initialize();
    }

    /**
     * Initializes all content of this modal.
     */
    private void initialize() {
        titleLabel.setText("Are you sure you want to separate "
                + this.cameraShot.getName() + " from it's director shot?");

        cancelButton = createButton("Cancel", true);
        confirmButton = createButton("Confirm", true);

        buttonPane.getChildren().addAll(confirmButton, cancelButton);
    }

}
