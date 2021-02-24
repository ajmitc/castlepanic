package castlepanic.game;

public enum Ring {
    CASTLE,
    SWORDSMAN,
    KNIGHT,
    ARCHER,
    FOREST;

    public Ring closerToCastle(){
        if (this == CASTLE)
            return CASTLE;
        return Ring.values()[ordinal() - 1];
    }

    public Ring closerToForest(){
        if (this == FOREST)
            return FOREST;
        return Ring.values()[ordinal() + 1];
    }
}
