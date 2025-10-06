import java.awt.Graphics;

interface Collider {
    enum ColliderType {
        Line,
        ClosedLine,
        Quad,
        Circle,
    }

    class DummyObject implements GameObject {

        @Override
        public Vec2 getPos() {
            return new Vec2(0, 0);
        }

        @Override
        public void setup() {
        }

        @Override
        public void draw(Graphics g) {
        }

        @Override
        public void update() {
        }

    }

    GameObject getObject();

    ColliderType getType();

    /**
     * Generic method for checking collision. Default implementation just calls the
     * specific method for handling collision.
     * 
     * @param c a collider implementing this interface
     * @return true if the colliders collide, else false.
     */
    default boolean collides(Collider c) {
        switch (c.getType()) {
            case ColliderType.Line:
                return collides((Line) c);
            case ColliderType.ClosedLine:
                return collides((ClosedLine) c);
            case ColliderType.Quad:
                return collides((Quad) c);
            case ColliderType.Circle:
                return collides((Circle) c);
            default:
                break;
        }
        throw new RuntimeException("""
                Invalid collider type. Implementing additional collider types requires the\
                modification of ColliderType enum and addition of methods\
                for handling collidon with said collider to the Collider interface.""");
    }

    boolean collides(Line c);

    boolean collides(ClosedLine c);

    boolean collides(Quad c);

    boolean collides(Circle c);
}
