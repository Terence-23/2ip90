
public class ClosedLine extends Line {
    ClosedLine(Vec2 origin, Vec2 direction, GameObject object) {
        super(origin, direction, object);
    }

    ClosedLine(Vec2 origin, Vec2 direction) {
        super(origin, direction);
    }

    @Override
    public ColliderType getType() {
        return ColliderType.ClosedLine;
    }

    @Override
    public boolean collides(Line l) {

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
    public boolean collides(Quad q) {
        for (int i = 0; i < 4; ++i) {
            ClosedLine l = q.makeQuadBound(i);
            if (collides(l)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean collides(Circle circle) {
        double t = collisionT(circle);
        // System.err.println(t);
        return !((Double) t).isNaN() && t >= 0 && t <= 1;

    }

    @Override
    public boolean collides(ClosedLine l) {
        return l.collides((Line) this)
                && collides((Line) l);

    }

}
