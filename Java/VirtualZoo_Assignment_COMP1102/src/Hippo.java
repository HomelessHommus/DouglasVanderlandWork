public class Hippo extends Animal{

//    This is the constructor for the Hippo animal
//    It calls the Animal constructor using super and sets the species to Hippo automatically
    public Hippo(String name, String species) {
        super(name, "Hippo");
    }

//    This method overrides the giveFood from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Hippo
    public void giveFood() {

        setHunger(getHunger() - 25);
        setThirst(getThirst() + 5);
        setBoredom(getBoredom() + 25);

        if (getHunger() < 0) {
            setHunger(0);
        }
        if (getThirst() < 0) {
            setThirst(0);
        }
        if (getBoredom() < 0) {
            setBoredom(0);
        }
    }

//    This method overrides the giveWater from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Hippo
    public void giveWater() {

        setHunger((getHunger()) + 5);
        setThirst((getThirst()) - 25);
        setBoredom((getBoredom()) + 25);

        if (getHunger() < 0) {
            setHunger(0);
        }
        if (getThirst() < 0) {
            setThirst(0);
        }
        if (getBoredom() < 0) {
            setBoredom(0);
        }
    }

//    This method overrides the giveToy from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Hippo
    public void giveToy() {

        setHunger(getHunger() + 20);
        setThirst(getThirst() + 20);
        setBoredom(getBoredom() - 50);

        if (getHunger() < 0) {
            setHunger(0);
        }
        if (getThirst() < 0) {
            setThirst(0);
        }
        if (getBoredom() < 0) {
            setBoredom(0);
        }
    }
}
