public class Giraffe extends Animal {

//    This is the constructor for the Giraffe animal
//    It calls the Animal constructor using super and sets the species to Giraffe automatically
    public Giraffe(String name, String species) {
        super(name, "Giraffe");
    }

//    This method overrides the giveFood from the abstract parent class of Animal
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Giraffe
    public void giveFood() {

        setHunger(getHunger() - 50);
        setThirst(getThirst() + 30);
        setBoredom(getBoredom() + 20);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Giraffe
    public void giveWater() {

        setHunger((getHunger()) + 20);
        setThirst((getThirst()) - 50);
        setBoredom((getBoredom()) + 30);

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
//    and sets unique attributes to Hunger, Thirst and Boredom based on the specifications of Giraffe
    public void giveToy() {

        setHunger(getHunger() + 30);
        setThirst(getThirst() + 10);
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
