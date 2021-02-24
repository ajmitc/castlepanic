package castlepanic.game.monster;

public class GoblinKing extends Monster{
    public GoblinKing(){
        super(MonsterType.BOSS, "Goblin King", 2, "Goblin King.png");
        abilities.add(MonsterAbility.GOBLIN_KING);
    }
}
