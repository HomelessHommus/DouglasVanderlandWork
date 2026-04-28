import java.util.ArrayList;

public class Concatenation {
    public static String concat(ArrayList<String> list) {
        String concatenatedList = "";

        for (String listItem : list) {
            concatenatedList = concatenatedList.concat(listItem);
        }

        return concatenatedList;
    }
}
