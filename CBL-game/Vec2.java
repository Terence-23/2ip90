
/**
 * A 2d vector.
 */
public class Vec2 {
    public double x;
    public double y;

    Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 perpendicular() {
        return new Vec2(y, -x);
    }

    public double length2() {
        return x * x + y * y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 add(Vec2 oth) {
        return new Vec2(x + oth.x, y + oth.y);
    }

    public Vec2 sub(Vec2 oth) {
        return new Vec2(x - oth.x, y - oth.y);
    }

    public Vec2 mul(double oth) {
        return new Vec2(x * oth, y * oth);
    }

    public Vec2 div(double oth) {
        return new Vec2(x / oth, y / oth);
    }

    public double dot(Vec2 oth) {
        return x * oth.x + y * oth.y;
    }

    public Vec2 unit() {
        return this.div(this.length());
    }

    public double cross(Vec2 oth) {
        return x * oth.y - y * oth.x;
    }
}
