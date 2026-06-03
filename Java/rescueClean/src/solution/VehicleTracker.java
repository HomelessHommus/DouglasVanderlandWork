package solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleTracker {

    private int id;
    private String finalDestination;
    private String startDestination;
    private boolean inUse =  false;
    private String position;
    private String rescuePosition;
    List<Long> path = new ArrayList<>();

    public VehicleTracker(int id) {
        this.id = id;
    }

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
    public String getPathString(){
        Collections.reverse(path);

        StringBuilder pathString = new StringBuilder();

        for (Long key : path) {
            pathString.append(key).append(",");
        }
        pathString.deleteCharAt(pathString.length() - 1);
        return pathString.toString();
    }
}
