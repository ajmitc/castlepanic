package castlepanic.game.monster;

public class Hydra extends Monster{
    public Hydra(){
        super(MonsterType.MEGABOSS, "Hydra", 4, "Hydra.png");
        abilities.add(MonsterAbility.HYDRA);
    }
}
