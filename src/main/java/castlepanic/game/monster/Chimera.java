package castlepanic.game.monster;

public class Chimera extends Monster{
    public Chimera(){
        super(MonsterType.MEGABOSS, "Chimera", 5, "Chimera.png");
        abilities.add(MonsterAbility.CHIMERA);
    }
}
