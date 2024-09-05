public class GameLevel {
    private int enemySpawnRate;

    public int getEnemySpawnRate() {
        return enemySpawnRate;
    }

    private int numEnemyToSpawn;

    public int getNumEnemyToSpawn() {
        return numEnemyToSpawn;
    }

    private int numEnemySpawned;

    public int getNumEnemySpawned() {
        return numEnemySpawned;
    }

    /**
     * Chances that a special enemy will spawn.
     */
    private float specialEnemyChance;

    public float getSpecialEnemyChance() {
        return specialEnemyChance;
    }

    /**
     * If true, a boss will be spawned this level.
     */
    private boolean spawnBoss;

    public boolean getSpawnBoss()
    {
        return spawnBoss;
    }

    /**
     * A GameLevel represents a certain level in the game. Adjusts how fast enemies spawn, how many are spawned,
     * if a boss spawns and so on.
     * @param enemySpawnRate How fast enemies spawn, in milliseconds.
     * @param numEnemyToSpawn Number of enemies to spawn this level.
     * @param specialEnemyChance Between 0-1, determines the odds of a special enemy spawning.
     * @param spawnBoss If true, a boss will be spawned this level.
     */
    public GameLevel(int enemySpawnRate, int numEnemyToSpawn, float specialEnemyChance, boolean spawnBoss)
    {
        this.enemySpawnRate = enemySpawnRate;
        this.numEnemyToSpawn = numEnemyToSpawn;
        this.specialEnemyChance = specialEnemyChance;
        this.spawnBoss = spawnBoss;
    }

    public boolean shouldSpawnSpecial()
    {
        double num = Math.random();
        return num <= specialEnemyChance;
    }

    public void iterateSpawnedEnemies()
    {
        numEnemySpawned++;
    }

    public boolean isLevelFinished()
    {
        return numEnemySpawned >= numEnemyToSpawn;
    }
}
