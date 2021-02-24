package castlepanic.game.card;

import castlepanic.game.card.wizard.ArcaneAssemblyCard;

public class DeckBuilder {

    public static Deck buildCastlePanicDeck(){
        Deck deck = new Deck();
        deck.add(new ArcherAnyColorCard());
        return deck;
    }

    public static Deck buildWizardDeck(){
        Deck deck = new Deck();
        deck.add(new ArcaneAssemblyCard());
        return deck;
    }

    private DeckBuilder(){}
}
