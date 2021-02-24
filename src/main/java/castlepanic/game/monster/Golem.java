package castlepanic.game.monster;

import castlepanic.game.Ring;

public class Golem extends Monster{
    public Golem(){
        super(MonsterType.NORMAL, "Golem", 3, "Golem.png");
        fatalRing = Ring.SWORDSMAN;
        immunityRing = Ring.KNIGHT;
    }
}
