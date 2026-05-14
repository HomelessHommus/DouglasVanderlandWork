import java.util.HashSet;
import java.util.Set;

/**
 * @author moon0016
 */
public class Boat {

    public static int count = 0;
    private final int seqNum;
    private int regNum = -1;
    private String bClass = "unknown";
    private String name = "unknown";
    private Set<String> crew = new HashSet<>();

    public Boat() {
        seqNum = ++count;
    }

    public Boat(String the_name, String the_bClass, int the_regNum) {
        name = the_name;
        bClass = the_bClass;
        regNum = the_regNum;
        seqNum = ++count;
    }

    public static String printTotal() {
        return "Total Number of Boats made = " + count;
    }

    public String getbClass() {
        return bClass;
    }

    public void setbClass(String bClass) {
        this.bClass = bClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRegNum() {
        return regNum;
    }

    public void setRegNum(int regNum) {
        this.regNum = regNum;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void addCrew(String crewmember) {
        crew.add(crewmember);
    }

    public Set<String> getCrew() {
        return crew;
    }

    public String toString() {
        return "practical2.Boat " + name + ", " +
                "Class = " + bClass + ", " +
                "#Crew = " + crew.size() + ", " +
                "Registration # = KA " + regNum + ", " +
                "Sequence # = " + seqNum;
    }

}
