package castlepanic.game.monster;

public class PlagueSwordsmen extends Monster{
    public PlagueSwordsmen(){
        super(MonsterType.EFFECT, "Plague Swordsmen", 0, "Plague_Swordsmen.png");
        abilities.add(MonsterAbility.PLAGUE_SWORDSMEN);
    }
}
