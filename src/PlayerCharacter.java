import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class PlayerCharacter extends PhysicalObject {
    
    // -- Movement variables --

    // The max speed the player can go.
    private float maxSpeed;

    // Rate the player accelerates at. Is added per input.
    private float acceleration;

    // Player's friction when moving around.
    private float friction;

    // How much friction on turning.
    private float turnFriction;

    // How fast the player can turn either direction.
    private float turnRate;

    // Velocity player is turning.
    private float turnVel;

    private float size;

    private int health;
    
    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    private boolean isInvincible;

    private float invincibilityLength;

    private float currentInvincibilityTime;

    /**
     * @param g The current graphics object to draw this player.
     * @param xMax The max X the player can go to.
     * @param yMax The max Y the player can go to.
     */
    public PlayerCharacter(Graphics g, float xMax, float yMax)
    {
        super(g, xMax, yMax);
        maxSpeed = 3;
        acceleration = 0.75f;
        friction = 0.99f;
        turnRate = 0.75f;
        xVelocity = 0.0f;
        yVelocity = 0.0f;
        turnAngle = 0.0f;
        turnFriction = 0.975f;
        health = 3;
        invincibilityLength = 3000;
        size = 15.0f;
    }

    @Override
    public double getXTipLocation()
    {
        return calcNewVerticeX(xLocation, xLocation, yLocation, yLocation-size, turnAngle);
    }

    @Override
    public double getYTipLocation()
    {
        return calcNewVerticeY(xLocation, xLocation, yLocation, yLocation-size, turnAngle);
    }

    // Called each frame. Player should be drawn here.
    @Override
    public void tick(int deltaTime)
    {
        xVelocity = xVelocity*friction;
        yVelocity = yVelocity*friction;

        // Handles hitting edge of the screen.
        if(xLocation > xMax)
        {
            xLocation = xMax;
            xVelocity = -xVelocity;
        }

        if(xLocation < 0)
        {
            xLocation = 0;
            xVelocity = -xVelocity;
        }

        if(yLocation > yMax)
        {
            yLocation = yMax;
            yVelocity = -yVelocity;
        }

        if(yLocation < 0)
        {
            yLocation = 0;
            yVelocity = -yVelocity;
        }

        xLocation += xVelocity;
        yLocation += yVelocity;

        turnVel = turnVel*turnFriction;
        turnAngle += turnVel;
        
        // Initial player color.
        g.setColor(Color.WHITE);

        // Change player color to yellow until invincibilityTime runs out.
        if(isInvincible)
        {
            currentInvincibilityTime -= deltaTime;
            if(currentInvincibilityTime <= 0)
            {
                isInvincible = false;
            }

            g.setColor(Color.YELLOW);
        }

        g.fillPolygon(constructPolygon());
        int xExtended = (int) calcNewVerticeX(xLocation, xLocation, yLocation, yLocation-350, turnAngle);
        int yExtended = (int) calcNewVerticeY(xLocation, xLocation, yLocation, yLocation-350, turnAngle);

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine((int) getXTipLocation(), (int) getYTipLocation(), xExtended, yExtended);
    }

    // Called each time a keyboard input is recieved.
    public void movementInput(char letter)
    {
        switch(letter)
        {
            case('w'):
                if(Math.abs(xVelocity) + Math.abs(yVelocity) < maxSpeed)
                {
                    xVelocity += Math.sin(Math.toRadians(turnAngle)) * acceleration;
                    yVelocity -= Math.cos(Math.toRadians(turnAngle)) * acceleration;
                }
                
            break;

            case('a'):

                if(Math.abs(turnVel) < maxSpeed*1.5)
                {
                    turnVel += -turnRate;
                }
                
            break;

            case('d'):

                if(Math.abs(turnVel) < maxSpeed*1.5)
                {
                    turnVel += turnRate;
                }

            break;

            case('s'):

                if(Math.abs(xVelocity) < maxSpeed)
                {
                    xVelocity -= Math.sin(Math.toRadians(turnAngle)) * acceleration;
                }

                if(Math.abs(yVelocity) < maxSpeed)
                {
                    yVelocity += Math.cos(Math.toRadians(turnAngle)) * acceleration;
                }

            break;
        }
    }

    public void takeDamage()
    {
        if(!isInvincible)
        {
            health--;
            isInvincible = true;
            currentInvincibilityTime = invincibilityLength;
        }

        if(health <= 0)
        {

        }
    }

    @Override
    public Polygon constructPolygon()
    {
        Polygon shape = new Polygon();

        int pointX = (int) calcNewVerticeX(xLocation, xLocation, yLocation, yLocation-size, turnAngle);
        int pointY = (int) calcNewVerticeY(xLocation, xLocation, yLocation, yLocation-size, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation-size/2, yLocation, yLocation+size, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation-size/2, yLocation, yLocation+size, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation, yLocation, yLocation+size/2, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation, yLocation, yLocation+size/2, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation+size/2, yLocation, yLocation+size, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+size/2, yLocation, yLocation+size, turnAngle);

        shape.addPoint(pointX, pointY);

        return shape;
    }
}