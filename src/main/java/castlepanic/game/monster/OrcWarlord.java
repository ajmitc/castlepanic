package castlepanic.game.monster;

public class OrcWarlord extends Monster{
    public OrcWarlord(){
        super(MonsterType.BOSS, "Orc Warlord", 3, "Orc Warlord.png");
        abilities.add(MonsterAbility.ORC_WARLORD);
    }
}
