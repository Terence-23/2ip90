/**
 * General class for describing objects that can collide.
 */
class Collider {
    /**
     * Parametric line.
     */
    public static class Line {
        Vec2 origin;
        Vec2 direction;
        GameObject object;

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
        boolean collides(Line l) {
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
        boolean collides(Quad q) {
            for (int i = 0; i < 4; ++i) {
                ClosedLine l = q.makeQuadBound(i);
                if (collides(l)) {
                    return true;
                }
            }
            return false;
        }

        boolean collides(ClosedLine l) {
            return l.collides(this);
        }

        /**
         * Check if the line collides with the circle.
         * 
         * @param c the circle
         * @return true if they collide false otherwise.
         */
        boolean collides(Circle c) {
            Vec2 originDifference = getOrigin().sub(c.getOrigin());
            Vec2 normal = direction.perpendicular().unit();
            // The dot product calculates the distance between circle origin and the line.
            // It can be negative.
            return Math.abs(originDifference.dot(normal)) <= c.radius;
        }

        Vec2 getOrigin() {
            return this.origin.add(object.getPos());
        }
    }

    // this line only goes from origin to origin+direction
    public static class ClosedLine extends Line {
        ClosedLine(Vec2 origin, Vec2 direction, GameObject object) {
            super(origin, direction, object);
        }

        ClosedLine(Vec2 origin, Vec2 direction) {
            super(origin, direction);
        }

        @Override
        boolean collides(Collider.Line l) {

            // if the infinte lines don't collide no reason to check if their segments do
            if (!super.collides(l)) {
                return false;
            }

            double directionNumber = collisionT(l);
            // As the line goes only from origin to origin +direction the line is expressed
            // as origin + t*direction for any t in [0,1]
            return !((Double) directionNumber).isNaN()
                    && directionNumber >= 0. && directionNumber <= 1.;

        }

        /**
         * check whether the line collides with the quad.
         * 
         * @param q the quad.
         * @return true if they collide else false.
         */
        boolean collides(Quad q) {
            for (int i = 0; i < 4; ++i) {
                ClosedLine l = q.makeQuadBound(i);
                if (collides(l)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        boolean collides(Circle circle) {
            double t = collisionT(circle);
            // System.err.println(t);
            return !((Double) t).isNaN() && t >= 0 && t <= 1;

        }

        @Override
        boolean collides(ClosedLine l) {
            return l.collides((Line) this)
                    && collides((Line) l);

        }

    }

    public static class Circle {

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

        boolean collides(Circle c) {
            return getOrigin().sub(c.getOrigin()).length() <= radius + c.radius;
        }

        /**
         * check if circle collides with the quad.
         * 
         * @param q The quad
         * @return true if they collide else false.
         */
        boolean collides(Quad q) {

            return q.checkPointIn(getOrigin()) || checkQuadBounds(this, q);
        }

        boolean collides(Line l) {
            return l.collides(this);
        }

        boolean collides(ClosedLine l) {
            return l.collides(this);
        }
    }

    /**
     * Parallelogram.
     */
    public static class Quad {
        Vec2 origin;
        // the sides of the quadrilateral
        Vec2 u;
        Vec2 v;
        GameObject object;

        Vec2 getOrigin() {
            return origin.add(object.getPos());
        }

        Quad(Vec2 origin, Vec2 u, Vec2 v, GameObject object) {
            this.origin = origin;
            this.u = u;
            this.v = v;
            this.object = object;
        }

        Quad(Vec2 origin, Vec2 u, Vec2 v) {
            this.origin = origin;
            this.u = u;
            this.v = v;
            object = new DummyObject();
        }

        /**
         * makes the ith side of the quad.
         * 
         * @param i the number of the side should be in [1,4]
         * @return the ith side of the quad.
         */
        public ClosedLine makeQuadBound(int i) {
            Vec2 primary = ((i & 1) != 0) ? u : v;
            Vec2 secondary = ((i & 1) == 0) ? u : v;
            Vec2 lOrigin = ((i & 2) != 0) ? secondary : new Vec2(0, 0);
            return new ClosedLine(lOrigin, primary, object);
        }

        /**
         * checks whether a point is in quad.
         * 
         * @param v the point in global space.
         * @return true if the point is in the quad.
         */
        public boolean checkPointIn(Vec2 v) {

            Vec2 movedOrigin = v.sub(getOrigin());
            double normalLength = u.cross(this.v);
            // Check if the circle origin is in the quadrilateral
            var upos = movedOrigin.cross(this.v);
            var vpos = u.cross(movedOrigin);
            // System.out.println("Check point in quad");
            // System.out.println(upos);
            // System.out.println(vpos);
            // System.out.println(normalLength);
            return upos <= normalLength && upos >= 0
                    && vpos >= 0 && vpos <= normalLength;
        }

        public boolean collides(ClosedLine l) {
            return l.collides(this);
        }

        public boolean collides(Circle l) {
            return l.collides(this);
        }

        public boolean collides(Line l) {
            return l.collides(this);
        }

        /**
         * checks whether two Quads collide.
         * 
         * @param q the other quad
         * @return true if they collide else false
         */
        public boolean collides(Quad q) {
            // check whether one center is in the other quad.
            var center = getOrigin().add(u.mul(0.5)).add(v.mul(0.5));
            var qCenter = q.getOrigin().add(q.u.mul(0.5)).add(q.v.mul(0.5));
            if (q.checkPointIn(center) || checkPointIn(qCenter)) {
                return true;
            }
            return border_check(q);
        }

        private boolean border_check(Quad q) {
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    ClosedLine l1 = this.makeQuadBound(i);
                    ClosedLine l2 = this.makeQuadBound(j);
                    if (l1.collides(l2)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static enum Type {
        Circle,
        Line,
        ClosedLine,
        Quad
    }

    private static class DummyObject implements GameObject {

        @Override
        public Vec2 getPos() {
            return new Vec2(0, 0);
        }

        @Override
        public void setup() {
        }

        @Override
        public void draw() {
        }

        @Override
        public void update() {
        }
    }

    /**
     * collides a line with this collider.
     *
     * @param c this line.
     * @return true if they collide else false.
     */
    boolean collide(Line c) {
        switch (type) {
            case Type.Line:
                Line l = getLine();
                return c.collides(l);
            case Type.ClosedLine:
                ClosedLine cl = getClosedLine();
                return c.collides(cl);
            case Type.Quad:
                Quad q = getQuad();
                return c.collides(q);
            case Type.Circle:
                Circle ci = getCircle();
                return c.collides(ci);
            default:
                return false;
        }
    }

    /**
     * collides a closed line with this collider.
     *
     * @param c this line.
     * @return true if they collide else false.
     */
    boolean collide(ClosedLine c) {
        switch (type) {
            case Type.Line:
                Line l = getLine();
                return c.collides(l);
            case Type.ClosedLine:
                ClosedLine cl = getClosedLine();
                return c.collides(cl);
            case Type.Quad:
                Quad q = getQuad();
                return c.collides(q);
            case Type.Circle:
                Circle ci = getCircle();
                return c.collides(ci);
            default:
                return false;
        }
    }

    /**
     * collides a quad with this collider.
     *
     * @param c this Quad.
     * @return true if they collide else false.
     */
    boolean collide(Quad c) {
        switch (type) {
            case Type.Line:
                Line l = getLine();
                return c.collides(l);
            case Type.ClosedLine:
                ClosedLine cl = getClosedLine();
                return c.collides(cl);
            case Type.Quad:
                Quad q = getQuad();
                return c.collides(q);
            case Type.Circle:
                Circle ci = getCircle();
                return c.collides(ci);
            default:
                return false;
        }
    }

    /**
     * collides a circle with this collider.
     *
     * @param c this circle.
     * @return true if they collide else false.
     */
    boolean collide(Circle c) {

        switch (type) {
            case Type.Line:
                Line l = getLine();
                return c.collides(l);
            case Type.ClosedLine:
                ClosedLine cl = getClosedLine();
                return c.collides(cl);
            case Type.Quad:
                Quad q = getQuad();
                return c.collides(q);
            case Type.Circle:
                Circle ci = getCircle();
                return c.collides(ci);
            default:
                return false;
        }
    }

    /**
     * Collides both colliders.
     * 
     * @param c the other collider.
     * @return true if they collide else false.
     */
    boolean collide(Collider c) {
        switch (type) {
            case Type.Line:
                Line l = getLine();
                return c.collide(l);
            case Type.ClosedLine:
                ClosedLine cl = getClosedLine();
                return c.collide(cl);
            case Type.Quad:
                Quad q = getQuad();
                return c.collide(q);
            case Type.Circle:
                Circle ci = getCircle();
                return c.collide(ci);
            default:
                return false;
        }
    }

    // type of the collider
    private Type type;
    // stored collider of unknown type
    private Object val;

    /**
     * Make a Collider containing a circle.
     * 
     * @param c the circle.
     */
    Collider(Circle c) {
        val = c;
        type = Type.Circle;
    }

    /**
     * Make a Collider containing a Line.
     * 
     * @param c the line.
     */
    Collider(Line c) {
        val = c;
        type = Type.Line;
    }

    /**
     * Make a Collider containing a ClosedLine.
     * 
     * @param c the ClosedLine.
     */
    Collider(ClosedLine c) {
        val = c;
        type = Type.ClosedLine;
    }

    /**
     * Make a Collider containing a Quad.
     * 
     * @param c the quad.
     */
    Collider(Quad c) {
        val = c;
        type = Type.Quad;
    }

    /**
     * get the inner circle if this collider contains a circle else null.
     * 
     * @return circle if the collider is a circle else null
     */
    public Circle getCircle() {
        if (type == Type.Circle) {
            return (Circle) val;
        }
        return null;
    }

    public Type getType() {
        return type;
    }

    /**
     * get the inner quad if this collider contains a quad else null.
     * 
     * @return quad if the collider is a quad else null
     */
    public Quad getQuad() {
        if (type == Type.Quad) {
            return (Quad) val;
        }
        return null;
    }

    /**
     * get the inner quad if this collider contains a quad else null.
     * 
     * @return quad if the collider is a quad else null
     */
    public Line getLine() {
        if (type == Type.Line) {
            return (Line) val;
        }
        return null;
    }

    /**
     * get the inner quad if this collider contains a quad else null.
     * 
     * @return quad if the collider is a quad else null
     */
    public ClosedLine getClosedLine() {
        if (type == Type.ClosedLine) {
            return (ClosedLine) val;
        }
        return null;
    }

    public Object getVal() {
        return val;
    }

}
