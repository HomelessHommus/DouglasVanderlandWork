public class Tiger extends Animal{

//    This is the constructor for the Tiger animal
//    It calls the Animal constructor using super and sets the species to Tiger automatically
    public Tiger(String name, String species) {
        super(name, "Tiger");
    }

//    This method overrides the giveFood from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Tiger
    public void giveFood() {

        setHunger(getHunger() - 30);
        setThirst(getThirst() + 15);
        setBoredom(getBoredom() + 15);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Tiger
    public void giveWater() {

        setHunger((getHunger()) + 15);
        setThirst((getThirst()) - 30);
        setBoredom((getBoredom()) + 15);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Tiger
    public void giveToy() {

        setHunger(getHunger() + 15);
        setThirst(getThirst() + 15);
        setBoredom(getBoredom() - 30);

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
