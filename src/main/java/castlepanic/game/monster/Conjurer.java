package castlepanic.game.monster;

public class Conjurer extends Monster{
    public Conjurer(){
        super(MonsterType.NORMAL, "Conjurer", 2, "Conjurer.png");
        abilities.add(MonsterAbility.CONJURER);
    }
}
