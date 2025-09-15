
import java.util.Scanner;

/**
 * Detects if a point hits any of two circles.
 * 
 * Usage:
 * Expected input consists of 8 floating point numbers.
 * First 3 are the x coordinate, the y coordinate, the radius for the first
 * circle.
 * The next 3 are simmilar but for the second circle.
 * The Last 2 are the x and the y of the point for which we check the collision.
 * 
 * @author Jerzy Puchalski
 * @ID 2253461
 * @author Dominik Kostecki
 * @ID 2332361
 * 
 */
class HitDetection {
    class Point {
        double x;
        double y;

        /**
         * Distance returns distance between self and other.
         * 
         * @param other the other point
         */
        public double calculateDistance(Point other) {
            return Math.sqrt((this.x - other.x) * (this.x - other.x)
                    + (this.y - other.y) * (this.y - other.y));
        }

        /**
         * builds a point from its cartesian coordinates.
         * 
         * @param x the x coordinate
         * @param y the y coordinate
         */
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Same as the constructor before but reads provided scanner for input.
         * 
         * @param scanner the scanner being read
         */
        public Point(Scanner scanner) {
            this.x = scanner.nextDouble();
            this.y = scanner.nextDouble();
        }

    }

    /**
     * This function is required in order to comply with the per-function
     * if statement limit of 4 enforced by the university code standard.
     * 
     * @param h1 collision result for circle 1
     * @param h2 collision result for circle 2
     */
    static void writeCollisionOutputToStdOut(boolean h1, boolean h2) {

        if (h1 && h2) {
            System.out.println("The point hits both circles");
        } else if (h1) {
            System.out.println("The point hits the first circle");
        } else if (h2) {
            System.out.println("The point hits the second circle");
        } else {
            System.out.println("The point does not hit either circle");
        }
    }

    /**
     * This method is not static and therefore allows for instantiation of Points.
     * 
     */
    void run() {

        Scanner scanner = new Scanner(System.in);
        // center of first circle
        Point p1 = new Point(scanner);
        // radius of first circle
        double r1 = scanner.nextDouble();
        // center of second circle
        Point p2 = new Point(scanner);
        // radius of second circle
        double r2 = scanner.nextDouble();
        // point for which we check the collision
        Point collision = new Point(scanner);

        scanner.close();
        if (r1 < 0. || r2 < 0.) {
            System.out.println("input error");
            return;
        }

        boolean h1 = p1.calculateDistance(collision) <= r1;
        boolean h2 = p2.calculateDistance(collision) <= r2;

        writeCollisionOutputToStdOut(h1, h2);
    }

    public static void main(String[] args) {
        new HitDetection().run();
    }
}
