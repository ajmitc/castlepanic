package castlepanic;

import castlepanic.game.*;
import castlepanic.game.card.Card;
import castlepanic.game.card.CardAbility;
import castlepanic.game.monster.Imp;
import castlepanic.game.monster.Monster;
import castlepanic.game.monster.MonsterAbility;
import castlepanic.game.monster.MonsterType;
import castlepanic.view.View;

import java.awt.*;

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
                            model.getGame().setup();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE:{
                            model.getGame().addInitialMonstersToBoard();
                            model.getGame().addMegaBossesToBag();
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
                            while (model.getGame().getHand().size() < Game.HAND_SIZE){
                                model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DISCARD_AND_DRAW);
                            break;
                        }
                        case PLAY_DISCARD_AND_DRAW:{
                            // TODO Ask player if they want to discard and draw
                            model.getGame().setPhaseStep(PhaseStep.PLAY_TRADE_CARDS);
                            break;
                        }
                        case PLAY_TRADE_CARDS:{
                            // TODO, not useful for solo games
                            model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS);
                            break;
                        }
                        case PLAY_CARDS:{
                            // TODO Ask player to choose a card to play
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MOVE_MONSTERS);
                            break;
                        }
                        case PLAY_MOVE_MONSTERS:{
                            moveMonsters();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DRAW_NEW_MONSTERS);
                            break;
                        }
                        case PLAY_DRAW_NEW_MONSTERS:{
                            drawNewMonsters();
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

    private void moveMonsters(){
        for (Monster monster: model.getGame().getMonstersOnBoard()){
            moveMonster(monster);
        }
    }

    private void moveMonsters(Color color){
        for (Monster monster: model.getGame().getMonstersOnBoard()){
            if (monster.getArc().getColor() == color)
                moveMonster(monster);
        }
    }

    private void moveMonster(Monster monster){
        int numRings = monster.hasAbility(MonsterAbility.MOVE_2_SPACES)? 2: 1;
        for (int i = 0; i < numRings; ++i) {
            switch (monster.getRing()) {
                case CASTLE: {
                    // Move clockwise
                    Arc nextArc = monster.getArc().rotateClockwise();
                    // Check if arc contains castle tower
                    CastleTower tower = model.getGame().getTower(nextArc);
                    if (!tower.isDestroyed()) {
                        tower.setDestroyed(true);
                        monster.adjHitpoints(-1);
                    } else
                        monster.setArc(monster.getArc().rotateClockwise());
                    break;
                }
                case SWORDSMAN: {
                    // Check if there's a wall
                    CastleWall wall = model.getGame().getWall(monster.getArc());
                    if (!wall.isDestroyed()) {
                        wall.setDestroyed(true);
                        monster.adjHitpoints(-1);
                    } else
                        monster.setRing(Ring.CASTLE);
                    break;
                }
                case KNIGHT: {
                    monster.setRing(Ring.SWORDSMAN);
                    break;
                }
                case ARCHER: {
                    monster.setRing(Ring.KNIGHT);
                    break;
                }
                case FOREST: {
                    monster.setRing(Ring.ARCHER);
                    break;
                }
            }
        }
    }

    private void drawNewMonsters() {
        drawNewMonsters(2);
    }

    private void drawNewMonsters(int numMonsters){
        for (int i = 0; i < numMonsters; ++i){
            drawNewMonster();
        }
    }

    private void drawNewMonster(){
        Monster monster = model.getGame().getMonsterBag().draw();
        if (monster.getType() == MonsterType.EFFECT){
            handleMonsterEffect(monster);
        }
        else {
            placeMonsterOnBoard(monster);
        }
    }

    private void handleMonsterEffect(Monster monster){
        for (MonsterAbility ability: monster.getAbilities()) {
            switch (ability){
                case HYDRA:{
                    break;
                }
                case DRAGON:{
                    break;
                }
                case CHIMERA:{
                    break;
                }
                case WARLOCK:{
                    break;
                }
                case HEALER:{
                    break;
                }
                case BASILISK:{
                    break;
                }
                case CONJURER:{
                    break;
                }
                case TREBUCHET:{
                    break;
                }
                case TROLL_MAGE:{
                    break;
                }
                case NECROMANCER:{
                    break;
                }
                case GOBLIN_KING:{
                    break;
                }
                case ORC_WARLORD:{
                    break;
                }
                case DOPPELGANGER:{
                    break;
                }
                case GIANT_BOULDER:{
                    // Boulder comes from forest and destroys all monsters in path until it hits a wall, tower, or goes off board
                    rollGiantBoulder(monster);
                    break;
                }
                case MOVE_2_SPACES:{
                    // Move all monsters 2 spaces
                    moveMonsters();
                    moveMonsters();
                    break;
                }
                case PLAGUE_ARCHERS:{
                    break;
                }
                case PLAGUE_KNIGHTS:{
                    break;
                }
                case PLAGUE_SWORDSMEN:{
                    break;
                }
                case ALL_PLAYERS_DISCARD_ONE_CARD:{
                    break;
                }
                case MONSTERS_MOVE_1_RED:{
                    moveMonsters(Color.RED);
                    break;
                }
                case MONSTERS_MOVE_1_BLUE:{
                    moveMonsters(Color.BLUE);
                    break;
                }
                case MONSTERS_MOVE_1_GREEN:{
                    moveMonsters(Color.GREEN);
                    break;
                }
                case DRAW_3_MONSTER_TOKENS:{
                    drawNewMonsters(3);
                    break;
                }
                case DRAW_4_MONSTER_TOKENS:{
                    drawNewMonsters(4);
                    break;
                }
                case MONSTERS_MOVE_CLOCKWISE:{
                    model.getGame().getMonstersOnBoard().stream().forEach(monster1 -> {
                        Arc nextArc = monster1.getArc().rotateClockwise();
                        // TODO Check for castle tower, barricade, etc
                        monster1.setArc(nextArc);
                    });
                    break;
                }
                case MONSTERS_MOVE_COUNTER_CLOCKWISE:{
                    model.getGame().getMonstersOnBoard().stream().forEach(monster1 -> {
                        Arc nextArc = monster1.getArc().rotateCounterClockwise();
                        // TODO Check for castle tower, barricade, etc
                        monster1.setArc(nextArc);
                    });
                    break;
                }
                case IMP_PER_TOWER:{
                    for (CastleTower tower: model.getGame().getTowers()){
                        if (!tower.isDestroyed()){
                            Imp imp = new Imp();
                            imp.setArc(tower.getArc());
                            imp.setRing(Ring.FOREST);
                            model.getGame().getMonstersOnBoard().add(imp);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void placeMonsterOnBoard(Monster monster){
        Arc arc = Arc.values()[Util.randInt(6)];
        monster.setArc(arc);
        monster.setRing(Ring.FOREST);
        model.getGame().getMonstersOnBoard().add(monster);
    }

    private void rollGiantBoulder(Monster monster){
        // Boulder comes from forest and destroys all monsters in path until it hits a wall, tower, or goes off board
        Arc arc = Arc.randomArc();
        monster.setArc(arc);
        monster.setRing(Ring.FOREST);
        model.getGame().getMonstersOnBoard().add(monster);

        for (int i = Ring.FOREST.ordinal(); i >= Ring.CASTLE.ordinal(); --i){
            // TODO Finish this
        }
    }
}
