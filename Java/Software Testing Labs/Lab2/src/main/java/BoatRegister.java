import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author moon0016
 */
public class BoatRegister {

    private List<Boat> register;

    public BoatRegister() {
        register = new ArrayList<Boat>();
    }

    public List<Boat> getRegister() {
        return register;
    }

    public void addBoat(Boat boat) {
        register.add(boat);
    }

    public Boat getBoat(int regNum) {
        for (Boat b : register) {
            if (b.getRegNum() == regNum) {
                return b;
            }
        }
        return null;
    }

    public void groupByClass() {
        Collections.sort(register, new BoatClassComparator());
    }

    public Collection<Boat> getByCondition(Condition c) {
        Collection<Boat> result = new ArrayList<Boat>();
        for (Boat b : register) {
            if (c.satisfies(b)) {
                result.add(b);
            }
        }
        return result;
    }

    public Collection<Boat> getBoatsWhoseNameContains(String charSequence) {
        Collection<Boat> result = new ArrayList<Boat>();
        for (Boat b : register) {
            if (b.getName().contains(charSequence)) {
                result.add(b);
            }
        }
        return result;
    }

    public Boat deleteBoat(int regNum) {
        Boat b = getBoat(regNum);
        if (b != null) {
            register.remove(b);
        }
        return b;
    }

    public String toString() {
        return register.toString();
    }

}
