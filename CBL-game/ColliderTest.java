public class ColliderTest {

    public static void main(String[] args) {
        ColliderTest test = new ColliderTest();
        test.testLineIntersectsLine();
        test.testLineDoesNotIntersectParallelLine();
        test.testClosedLineIntersectsCircle();
        test.testCircleDoesNotCollideWithFarCircle();
        test.testCirclecollideWithCircle();
        test.testQuadcollideWithCircle();
        test.testQuadDoesNotCollideWithCircle();
        test.testClosedLineInsideQuad();
        test.testLineDoesNotIntersectQuad();

        System.out.println("✅ All tests passed!");
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("❌ Assertion failed: " + message);
        }
    }

    private void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError("❌ Assertion failed: " + message);
        }
    }

    public void testLineIntersectsLine() {
        // System.err.println(new Vec2(1, 1).cross(new Vec2(1, -1)));
        Collider line1 = new Line(new Vec2(0, 0), new Vec2(1, 1));
        Collider line2 = new Line(new Vec2(0, 1), new Vec2(1, -1));

        assertTrue(line1.collides(line2), "Lines should intersect");
        assertTrue(line2.collides(line1), "Collision should be symmetric");
    }

    public void testLineDoesNotIntersectParallelLine() {
        Collider line1 = new Line(new Vec2(0, 0), new Vec2(1, 0));
        Collider line2 = new Line(new Vec2(0, 1), new Vec2(1, 0));

        assertFalse(line1.collides(line2), "Parallel lines should not intersect");
        assertFalse(line2.collides(line1), "Collision should be symmetric");
    }

    public void testClosedLineIntersectsCircle() {
        Collider closedLine = new ClosedLine(new Vec2(-1, 0), new Vec2(2, 0));
        Collider circle = new Circle(new Vec2(0, 0), 1);

        assertTrue(closedLine.collides(circle), "Closed line should intersect circle");
        assertTrue(circle.collides(closedLine), "Collision should be symmetric");
    }

    public void testCircleDoesNotCollideWithFarCircle() {
        Collider circle1 = new Circle(new Vec2(0, 0), 1);
        Collider circle2 = new Circle(new Vec2(5, 5), 1);

        assertFalse(circle1.collides(circle2), "Circles far apart should not collide");
        assertFalse(circle2.collides(circle1), "Collision should be symmetric");
    }

    public void testCirclecollideWithCircle() {
        Collider circle1 = new Circle(new Vec2(0, 0), 2);
        Collider circle2 = new Circle(new Vec2(3, 0), 2);

        assertTrue(circle1.collides(circle2), "Overlapping circles should collide");
        assertTrue(circle2.collides(circle1), "Collision should be symmetric");
    }

    public void testQuadcollideWithCircle() {
        Collider quad = new Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2));
        Collider circle = new Circle(new Vec2(1, 1), 0.5);

        assertTrue(quad.collides(circle), "Circle inside quadrilateral should collide");
        assertTrue(circle.collides(quad), "Collision should be symmetric");
    }

    public void testQuadDoesNotCollideWithCircle() {
        Collider quad = new Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2));
        Collider circle = new Circle(new Vec2(5, 5), 0.5);

        assertFalse(quad.collides(circle), "Circle far away should not collide with quadrilateral");
        assertFalse(circle.collides(quad), "Collision should be symmetric");
    }

    public void testClosedLineInsideQuad() {
        Collider quad = new Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2));
        Collider closedLine = new ClosedLine(new Vec2(0.5, 0.5), new Vec2(1.5, 1.5));

        assertTrue(quad.collides(closedLine), "Closed line inside quadrilateral should collide");
        assertTrue(closedLine.collides(quad), "Collision should be symmetric");
    }

    public void testLineDoesNotIntersectQuad() {
        Collider quad = new Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2));
        Collider line = new Line(new Vec2(5, 5), new Vec2(1, 0));

        assertFalse(line.collides(quad), "Line far away should not collide with quadrilateral");
        assertFalse(quad.collides(line), "Collision should be symmetric");
    }
}
