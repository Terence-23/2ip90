
/**
 * Parallelogram.
 */
public class Quad implements Collider {
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

    @Override
    public GameObject getObject() {
        return object;
    }

    @Override
    public Collider.ColliderType getType() {
        return ColliderType.Quad;
    }
}
