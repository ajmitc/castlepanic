package castlepanic.game.monster;

public class GiantBoulder extends Monster{
    public GiantBoulder(){
        super(MonsterType.EFFECT, "Giant Boulder", 0, "Giant Boulder.png");
        abilities.add(MonsterAbility.GIANT_BOULDER);
    }
}
