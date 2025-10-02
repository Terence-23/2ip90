import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;

class GameCanvas extends Canvas {
    private ArrayList<GameObject> objects;

    /**
     * A Canvas for drawing all the in game objects. It should fill the main JFrame.
     * 
     * @param objectList ArrayList of all objects in the game. User of this method
     *                   should keep the array because this object does not
     *                   facilitate any changing of this array.
     *
     */
    GameCanvas(ArrayList<GameObject> objectList) {
        super();
        objects = objectList;
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
