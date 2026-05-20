package solution;

public class Road {

    private Long destination;
    private double cost;
    private String access;

    Road (Long d, double c) {
        this.destination = d;
        this.cost = c;
        this.access = "open";
    }

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

