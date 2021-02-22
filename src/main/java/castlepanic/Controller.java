package castlepanic;

import castlepanic.game.Phase;
import castlepanic.game.PhaseStep;
import castlepanic.game.card.Card;
import castlepanic.game.card.CardAbility;
import castlepanic.view.View;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }

    public void run(){
        while (model.getGame() != null && model.getGame().getPhase() != Phase.GAMEOVER){
            view.refresh();
            switch (model.getGame().getPhase()){
                case SETUP:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:{
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE:{
                            model.getGame().setPhase(Phase.PLAY);
                            break;
                        }
                    }
                    break;
                }
                case PLAY:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DRAW_UP);
                            break;
                        }
                        case PLAY_DRAW_UP:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DISCARD_AND_DRAW);
                            break;
                        }
                        case PLAY_DISCARD_AND_DRAW:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_TRADE_CARDS);
                            break;
                        }
                        case PLAY_TRADE_CARDS:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS);
                            break;
                        }
                        case PLAY_CARDS:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MOVE_MONSTERS);
                            break;
                        }
                        case PLAY_MOVE_MONSTERS:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DRAW_NEW_MONSTERS);
                            break;
                        }
                        case PLAY_DRAW_NEW_MONSTERS:{
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE:{
                            // Check if game over.  If so, goto teardown.  Else, goto DRAW_UP
                            model.getGame().setPhase(Phase.TEARDOWN);
                            break;
                        }
                    }
                    break;
                }
                case TEARDOWN:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:{
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE:{
                            model.getGame().setPhase(Phase.GAMEOVER);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void applyCardAbility(Card card){
        switch (card.getType()){
            case CASTLEPANIC:
                applyCastlePanicCardAbility(card.getAbility());
                break;
            case WIZARD:
                applyWizardCardAbility(card.getAbility());
                break;
            case RESOURCE:
                break;
        }
    }

    private void applyCastlePanicCardAbility(CardAbility ability){
        switch (ability){
            case ARCHER_ANY_COLOR:{
                // TODO Hit 1 monster in any color Archer ring
                break;
            }
        }
    }

    private void applyWizardCardAbility(CardAbility ability){
        switch (ability){
            case ARCANE_ASSEMBLY:{
                // TODO All player may immediately build walls for 1 Brick OR 1 Mortar per wall
                break;
            }
        }
    }
}
