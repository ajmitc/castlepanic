package castlepanic.game.monster;

public class MonstersMove1Red extends Monster{
    public MonstersMove1Red(){
        super(MonsterType.EFFECT, "Monsters Move 1 Red", 0, "Monsters Move 1_Red.png");
        abilities.add(MonsterAbility.MONSTERS_MOVE_1_RED);
    }
}
