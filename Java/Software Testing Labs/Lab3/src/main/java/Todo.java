import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Todo extends Item {
    private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    private final String what;
    private final Date when;

    public Todo(Date when, String what) {
        this.when = when;
        this.what = what;
    }

    public static Date dateParse(String when) {
        try {
            return df.parse(when);
        } catch (ParseException pe) {
            System.out.println("Can't understand date " + when);
            return null;
        }
    }

    public boolean matchDate(Date when) {
        long diff = this.when.getTime() - when.getTime();
        return Math.abs(diff) <= 24 * 60 * 60 * 1000;
    }

    @Override
    public void print() {
        System.out.print("todo: ");
        System.out.println(df.format(when) + " " + what);

    }

    public Date getWhen() {
        return when;
    }
}
