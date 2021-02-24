package castlepanic.game.monster;

public class Gargoyle extends Monster{
    public Gargoyle(){
        super(MonsterType.NORMAL, "Gargoyle", 2, "Gargoyle.png");
        abilities.add(MonsterAbility.FLYING);
    }
}
