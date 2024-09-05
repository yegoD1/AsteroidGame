import java.awt.Graphics;

public class Powerup extends PhysicalObject{

    private int randomNum;

    private int powerupTypes;

    public Powerup(Graphics g, float xMax, float yMax) {
        super(g, xMax, yMax);
        powerupTypes = 2;
        randomNum = (int) Math.random() * powerupTypes+1;
    }

    @Override
    public void tick(int deltaTime)
    {
    
        switch(randomNum)
        {
            case(0):

                

            break;

            case(1):



            break;
        }
    }
}
