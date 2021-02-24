package castlepanic.game.monster;

public class GoblinCavalry extends Monster{
    public GoblinCavalry(){
        super(MonsterType.NORMAL, "Goblin Cavalry", 2, "Goblin Cavalry.png");
        abilities.add(MonsterAbility.MOVE_2_SPACES);
    }
}
