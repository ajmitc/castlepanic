package castlepanic.game.monster;

public class Basilisk extends Monster{
    public Basilisk(){
        super(MonsterType.MEGABOSS, "Basilisk", 5, "Basilisk.png");
        abilities.add(MonsterAbility.BASILISK);
    }
}
