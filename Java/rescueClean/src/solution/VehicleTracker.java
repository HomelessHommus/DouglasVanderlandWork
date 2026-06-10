package solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleTracker {

    // all variables that keep track of vehicle info
    private int id;
    private String finalDestination;
    private String startDestination;
    private boolean inUse =  false;
    private boolean destroyed = false;
    private String position;
    private String rescuePosition;
    List<Long> path = new ArrayList<>();

    // constructor setting the ID int
    public VehicleTracker(int id) {
        this.id = id;
    }

    // getters and setters for all variables
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFinalDestination() {
        return finalDestination;
    }
    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getStartDestination() {
        return startDestination;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setStartDestination(String startDestination) {
        this.startDestination = startDestination;
    }
    public boolean getInUse() {
        return inUse;
    }
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    public String getRescuePosition() {
        return rescuePosition;
    }
    public void setRescuePosition(String rescuePosition) {
        this.rescuePosition = rescuePosition;
    }
    public void addPath(Long key){
        path.add(key);
    }
    public boolean getDestroyed() {
        return destroyed;
    }
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }



}
