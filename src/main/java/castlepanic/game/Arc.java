package castlepanic.game;

import castlepanic.Util;

import java.awt.*;

public enum Arc {
    ARC_1(Color.RED),
    ARC_2(Color.RED),
    ARC_3(Color.GREEN),
    ARC_4(Color.GREEN),
    ARC_5(Color.BLUE),
    ARC_6(Color.BLUE);

    private Color color;
    Arc(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Arc rotateClockwise(){
        int ordinal = (ordinal() + 1) % 6;
        return Arc.values()[ordinal];
    }

    public Arc rotateCounterClockwise(){
        int ordinal = ordinal() - 1;
        if (ordinal < 0)
            ordinal = 6;
        return Arc.values()[ordinal];
    }

    public static Arc randomArc(){
        return Arc.values()[Util.randInt(6)];
    }

    @Override
    public String toString() {
        return "Arc " + ordinal() + " (" + color + ')';
    }
}
