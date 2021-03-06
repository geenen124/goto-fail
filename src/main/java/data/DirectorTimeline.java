package data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to store information about a directors timeline.
 */
@XmlRootElement(name = "directorTimeline")
@ToString
public class DirectorTimeline extends Timeline {

    // Collection of all Shot elements in this Timeline.
    @Getter
    @XmlElementWrapper(name = "shotList")
    @XmlElement(name = "shot")
    private LinkedList<DirectorShot> shots;
    
    @Getter @Setter
    private String description;
    
    /**
     * Default constructor.
     */
    public DirectorTimeline() {
        super();
        description = "";
        shots = null;
    }

    /**
     * Constructor.
     *
     * @param description the description of this timeline
     * @param project the project that contains this timeline
     */
    public DirectorTimeline(String description, ScriptingProject project) {
        super(project);
        this.description = description;
        shots = new LinkedList<>();
    }

    /**
     * Adds a Shot to the Timeline. The Shot is inserted in a sorted manner. When a Shot is
     * inserted, the shots before the Shot have a lower start count of a lower end count. The
     * Shot after the inserted shot have a higher start count or end count. This method also checks
     * for colliding shots. The colliding shots will have their colliding variable set to
     * true.
     *
     * @param shot the Shot to add to the timeline
     * @return If no overlap is found, only the newly added shot will be returned. If any
       colliding shots are found, all colliding shots will be returned. If any colliding
       shots are found, the shot that was added will be the last one in the list.
     */
    public ArrayList<DirectorShot> addShot(DirectorShot shot) {
        ArrayList<DirectorShot> result = new ArrayList<>();
        boolean added = false;

        // Add the new Shot the the shots
        for (int i = 0; i < shots.size(); i++) {
            DirectorShot other = shots.get(i);
            if (!added && shot.compareTo(other) <= 0) {
                shots.add(i, shot);
                added = true;
            }
            if (checkOverlap(shot, other, 0)) {
                result.add(other);
            }
        }
        if (!added) {
            shots.add(shot);
        }
        result.add(shot);
        return result;
    }

    /**
     * Removes all shots from the Timeline.
     */
    public void clearShots() {
        shots.clear();
    }

    /**
     * Removes the specified Shot.
     * @param shot the Shot to remove
     */
    public void removeShot(DirectorShot shot) {
        shots.remove(shot);
    }

    /**
     * Get the list of shots colliding with the given shots.
     * @param shot - the shot to check with
     * @return - only the shot when no overlap, list of colliding shots otherwise
     */
    public ArrayList<DirectorShot> getOverlappingShots(DirectorShot shot) {
        ArrayList<DirectorShot> result = new ArrayList<>();

        // check for colliding shots
        result.addAll(shots.stream()
                .filter(other -> shot != other)
                .filter(other -> checkOverlap(shot, other, 0))
                .collect(Collectors.toList()));
        result.add(shot);
        return result;
    }
}
