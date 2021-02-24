package castlepanic.game.monster;

public class Phoenix extends Monster{
    public Phoenix(){
        super(MonsterType.NORMAL, "Phoenix", 1, "Phoenix.png");
        abilities.add(MonsterAbility.FLYING);
        abilities.add(MonsterAbility.FIRE_STARTER);
        abilities.add(MonsterAbility.PHOENIX);
    }
}
