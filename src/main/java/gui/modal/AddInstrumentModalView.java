package gui.modal;


import data.Instrument;
import gui.root.RootPane;
import gui.styling.StyledButton;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class AddInstrumentModalView extends CameraModificationModalView {

    private static final int WIDTH = 450;
    private static final int HEIGHT = 300;
    
    @Getter
    private StyledButton addInstrumentButton;
    
    /**
     * Construct modal with default size.
     * @param rootPane the rootPane for this modal
     */
    public AddInstrumentModalView(RootPane rootPane) {
        this(rootPane, WIDTH, HEIGHT);
    }
    
    /**
     * Construct a new AddInstrumentModalView.
     * @param rootPane the rootPane for this modal
     * @param width the width of the modal
     * @param height the height of the modal
     */
    public AddInstrumentModalView(RootPane rootPane, int width, int height) {
        super(rootPane, width, height);
        initializeView();
        this.titleLabel.setText("Create an instrument");
    }
    
    /**
     * Construct modal for edit instrument.
     * @param rootPane the rootPane for this modal
     * @param instrument the instrument to edit
     */
    public AddInstrumentModalView(RootPane rootPane, Instrument instrument) {
        this(rootPane, WIDTH, HEIGHT);
        this.nameField.setText(instrument.getName());
        this.descriptionField.setText(instrument.getDescription());
        this.addInstrumentButton.setText("Save");
        this.titleLabel.setText("Edit an instrument");
    }
    
    /**
     * Initialize the view.
     */
    private void initializeView() {
        // force minimum size
        forceBounds(HEIGHT, WIDTH);
        
        this.viewPane = new VBox();
        initTitleLabel();
        initFields();
        initButtons();
        
        super.setModalView(this.viewPane);
        super.displayModal();
    }
    
    /**
     * Initialize the fields.
     */
    private void initFields() {
        VBox content = initNameDescriptionFields();
        this.viewPane.getChildren().add(content);
    }
    
    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        addInstrumentButton = createButton("Add", false);
        initCancelButton();
        initHBoxForButtons().getChildren().addAll(addInstrumentButton, cancelButton);
    }
}
