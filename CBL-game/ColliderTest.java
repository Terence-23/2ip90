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
        Collider line1 = new Collider(new Collider.Line(new Vec2(0, 0), new Vec2(1, 1)));
        Collider line2 = new Collider(new Collider.Line(new Vec2(0, 1), new Vec2(1, -1)));

        assertTrue(line1.collide(line2), "Lines should intersect");
        assertTrue(line2.collide(line1), "Collision should be symmetric");
    }

    public void testLineDoesNotIntersectParallelLine() {
        Collider line1 = new Collider(new Collider.Line(new Vec2(0, 0), new Vec2(1, 0)));
        Collider line2 = new Collider(new Collider.Line(new Vec2(0, 1), new Vec2(1, 0)));

        assertFalse(line1.collide(line2), "Parallel lines should not intersect");
        assertFalse(line2.collide(line1), "Collision should be symmetric");
    }

    public void testClosedLineIntersectsCircle() {
        Collider closedLine = new Collider(new Collider.ClosedLine(new Vec2(-1, 0), new Vec2(2, 0)));
        Collider circle = new Collider(new Collider.Circle(new Vec2(0, 0), 1));

        assertTrue(closedLine.collide(circle), "Closed line should intersect circle");
        assertTrue(circle.collide(closedLine), "Collision should be symmetric");
    }

    public void testCircleDoesNotCollideWithFarCircle() {
        Collider circle1 = new Collider(new Collider.Circle(new Vec2(0, 0), 1));
        Collider circle2 = new Collider(new Collider.Circle(new Vec2(5, 5), 1));

        assertFalse(circle1.collide(circle2), "Circles far apart should not collide");
        assertFalse(circle2.collide(circle1), "Collision should be symmetric");
    }

    public void testCirclecollideWithCircle() {
        Collider circle1 = new Collider(new Collider.Circle(new Vec2(0, 0), 2));
        Collider circle2 = new Collider(new Collider.Circle(new Vec2(3, 0), 2));

        assertTrue(circle1.collide(circle2), "Overlapping circles should collide");
        assertTrue(circle2.collide(circle1), "Collision should be symmetric");
    }

    public void testQuadcollideWithCircle() {
        Collider quad = new Collider(new Collider.Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2)));
        Collider circle = new Collider(new Collider.Circle(new Vec2(1, 1), 0.5));

        assertTrue(quad.collide(circle), "Circle inside quadrilateral should collide");
        assertTrue(circle.collide(quad), "Collision should be symmetric");
    }

    public void testQuadDoesNotCollideWithCircle() {
        Collider quad = new Collider(new Collider.Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2)));
        Collider circle = new Collider(new Collider.Circle(new Vec2(5, 5), 0.5));

        assertFalse(quad.collide(circle), "Circle far away should not collide with quadrilateral");
        assertFalse(circle.collide(quad), "Collision should be symmetric");
    }

    public void testClosedLineInsideQuad() {
        Collider quad = new Collider(new Collider.Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2)));
        Collider closedLine = new Collider(new Collider.ClosedLine(new Vec2(0.5, 0.5), new Vec2(1.5, 1.5)));

        assertTrue(quad.collide(closedLine), "Closed line inside quadrilateral should collide");
        assertTrue(closedLine.collide(quad), "Collision should be symmetric");
    }

    public void testLineDoesNotIntersectQuad() {
        Collider quad = new Collider(new Collider.Quad(new Vec2(0, 0), new Vec2(2, 0), new Vec2(0, 2)));
        Collider line = new Collider(new Collider.Line(new Vec2(5, 5), new Vec2(1, 0)));

        assertFalse(line.collide(quad), "Line far away should not collide with quadrilateral");
        assertFalse(quad.collide(line), "Collision should be symmetric");
    }
}
