package castlepanic.game.monster;

public class Warlock extends Monster{
    public Warlock(){
        super(MonsterType.MEGABOSS, "Warlock", 4, "Warlock.png");
        abilities.add(MonsterAbility.WARLOCK);
    }
}
