package castlepanic.game.card;

import castlepanic.game.card.wizard.*;

public class DeckBuilder {

    public static Deck buildCastlePanicDeck(){
        Deck deck = new Deck();
        deck.add(new ArcherAnyColorCard());
        deck.add(new ArcherBlueCard());
        deck.add(new ArcherGreenCard());
        deck.add(new ArcherRedCard());
        deck.add(new BarbarianCard());
        deck.add(new BerserkCard());
        deck.add(new BrickCard());
        deck.add(new ChangeColorCard());
        deck.add(new ChangeRangeCard());
        deck.add(new DoubleStrikeCard());
        deck.add(new Draw2CardsCard());
        deck.add(new DriveHimBackCard());
        deck.add(new EnchantedCard());
        deck.add(new FlamingCard());
        deck.add(new FortifyWall());
        deck.add(new MortarCard());
        return deck;
    }

    public static Deck buildWizardDeck(){
        Deck deck = new Deck();
        deck.add(new ArcaneAssemblyCard());
        deck.add(new AzrielsFistCard());
        deck.add(new BurningBlastCard());
        deck.add(new ChainLightningCard());
        deck.add(new ExtinguishingWindCard());
        deck.add(new EyeOfTheOracleCard());
        deck.add(new FireballGreenCard());
        deck.add(new FireballRedCard());
        deck.add(new FireballBlueCard());
        deck.add(new HammerOfLightCard());
        return deck;
    }

    private DeckBuilder(){}
}
