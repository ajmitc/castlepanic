package castlepanic.game.monster;

import castlepanic.game.Ring;

public class Centaur extends Monster{
    public Centaur(){
        super(MonsterType.NORMAL, "Centaur", 3, "Centaur.png");
        fatalRing = Ring.KNIGHT;
        immunityRing = Ring.ARCHER;
    }
}
