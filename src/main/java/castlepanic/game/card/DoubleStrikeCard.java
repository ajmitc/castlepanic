package castlepanic.game.card;

public class DoubleStrikeCard extends Card{
    public DoubleStrikeCard(){
        super(CardType.CASTLEPANIC, "Double Strike", CardAbility.DOUBLE_STRIKE, "Double Strike.png");
        this.order = 5;
    }
}
