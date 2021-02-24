package castlepanic.game.monster;

public class PlagueKnights extends Monster{
    public PlagueKnights(){
        super(MonsterType.EFFECT, "Plague Knights", 0, "Plague_Knights.png");
        abilities.add(MonsterAbility.PLAGUE_KNIGHTS);
    }
}
