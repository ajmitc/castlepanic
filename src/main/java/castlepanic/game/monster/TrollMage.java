package castlepanic.game.monster;

public class TrollMage extends Monster{
    public TrollMage(){
        super(MonsterType.BOSS, "Troll Mage", 3, "Troll Mage.png");
        abilities.add(MonsterAbility.TROLL_MAGE);
    }
}
