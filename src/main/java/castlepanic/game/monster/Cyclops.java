package castlepanic.game.monster;

import castlepanic.game.Ring;

public class Cyclops extends Monster{
    public Cyclops(){
        super(MonsterType.NORMAL, "Cyclops", 3, "Cyclops.png");
        fatalRing = Ring.ARCHER;
        immunityRing = Ring.SWORDSMAN;
    }
}
