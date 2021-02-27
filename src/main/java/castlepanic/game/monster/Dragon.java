package castlepanic.game.monster;

public class Dragon extends Monster{
    public Dragon(){
        super(MonsterType.MEGABOSS, "Dragon", 5, "Dragon.png");
        abilities.add(MonsterAbility.DRAGON);
        shape = MonsterShape.SIDED_5;
    }
}
