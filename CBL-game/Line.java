
/**
 * Parametric line.
 */
public class Line implements Collider {
    Vec2 origin;
    Vec2 direction;
    GameObject object;

    public GameObject getObject() {
        return object;
    }

    public ColliderType getType() {
        return ColliderType.Line;
    }

    /**
     * Make a {@link Line}.
     *
     * @param origin    Line's origin.
     * @param direction Line's direction.
     * @param object    Object to which this line is attached. It's position affects
     *                  the line.
     */
    Line(Vec2 origin, Vec2 direction, GameObject object) {
        this.origin = origin;
        this.direction = direction;
        this.object = object;
    }

    /**
     * Make a {@link Line}.
     * 
     * @param origin    The Line's origin.
     * @param direction The Line's direction.
     */
    Line(Vec2 origin, Vec2 direction) {
        this.origin = origin;
        this.direction = direction;
        this.object = new DummyObject();
    }

    /**
     * get the parameter t of collision with another line.
     * 
     * @param l the other line
     * @return value t such that origin + t ⋅ direction is a point on {@link l}. If
     *         there is no collision should result in NaN or +/- inf
     */
    double collisionT(Line l) {

        // from the other line to here;
        Vec2 originOffset = getOrigin().sub(l.getOrigin());
        // vector perpendicular to the direction of the other line. This will determine
        // how many times do we have to use our direction to compensate. Unit length.
        Vec2 normal = l.direction.perpendicular().unit();
        double lengthToDo = normal.dot(originOffset);
        double lengthPerDirection = normal.dot(direction);
        double directionNumber = lengthToDo / lengthPerDirection;
        return directionNumber;
    }

    /**
     * get the parameter t of collision with a circle.
     * 
     * @param circle The circle for which we calculate the collision
     * @return value t such that origin + t ⋅ direction is a point on
     *         {@link circle}. If
     *         there is no collision should result in NaN or +/- inf. Additionally
     *         the value is the smallest positive t for which this is true or
     *         largest negative t if there is no positive t for which this is true.
     */
    double collisionT(Circle circle) {
        Vec2 originDifference = getOrigin().sub(circle.getOrigin());

        // Detailed explanantion can be found at
        // https://raytracing.github.io/books/RayTracingInOneWeekend.html#addingasphere/ray-sphereintersection
        // In short the formula for circle can be expressed as (C-P)⋅(C-P) = r^2 for any
        // circle centered at C with radius r.
        // Therefore the equation for collison takes form of
        // t^2d⋅d−2td⋅(C−Q)+(C−Q)⋅(C−Q)−r^2=0 which is a quadratic equation that is easy
        // to solve

        double a = direction.dot(direction);
        double b = 2 * direction.dot(originDifference);
        double c = originDifference.dot(originDifference) - circle.radius * circle.radius;
        // quadratic formula
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return Double.NaN;
        }
        double t = (-b - Math.sqrt(discriminant)) / (2 * a);
        if (t < 0) {
            t = (-b + Math.sqrt(discriminant) / (2 * a));
        }
        return t;
    }

    /**
     * Check if this line collides with l.
     *
     * @param l the other line
     * @return true if they collide false otherwise
     */
    public boolean collides(Line l) {
        // Parallel, check if colinear.
        if (Math.abs(direction.cross(l.direction)) < 1e-8) {

            // System.err.println("parallel");
            Vec2 originDifference = getOrigin().sub(l.getOrigin());

            // if the difference from one origin to the other is parallel to the direction
            // they are colinear.
            return Math.abs(originDifference.cross(direction)) < 1e-8;
        }
        return true;
    }

    /**
     * check whether the line collides with the quad.
     * 
     * @param q the quad.
     * @return true if they collide else false.
     */
    public boolean collides(Quad q) {
        for (int i = 0; i < 4; ++i) {
            ClosedLine l = q.makeQuadBound(i);
            if (collides(l)) {
                return true;
            }
        }
        return false;
    }

    public boolean collides(ClosedLine l) {
        return l.collides(this);
    }

    /**
     * Check if the line collides with the circle.
     * 
     * @param c the circle
     * @return true if they collide false otherwise.
     */
    public boolean collides(Circle c) {
        Vec2 originDifference = getOrigin().sub(c.getOrigin());
        Vec2 normal = direction.perpendicular().unit();
        // The dot product calculates the distance between circle origin and the line.
        // It can be negative.
        return Math.abs(originDifference.dot(normal)) <= c.radius;
    }

    public Vec2 getOrigin() {
        return this.origin.add(object.getPos());
    }
}
