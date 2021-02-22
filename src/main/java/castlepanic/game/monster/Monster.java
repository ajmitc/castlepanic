package castlepanic.game.monster;

public abstract class Monster {
    private MonsterType type;
    private String name;
    private int maxHitpoints;
    private int hitpoints;

    public Monster(MonsterType type, String name, int maxHitpoints){
        this.type = type;
        this.name = name;
        this.maxHitpoints = maxHitpoints;
        this.hitpoints = maxHitpoints;
    }

    public MonsterType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public void adjHitpoints(int amount){
        this.hitpoints += amount;
        if (this.hitpoints < 0)
            this.hitpoints = 0;
        else if (this.hitpoints > this.maxHitpoints)
            this.hitpoints = this.maxHitpoints;
    }
}
