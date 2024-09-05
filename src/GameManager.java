import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics;

/**
 * GameManager is the class that handles the logistics of the game.
 * Here is where objects tick, projectiles are evaluated, and so on.
 */
public class GameManager extends Tickable {

    // -- Object Handling --

    // Since there are going to be many objects on screen of different class types,
    // use the parent class and call tick.
    private ArrayList<Tickable> objects;
    // Will add every Tickable in this list to the objects array on the next frame.
    // Done to prevent comod error.
    private ArrayList<Tickable> pendingObjects;

    // Projectiles will also be added to objects array, but is also added here to
    // be specifically evaluated.
    private ArrayList<Projectile> playerProjectiles;
    // Done to prevent comod error.
    private ArrayList<Projectile> pendingProjectiles;
    // Projectiles that are being removed.
    private ArrayList<Projectile> removingProjectiles;

    // Enemy projectiles are also added here.
    private ArrayList<Projectile> enemyProjectiles;
    // Done to prevent comod error.
    private ArrayList<Projectile> pendingEnemyProjectiles;

    // All enemy characters on the screen.
    private ArrayList<EnemyCharacter> enemies;

    private ArrayList<GameLevel> gameLevels;

    private PlayerCharacter player;

    private Graphics g;

    // -- Gameplay --

    private int gameWidth;
    private int gameHeight;

    // The time betweeen new enemy spawns in milliseconds
    private int enemySpawnRate;

    // Current time before a new enemy spawn.
    private int currentSpawnTime;

    private int currentGameLevel;

    // -- Scoring --

    private int playerScore;

    public int getPlayerScore() {
        return playerScore;
    }

    // Player's current combo.
    private int currentCombo;

    public int getCurrentCombo()
    {
        return currentCombo;
    }

    // Max combo player can go to.
    private int maxCombo;

    public int getMaxCombo() {
        return maxCombo;
    }

    // How many points are needed to reach next combo level.
    private int comboVal;

    public int getComboVal() {
        return comboVal;
    }

    private int currentComboVal;

    public int getCurrentComboVal() {
        return currentComboVal;
    }

    // Value at which combo regresses.
    private int comboRegressionVal;

    // Time between regression ticks (milliseconds).
    private int comboRegressionInterval;
    
    // Current time before regression tick.
    private int currentComboTime;

    // Time between powerup spawns.
    private int powerupInterval;
    
    // Current time before spawning a power-up.
    private int powerupTime;

    /**
     * GameManager is the class that handles the logistics of the game.
     * Here is where objects tick, projectiles are evaluated, and so on.
     */
    public GameManager(Graphics g, PlayerCharacter currentPlayer, int gameWidth, int gameHeight) {
        // Array initialization
        objects = new ArrayList<Tickable>();
        pendingObjects = new ArrayList<Tickable>();
        playerProjectiles = new ArrayList<Projectile>();
        pendingProjectiles = new ArrayList<Projectile>();
        removingProjectiles = new ArrayList<Projectile>();
        enemyProjectiles = new ArrayList<Projectile>();
        pendingEnemyProjectiles = new ArrayList<Projectile>();
        enemies = new ArrayList<EnemyCharacter>();
        gameLevels = new ArrayList<GameLevel>();

        // Save player for future use.
        player = currentPlayer;
        registerNewObject(player);

        this.g = g;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        currentGameLevel = 0;

        setupLevels();

        enemySpawnRate = gameLevels.get(0).getEnemySpawnRate();
        currentSpawnTime = enemySpawnRate;

        playerScore = 0;

        // Combo setup
        currentCombo = 1;
        maxCombo = 5;
        comboVal = 350;
        comboRegressionVal = 50;
        comboRegressionInterval = 1750;
        currentComboTime = comboRegressionInterval;

        powerupInterval = 7500;
        powerupTime = powerupInterval;
    }

    // This is the main tick, then it passes that tick down to each object.
    @Override
    public void tick(int deltaTime) {
        
        /*
        Iterator<Tickable> tickIt = objects.iterator();
        // Ticks each Tickable.
        
        while(tickIt.hasNext())
        {
            Tickable tickableObject = tickIt.next();

            if (!tickableObject.ignoreTick) {
                tickableObject.tick(deltaTime);
            }
        }
        */

        // -- Pending objects handling --
        // Add to respective arrays before starting to iterate as it will cause an error.
        if(pendingObjects.size() > 0)
        {
            for (Tickable tickableObject : pendingObjects) {
                objects.add(tickableObject);
            }
            pendingObjects.clear();
        }

        if(pendingProjectiles.size() > 0)
        {
            for (Projectile projectileObject : pendingProjectiles)
            {
                playerProjectiles.add(projectileObject);
            }
            pendingProjectiles.clear();
        }

        if(pendingEnemyProjectiles.size() > 0)
        {
            for(Projectile projectileObject : pendingEnemyProjectiles)
            {
                enemyProjectiles.add(projectileObject);
            }
            pendingEnemyProjectiles.clear();
        }

        // Going to use a regular for loop as new items may be added to tick list on same frame (which would cause an error).
        // New objects will be ticked next frame.
        for (int i = 0; i < objects.size(); i++) {
            Tickable tickableObject = objects.get(i);
            if(!tickableObject.ignoreTick)
            {
                tickableObject.tick(deltaTime);
            }
        }


        // Decrease spawn time by deltatime until it reaches 0, then spawn a new enemy.
        currentSpawnTime -= deltaTime;
        if (currentSpawnTime <= 0) {
            currentSpawnTime = enemySpawnRate;
            spawnEnemy();
        }

        // Removes projectile if it is off the screen.
        // Need to use an iterator as array may shrink during iteration.
        // https://stackoverflow.com/questions/1196586/calling-remove-in-foreach-loop-in-java

        Iterator<Projectile> i = playerProjectiles.iterator();
        // Projectile evaluation.
        while (i.hasNext()) {
            Projectile projectileObject = i.next();

            // Iterate through every enemy to see if current projectile is hitting anything.
            Iterator<EnemyCharacter> j = enemies.iterator();
            while (j.hasNext()) {

                EnemyCharacter enemyObject = j.next();
                if (projectileObject.isIntersecting(enemyObject)) {

                    enemyObject.takeDamage();

                    // Remove from objects list.
                    removeObject(projectileObject);
                    i.remove();

                    // Here is where enemies gets destroyed.
                    if (!enemyObject.isAlive()) {
                        // Removes object from tick list and from enemy list.
                        earnScore(enemyObject.getScoreValue());
                        j.remove();
                        removeObject(enemyObject);
                    }
                }
            }

            // Check if current projectile is hitting any borders of the screen.
            if (projectileObject.getXLocation() > gameWidth || projectileObject.getXLocation() < 0) {
                removeObject(projectileObject);
                i.remove();
            } else if (projectileObject.getYLocation() > gameHeight || projectileObject.getYLocation() < 0) {
                removeObject(projectileObject);
                i.remove();
            }
        }

        // Cleanup projectiles.
        for (Projectile playerProjectile : removingProjectiles) {
            playerProjectiles.remove(playerProjectile);
        }

        // Enemy projectile evaluation.
        Iterator<Projectile> j = enemyProjectiles.iterator();
        while(j.hasNext())
        {
            Projectile projectileObject = j.next();
            if(projectileObject.isIntersecting(player))
            {
                player.takeDamage();
                removeObject(projectileObject);
                j.remove();
            }
        }

        // Combo evaluation.
        evaluateCombo(deltaTime);

        evaluatePowerup(deltaTime);

        if(enemies.size() == 0 && gameLevels.get(currentGameLevel).isLevelFinished())
        {
            currentGameLevel++;
            enemySpawnRate = gameLevels.get(currentGameLevel).getEnemySpawnRate();
            System.out.println("Moving to level " + (currentGameLevel+1));
        }

    }

    private void evaluatePowerup(int deltaTime)
    {
        powerupTime -= deltaTime;
        
        if(powerupTime <= 0)
        {
            // Reset time.
            powerupTime = powerupInterval;      
        }
    }

    private void setupLevels()
    {
        for(int i = 1; i < 10; i++)
        {
            // 0-3 or levels 1-3.
            // Higher levels spawn enemies faster.
            if(i < 3)
            {
                gameLevels.add(new GameLevel((int)(2000/(i*0.5)), 5*i, 0.20f, false));
            }
            else if(i >=3 && i<7)
            {
                gameLevels.add(new GameLevel((int)(2000/(i*0.5)), 5*i, 0.40f, false));
            }
            else if(i>=7)
            {
                gameLevels.add(new GameLevel((int)(2000/(i*0.5)), 5*i, 0.55f, true));
            }
           
        }
    }

    // Here is where combo evaluation is handled.
    private void evaluateCombo(int deltaTime)
    {
        if(currentCombo < maxCombo && currentComboVal >= comboVal)
        {
            currentComboVal = 0;
            currentCombo++;
            currentComboTime = comboRegressionInterval;
        }

        // Only degrade combo if above 1.
        if(currentCombo > 1)
        {
            currentComboTime -= deltaTime;
            if(currentComboTime <= 0)
            {
                // Reset time.
                currentComboTime = comboRegressionInterval;
                currentComboVal -= comboRegressionVal;

                // Degrade combo if val is 0 or less.
                if(currentComboVal < 0)
                {
                    currentComboVal = comboVal - comboRegressionVal;
                    currentCombo--;
                }
            }
        }
    }

    /**
     * Registers a new Tickable to the GameManager.
     * 
     * @param obj The object to register.
     */
    public void registerNewObject(Tickable obj) {
        pendingObjects.add(obj);
    }

    /**
     * Removes object from tick list.
     * 
     * @param obj
     */
    public void removeObject(Tickable obj) {
        objects.remove(obj);
    }

    /**
     * Creates a new projectile from the supplied object.
     * 
     * @param obj Object projectile is firing from.
     * @param isEnemy If true, this projectile was fired from an enemy.
     * @param size Size of the projectile.
     * @param speed How fast this projectile will move.
     */
    public void createProjectile(PhysicalObject obj, boolean isEnemy, int size, int speed) {

        Projectile projectile = new Projectile(g, speed, obj.getTurnAngle(), size, gameWidth, gameHeight);
        projectile.setLocation(obj.getXTipLocation(), obj.getYTipLocation());

        // Register to list.
        registerNewObject(projectile);

        if (isEnemy) {
            enemyProjectiles.add(projectile);
        } else {
            pendingProjectiles.add(projectile);
        }
    }

    /**
     * Spawns a new projectile at a specified location and angle.
     * @param x X location of projectile.
     * @param y Y location of projectile.
     * @param angle Angle (in degrees) of the projectile.
     * @param isEnemy If true, this projectile was fired from an enemy.
     * @param size Size of the projectile.
     * @param speed How fast this projectile will move.
     */
    public void createProjectile(int x, int y, double angle, boolean isEnemy, int size, int speed)
    {
        Projectile projectile = new Projectile(g, speed, angle, size, gameWidth, gameHeight);
        projectile.setLocation(x, y);

        // Register to list.
        registerNewObject(projectile);

        if (isEnemy) {
            enemyProjectiles.add(projectile);
        } else {
            pendingProjectiles.add(projectile);
        }
    }

    /**
     * Removes projectile from the game. Will also remove it from ticking objects.
     * 
     * @param proj The projectile to remove.
     */
    public void removeProjectile(Projectile projectileObject) {
        removingProjectiles.add(projectileObject);
        removeObject(projectileObject);
    }

    /**
     * Spawns a new enemy 
     */
    public void spawnEnemy() {
        
        // Only spawn a new enemy if haven't reach limit yet.
        GameLevel level = gameLevels.get(currentGameLevel);
        if(!level.isLevelFinished())
        {
            if(Math.random() <= level.getSpecialEnemyChance())
            {
                // Randomly pick a special enemy to spawn.
                double val = Math.random();

                EnemyCharacter enemy = new EnemyCharacter(g, player, this, gameWidth, gameHeight);

                if(val <= 0.5)
                {
                    enemy = new ExplosiveEnemy(g, player, this, gameWidth, gameHeight);
                    
                }
                else if(val > 0.5)
                {
                    enemy = new ShotgunEnemy(g, player, this, gameWidth, gameHeight);
                }
                
                enemy.setLocation((int) (Math.random() * (gameWidth-200) + 100), (int) (Math.random() * (gameHeight-200) + 100));
                registerNewObject(enemy);
                enemies.add(enemy);
            }
            else
            {
                EnemyCharacter enemy = new EnemyCharacter(g, player, this, gameWidth, gameHeight);
                // Set a random location for the enemy.
                enemy.setLocation((int) (Math.random() * (gameWidth-200) + 100), (int) (Math.random() * (gameHeight-200) + 100));
                registerNewObject(enemy);
                enemies.add(enemy);
            }
            level.iterateSpawnedEnemies();
        }
    }

    /**
     * Adds inputted score to player score.
     * Combo also gets evaluated here as enemies will immediately give score on destroy.
     * @param scoreToGive Amount of score to give to the player.
     */
    private void earnScore(int scoreToGive)
    {
        if(currentCombo <= maxCombo && currentComboVal < comboVal)
        {
            currentComboVal += scoreToGive;
        }

        // Reset regression time.
        currentComboTime = comboRegressionInterval;

        playerScore += scoreToGive*currentCombo;
    }
}