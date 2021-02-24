package castlepanic.game.monster;

public class ClimbingTroll extends Monster{
    public ClimbingTroll(){
        super(MonsterType.NORMAL, "Climbing Troll", 3, "Climbing Troll.png");
        abilities.add(MonsterAbility.CLIMB_WALLS);
    }
}
