import java.util.Comparator;

/**
 * @author moon0016
 */
public class BoatClassComparator implements Comparator<Boat> {

    public int compare(Boat o1, Boat o2) {
        int diff = o1.getbClass().compareTo(o2.getbClass());
        if (diff == 0) {
            diff = o1.getSeqNum() - o2.getSeqNum();
        }
        return diff;
    }
}
