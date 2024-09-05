import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;

public class EnemyCharacter extends PhysicalObject{

    protected int health;

    protected PlayerCharacter playerToFocus;

    // Lower numbers for turnRate mean faster turning.
    private float turnSlowness;

    // Velocity player is turning.
    private double turnVel;

    public double getTurnVel() {
        return turnVel;
    }

    // Time between shots in milliseconds.
    private float fireRate;

    // How much time is remaining before a new projectile.
    private float timeForProjectile;

    protected GameManager gameManager;

    // How many points to give to the player after they destroy this enemy.
    private int scoreValue;
    
    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public EnemyCharacter(Graphics g, PlayerCharacter playerToFocus, GameManager gameManager, float xMax, float yMax) {
        super(g, xMax, yMax);
        health = 1;
        this.playerToFocus = playerToFocus;
        turnSlowness = 20f;
        fireRate = 3000;
        timeForProjectile = fireRate/2;
        this.gameManager = gameManager;
        scoreValue = 50;
    }

    // Override tick method.
    @Override
    public void tick(int deltaTime) {

        double targetAngle = calcPlayerAngle();

        // Turn strength is the value the AI will turn by.
        double turnStrength = (targetAngle - turnAngle) / turnSlowness;

        turnVel = turnStrength;

        turnAngle += turnVel;

        
        timeForProjectile -= deltaTime;
        // Math.abs(turnVel) is esentially the accuracy the AI has.
        // Lower values means less tolerance for aiming off the player.
        if(timeForProjectile <= 0 && Math.abs(turnVel) < 1)
        {
            timeForProjectile = fireRate;
            fireProjectile();
        }
        
        draw();
    }

    protected void draw()
    {
        // Draw a red triangle to represent the enemy.
        g.setColor(Color.RED);
        g.fillPolygon(constructPolygon());
    }

    // Calculates the angle needed to turn to the player.
    public double calcPlayerAngle()
    {
        // Calculate the angle to the player.
        double angleToPlayer = Math.toDegrees(Math.atan2(playerToFocus.getYLocation() - getYLocation(), playerToFocus.getXLocation() - getXLocation())) + 90;

        return angleToPlayer;
    }

    public void takeDamage()
    {
        // Decrease health and if health is 0, destroy the enemy.
        health--;
        if(health == 0)
        {
            onDestroy();
        }
    }

    protected void onDestroy()
    {
        // Do extra stuff if enemy gets destroyed.
    }

    protected void fireProjectile()
    {   
        gameManager.createProjectile(this, true, 12, 4);
    }

    // Returns where the "tip" of the object would be in X.
    @Override
    public double getXTipLocation()
    {
        return calcNewVerticeX(xLocation, xLocation, yLocation, yLocation-20, turnAngle);
    }
    
    // Returns where the "tip" of the object would be in Y.
    @Override
    public double getYTipLocation()
    {
        return calcNewVerticeY(xLocation, xLocation, yLocation, yLocation-20, turnAngle);
    }

    public boolean isAlive()
    {
        return health > 0;
    }

    @Override
    public Polygon constructPolygon()
    {
        Polygon shape = new Polygon();

        int pointX = (int) calcNewVerticeX(xLocation, xLocation, yLocation, yLocation-20, turnAngle);
        int pointY = (int) calcNewVerticeY(xLocation, xLocation, yLocation, yLocation-20, turnAngle);

        shape.addPoint(pointX, pointY);

        // Begin of left adjacent triangle.
        pointX = (int) calcNewVerticeX(xLocation, xLocation-2, yLocation, yLocation-10, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation-2, yLocation, yLocation-10, turnAngle);

        shape.addPoint(pointX, pointY);

        // Tip of left triangle.
        pointX = (int) calcNewVerticeX(xLocation, xLocation-8, yLocation, yLocation-14, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation-8, yLocation, yLocation-14, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation-4, yLocation, yLocation+2, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation-4, yLocation, yLocation+2, turnAngle);

        shape.addPoint(pointX, pointY);

        // Bottom left corner.
        pointX = (int) calcNewVerticeX(xLocation, xLocation-10, yLocation, yLocation+20, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation-10, yLocation, yLocation+20, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation, yLocation, yLocation+10, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation, yLocation, yLocation+10, turnAngle);

        shape.addPoint(pointX, pointY);

        // Bottom rigth corner.
        pointX = (int) calcNewVerticeX(xLocation, xLocation+10, yLocation, yLocation+20, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+10, yLocation, yLocation+20, turnAngle);

        shape.addPoint(pointX, pointY);

        // Begin of right adjacent triangle.
        pointX = (int) calcNewVerticeX(xLocation, xLocation+4, yLocation, yLocation+2, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+4, yLocation, yLocation+2, turnAngle);

        shape.addPoint(pointX, pointY);

        // Tip of right triangle.
        pointX = (int) calcNewVerticeX(xLocation, xLocation+8, yLocation, yLocation-14, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+8, yLocation, yLocation-14, turnAngle);

        shape.addPoint(pointX, pointY);

        pointX = (int) calcNewVerticeX(xLocation, xLocation+2, yLocation, yLocation-10, turnAngle);
        pointY = (int) calcNewVerticeY(xLocation, xLocation+2, yLocation, yLocation-10, turnAngle);

        shape.addPoint(pointX, pointY);
        
        return shape;
    }
}
