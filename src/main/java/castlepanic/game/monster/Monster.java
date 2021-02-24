package castlepanic.game.monster;

import castlepanic.game.Arc;
import castlepanic.game.Ring;
import castlepanic.view.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public abstract class Monster {
    protected MonsterType type;
    protected String name;
    protected int maxHitpoints;
    protected int hitpoints;
    protected BufferedImage image;
    protected Ring immunityRing;
    protected Ring fatalRing;
    protected Set<MonsterAbility> abilities = new HashSet<>();
    protected boolean tar;
    protected int fireTokens;

    // Current location
    protected Arc arc;
    protected Ring ring;

    public Monster(MonsterType type, String name, int maxHitpoints, String imageName){
        this.type = type;
        this.name = name;
        this.maxHitpoints = maxHitpoints;
        this.hitpoints = maxHitpoints;
        this.image = ImageUtil.get(imageName);
        this.tar = false;
        this.fireTokens = 0;
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

    public Arc getArc() {
        return arc;
    }

    public void setArc(Arc arc) {
        this.arc = arc;
    }

    public Ring getRing() {
        return ring;
    }

    public void setRing(Ring ring) {
        this.ring = ring;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Ring getFatalRing() {
        return fatalRing;
    }

    public Ring getImmunityRing() {
        return immunityRing;
    }

    public Set<MonsterAbility> getAbilities() {
        return abilities;
    }

    public boolean hasAbility(MonsterAbility ability){
        return abilities.contains(ability);
    }

    public boolean isTar() {
        return tar;
    }

    public void setTar(boolean tar) {
        this.tar = tar;
    }

    public int getFireTokens() {
        return fireTokens;
    }

    public void setFireTokens(int fireTokens) {
        this.fireTokens = fireTokens;
    }

    public void adjFireTokens(int amount) {
        this.fireTokens += amount;
    }
}
