import java.util.ArrayList;

/**
 * CollisionLayer
 */
public class CollisionLayer {

    ArrayList<Collider> colliders = new ArrayList<>();

    void add(Collider col) {
        colliders.add(col);
    }

    /**
     * checks whether obejcts on the layer collide with given collider. Executes on
     * hit callback.
     * 
     * @param col the collide rto be checked against the layer.
     */
    void collide_with(Collider col) {
        for (Collider collider : colliders) {
            if (collider.collides(col)) {
                GameObject o1 = collider.getObject();
                GameObject o2 = col.getObject();

                o1.onCollide(o2);
                o2.onCollide(o1);
            }
        }
    }

}
