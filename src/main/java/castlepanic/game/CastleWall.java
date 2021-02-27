package castlepanic.game;

import castlepanic.view.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CastleWall {
    private static final int IMAGE_WIDTH = 150;
    private static final int SLANTED_IMAGE_WIDTH = 80;

    private Arc arc;
    private boolean destroyed;
    private int fireTokens;
    private boolean fortified;
    private BufferedImage image;

    private Rectangle bounds = new Rectangle();

    public CastleWall(Arc arc){
        this.arc = arc;
        this.destroyed = false;
        this.fireTokens = 0;
        this.fortified = false;

        switch (arc) {
            case ARC_1: {
                this.image = ImageUtil.get("Castle Wall_06.png", SLANTED_IMAGE_WIDTH);
                break;
            }
            case ARC_2: {
                this.image = ImageUtil.get("Castle Wall_01.png", IMAGE_WIDTH);
                break;
            }
            case ARC_3: {
                this.image = ImageUtil.get("Castle Wall_02.png", SLANTED_IMAGE_WIDTH);
                break;
            }
            case ARC_4: {
                this.image = ImageUtil.get("Castle Wall_03.png", SLANTED_IMAGE_WIDTH);
                break;
            }
            case ARC_5: {
                this.image = ImageUtil.get("Castle Wall_04.png", IMAGE_WIDTH);
                break;
            }
            case ARC_6: {
                this.image = ImageUtil.get("Castle Wall_05.png", SLANTED_IMAGE_WIDTH);
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

    public int getFireTokens() {
        return fireTokens;
    }

    public void setFireTokens(int fireTokens) {
        this.fireTokens = fireTokens;
        if (this.fireTokens == 3)
            this.destroyed = true;
    }

    public void adjFireTokens(int amount) {
        this.fireTokens += amount;
        if (this.fireTokens == 3)
            this.destroyed = true;
    }

    public BufferedImage getImage() {
        if (destroyed)
            return null;
        return image;
    }

    public boolean isFortified() {
        return fortified;
    }

    public void setFortified(boolean fortified) {
        this.fortified = fortified;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
