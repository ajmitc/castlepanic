package castlepanic.game;

import castlepanic.view.ImageUtil;

import java.awt.image.BufferedImage;

public class CastleTower {
    private Arc arc;
    private boolean destroyed;
    private boolean onFire;
    private BufferedImage image;
    private boolean wizardTower;

    public CastleTower(Arc arc){
        this.arc = arc;
        this.destroyed = false;
        this.onFire = false;
        this.image = ImageUtil.get("Castle Tower_aerial 3.png");
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

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
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
        this.image = ImageUtil.get("Wizard Tower_aerial 3.png");
    }
}
