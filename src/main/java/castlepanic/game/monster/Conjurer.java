package castlepanic.game.monster;

public class Conjurer extends Monster{
    public Conjurer(){
        super(MonsterType.BOSS, "Conjurer", 2, "Conjurer.png");
        abilities.add(MonsterAbility.CONJURER);
    }
}
