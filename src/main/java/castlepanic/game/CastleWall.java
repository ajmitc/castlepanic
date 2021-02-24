package castlepanic.game;

import castlepanic.view.ImageUtil;

import java.awt.image.BufferedImage;

public class CastleWall {
    private Arc arc;
    private boolean destroyed;
    private boolean onFire;
    private BufferedImage image;

    public CastleWall(Arc arc){
        this.arc = arc;
        this.destroyed = false;
        this.onFire = false;

        switch (arc) {
            case ARC_1: {
                this.image = ImageUtil.get("Castle Wall_06.png");
                break;
            }
            case ARC_2: {
                this.image = ImageUtil.get("Castle Wall_01.png");
                break;
            }
            case ARC_3: {
                this.image = ImageUtil.get("Castle Wall_02.png");
                break;
            }
            case ARC_4: {
                this.image = ImageUtil.get("Castle Wall_03.png");
                break;
            }
            case ARC_5: {
                this.image = ImageUtil.get("Castle Wall_04.png");
                break;
            }
            case ARC_6: {
                this.image = ImageUtil.get("Castle Wall_05.png");
                break;
            }
        }
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
}
