package castlepanic.game.monster;

public class OneImpPerTower extends Monster{
    public OneImpPerTower(){
        super(MonsterType.EFFECT, "1 Imp Per Tower", 0, "1 Imp per Tower.png");
        abilities.add(MonsterAbility.IMP_PER_TOWER);
    }
}
