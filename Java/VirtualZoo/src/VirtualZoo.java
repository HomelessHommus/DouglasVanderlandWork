// All java imports required for various parts of the pragram
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author Douglas Vanderland, vand0475
 */

public class VirtualZoo {

//    Defines the scanner to be used for all inputs in the program
    public VirtualZoo() {
    scan = new Scanner(System.in);
}
    private final Scanner scan;
//    The ArrayList of all the animal names
    private final static ArrayList<String> allNames = new ArrayList<>();
//    The total cost of the zoo throughout the week
    private static int totalCost = 0;

//    The method to begin the simulation
    public void beginSimulation() {
        displayWelcome();
        ArrayList<Animal> zooAnimals = animalSelection();
        weekCycle(zooAnimals);
    }

    //-------------------------operational methods------------------------------

//    Defines the welcome text to display at the start of the simulation
    public void displayWelcome()
    {
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|                     Welcome to the Virtual Zoo!                      |");
        System.out.println("|     Use this application to simulate running a zoo over one week     |");
        System.out.println("|           This program is intended for zoo employees only!           |");
        System.out.println("+----------------------------------------------------------------------+");
    }

//    Getter and setter methods for the total cost of the zoo
    public static int getTotalCost() {
        return totalCost;
    }
    public static void setTotalCost(int newTotalCost) {
        totalCost = newTotalCost;
    }

//    Asks the user what item they would like to give the animal and the applies the given effects of the item
    public void askItem(Animal animal) {
        String item;
        boolean errorCheck = false;

        do {
            System.out.println("What item would you like to give to " + animal.getName() + "?");
            item = scan.nextLine();
            item = item.toUpperCase();

            if (item.equals("FOOD") || item.equals("TOY") || item.equals("WATER")) {
                if (Objects.equals(animal.getYesterdayItem(), item))
                {
                    System.out.println("You cannot give the same item as yesterday");
                } else
                    errorCheck = true;
                    animal.setYesterdayItem(item);
            } else
                System.out.println("That is not a valid item");

            if (Objects.equals(animal.getSpecies(), "Tiger")) {
                if (errorCheck) {
                    if (item.equals("FOOD")) {
                        animal.giveFood();
                        setTotalCost(getTotalCost() + 30);
                    }
                    if (item.equals("TOY")) {
                        animal.giveToy();
                        setTotalCost(getTotalCost() + 40);
                    }
                    if (item.equals("WATER")) {
                        animal.giveWater();
                        setTotalCost(getTotalCost() + 20);
                    }
                }
            }

            if (Objects.equals(animal.getSpecies(), "Giraffe")) {
                if (errorCheck) {
                    if (item.equals("FOOD")) {
                        animal.giveFood();
                        setTotalCost(getTotalCost() + 100);
                    }
                    if (item.equals("TOY")) {
                        animal.giveToy();
                        setTotalCost(getTotalCost() + 75);
                    }
                    if (item.equals("WATER")) {
                        animal.giveWater();
                        setTotalCost(getTotalCost() + 40);
                    }
                }
            }

            if (Objects.equals(animal.getSpecies(), "Hippo")) {
                if (errorCheck) {
                    if (item.equals("FOOD")) {
                        animal.giveFood();
                        setTotalCost(getTotalCost() + 50);
                    }
                    if (item.equals("TOY")) {
                        animal.giveToy();
                        setTotalCost(getTotalCost() + 20);
                    }
                    if (item.equals("WATER")) {
                        animal.giveWater();
                        setTotalCost(getTotalCost() + 50);
                    }
                }
            }

            if (Objects.equals(animal.getSpecies(), "Panda")) {
                if (errorCheck) {
                    if (item.equals("FOOD")) {
                        animal.giveFood();
                        setTotalCost(getTotalCost() + 70);
                    }
                    if (item.equals("TOY")) {
                        animal.giveToy();
                        setTotalCost(getTotalCost() + 20);
                    }
                    if (item.equals("WATER")) {
                        animal.giveWater();
                        setTotalCost(getTotalCost() + 5);
                    }
                }
            }

            if (Objects.equals(animal.getSpecies(), "Monkey")) {
                if (errorCheck) {
                    if (item.equals("FOOD")) {
                        animal.giveFood();
                        setTotalCost(getTotalCost() + 5);
                    }
                    if (item.equals("TOY")) {
                        animal.giveToy();
                        setTotalCost(getTotalCost() + 15);
                    }
                    if (item.equals("WATER")) {
                        animal.giveWater();
                        setTotalCost(getTotalCost() + 5);
                    }
                }
            }

            if (animal.isDead()) {
                System.out.println(animal.getName() + " has died");
                setTotalCost(getTotalCost() + 1000);
            }
        }
        while (!errorCheck);
    }

//    Asks the user how many animal are in the zoo and returns an int of the number of animals
    public int askNumberAnimals() {

        boolean charTry;
        String animalNumber;

        do {

            System.out.println("How many animals are at your zoo?");
            animalNumber = scan.nextLine();

            try {
                Integer.parseInt(animalNumber);
                charTry = false;
            }
            catch(NumberFormatException error) {
                charTry = true;
                System.out.println("Please enter a valid number of animals");
            }

            if(!charTry) {
                if(Integer.parseInt(animalNumber) < 0) {
                    System.out.println("Please enter a positive number of animals");
                    charTry = true;
                }
            }
        }
        while(charTry);

        return Integer.parseInt(animalNumber);
    }

//    Asks the user what is the name of a particular animal at a
//    certain id number taken from the askNumberAnimals method and then sets the name to the animal
    public String askAnimalName(int id) {
        boolean errorCheck = true;
        String animalName;
        do {
            System.out.println("What is the name of animal #" + id + "?");
            animalName = scan.nextLine();

            if (allNames.contains(animalName)) {
                System.out.println("That name is already taken");
            } else {
                errorCheck = false;
                allNames.add(animalName);
            }
        }
        while (errorCheck);
        return animalName;
    }

//    Asks the user what the animal species is for the given animal name and returns an Animal object
//    with the species and name set in the constructor
    public Animal askAnimalSpecies(String name) {

        boolean errorCheck = false;
        String animalSpecies;

        do {
            System.out.println("What is the species of " + name + "?");
            animalSpecies = scan.nextLine();
            animalSpecies = animalSpecies.toUpperCase();

            if(animalSpecies.equals("TIGER") ||
                    animalSpecies.equals("GIRAFFE") ||
                    animalSpecies.equals("HIPPO") ||
                    animalSpecies.equals("PANDA") ||
                    animalSpecies.equals("MONKEY")) {

                if(animalSpecies.equals("TIGER")) {
                    return new Tiger(name, "Tiger");
                }
                if(animalSpecies.equals("GIRAFFE")) {
                    return new Giraffe(name, "Giraffe");
                }
                if(animalSpecies.equals("HIPPO")) {
                    return new Hippo(name, "Hippo");
                }
                if(animalSpecies.equals("PANDA")) {
                    return new Panda(name, "Panda");
                }
                if(animalSpecies.equals("MONKEY")) {
                    return new Monkey(name, "Monkey");
                }
                errorCheck = true;
            }
            else
                System.out.println("That is not a valid species");

        }
        while(!errorCheck);

        return new Tiger(name, animalSpecies);
    }

//    This method returns an ArrayList of all the animals by calling all the ask methods for the animal information
    public ArrayList<Animal> animalSelection() {

        ArrayList<Animal> animalSelection = new ArrayList<>();
        String name;
        int numAnimal;
        Animal species;
        numAnimal = askNumberAnimals();
        System.out.println();

        for(int i = 1; i <= numAnimal; i++) {

            name = askAnimalName(i);

            species = askAnimalSpecies(name);

            animalSelection.add(species);
        }
        return animalSelection;
    }

//    This method cycles through a week of seven days and calling each animals information as well as calling
//    the askItem method to update the animals hunger, thirst and, boredom throughout
    public void weekCycle(ArrayList<Animal> zooAnimals) {
        System.out.println();
        ArrayList<String> dayOfWeek = new ArrayList<>();
        dayOfWeek.add(0,"Monday");
        dayOfWeek.add(1,"Tuesday");
        dayOfWeek.add(2,"Wednesday");
        dayOfWeek.add(3,"Thursday");
        dayOfWeek.add(4,"Friday");
        dayOfWeek.add(5,"Saturday");
        dayOfWeek.add(6,"Sunday");

        for (int i = 0; i <= 6; i++) {
            System.out.println("The current day is " + dayOfWeek.get(i));
            System.out.println();

            for (Animal zooAnimal : zooAnimals) {
                System.out.println(zooAnimal.toString());
            }

            for (Animal zooAnimal : zooAnimals) {
                if (!zooAnimal.isDead()) {
                    askItem(zooAnimal);
                }
            }
            System.out.println();
        }
        System.out.println("Total week cost = $" + totalCost);
    }
}
