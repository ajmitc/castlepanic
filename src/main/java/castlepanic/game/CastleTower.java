package castlepanic.game;

import castlepanic.view.ImageUtil;

import java.awt.image.BufferedImage;

public class CastleTower {
    private Arc arc;
    private boolean destroyed;
    // If 3 fire tokens, destroy tower
    private int fireTokens;
    private BufferedImage image;
    private boolean wizardTower;

    public CastleTower(Arc arc){
        this.arc = arc;
        this.destroyed = false;
        this.fireTokens = 0;
        this.image = ImageUtil.get("Castle Tower_aerial 3.png", 75);
        this.wizardTower = false;
    }

    public Arc getArc() {
        return arc;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public int getFireTokens() {
        return fireTokens;
    }

    public void setFireTokens(int fireTokens) {
        this.fireTokens = fireTokens;
        if (this.fireTokens == 3)
            this.destroyed = true;
    }

    public void adjFireTokens(int amount){
        this.fireTokens += amount;
        if (this.fireTokens == 3)
            this.destroyed = true;
    }

    public BufferedImage getImage() {
        if (destroyed)
            return null;
        return image;
    }

    public boolean isWizardTower() {
        return wizardTower;
    }

    public void setWizardTower() {
        this.wizardTower = true;
        this.image = ImageUtil.get("Wizard Tower_aerial 3.png", 80);
    }
}
