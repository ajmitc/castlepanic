package castlepanic.game.monster;

public class Trebuchet extends Monster{
    public Trebuchet(){
        super(MonsterType.EFFECT, "Trebuchet", 0, "Trebuchet.png");
        abilities.add(MonsterAbility.TREBUCHET);
    }
}
