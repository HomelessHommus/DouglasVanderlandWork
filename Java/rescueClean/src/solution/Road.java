package solution;

public class Road {

    // variables to store in the object
    private Long destination;
    private double cost;
    private String access;

    // constructor for setting the defined variables
    Road (Long d, double c) {
        this.destination = d;
        this.cost = c;
        this.access = "open";
    }

    // getters and setters for all variables
    public Long getDestination() {
        return destination;
    }
    public void setDestination(Long destination) {
        this.destination = destination;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public String getAccess() {
        return access;
    }
    public void setAccess(String access) {
        this.access = access;
    }

    public String toString(){
        return "(Dest: " + destination + " Cost: " + cost + " Access: " + access + ")";
    }
}

