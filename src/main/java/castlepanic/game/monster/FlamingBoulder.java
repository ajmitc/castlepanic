package castlepanic.game.monster;

public class FlamingBoulder extends Monster{
    public FlamingBoulder(){
        super(MonsterType.EFFECT, "Flaming Boulder", 0, "Flaming Boulder.png");
        abilities.add(MonsterAbility.GIANT_BOULDER);
        abilities.add(MonsterAbility.FIRE_STARTER);
    }
}
