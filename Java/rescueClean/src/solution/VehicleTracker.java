package solution;

public class VehicleTracker {

    private int id;
    private String finalDestination;
    private boolean inUse =  false;

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
    public boolean getInUse() {
        return inUse;
    }
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
