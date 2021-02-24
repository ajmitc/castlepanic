package castlepanic.game.monster;

public class Healer extends Monster{
    public Healer(){
        super(MonsterType.BOSS, "Healer", 2, "Healer.png");
        abilities.add(MonsterAbility.HEALER);
    }
}
