public class Monkey extends Animal{

//    This is the constructor for the Monkey animal
//    It calls the Animal constructor using super and sets the species to Monkey automatically
    public Monkey(String name, String species) {
        super(name, "Monkey");
    }

//    This method overrides the giveFood from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Monkey
    public void giveFood() {

        setHunger(getHunger() - 30);
        setThirst(getThirst() + 10);
        setBoredom(getBoredom() + 10);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Monkey
    public void giveWater() {

        setHunger((getHunger()) + 10);
        setThirst((getThirst()) - 40);
        setBoredom((getBoredom()) + 10);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Monkey
    public void giveToy() {

        setHunger(getHunger() + 10);
        setThirst(getThirst() + 10);
        setBoredom(getBoredom() - 15);

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
