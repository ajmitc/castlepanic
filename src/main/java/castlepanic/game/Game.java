package castlepanic.game;

import castlepanic.game.card.Deck;
import castlepanic.game.card.DeckBuilder;

public class Game {
    private Phase phase;
    private PhaseStep phaseStep;

    private Deck castlePanicDeck;
    private Deck wizardDeck;
    private Deck resourceDeck;

    public Game(){
        phase = Phase.SETUP;
        phaseStep = PhaseStep.START_PHASE;

        castlePanicDeck = DeckBuilder.buildCastlePanicDeck();
        wizardDeck      = DeckBuilder.buildWizardDeck();
        resourceDeck    = DeckBuilder.buildResourceDeck();
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
        this.phaseStep = PhaseStep.START_PHASE;
    }

    public PhaseStep getPhaseStep() {
        return phaseStep;
    }

    public void setPhaseStep(PhaseStep phaseStep) {
        this.phaseStep = phaseStep;
    }

    public Deck getCastlePanicDeck() {
        return castlePanicDeck;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getWizardDeck() {
        return wizardDeck;
    }
}
