package castlepanic.game.monster;

public class PlagueArchers extends Monster{
    public PlagueArchers(){
        super(MonsterType.EFFECT, "Plague Archers", 0, "Plague_Archers.png");
        abilities.add(MonsterAbility.PLAGUE_ARCHERS);
    }
}
