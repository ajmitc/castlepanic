package castlepanic.game.monster;

public class Doppelganger extends Monster{
    public Doppelganger(){
        super(MonsterType.NORMAL, "Doppelganger", 0, "Doppelganger.png");
        abilities.add(MonsterAbility.DOPPELGANGER);
    }
}
