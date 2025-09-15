/* `Greeter prints a greeeting to stdout 
 *
 * @author Jerzy Puchalski 2253461
 * @date 2025/08/30
*/

class Greeter {
    /**
     * Greet the programmer.
     */
    void greet() {
        System.out.println("Hello, YOUR NAME!");
        System.out.println("Good luck in the course Programming; Enjoy!");
        System.out.println(10 + '8');
    }

    public static void main(String[] args) {
        new Greeter().greet();
    }
}
