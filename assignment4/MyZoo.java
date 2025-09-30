
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * A class handling simulation of day-to-day operations of a zoo. It reads
 * standard input looking for commands and for each comand outputs whether that
 * command is valid.
 *
 * Usage:
 * Input: the program expects a set of commands as input. Allowed commands are
 * as follows:
 * 1. 0 t "name" h instructs program to add an animal of species t named "name"
 * to habitat h
 * 2. 1 "name" h moves animal called "name" to habitat h
 * 3. 2 "name" removes the animal "name" from the zoo
 * 4. 3 f x instructs the zoo to add x ammount of food f to the food stores
 * 5. 4 f x h instructs the simulation to feed x ammount of food f to habitat h
 *
 * Any command id number other than 0-4 results in simulation termination.
 *
 * Species identifiers:
 * 1. Lion
 * 2. Tiger
 * 3. Leopard
 * 4. Zebra
 * 5. Antelope
 * 6. Giraffe
 * 7. Bear
 *
 * Food identifiers:
 * 1. hay
 * 2. corn
 * 3. grain
 * 4. carrots
 * 5. chicken
 * 6. beef
 *
 * Habitat identifiers:
 * 0-9: cages that fit up to 2 animals
 * 10-14: open enclosures that fit up to 6 animals
 *
 * Output: For each command the output is the command id followed by ! in case
 * the command violates the zoo specification.
 * 
 * @author Jerzy Puchalski
 * @ID 2253461
 * @author Kerem Can Ayhan
 * @ID 2010399
 * 
 */

public class MyZoo {

    /**
     * Home for animals. either a cage or an open enclosure.
     */
    private class Home {
        int id;
        boolean isOpen;
        int capacity;
        int inhabitantCount;
        Animal[] inhbitants;

        Home(int id) {
            this.id = id;
            inhabitantCount = 0;
            capacity = id > 9 ? 6 : 2;
            inhbitants = new Animal[capacity];
            isOpen = id > 9;
        }

        boolean hasMeatEaters() {
            return Arrays.stream(inhbitants).anyMatch(x -> x != null && !x.isHerbivorus);
        }

        boolean hasHerbivores() {

            return Arrays.stream(inhbitants).anyMatch(x -> x != null && x.isHerbivorus);
        }

        boolean hasOtherSpecies(int species) {
            return Arrays.stream(inhbitants).anyMatch(x -> x != null && x.species != species);
        }

        // checks whether the animal can be added
        void addAnimal(Animal a) throws ZooException {
            spaceCheck();
            opennessCheck(a);
            herbivorusCheck(a);

            if (!a.isHerbivorus && hasOtherSpecies(a.species)) {
                throw new ZooException("Non herbivores dont like to live with other species", 0);
            }
            if (a.isSolitary() && inhabitantCount > 0) {
                throw new ZooException(
                        "The animal is solitary but htere are already other inhabitants",
                        0);
            }
            a.home = id;
            inhbitants[inhabitantCount] = a;
            inhabitantCount++;
        }

        void removeAnimal(Animal a) throws ZooException {
            for (int i = 0; i < inhabitantCount; ++i) {
                if (inhbitants[i].name.equals(a.name)) {

                    for (int j = i + 1; j < inhabitantCount; ++j) {
                        inhbitants[j - 1] = inhbitants[j];
                    }

                    inhabitantCount--;
                    return;
                }
            }
            throw new ZooException("No animal called %s in %d".formatted(a.name, id), 2);
        }

        /**
         * Move animal from this home to tHome.
         * 
         * @param a     The animal to be moved.
         * @param tHome The home to which the animal moves.
         * @throws ZooException if the move would violate the housing
         *                      guidelines for the animals.
         */
        void moveAnimal(Animal a, Home tHome) throws ZooException {
            try {
                boolean inThisHome = false;
                for (int i = 0; i < inhabitantCount; ++i) {
                    if (a.name.equals(inhbitants[i].name)) {

                        inThisHome = true;
                        break;
                    }
                }
                if (!inThisHome) {
                    throw new ZooException("No animal called %s in %d".formatted(a.name, id), 1);
                }
                tHome.addAnimal(a);
                removeAnimal(a);

                a.home = tHome.id;
            } catch (ZooException ex) {
                ex.command = 1;
                throw ex;
            }
        }

        void feed(int food) throws ZooException {
            if (!Arrays.stream(inhbitants).allMatch(x -> x != null && x.canEat(food + 1))) {
                throw new ZooException("Not all inhabitants can eat food %d".formatted(food), 4);
            }
        }

        // following three methods extract some conditions for adding an animal to a
        // habitat.
        private void spaceCheck() throws ZooException {
            if (inhabitantCount == capacity) {
                throw new ZooException("Not enough space in %d".formatted(id), 0);
            }
        }

        private void opennessCheck(Animal a) throws ZooException {
            if (a.isHerbivorus && !isOpen) {
                throw new ZooException("Herbivores need open space", 0);
            }
        }

        private void herbivorusCheck(Animal a) throws ZooException {
            if (hasMeatEaters() && a.isHerbivorus || !a.isHerbivorus && hasHerbivores()) {
                throw new ZooException("Herbivores and carnivores cant mix", 0);
            }
        }

    }

    /**
     * Custom class for handling command output
     */
    private class Output {
        ArrayList<String> output = new ArrayList<>();

        @Override
        public String toString() {
            return output.stream().collect(Collectors.joining(" "));
        }

        void add(int c) {
            output.add(Integer.toString(c));
        }

        void addE(int c) {
            output.add(Integer.toString(c) + '!');
        }
    }

    /**
     * An animal.
     */
    private class Animal {
        boolean isHerbivorus;
        int species;

        String name;
        int home;

        Animal(String name, int species) {
            this.species = species;
            this.name = name;
            isHerbivorus = 0 <= Arrays.binarySearch(HERBIVORES, species);
            home = -1;
        }

        boolean isSolitary() {
            return species == 2 || species == 3 || species == 7;
        }

        // the folllowing methods contain checks whather a specific food type is
        // suitable for this animal.

        boolean canEatPlantBased(int food) {
            return (food == 1 || food == 2 || food == 3) && isHerbivorus;
        }

        boolean canEatMeatBased(int food) {
            return (food == 5 || food == 6 || food == 7) && !isHerbivorus;
        }

        boolean canEatCarrots() {
            return species == 4 || species == 5 || species == 7;
        }

        boolean canEatCarrots(int food) {
            return food == 4 && canEatCarrots();
        }

        boolean canEat(int food) {

            return canEatPlantBased(food) || canEatCarrots(food)
                    || canEatMeatBased(food);

        }

    }

    private Output output = new Output();
    private static final int[] HERBIVORES = { 4, 5, 6 };
    private HashMap<String, Animal> animals = new HashMap<>();

    private HashMap<Integer, Home> homes = new HashMap<>();

    private int[] foodStore = { 0, 0, 0, 0, 0, 0 };

    /**
     * The default constructor of the MyZoo class.
     * 
     */
    MyZoo() {
        for (int i = 0; i < 15; ++i) {
            homes.put(i, new Home(i));
        }

    }

    /**
     * Reads the add command from {@link Scanner} {@link s} and executes it.
     * 
     * @param s scanner containing input
     * @throws ZooException if executing command would violate the housing
     *                      guidelines for the animals.
     */
    void addAnimal(Scanner s) throws ZooException {
        int type = s.nextInt();
        String name = s.next();
        int homeId = s.nextInt();

        if (animals.get(name) != null) {
            throw new ZooException("Animals must have unique names", 0);
        }

        Animal a = new Animal(name, type);

        Home home = homes.get(homeId);
        if (home == null) {
            throw new ZooException("No such home: %d".formatted(homeId), 0);
        }
        home.addAnimal(a);
        animals.put(name, a);

        output.add(0);
    }

    /**
     * Reads the move command from {@link Scanner} {@link s} and executes it.
     * 
     * @param s scanner containing input
     * @throws ZooException if executing command would violate the housing
     *                      guidelines for the animals.
     */
    void moveAnimal(Scanner s) throws ZooException {
        String name = s.next();
        Home tHome = homes.get(s.nextInt());
        Animal a = animals.get(name);
        if (a == null) {
            throw new ZooException("No animal called %s".formatted(a), 1);
        }
        if (tHome == null) {
            throw new ZooException("No such home", 1);
        }

        Home home = homes.get(a.home);
        home.moveAnimal(a, tHome);
        output.add(1);
    }

    /**
     * Reads the remove command from {@link Scanner} {@link s} and executes it.
     * 
     * @param s scanner containing input
     * @throws ZooException if it cant find the animal.
     */
    void removeAnimal(Scanner s) throws ZooException {
        String name = s.next();
        Animal a = animals.get(name);
        if (a == null) {
            throw new ZooException("No animal called %s".formatted(a), 2);
        }
        Home h = homes.get(a.home);
        h.removeAnimal(a);

        output.add(2);
    }

    /**
     * Reads the buy food command from {@link Scanner} {@link s} and executes it.
     * 
     * @param s scanner containing input
     * @throws ZooException if executing command would overflow the food stores.
     */
    void buyFood(Scanner s) throws ZooException {
        int type = s.nextInt();
        int ammount = s.nextInt();
        if (type > 6 || type < 1) {
            throw new ZooException("No such food type: %d".formatted(type), 3);
        }
        type--;

        if (ammount <= 0) {
            throw new ZooException("Negative buy ammount", 3);
        }

        if (foodStore[type] + ammount > 100) {
            throw new ZooException("Too much food of type: %d".formatted(type), 3);
        }
        foodStore[type] += ammount;
        output.add(3);
    }

    /**
     * Reads the feed command from {@link Scanner} {@link s} and executes it.
     * 
     * @param s scanner containing input
     * @throws ZooException if executing command would violate the feeding
     *                      guidelines for the animals or the ammount of food stored
     *                      is insufficient.
     */
    void feed(Scanner s) throws ZooException {
        int type = s.nextInt();
        int ammount = s.nextInt();
        int home = s.nextInt();
        if (type > 6 || type < 1) {
            throw new ZooException("No such food type: %d".formatted(type), 4);
        }
        type--;
        if (foodStore[type] < ammount) {
            throw new ZooException("Not enough food of type %d".formatted(type), 4);
        }
        homes.get(home).feed(type);
        foodStore[type] -= ammount;
        output.add(4);
    }

    // stuff that should happen when the program exits.
    void onExit() {

        System.out.println(output.toString());
    }

    /**
     * The main simulation routine.
     */
    void run() {
        Scanner scan = new Scanner(System.in);
        int code = scan.nextInt();
        while (true) {
            try {
                switch (code) {
                    case 0:
                        addAnimal(scan);
                        break;
                    case 1:
                        moveAnimal(scan);
                        break;
                    case 2:
                        removeAnimal(scan);
                        break;
                    case 3:
                        buyFood(scan);
                        break;
                    case 4:
                        feed(scan);
                        break;
                    default:
                        onExit();
                        return;
                }

            } catch (ZooException ex) {
                output.addE(ex.command);
                System.err.println(ex.toString());
            }
            if (scan.hasNext()) {
                code = scan.nextInt();
            } else {
                onExit();
                return;
            }
        }

    }
}

class ZooException extends Exception {
    int command;

    /**
     * Creates a ZooError.
     * 
     * @param m       Error message
     * @param command erroneous command
     */
    ZooException(String m, int command) {
        super(m);
        this.command = command;
    }

}
