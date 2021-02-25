package castlepanic.game.card;

import castlepanic.game.card.wizard.*;

public class DeckBuilder {

    public static Deck buildCastlePanicDeck(){
        // TODO Determine how many of each card is in the deck
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
        deck.add(new HeroBlueCard());
        deck.add(new HeroGreenCard());
        deck.add(new HeroRedCard());
        deck.add(new KnightAnyColorCard());
        deck.add(new KnightBlueCard());
        deck.add(new KnightGreenCard());
        deck.add(new KnightRedCard());
        deck.add(new KnockBackCard());
        deck.add(new MissingCard());
        deck.add(new MortarCard());
        deck.add(new NeverLoseHopeCard());
        deck.add(new NiceShotCard());
        deck.add(new ReinforceCard());
        deck.add(new ScavengeCard());
        deck.add(new StandTogetherCard());
        deck.add(new SwordsmanAnyColorCard());
        deck.add(new SwordsmanBlueCard());
        deck.add(new SwordsmanGreenCard());
        deck.add(new SwordsmanRedCard());
        deck.add(new TarCard());
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
        deck.add(new HypnotizeCard());
        deck.add(new LightningBoltCard());
        deck.add(new MysticalManufacturingCard());
        deck.add(new RainOfIronCard());
        deck.add(new RainOfIceCard());
        deck.add(new RingOfFireCard());
        deck.add(new TeleportCard());
        deck.add(new ThalgarsBlessingCard());
        deck.add(new ValadorsWaveCard());
        deck.add(new WallOfForceCard());
        deck.add(new WarStormCard());
        deck.add(new WizardQuakeCard());
        return deck;
    }

    private DeckBuilder(){}
}
