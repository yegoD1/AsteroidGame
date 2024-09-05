import java.awt.Graphics;
import java.awt.Polygon;

// Class that is useful for representing a physical object on the screen.
public class PhysicalObject extends Tickable{
    
    // -- Protected variables --
    // Going to be protected as it needs to be easily accessed from child classes.

    // Going to save graphics as it is easier to work that way.
    protected Graphics g;

    // X location of the object.
    protected double xLocation;

    public double getXLocation() {
        return xLocation;
    }

    // Y location of the object.
    protected double yLocation;

    public double getYLocation() {
        return yLocation;
    }

    // The max value xLocation can be.
    protected float xMax;

    public float getxMax() {
        return xMax;
    }

    // The max value yLocation can be.
    protected float yMax;

    public float getyMax() {
        return yMax;
    }
    
    // Current xVelocity.
    protected double xVelocity;

    public double getxVelocity() {
        return xVelocity;
    }

    // Current yVelocity.
    protected double yVelocity;

    public double getyVelocity() {
        return yVelocity;
    }

    // Current angle this object is at.
    protected double turnAngle;

    public double getTurnAngle() {
        return turnAngle;
    }

    // Returns where the "tip" of the object would be in X. This can be overriden in child classes.
    public double getXTipLocation()
    {
        return xLocation;
    }
    
    // Returns where the "tip" of the object would be in Y. This can be overriden in child classes.
    public double getYTipLocation()
    {
        return yLocation;
    }

    public PhysicalObject(Graphics g, float xMax, float yMax)
    {
        this.g = g;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    // Make sure to call this to set correct location.
    public void setLocation(double x, double y)
    {
        xLocation = x;
        yLocation = y;
    }

    // Rotate verticies of polygon around another point.
    // newX = centerX + (point2x-centerX)*Math.cos(x) - (point2y-centerY)*Math.sin(x);
    // newY = centerY + (point2x-centerX)*Math.sin(x) + (point2y-centerY)*Math.cos(x);
    // https://stackoverflow.com/questions/12161277/how-to-rotate-a-vertex-around-a-certain-point

    // Used for rotating a point around another point. Use with calcNewVerticeY.
    protected double calcNewVerticeX(double centerPointX, double pointToRotateX, double centerPointY, double pointToRotateY, double angle)
    {
        // newX = centerX + (point2x-centerX)*Math.cos(x) - (point2y-centerY)*Math.sin(x);
        return (centerPointX + (pointToRotateX-centerPointX)*Math.cos(Math.toRadians(angle)) - (pointToRotateY-centerPointY)*Math.sin(Math.toRadians(angle)));
    }

    protected double calcNewVerticeY(double centerPointX, double pointToRotateX, double centerPointY, double pointToRotateY, double angle)
    {
        // newY = centerY + (point2x-centerX)*Math.sin(x) + (point2y-centerY)*Math.cos(x);
        return (centerPointY + (pointToRotateX-centerPointX)*Math.sin(Math.toRadians(angle)) + (pointToRotateY-centerPointY)*Math.cos(Math.toRadians(angle)));
    }

    // Needs to be overrided in child classes.
    public Polygon constructPolygon()
    {
        Polygon newPolygon = new Polygon();

        return newPolygon;
    }

}
