/**
 * @author moon0016
 */
public class SuperBoatClassCondition implements Condition {

    public boolean satisfies(Boat b) {
        return b.getbClass().equals("Super practical2.Boat");
    }
}
