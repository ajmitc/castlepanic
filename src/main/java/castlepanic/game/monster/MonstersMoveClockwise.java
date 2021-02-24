package castlepanic.game.monster;

public class MonstersMoveClockwise extends Monster{
    public MonstersMoveClockwise(){
        super(MonsterType.EFFECT, "Monsters Move Clockwise", 0, "Monsters Move Clockwise.png");
        abilities.add(MonsterAbility.MONSTERS_MOVE_CLOCKWISE);
    }
}
