import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;


public class TopDownGame extends JPanel implements MouseInputListener, KeyListener {
    
    // Controls the size of the window.
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    // Time between ticks in milliseconds.
    private static final int DELTATIME = 10;
    
    private BufferedImage image;
	private Graphics g;

    private GameManager gameManager;
    private PlayerCharacter player;
    public Timer timer;

    public TopDownGame()
    {
        image =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

        player = new PlayerCharacter(g, WIDTH, HEIGHT);
        player.setLocation(WIDTH/2, HEIGHT/2);

        gameManager = new GameManager(g, player, WIDTH, HEIGHT);

        // Listeners for user input. Inputs have to be handled here as no other objects are a JPanel.
        addMouseListener(this);
        addKeyListener(this);

        // Timer that ticks the game.
        timer = new Timer(DELTATIME, new TimerListener());
		timer.start();
    }

    // Essentially, this is the "tick" method. Calls to the gameManager to tick, which is the brains of the game.
    private class TimerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            gameManager.tick(DELTATIME);

            // User interface.
            g.setColor(Color.WHITE);
            
            g.setFont(new Font("Arial", Font.PLAIN, 25));

            g.drawString("Health: " + player.getHealth(), WIDTH - 150, 50);
            
            g.drawString("Score: " + gameManager.getPlayerScore(), WIDTH - 150, 100);

            // 
            if(gameManager.getCurrentCombo() >= gameManager.getMaxCombo()-1)
            {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.ITALIC, 60));
                g.drawString(gameManager.getCurrentCombo() + "x", 50, 85);
                g.setFont(new Font("Arial", Font.ITALIC, 30));
                g.drawString("Combo", 50, 115);  
            }
            else
            {
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.drawString(gameManager.getCurrentCombo() + "x", 50, 75);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("Combo", 50, 95);
            }

            // Used for percentage bars that represent combo.
            float ratio = ((float) gameManager.getCurrentComboVal()/(float) gameManager.getComboVal());

            // Draw combo gauge.
            g.setColor(Color.YELLOW);
            g.fillRect(180, (int) (120-(100*ratio)), 25, (int) (100*ratio));
            g.setColor(Color.GRAY);
            g.fillRect(180, 20, 25, (int) (100-(100*ratio)));

            // Stop entire game once game is finished.
            if(!player.isAlive())
            {
                timer.stop();
                // Draw string that says 'Game Over!';
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(Color.WHITE);
                g.drawString("Game Over!", 275, 50);
            }

            // Repaint.
            repaint();
        }
    }

    public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Top Down Game");
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new TopDownGame());
		frame.setVisible(true);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocus();
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
        // Pass inputs down to player object.
        player.movementInput(e.getKeyChar());
        
        // Spacebar is to fire a projectile.
        if(e.getKeyChar() == ' ')
        {
            gameManager.createProjectile(player, false, 25, 8);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}