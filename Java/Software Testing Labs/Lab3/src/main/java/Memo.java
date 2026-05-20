public class Memo extends Item {
    private final String message;

    public Memo(String message) {
        this.message = message;
    }

    @Override
    public void print() {
        System.out.print("memo: ");
        System.out.println(message);
    }
}
