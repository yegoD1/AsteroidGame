import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.geom.Area;

public class Projectile extends PhysicalObject {

    // -- Movement variables --

    // -- Gameplay variables --
    
    // Size of this projectile.
    private int size;

    private boolean isEnemy;

    public void setEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }

    public boolean getIsEnemy()
    {
        return isEnemy;
    }
    
    // Returns true if this projectile is an enemy projectile.
    public boolean getisEnemy()
    {
        return isEnemy;
    }
    

    // Constructor includes options for speed and size as enemies will be using the same class.
    public Projectile(Graphics g, int speed, double angle, int size, float xMax, float yMax) 
    {
        super(g, xMax, yMax);
        this.size = size;

        xVelocity = Math.sin(Math.toRadians(angle)) * speed;
        yVelocity = -Math.cos(Math.toRadians(angle)) * speed;
        turnAngle = angle;
    }

    @Override
    public void tick(int deltaTime)
    {
        move();

        g.setColor(Color.WHITE);
        g.fillPolygon(constructPolygon());
    }

    @Override
    public Polygon constructPolygon()
    {
        // Construct a rectangle.
        Polygon shape = new Polygon();

        int pointX = (int) calcNewVerticeX(xLocation, xLocation-size/4, yLocation, yLocation+size/2, turnAngle);
        int pointY = (int) calcNewVerticeY(xLocation, xLocation-size/4, yLocation, yLocation+size/2, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation+size/4, yLocation, yLocation+size/2, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+size/4, yLocation, yLocation+size/2, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation+size/4, yLocation, yLocation-size/2, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+size/4, yLocation, yLocation-size/2, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation-size/4, yLocation, yLocation-size/2, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation-size/4, yLocation, yLocation-size/2, turnAngle);

        shape.addPoint(pointX, pointY);

        return shape;
    }

    public boolean isIntersecting(PhysicalObject obj)
    {
        Area mainObject = new Area(constructPolygon());
        Area testObject = new Area(obj.constructPolygon());
        mainObject.intersect(testObject);

        if(!mainObject.isEmpty())
        {
            return true;
        }

        return false;
    }

    private void move()
    {
        xLocation += xVelocity;
        yLocation += yVelocity;
    }
}
