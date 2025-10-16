
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

//class for upgrade interactions with the world
class UpgradeObject implements GameObject {
    // the upgrade for which this is a container.
    Upgrade upgrade;
    Vec2 pos;
    double pickupRange = 1;
    Vec2 size = new Vec2(1, 1);
    Rectangle lastDraw;

    @Override
    public Vec2 getPos() {
        return pos;
    }

    @Override
    public void setup() {
    }

    UpgradeObject(Upgrade u, Vec2 pos) {
        upgrade = u;
        this.pos = pos;
    }

    @Override
    public void draw(Graphics g) {

        var rt = GameRuntime.rt;
        final Vec2 CORNER_OFFSET = size.mul(0.5);

        if (lastDraw != null) {

            g.clearRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);
        }

        Vec2 startPos = rt.map_space_to_screen(pos.sub(CORNER_OFFSET));
        Vec2 endPos = rt.map_space_to_screen(pos.add(CORNER_OFFSET));
        g.setColor(Color.green);
        lastDraw = new Rectangle(
                (int) startPos.x,
                (int) startPos.y,
                (int) endPos.x - (int) startPos.x,
                (int) endPos.y - (int) startPos.y);

        g.fillRect(lastDraw.x, lastDraw.y, lastDraw.width, lastDraw.height);

    }

    @Override
    public void update() {
        if (GameRuntime.rt.player.getPos().sub(getPos()).length2() < pickupRange * pickupRange) {

            onCollide(GameRuntime.rt.player);
        }
    }

    @Override
    public void onDestroy() {
        if (lastDraw != null) {
            try {
                var bs = GameRuntime.rt.canvas.getBufferStrategy();
                var g = bs.getDrawGraphics();

                g.clearRect(
                        lastDraw.x - 1,
                        lastDraw.y - 1,
                        lastDraw.width + 2,
                        lastDraw.height + 2);

                g.dispose();
                bs.show();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onCollide(GameObject oth) {
        if (oth instanceof Player) {
            upgrade.onPickup((Player) oth);
            GameRuntime.rt.remove(this);
        }
    }
}
