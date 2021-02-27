package castlepanic.game.monster;

public class Necromancer extends Monster{
    public Necromancer(){
        super(MonsterType.MEGABOSS, "Necromancer", 4, "Necromancer.png");
        abilities.add(MonsterAbility.NECROMANCER);
        shape = MonsterShape.SIDED_4;
    }
}
