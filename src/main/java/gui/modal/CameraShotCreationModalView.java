package gui.modal;

import gui.events.CameraShotCreationEvent;
import gui.headerarea.DoubleTextField;
import gui.root.RootPane;
import gui.styling.StyledButton;
import gui.styling.StyledCheckbox;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Class responsible for displaying a modal view for the creation of shots.
 * @author alex
 */
public class CameraShotCreationModalView extends ModalView {

    /*
     * Tweakable styling variables.
     */

    // width and height of screen. 680 and 290 work very, very well.
    private static final int width = 680;
    private static final int height = 290;

    // simple background styles of the three main areas.
    private String topStyle = "-fx-background-color: rgb(240,240,240);"
            + "-fx-text-fill: black; -fx-font-size: 20;";
    private String centerStyle = "-fx-background-color: rgb(230, 230, 230);";
    private String bottomStyle = "-fx-background-color: rgb(240, 240, 240);";

    // variables for the Create and Cancel buttons
    private int buttonWidth = 90;
    private int buttonHeight = 25;
    private Point3D createButtonColor = new Point3D(200, 200, 200);
    private Point3D cancelButtonColor = new Point3D(200, 200, 200);
    private int buttonFontSize = 16;
    private int buttonSpacing = 20;

    // color of the "active" element of a checkbox
    private Point3D checkboxColor = new Point3D(250, 120, 50);

    // variables for the title label
    private int titlelabelOffsetFromLeft = 20;

    // variables for the shadow effects
    private double softShadowRadius = 15;
    private double softShadowCutoff = 0.2;
    private double softShadowOpacity = 0.05;
    private double hardShadowRadius = 1;
    private double hardShadowCutoff = 1;
    private double hardShadowOpacity = 0.15;

    /*
     * Other variables.
     */

    // No touching these constants. They work well for all general cases,
    // and there is no reason to change them ever again.
    private static final int GENERAL_SIZE = 10000;
    private static final int GENERAL_SPACING = 10;
    private static final int GENERAL_PADDING = 20;
    private static final int TEXT_AREA_MIN_WIDTH = 350;
    private static final int CAMERA_AREA_MIN_WIDTH = 250;

    private int numberOfCameras;

    @Setter
    private String defaultStartCount = "0";
    @Setter
    private String defaultEndCount = "1";

    private VBox viewPane;
    private HBox contentPane;
    private HBox buttonPane;
    private FlowPane checkboxPane;
    private List<StyledCheckbox> cameraCheckboxes;
    private TextField descriptionField;
    private TextField nameField;
    private Label titleLabel;

    @Getter
    private StyledButton creationButton;
    @Getter
    private StyledButton cancelButton;

    @Getter
    private DoubleTextField startField;
    @Getter
    private DoubleTextField endField;

    private InnerShadow topInnerShadow;
    private InnerShadow topOuterShadow;
    private DropShadow bottomOuterShadow;

    private EventHandler<CameraShotCreationEvent> cameraShotCreationEventHandler;

    /**
     * Constructor with default modal size.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     */
    public CameraShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                       EventHandler<CameraShotCreationEvent> creationHandler) {
        this(rootPane, numberOfCamerasInTimeline, creationHandler, width, height);
    }

    /**
     * Constructor.
     * @param rootPane Pane to display modal on top of
     * @param numberOfCamerasInTimeline Amount of cameras in timeline
     * @param creationHandler Event handler for the creation of a shot
     * @param modalWidth Modal display width
     * @param modalHeight Modal display height
     */
    public CameraShotCreationModalView(RootPane rootPane, int numberOfCamerasInTimeline,
                                       EventHandler<CameraShotCreationEvent> creationHandler,
                                       int modalWidth, int modalHeight) {
        super(rootPane, modalWidth, modalHeight);
        this.numberOfCameras = numberOfCamerasInTimeline;
        this.cameraShotCreationEventHandler = creationHandler;
        initializeCreationView();
    }

    /**
     * Initialize and display the modal view.
     */
    private void initializeCreationView() {
        // force minimum size
        getModalStage().setMinWidth(width);
        getModalStage().setMinHeight(height);

        // Create a new VBox for vertical layout
        this.viewPane = new VBox();

        // Add label at top
        initTitleLabel();

        // add space for textfields and checkboxes
        this.contentPane = new HBox();
        this.contentPane.setAlignment(Pos.CENTER);
        this.contentPane.setPadding(new Insets(0, GENERAL_PADDING, 0, 0));
        this.contentPane.setPrefHeight(GENERAL_SIZE);
        this.contentPane.setSpacing(40.0);
        this.contentPane.setStyle(centerStyle);
        this.viewPane.getChildren().add(contentPane);

        // actually add textfields and checkboxes
        initTextFields();
        initCamCheckBoxes();

        // add buttons at bottom.
        initButtons();

        // once we're done, setup shadows etc.
        initEffects();

        super.setModalView(this.viewPane);
        super.displayModal();
    }

    /**
     * Initialize title label.
     */
    private void initTitleLabel() {
        titleLabel = new Label("Add a new shot...");
        titleLabel.setStyle(topStyle);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        titleLabel.setPrefWidth(GENERAL_SIZE);
        titleLabel.setPrefHeight(GENERAL_SIZE);
        this.viewPane.getChildren().add(titleLabel);
    }

    /**
     * Sets up effects and adds them to the appropriate panes.
     */
    private void initEffects() {
        topInnerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, hardShadowOpacity),
                hardShadowRadius, hardShadowCutoff, 0, -2);
        topOuterShadow = new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, softShadowOpacity),
                softShadowRadius, softShadowCutoff, 0, 1);
        bottomOuterShadow = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, softShadowOpacity),
                softShadowRadius, softShadowCutoff, 0, -1);
        titleLabel.setEffect(topInnerShadow);
        contentPane.setEffect(topOuterShadow);
        buttonPane.setEffect(bottomOuterShadow);
    }

    /**
     * Initializes pane with buttons at bottom.
     */
    private void initButtons() {
        // setup button pane
        this.buttonPane = new HBox();
        this.buttonPane.setSpacing(buttonSpacing);
        this.buttonPane.setAlignment(Pos.CENTER_LEFT);
        this.buttonPane.setPrefHeight(GENERAL_SIZE);
        this.buttonPane.setStyle(bottomStyle);
        this.buttonPane.setPadding(new Insets(0, 0, 0, titlelabelOffsetFromLeft));
        this.viewPane.getChildren().add(buttonPane);

        // Add cancel button
        cancelButton = new StyledButton("Cancel");
        cancelButton.setOnMouseReleased(e -> {
                getModalStage().close(); // kill window
            }
        );
        cancelButton.setPrefWidth(buttonWidth);
        cancelButton.setPrefHeight(buttonHeight);
        cancelButton.setFontSize(buttonFontSize);
        cancelButton.setButtonColor(createButtonColor);
        cancelButton.setAlignment(Pos.CENTER);

        // Add creation button
        creationButton = new StyledButton("Create");
        creationButton.setOnMouseReleased(this::createShot);
        creationButton.setPrefWidth(buttonWidth);
        creationButton.setPrefHeight(buttonHeight);
        creationButton.setFontSize(buttonFontSize);
        creationButton.setButtonColor(cancelButtonColor);
        creationButton.setAlignment(Pos.CENTER);

        this.buttonPane.getChildren().addAll(creationButton, cancelButton);
    }

    /**
     * Initialize all textfields, add them to a left-central VBox.
     */
    private void initTextFields() {
        VBox content = new VBox(GENERAL_SPACING);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinWidth(TEXT_AREA_MIN_WIDTH);
        content.setPrefWidth(GENERAL_SIZE);
        content.setPrefHeight(GENERAL_SIZE);
        content.setPadding(new Insets(GENERAL_PADDING));

        // init name field
        final Label nameLabel = new Label("Shot Name: ");
        nameField = new TextField();
        HBox nameBox = new HBox(GENERAL_SPACING);
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_RIGHT);

        // init description field
        final Label descripLabel = new Label("Shot Description: ");
        descriptionField = new TextField();
        HBox descripBox = new HBox(GENERAL_SPACING);
        descripBox.getChildren().addAll(descripLabel, descriptionField);
        descripBox.setAlignment(Pos.CENTER_RIGHT);

        // init start count field
        final Label startLabel = new Label("Start:");
        startField = new DoubleTextField(this.defaultStartCount);
        HBox startBox = new HBox(GENERAL_SPACING);
        startBox.getChildren().addAll(startLabel, startField);
        startBox.setAlignment(Pos.CENTER_RIGHT);

        // init end count field
        final Label endLabel = new Label("End:");
        endField = new DoubleTextField(this.defaultEndCount);
        HBox endBox = new HBox(GENERAL_SPACING);
        endBox.getChildren().addAll(endLabel, endField);
        endBox.setAlignment(Pos.CENTER_RIGHT);

        // add all to scene
        content.getChildren().addAll(nameBox, descripBox, startBox, endBox);
        this.contentPane.getChildren().add(content);
    }

    /**
     * Initialize the checkboxes with labels for each camera, in a flowpane.
     */
    private void initCamCheckBoxes() {
        // Create new FlowPane to hold the checkboxes.
        this.checkboxPane = new FlowPane();
        this.checkboxPane.setHgap(GENERAL_PADDING);
        this.checkboxPane.setVgap(GENERAL_PADDING);
        this.checkboxPane.setMinWidth(CAMERA_AREA_MIN_WIDTH);
        this.checkboxPane.setPrefWidth(GENERAL_SIZE);
        this.checkboxPane.setAlignment(Pos.CENTER);

        // add checkboxes
        cameraCheckboxes = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < numberOfCameras; i++) {
            j = (j > 4) ? 0 : j + 1;
            String checkBoxString = "Camera " + (i + 1);
            StyledCheckbox checkBox = new StyledCheckbox(checkBoxString);
            checkBox.setMarkColor(checkboxColor);
            cameraCheckboxes.add(checkBox);
        }

        // add all to scene
        this.checkboxPane.getChildren().addAll(cameraCheckboxes);
        this.contentPane.getChildren().add(this.checkboxPane);
    }

    /**
     * Validate and then pass shot information along.
     * @param event Creation button event
     */
    private void createShot(MouseEvent event) {
        if (validateShot()) {
            super.hideModal();
            this.cameraShotCreationEventHandler.handle(this.buildCreationEvent());
        }
    }

    /**
     * Validates that the fields are correctly filled, and if not, displays
     * a corresponding error message.
     * @return whether or not the fields are valid
     */
    private boolean validateShot() {
        String errorString = "";

        boolean aCameraSelected = false;
        for (CheckBox cb : this.cameraCheckboxes) {
            if (cb.isSelected()) {
                aCameraSelected = true;
            }
        }

        if (!aCameraSelected) {
            errorString = "Please select at least one camera for this shot.";
        }

        double startVal = Double.parseDouble(startField.getText());
        double endVal = Double.parseDouble(endField.getText());
        if (startVal >= endVal) {
            errorString = "Please make sure that the shot ends after it begins.\n";
        }

        if (descriptionField.getText().isEmpty()) {
            errorString = "Please add a description.\n";
        }

        if (nameField.getText().isEmpty()) {
            errorString = "Please name your shot.\n";
        }

        if (errorString.isEmpty()) {
            return true;
        } else {
            titleLabel.setText(errorString);
            titleLabel.setTextFill(Color.RED);
            return false;
        }
    }

    /**
     * Build the shot creation event.
     * @return the shot creation event
     */
    private CameraShotCreationEvent buildCreationEvent() {
        String shotName = this.nameField.getText();
        String shotDescrip = this.descriptionField.getText();
        List<Integer> camerasInShot = getCamerasInShot();
        double startPoint = Double.parseDouble(this.startField.getText());
        double endPoint = Double.parseDouble(this.endField.getText());

        return new CameraShotCreationEvent(shotName, shotDescrip, camerasInShot,
                                             startPoint, endPoint);
    }

    /**
     * Builds a list of which camera centerarea are in the shot.
     * @return list of cameras in shot
     */
    private List<Integer> getCamerasInShot() {
        List<Integer> camsInShot = new ArrayList<>();

        for (int i = 0; i < cameraCheckboxes.size(); i++) {
            if (cameraCheckboxes.get(i).isSelected()) {
                camsInShot.add(i);
            }
        }

        return camsInShot;
    }
}