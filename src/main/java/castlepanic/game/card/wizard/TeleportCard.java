package castlepanic.game.card.wizard;

import castlepanic.game.card.Card;
import castlepanic.game.card.CardAbility;
import castlepanic.game.card.CardType;

public class TeleportCard extends Card {

    public TeleportCard(){
        super(CardType.WIZARD, "Teleport", CardAbility.TELEPORT, "Teleport.png");
    }
}
