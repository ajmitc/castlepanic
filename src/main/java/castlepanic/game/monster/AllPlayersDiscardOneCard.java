package castlepanic.game.monster;

public class AllPlayersDiscardOneCard extends Monster{
    public AllPlayersDiscardOneCard(){
        super(MonsterType.EFFECT, "All Players Discard One Card", 0, "All Players Discard 1 Card.png");
        abilities.add(MonsterAbility.ALL_PLAYERS_DISCARD_ONE_CARD);
    }
}
