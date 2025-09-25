public class Panda extends Animal{

//    This is the constructor for the Panda animal
//    It calls the Animal constructor using super and sets the species to Panda automatically
    public Panda(String name, String species) {
        super(name, "Panda");
    }

//    This method overrides the giveFood from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Panda
    public void giveFood() {

        setHunger(getHunger() - 25);
        setThirst(getThirst() + 15);
        setBoredom(getBoredom() - 5);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Panda
    public void giveWater() {

        setHunger((getHunger()) + 40);
        setThirst((getThirst()) - 80);
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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Panda
    public void giveToy() {

        setHunger(getHunger() + 40);
        setThirst(getThirst() + 15);
        setBoredom(getBoredom() - 40);

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
