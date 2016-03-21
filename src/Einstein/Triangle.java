package Einstein;

/**
 * Created by Paul Cloete on 3/21/2016.
 */

import java.awt.*;
import java.util.Random;

public class Triangle {
    private Point pointA;
    private Point pointB;
    private Point pointC;
    private double alpha;
    private int red;
    private int blue;
    private int green;

    public Triangle(Random randomNumberGenerator, int width, int height){
        this.alpha = randomNumberGenerator.nextDouble() * 10;
        this.red = randomNumberGenerator.nextInt(256);
        this.blue = randomNumberGenerator.nextInt(256);
        this.green = randomNumberGenerator.nextInt(256);

        this.pointA = new Point(randomNumberGenerator.nextInt(width + 1), randomNumberGenerator.nextInt(height + 1));
        this.pointB = new Point(randomNumberGenerator.nextInt(width + 1), randomNumberGenerator.nextInt(height + 1));
        this.pointC = new Point(randomNumberGenerator.nextInt(width + 1), randomNumberGenerator.nextInt(height + 1));
    }

    public Triangle(Point pointA, Point pointB, Point pointC, double alpha, int red, int blue, int green) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC  = pointC;
        this.alpha = alpha;
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    @Override
    public String toString() {
        String pointA = this.pointA.getX() + "," + this.pointA.getY();
        String pointB = this.pointB.getX() + "," + this.pointB.getY();
        String pointC = this.pointC.getX() + "," + this.pointC.getY();
        String fill = "fill=\"rgb(" + this.red + ", " + this.green + ", " + this.blue + ")\"";
        String fillOpacity = "fill-opacity = \"" + this.alpha + "\"";
        return "<polygon points=\" " + pointA + "," + pointB + "," + pointC + "\" " + fill + " " + fillOpacity + " />";
    }
}
