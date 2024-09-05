import java.awt.Color;
import java.awt.Graphics;

public class ShotgunEnemy extends EnemyCharacter {

    public ShotgunEnemy(Graphics g, PlayerCharacter playerToFocus, GameManager gameManager, float xMax, float yMax) {
        super(g, playerToFocus, gameManager, xMax, yMax);
        setScoreValue(100);
    }

    @Override
    protected void fireProjectile()
    {
        for(int i = 0; i < 3; i++)
        {
            gameManager.createProjectile((int) getXTipLocation(), (int) getYTipLocation(), getTurnAngle()-15+(15*i), true, 10, 4);
        }
    }

    protected void draw()
    {
        g.setColor(Color.RED);
        g.drawPolygon(constructPolygon());
    }
    
}
