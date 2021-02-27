package castlepanic.game.card;

public class ArcherRedCard extends Card{
    public ArcherRedCard(){
        super(CardType.CASTLEPANIC, "Archer - Red", CardAbility.ARCHER_RED, "Archer_Red.png");
        this.hitCard = true;
    }
}
