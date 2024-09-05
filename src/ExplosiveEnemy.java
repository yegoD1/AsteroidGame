import java.awt.Graphics;
import java.awt.Color;

public class ExplosiveEnemy extends EnemyCharacter {

    public ExplosiveEnemy(Graphics g, PlayerCharacter playerToFocus, GameManager gameManager, float xMax, float yMax) {
        super(g, playerToFocus, gameManager, xMax, yMax);
        setScoreValue(100);
    }

    @Override
    protected void draw()
    {
        g.setColor(Color.ORANGE);
        g.fillPolygon(constructPolygon());
    }

    @Override
    protected void onDestroy()
    {
        double numOfProjectiles = 8;

        double maxAngle = 360/numOfProjectiles;

        double turn = Math.random()*maxAngle;

        for(int i = 0; i < numOfProjectiles; i++)
        {
            gameManager.createProjectile((int) getXLocation(), (int) getYLocation(), turn + maxAngle*i, true, 10, 5);
        }
    }
}
