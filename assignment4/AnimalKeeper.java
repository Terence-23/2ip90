
/**
 * A program handling simulation of day-to-day operations of a zoo. It reads
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
public class AnimalKeeper {
    public static void main(String[] args) {
        new MyZoo().run();
    }
}
