package castlepanic.game.card;

public class ArcherAnyColorCard extends Card{
    public ArcherAnyColorCard(){
        super(CardType.CASTLEPANIC, "Archer Any Color", CardAbility.ARCHER_ANY_COLOR, "Archer_Any Color.png");
        this.hitCard = true;
    }
}
