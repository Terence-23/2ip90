import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

class GameCanvas extends Canvas implements Runnable {

    private int height;
    private int width;
    private ArrayList<GameObject> objects;
    public boolean clean = true;

    /**
     * A Canvas for drawing all the in game objects. It should fill the main JFrame.
     * 
     * @param objectList ArrayList of all objects in the game. User of this method
     *                   should keep the array because this object does not
     *                   facilitate any changing of this array.
     *
     */

    void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        setSize(width, height);
    }

    int getCanvasWidth() {
        return getWidth();
    }

    int getCanvasHeight() {
        return getHeight();
    }

    /**
     * Create a canvas for a list of objects.
     * 
     * @param objectList the list of objects.
     */
    GameCanvas(ArrayList<GameObject> objectList) {
        super();
        objects = objectList;
    }

    /**
     * Render the Canvas to screen.
     */
    public synchronized void render() {

        BufferStrategy bs;
        Graphics g;
        try {
            bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(2);
                return;
            }
            g = bs.getDrawGraphics();
        } catch (IllegalStateException e) {
            return;
        }
        g.setColor(getBackground());
        // g.clearRect(0, 0, width, height);

        g.setColor(getForeground());
        paint(g);

        g.dispose();
        bs.show();
    }

    public void run() {
        render();
        clean = true;
    }

    /**
     * Function to paint the Canvas onto screen.
     * 
     * @param g the Graphics object used for the painting
     */
    public void paint(Graphics g) {
        synchronized (objects) {
            for (GameObject o : objects) {
                o.draw(g);
            }
        }
    }

}
