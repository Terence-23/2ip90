
public class Circle implements Collider {

    @Override
    public String toString() {
        return """
                Circle:
                origin: %s
                radius: %f""".formatted(getOrigin().toString(), radius);
    }

    /**
     * checks whether a circle intersects with a quad bounds.
     * 
     * @param c the circle.
     * @param q the quad.
     * @return true if they do intersect else false
     */
    private static boolean checkQuadBounds(Circle c, Quad q) {

        // check all the sides for collision.
        for (int i = 0; i < 4; ++i) {
            // one of the sides fo the Quad
            ClosedLine l = q.makeQuadBound(i);
            if (l.collides(c)) {
                return true;
            }
        }
        return false;
    }

    Vec2 origin;

    double radius;

    GameObject object;

    /**
     * Make a circle.
     * 
     * @param origin origin of the circle.
     * @param radius radius of the circle.
     */
    Circle(Vec2 origin, double radius) {
        this.origin = origin;
        this.radius = radius;
        this.object = new DummyObject();
    }

    /**
     * Make a circle.
     * 
     * @param origin origin of the circle.
     * @param radius radius of the circle.
     * @param object object to which the circle is attached. It affects the circle's
     *               position.
     */
    Circle(Vec2 origin, double radius, GameObject object) {
        this.origin = origin;
        this.radius = radius;
        this.object = object;
    }

    Vec2 getOrigin() {
        return origin.add(object.getPos());
    }

    public boolean collides(Circle c) {
        return getOrigin().sub(c.getOrigin()).length() <= radius + c.radius;
    }

    /**
     * check if circle collides with the quad.
     * 
     * @param q The quad
     * @return true if they collide else false.
     */
    public boolean collides(Quad q) {
        // center of circle in quad
        return q.checkPointIn(getOrigin())
                // origin of quad in circle
                || q.getOrigin().sub(getOrigin()).length2() < this.radius * this.radius
                // one of the bound intersects the circle
                || checkQuadBounds(this, q);

    }

    public boolean collides(Line l) {
        return l.collides(this);
    }

    public boolean collides(ClosedLine l) {
        return l.collides(this);
    }

    @Override
    public GameObject getObject() {
        return object;
    }

    @Override
    public Collider.ColliderType getType() {
        return ColliderType.Circle;
    }
}
