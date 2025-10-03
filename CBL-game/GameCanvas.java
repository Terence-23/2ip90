import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

class GameCanvas extends Canvas {

    private int height;
    private int width;
    private ArrayList<GameObject> objects;

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
    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(getBackground());
        g.clearRect(0, 0, width, height);

        g.setColor(getForeground());
        paint(g);

        g.dispose();
        bs.show();
    }

    /**
     * Function to paint the Canvas onto screen.
     * 
     * @param g the Graphics object used for the painting
     */
    public void paint(Graphics g) {
        for (GameObject o : objects) {
            o.draw(g);
        }
    }

}
