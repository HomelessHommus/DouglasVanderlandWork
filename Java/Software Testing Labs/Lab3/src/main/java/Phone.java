public class Phone extends Item {

    private final String name;
    private final String number;

    public Phone(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public boolean matchName(String n) {
        return name.startsWith(n);
    }

    @Override
    public void print() {
        System.out.print("phone: ");
        System.out.println(name + " " + number);
    }

    public String getName() {
        return name;
    }
}
