public abstract class Animal {

//    All instance variables for the various states of the animal
    private String name;
    private String species;
    private int hunger = 50;
    private int thirst = 50;
    private int boredom = 50;
    private boolean isDead;
    private String yesterdayItem = "";
    private static int totalCost = 0;

//    The constructor that takes two Strings for its name and species and sets them as the instance variables
    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
    }

//    All getter and setter methods for the various states of the animal
    public void setName(String name) {
        this.name = name;
    }
    public void setYesterdayItem(String yesterdayItem) {
        this.yesterdayItem = yesterdayItem;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }
    public void setThirst(int thirst) {
        this.thirst = thirst;
    }
    public void setBoredom(int boredom) {
        this.boredom = boredom;
    }
    public String getYesterdayItem()
    {
        return yesterdayItem;
    }
    public String getName() {
        return name;
    }
    public String getSpecies() {
        return species;
    }
    public int getHunger() {
        return hunger;
    }
    public int getThirst() {
        return thirst;
    }
    public int getBoredom() {
        return boredom;
    }

//    Checks if the animal has died and outputs a boolean of true or false
    public boolean isDead() {
        if (boredom > 100 || thirst > 100 || hunger > 100) {
            return true;
        }
        else
            return false;
    }

//    The abstract methods that are overridden by the children classes
    public abstract void giveFood();
    public abstract void giveWater();
    public abstract void giveToy();

//    The toString method that describes the animal and its status
    public String toString() {
        String statusMessage = isDead() ? "Name = " + getName() + " (Dead)\n" : "Name = " + getName() + "\n";
        statusMessage += "Species = " + getSpecies() + "\n";
        statusMessage += "Hunger = " + getHunger() + "\n";
        statusMessage += "Thirst = " + getThirst() + "\n";
        statusMessage += "Boredom = " + getBoredom() + "\n";
        return statusMessage;
    }
}
