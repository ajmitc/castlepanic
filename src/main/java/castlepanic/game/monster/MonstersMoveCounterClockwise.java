package castlepanic.game.monster;

public class MonstersMoveCounterClockwise extends Monster{
    public MonstersMoveCounterClockwise(){
        super(MonsterType.EFFECT, "Monsters Move Counter Clockwise", 0, "Monsters Move Counter-Clockwise.png");
        abilities.add(MonsterAbility.MONSTERS_MOVE_COUNTER_CLOCKWISE);
    }
}
