package gui.centerarea;

import data.CameraShot;
import gui.events.DirectorShotBlockUpdatedEvent;
import gui.events.ShotblockUpdatedEvent;
import gui.root.RootCenterArea;
import javafx.event.EventHandler;
import lombok.Getter;

/**
 * Created by Bart.
 */
public class DirectorShotBlock extends ShotBlock {

    // The id of the shot
    @Getter
    private int shotId;

    // The directorGridpane the shot is in
    @Getter
    private DirectorGridPane grid;

    public DirectorShotBlock(CameraShot shot, RootCenterArea rootCenterArea,
                             EventHandler<DirectorShotBlockUpdatedEvent> handler) {
        this(shot.getInstance(), rootCenterArea, shot.getBeginCount(),
                shot.getEndCount(), shot.getDescription(), shot.getName(), handler, shot);
    }

    /**
     * Constructor.
     * @param shotId the shot's unique id
     * @param rootCenterArea - the rootCenterArea this shot belongs to
     * @param beginCount - the begin count of this shot
     * @param endCount - the end count of this shot
     * @param handler - The handler for this camerashotblock
     * @param description - the description of this camerashotblock
     * @param name - the name of this camerashotblock
     * @param shot - The shot in the model belonging to this camerashotblock
     */
    public DirectorShotBlock(int shotId, RootCenterArea rootCenterArea,
                             double beginCount, double endCount, String description, String name,
                             EventHandler<DirectorShotBlockUpdatedEvent> handler, CameraShot shot) {

        super(rootCenterArea, beginCount, endCount, description,
                name, shot, CameraTimetableBlock.class);

        this.shotId = shotId;
        this.grid = rootCenterArea.getDirectorGridPane();

        this.getTimetableBlock().addEventHandler(ShotblockUpdatedEvent.SHOTBLOCK_UPDATED, e -> {
                this.setBeginCount(TimelinesGridPane.getRowIndex(
                        this.getTimetableBlock()), false);
                this.setEndCount(TimelinesGridPane.getRowSpan(
                    this.getTimetableBlock()) + this.getBeginCount(), false);

                if (e instanceof DirectorShotBlockUpdatedEvent) {
                    handler.handle((DirectorShotBlockUpdatedEvent) e);
                }
            });

        this.getTimetableBlock().setPrefWidth(grid.getHorizontalElementMinimumSize());
        this.getTimetableBlock().setPrefHeight(grid.getNumberOfVerticalGrids()
                * grid.getVerticalElementSize());
        grid.addDirectorShotBlock(this);
    }

    @Override
    public ShotblockUpdatedEvent getShotBlockUpdatedEvent() {
        return new DirectorShotBlockUpdatedEvent(this);
    }

    @Override
    public CameraShot getShot() {
        return (CameraShot) super.getShot();
    }

    /**
     * Remove this DirectorShotBlock's view from the view that it's in.
     */
    public void removeFromView() {
        this.grid.removeDirectorShotBlock(this);
    }
}