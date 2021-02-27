package castlepanic;

import castlepanic.game.*;
import castlepanic.game.card.Card;
import castlepanic.game.card.CardAbility;
import castlepanic.game.card.CardType;
import castlepanic.game.monster.*;
import castlepanic.view.View;
import castlepanic.view.ViewUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Controller {
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    private Model model;
    private View view;
    private Timer monsterDetailsTimer = null;
    private Monster hoverMonster = null;

    private MonsterTargetCriteria monsterTargetCriteria = new MonsterTargetCriteria();
    private Monster selectedMonster;

    private boolean berserkActivated = false;
    private boolean brickPlayed = false;
    private boolean mortarPlayed = false;
    private boolean fortifyPlayed = false;
    private Color overrideColor = null;
    private Ring overrideRing = null;
    private boolean doubleStrikeActivated = false;
    private boolean missingActivated = false;
    private boolean knockBackActivated = false;
    private boolean enchantedActivated = false;
    private boolean flamingActivated = false;
    private boolean niceShotActivated = false;
    private boolean monstersFrozen = false;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        this.view.getMainMenuPanel().getBtnExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.view.getMainMenuPanel().getBtnNewGame().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game game = new Game();
                model.setGame(game);
                view.init();
                view.showGame();
                run();
            }
        });

        this.view.getGamePanel().getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (brickPlayed && mortarPlayed){
                    // Check if player clicked on wall
                    List<CastleWall> selectedWalls = model.getGame().getWalls().stream().filter(wall -> wall.getBounds().contains(e.getX(), e.getY())).collect(Collectors.toList());
                    if (selectedWalls.isEmpty()){
                        ViewUtil.popupNotify("Select a wall to rebuild");
                    }
                    else if (selectedWalls.size() > 1) {
                        ViewUtil.popupNotify("Select the middle of the wall to remove multiple selections");
                    }
                    else {
                        CastleWall wall = selectedWalls.get(0);
                        wall.setDestroyed(false);
                        brickPlayed = false;
                        mortarPlayed = false;
                        run();
                    }
                    return;
                }

                if (fortifyPlayed){
                    // Check if player clicked on wall
                    List<CastleWall> selectedWalls = model.getGame().getWalls().stream().filter(wall -> wall.getBounds().contains(e.getX(), e.getY())).collect(Collectors.toList());
                    if (selectedWalls.isEmpty()){
                        ViewUtil.popupNotify("Select a wall to fortify");
                    }
                    else if (selectedWalls.size() > 1) {
                        ViewUtil.popupNotify("Select the middle of the wall to remove multiple selections");
                    }
                    else {
                        CastleWall wall = selectedWalls.get(0);
                        wall.setFortified(true);
                        fortifyPlayed = false;
                        run();
                    }
                    return;
                }

                // Check if user clicked on monster, if so and monster matches criteria, set selectedMonster
                Optional<Monster> optMonster =
                        model.getGame().getMonstersOnBoard().stream().filter(monster -> monster.getBounds().contains(e.getX(), e.getY())).findFirst();
                if (optMonster.isPresent()) {
                    if (monsterTargetCriteria.matches(optMonster.get())) {
                        selectedMonster = optMonster.get();
                        logger.info("Selected " + selectedMonster);
                        run();
                    } else {
                        monsterTargetCriteria.explain(optMonster.get());
                        ViewUtil.popupNotify("Invalid monster target");
                    }
                }
            }
        });

        this.view.getGamePanel().getBoardPanel().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Optional<Monster> optMonster =
                        model.getGame().getMonstersOnBoard().stream().filter(monster -> monster.getBounds().contains(e.getX(), e.getY())).findFirst();
                if (optMonster.isPresent()) {
                    if (optMonster.get() == hoverMonster){
                        return;
                    }
                    hoverMonster = optMonster.get();
                    if (monsterDetailsTimer != null){
                        monsterDetailsTimer.cancel();
                    }
                    monsterDetailsTimer = new Timer();
                    monsterDetailsTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.getGamePanel().getBoardPanel().setMonsterDetails(optMonster.get());
                                    view.refresh();
                                }
                            });
                        }
                    }, 1000);
                }
                else {
                    if (monsterDetailsTimer != null){
                        monsterDetailsTimer.cancel();
                    }
                    view.getGamePanel().getBoardPanel().setMonsterDetails(null);
                    hoverMonster = null;
                }
            }
        });

        this.view.getGamePanel().getHandPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Card selectedCard = view.getGamePanel().getHandPanel().getSelected(e.getX(), e.getY());
                if (selectedCard != null){
                    selectedCard.setSelected(!selectedCard.isSelected());
                    if (model.getGame().getSelectedCardsInHand().isEmpty())
                        view.getGamePanel().getDoneButtonPanel().getBtnDone().setText("Done");
                    else
                        view.getGamePanel().getDoneButtonPanel().getBtnDone().setText("Play");
                    view.refresh();
                }
            }
        });

        this.view.getGamePanel().getDoneButtonPanel().getBtnDone().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getGame().getPhaseStep() == PhaseStep.PLAY_CARDS_CHOOSE_CARDS){
                    model.getGame().setPhaseStep(PhaseStep.PLAY_MOVE_MONSTERS);
                }
                run();
            }
        });
    }

    public void run(){
        while (model.getGame() != null && model.getGame().getPhase() != Phase.GAMEOVER){
            //logger.info(model.getGame().getPhase() + "::" + model.getGame().getPhaseStep());
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
                            model.getGame().setPhaseStep(PhaseStep.PLAY_ASK_DISCARD_AND_DRAW_1);
                            break;
                        }
                        case PLAY_ASK_DISCARD_AND_DRAW_1:{
                            // Ask player if they want to discard and draw
                            if (ViewUtil.popupConfirm("Discard and Draw", "Do you want to discard and draw?")){
                                model.getGame().setPhaseStep(PhaseStep.PLAY_DISCARD_AND_DRAW_1);
                                return;
                            }
                            else
                                model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS_CHOOSE_CARDS);
                            break;
                        }
                        case PLAY_DISCARD_AND_DRAW_1:{
                            List<Card> selectedCards = model.getGame().getSelectedCardsInHand();
                            if (!selectedCards.isEmpty()) {
                                if (selectedCards.size() > 1){
                                    ViewUtil.popupNotify("You can only discard one card");
                                    model.getGame().deselectAllCards();
                                    return;
                                }
                                Card selectedCard = selectedCards.get(0);
                                if (selectedCard.getType() == CardType.CASTLEPANIC)
                                    model.getGame().getCastlePanicDeck().discard(selectedCard);
                                else
                                    model.getGame().getWizardDeck().discard(selectedCard);
                                model.getGame().getHand().remove(selectedCard);
                                if (ViewUtil.popupConfirm("Discard and Draw", "Do you want to draw a Wizard card?"))
                                    model.getGame().getHand().add(model.getGame().getWizardDeck().draw());
                                else
                                    model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                                model.getGame().setPhaseStep(PhaseStep.PLAY_ASK_DISCARD_AND_DRAW_2);
                                break;
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS_CHOOSE_CARDS);
                            break;
                        }
                        case PLAY_ASK_DISCARD_AND_DRAW_2:{
                            // Ask player if they want to discard and draw
                            if (ViewUtil.popupConfirm("Discard and Draw", "Do you want to discard and draw again?")){
                                model.getGame().setPhaseStep(PhaseStep.PLAY_DISCARD_AND_DRAW_2);
                                return;
                            }
                            else
                                model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS_CHOOSE_CARDS);
                            break;
                        }
                        case PLAY_DISCARD_AND_DRAW_2:{
                            List<Card> selectedCards = model.getGame().getSelectedCardsInHand();
                            if (!selectedCards.isEmpty()) {
                                if (selectedCards.size() > 1){
                                    ViewUtil.popupNotify("You can only discard one card");
                                    model.getGame().deselectAllCards();
                                    return;
                                }
                                Card selectedCard = selectedCards.get(0);
                                if (selectedCard.getType() == CardType.CASTLEPANIC)
                                    model.getGame().getCastlePanicDeck().discard(selectedCard);
                                else
                                    model.getGame().getWizardDeck().discard(selectedCard);
                                model.getGame().getHand().remove(selectedCard);
                                if (ViewUtil.popupConfirm("Discard and Draw", "Do you want to draw a Wizard card?"))
                                    model.getGame().getHand().add(model.getGame().getWizardDeck().draw());
                                else
                                    model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS_CHOOSE_CARDS);
                            break;
                        }
                        case PLAY_CARDS_CHOOSE_CARDS:{
                            // Ask player to choose a card to play
                            view.getGamePanel().getDoneButtonPanel().getBtnDone().setText("Done");
                            model.getGame().setPhaseStep(PhaseStep.PLAY_SELECTED_CARDS);
                            return;
                        }
                        case PLAY_SELECTED_CARDS:{
                            List<Card> selectedCards = model.getGame().getSelectedCardsInHand();
                            if (!selectedCards.isEmpty()) {
                                // TODO When Hydra is damaged (but not slain), add 2 Imps to FOREST ring of same Arc as Hydra
                                // TODO Warlock unaffected by wizard cards
                                Collections.sort(selectedCards, new Comparator<Card>() {
                                    @Override
                                    public int compare(Card o1, Card o2) {
                                        return o1.getOrder() == o2.getOrder()? 0: o1.getOrder() < o2.getOrder()? -1: 1;
                                    }
                                });
                                Iterator<Card> cardIterator = selectedCards.iterator();
                                while (cardIterator.hasNext()){
                                    Card card = cardIterator.next();
                                    if (applyCardAbility(card)){
                                        if (card.isHitCard() && doubleStrikeActivated){
                                            doubleStrikeActivated = false;
                                            break;
                                        }
                                        else {
                                            model.getGame().getHand().remove(card);
                                            if (card.getType() == CardType.WIZARD)
                                                model.getGame().getWizardDeck().discard(card);
                                            else
                                                model.getGame().getCastlePanicDeck().discard(card);
                                            cardIterator.remove();
                                        }
                                    }
                                    else
                                        return;
                                }

                                model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS_CHOOSE_CARDS);
                                break;
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MOVE_MONSTERS);
                            break;
                        }
                        case PLAY_MOVE_MONSTERS:{
                            moveMonsters();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DAMAGE_BURNING_MONSTERS);
                            break;
                        }
                        case PLAY_DAMAGE_BURNING_MONSTERS:{
                            // Reduce monster hitpoints from fire (1 hitpoint per fire token)
                            model.getGame().getMonstersOnBoard().stream().forEach(monster -> {
                                monster.adjHitpoints(-monster.getFireTokens());
                                // When Hydra is damaged (but not slain), add 2 Imps to FOREST ring of same Arc as Hydra
                                if (monster.hasAbility(MonsterAbility.HYDRA) && monster.getHitpoints() > 0){
                                    // TODO This might not work since we're adding monsters to list that is being streamed...
                                    handleHydraDamaged(monster, monster.getFireTokens());
                                }
                            });
                            model.getGame().setPhaseStep(PhaseStep.PLAY_REMOVE_DESTROYED_MONSTERS);
                            break;
                        }
                        case PLAY_REMOVE_DESTROYED_MONSTERS:{
                            removeDestroyedMonsters();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DRAW_NEW_MONSTERS);
                            break;
                        }
                        case PLAY_DRAW_NEW_MONSTERS:{
                            if (!missingActivated) {
                                drawNewMonsters();
                            }
                            missingActivated = false;
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE:{
                            berserkActivated = false;
                            monstersFrozen = false;
                            // Check if game over.  If so, goto teardown.  Else, goto DRAW_UP
                            if (model.getGame().getTowers().stream().allMatch(tower -> tower.isDestroyed())){
                                ViewUtil.popupNotify("Game Over - You Lose!");
                                model.getGame().setPhase(Phase.TEARDOWN);
                            }
                            else if (model.getGame().getMonsterBag().isEmpty() && model.getGame().getMonstersOnBoard().isEmpty()){
                                // Player wins!
                                ViewUtil.popupNotify("Game Over - You Win!");
                                model.getGame().setPhase(Phase.TEARDOWN);
                            }
                            else
                                model.getGame().setPhaseStep(PhaseStep.PLAY_DRAW_UP);
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

    private void removeDestroyedMonsters(){
        List<Monster> toRemove = new ArrayList<>();
        model.getGame().getMonstersOnBoard().stream().forEach(monster -> {
            if (monster.getHitpoints() == 0){
                toRemove.add(monster);
            }
        });
        if (!toRemove.isEmpty()){
            // Replace Doppelganger with first destroyed monster
            model.getGame().getMonstersOnBoard().stream().forEach(monster -> {
                if (monster.hasAbility(MonsterAbility.DOPPELGANGER)){
                    toRemove.add(monster);
                    Monster replacement = toRemove.remove(0);
                    replacement.setArc(monster.getArc());
                    replacement.setRing(monster.getRing());
                    replacement.setHitpoints(replacement.getMaxHitpoints());
                }
            });
            model.getGame().getMonstersOnBoard().removeAll(toRemove);
            for (Monster monster: toRemove)
                model.getGame().getMonsterBag().defeat(monster);
        }
    }

    private boolean applyCardAbility(Card card){
        switch (card.getType()){
            case CASTLEPANIC:
                applyOverrides(card);
                return applyCastlePanicCardAbility(card.getAbility());
            case WIZARD:
                return applyWizardCardAbility(card.getAbility());
        }
        return true;
    }

    /**
     *
     * @param ability
     * @return true if card resolved and controller can proceed to next card, false if controller should wait for user input
     */
    private boolean applyCastlePanicCardAbility(CardAbility ability){
        switch (ability) {
            case ARCHER_ANY_COLOR: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Ring.ARCHER);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.ARCHER){
                    return hitSelectedMonster();
                }
                return false;
            }
            case ARCHER_BLUE: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.BLUE).target(Ring.ARCHER);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.ARCHER && selectedMonster.getArc().getColor() == Color.BLUE){
                    return hitSelectedMonster();
                }
                return false;
            }
            case ARCHER_GREEN: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.GREEN).target(Ring.ARCHER);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.ARCHER && selectedMonster.getArc().getColor() == Color.GREEN){
                    return hitSelectedMonster();
                }
                return false;
            }
            case ARCHER_RED: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.RED).target(Ring.ARCHER);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.ARCHER && selectedMonster.getArc().getColor() == Color.RED){
                    return hitSelectedMonster();
                }
                return false;
            }
            case BARBARIAN: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset();
                    return false;
                }
                else {
                    return hitSelectedMonster();
                }
            }
            case BERSERK: {
                // Draw 1 castle panic card for every hit this turn
                berserkActivated = true;
                break;
            }
            case BRICK: {
                brickPlayed = true;
                if (mortarPlayed){
                    ViewUtil.popupNotify("Select wall to repair");
                    return false;
                }
                return true;
            }
            case CHANGE_COLOR: {
                // Select card in hand to change color (set card's ability override)
                overrideColor = ViewUtil.popupSelectColor("Change Color", "Select a new color for the hit card");
                return true;
            }
            case CHANGE_RANGE: {
                // Select card in hand to change range (set card's ability override)
                overrideRing = ViewUtil.popupSelectRing("Change Range", "Select a new range for the hit card");
                return true;
            }
            case DOUBLE_STRIKE: {
                doubleStrikeActivated = true;
                return true;
            }
            case DRAW_2_CARDS: {
                model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                return true;
            }
            case DRIVE_HIM_BACK: {
                // Move 1 monster back into the forest
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRingsExceptForest();
                    return false;
                }
                else {
                    selectedMonster.setRing(Ring.FOREST);
                    return true;
                }
            }
            case ENCHANTED: {
                // Play with Hit card, +2 damage
                enchantedActivated = true;
                return true;
            }
            case FLAMING: {
                // Play with Hit card, monster also catches fire
                flamingActivated = true;
                return true;
            }
            case FORTIFY_WALL: {
                // Choose wall to fortify
                fortifyPlayed = true;
                return false;
            }
            case HERO_BLUE: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.BLUE);
                    return false;
                }
                else if (selectedMonster.getArc().getColor() == Color.BLUE){
                    return hitSelectedMonster();
                }
                return false;
            }
            case HERO_GREEN: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.GREEN);
                    return false;
                }
                else if (selectedMonster.getArc().getColor() == Color.GREEN){
                    return hitSelectedMonster();
                }
                return false;
            }
            case HERO_RED: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.RED);
                    return false;
                }
                else if (selectedMonster.getArc().getColor() == Color.RED){
                    return hitSelectedMonster();
                }
                return false;
            }
            case KNIGHT_ANY_COLOR: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Ring.KNIGHT);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.KNIGHT){
                    return hitSelectedMonster();
                }
                return false;
            }
            case KNIGHT_BLUE: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.BLUE).target(Ring.KNIGHT);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.KNIGHT && selectedMonster.getArc().getColor() == Color.BLUE){
                    return hitSelectedMonster();
                }
                return false;
            }
            case KNIGHT_GREEN: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.GREEN).target(Ring.KNIGHT);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.KNIGHT && selectedMonster.getArc().getColor() == Color.GREEN){
                    return hitSelectedMonster();
                }
                return false;
            }
            case KNIGHT_RED: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.RED).target(Ring.KNIGHT);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.KNIGHT && selectedMonster.getArc().getColor() == Color.RED){
                    return hitSelectedMonster();
                }
                return false;
            }
            case KNOCK_BACK: {
                // Play with Hit card to move monster back one space after damaging it
                knockBackActivated = true;
                return true;
            }
            case MISSING: {
                // Do not draw monster tokens this turn
                missingActivated = true;
                return true;
            }
            case MORTAR: {
                mortarPlayed = true;
                if (brickPlayed){
                    ViewUtil.popupNotify("Select wall to repair");
                    return false;
                }
                return true;
            }
            case NEVER_LOSE_HOPE: {
                // TODO Discard as many cards as you want, for each card discarded, draw one Castle card
                ViewUtil.popupNotify("Card not yet implemented");
                return false;
            }
            case NICE_SHOT: {
                // Play with Hit card to slay monster
                niceShotActivated = true;
                return true;
            }
            case REINFORCE: {
                // Immediately draw a card from castle or wizard deck
                if (ViewUtil.popupConfirm("Reinforce", "Press Yes to draw Wizard card, No to draw Castle card"))
                    model.getGame().getHand().add(model.getGame().getWizardDeck().draw());
                else
                    model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                return true;
            }
            case SCAVENGE: {
                // TODO Choose card in discard pile and add to hand
                ViewUtil.popupNotify("Card not yet implemented");
                return true;
            }
            case STAND_TOGETHER: {
                // One player may immediately play 1 Hit or Wizard card
                // Not useful for solo game, removed from DeckBuilder
                return true;
            }
            case SWORDSMAN_ANY_COLOR: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Ring.SWORDSMAN);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.SWORDSMAN){
                    return hitSelectedMonster();
                }
                return false;
            }
            case SWORDSMAN_BLUE: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.BLUE).target(Ring.SWORDSMAN);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.SWORDSMAN && selectedMonster.getArc().getColor() == Color.BLUE){
                    return hitSelectedMonster();
                }
                return false;
            }
            case SWORDSMAN_GREEN: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.GREEN).target(Ring.SWORDSMAN);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.SWORDSMAN && selectedMonster.getArc().getColor() == Color.GREEN){
                    return hitSelectedMonster();
                }
                return false;
            }
            case SWORDSMAN_RED: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Color.RED).target(Ring.SWORDSMAN);
                    return false;
                }
                else if (selectedMonster.getRing() == Ring.SWORDSMAN && selectedMonster.getArc().getColor() == Color.RED){
                    return hitSelectedMonster();
                }
                return false;
            }
            case TAR: {
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetMiddleRings();
                    return false;
                }
                else {
                    selectedMonster.setTar(true);
                    return true;
                }
            }
        }
        return true;
    }

    private boolean applyWizardCardAbility(CardAbility ability){
        switch (ability){
            case ARCANE_ASSEMBLY:{
                // TODO All player may immediately build walls for 1 Brick OR 1 Mortar per wall
                return false;
            }
            case AZRIELS_FIST: {
                // Do 1 damage to 1 monster anywhere on board (including castle & forest)
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset();
                    return false;
                }
                else {
                    return hitSelectedMonster();
                }
            }
            case BURNING_BLAST: {
                if (selectedMonster == null) {
                    // Set all monsters in same space on fire (not in castle or forest)
                    monsterTargetCriteria.reset().targetMiddleRings();
                    return false;
                }
                else {
                    List<Monster> monsters =
                            model.getGame().getMonstersOnBoard().stream()
                                    .filter(monster -> monster.getRing() == selectedMonster.getRing() && monster.getArc() == selectedMonster.getArc())
                                    .collect(Collectors.toList());
                    monsters.stream().forEach(monster -> monster.adjFireTokens(1));
                    return true;
                }
            }
            case CHAIN_LIGHTNING: {
                // Do 1 damage to all monsters in same space (inc castle & forest)
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRings();
                    return false;
                }
                else {
                    List<Monster> monsters =
                            model.getGame().getMonstersOnBoard().stream()
                                    .filter(monster -> monster.getRing() == selectedMonster.getRing() && monster.getArc() == selectedMonster.getArc())
                                    .collect(Collectors.toList());
                    monsters.stream().forEach(monster -> {
                        selectedMonster = monster;
                        hitSelectedMonster();
                    });
                    return true;
                }
            }
            case EXTINGUISHING_WIND: {
                // remove all fire tokens from walls, towers, and monsters in all rings, then move all monsters back 1 space toward forest
                model.getGame().getTowers().stream().forEach(tower -> tower.setFireTokens(0));
                model.getGame().getWalls().stream().forEach(wall -> wall.setFireTokens(0));
                model.getGame().getMonstersOnBoard().stream().forEach(monster -> {
                    monster.setFireTokens(0);
                    monster.setRing(monster.getRing().closerToForest());
                });
                return true;
            }
            case EYE_OF_THE_ORACLE: {
                // TODO draw top 5 cards from castle deck, keep 1, return others to top of deck in any order
                return true;
            }
            case FIREBALL_BLUE: {
                // Do 1 damage to 1 monster in blue sectors (castle & forest included) and catch on fire
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRings().target(Color.BLUE);
                    return false;
                }
                else {
                    boolean ret = hitSelectedMonster();
                    if (ret)
                        selectedMonster.adjFireTokens(1);
                    return ret;
                }
            }
            case FIREBALL_GREEN: {
                // Do 1 damage to 1 monster in green sectors (castle & forest included) and catch on fire
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRings().target(Color.GREEN);
                    return false;
                }
                else {
                    boolean ret = hitSelectedMonster();
                    if (ret)
                        selectedMonster.adjFireTokens(1);
                    return ret;
                }
            }
            case FIREBALL_RED: {
                // Do 1 damage to 1 monster in red sectors (castle & forest included) and catch on fire
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRings().target(Color.RED);
                    return false;
                }
                else {
                    boolean ret = hitSelectedMonster();
                    if (ret)
                        selectedMonster.adjFireTokens(1);
                    return ret;
                }
            }
            case HAMMER_OF_LIGHT: {
                // slay 1 monster in forest ring
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().target(Ring.FOREST).slay();
                    return false;
                }
                else {
                    return hitSelectedMonster();
                }
            }
            case HYPNOTIZE: {
                // cause 2 monsters in same space to attack each other simultaneously (inc castle ring, not forest)
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRingsExceptForest();
                    return false;
                }
                else {
                    List<Monster> monsters =
                            model.getGame().getMonstersOnBoard().stream()
                                    .filter(monster -> monster.getRing() == selectedMonster.getRing() && monster.getArc() == selectedMonster.getArc())
                                    .collect(Collectors.toList());
                    if (monsters.size() <= 1)
                        return true;
                    if (monsters.size() > 2){
                        // TODO Player must choose which 2 monsters
                    }
                    Monster monster1 = monsters.get(0);
                    Monster monster2 = monsters.get(1);
                    for (int i = 0; i < monster1.getMaxHitpoints(); ++i){
                        selectedMonster = monster2;
                        hitSelectedMonster();
                    }
                    for (int i = 0; i < monster2.getMaxHitpoints(); ++i){
                        selectedMonster = monster1;
                        hitSelectedMonster();
                    }
                    return true;
                }
            }
            case LIGHTNING_BOLT: {
                // damage 1 monster for 1 point, then move to any arc in forest (inc castle ring, not forest)
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRingsExceptForest();
                    return false;
                }
                else {
                    boolean ret = hitSelectedMonster();
                    if (ret){
                        // TODO Ask player which Arc
                        selectedMonster.setArc(Arc.randomArc());
                        selectedMonster.setRing(Ring.FOREST);
                    }
                    return true;
                }
            }
            case MYSTICAL_MANUFACTURING: {
                // TODO play with 1 brick or 1 mortar to rebuild any tower
                return false;
            }
            case RAIN_OF_IRON: {
                // damage all monsters in one arc for 1 point (inc castle, not forest)
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRingsExceptForest();
                    return false;
                }
                else {
                    List<Monster> monsters =
                            model.getGame().getMonstersOnBoard().stream()
                                    .filter(monster -> monster.getArc() == selectedMonster.getArc() && monster.getRing() != Ring.FOREST)
                                    .collect(Collectors.toList());
                    for (Monster monster: monsters) {
                        selectedMonster = monster;
                        hitSelectedMonster();
                    }
                    return true;
                }
            }
            case RAIN_OF_ICE: {
                // no monsters move this turn (even if another token would make them move), all fire tokens removed from monsters (inc castle & forest)
                model.getGame().getMonstersOnBoard().stream().forEach(monster -> monster.setFireTokens(0));
                monstersFrozen = true;
                return true;
            }
            case RING_OF_FIRE: {
                // add fire token to all monsters in swordsman ring (all arcs)
                model.getGame().getMonstersOnBoard().stream().filter(monster -> monster.getRing() == Ring.SWORDSMAN).forEach(monster -> monster.adjFireTokens(1));
                return true;
            }
            case TELEPORT: {
                // TODO move any monster to any other space (castle & forest) or move fortify token to a different wall
                return false;
            }
            case THALGARS_BLESSING: {
                // all players draw up from castle deck
                while (model.getGame().getHand().size() < Game.HAND_SIZE){
                    model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
                }
                return true;
            }
            case VALADORS_WAVE: {
                // TODO assign up to 4 damage to any number of monsters in a single color (castle & forest)
                return false;
            }
            case WALL_OF_FORCE: {
                // move all monsters in one arc back to forest (inc castle)
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetAllRingsExceptForest();
                    return false;
                }
                else {
                    List<Monster> monsters =
                            model.getGame().getMonstersOnBoard().stream()
                                    .filter(monster -> monster.getArc() == selectedMonster.getArc() && monster.getRing() != Ring.FOREST)
                                    .collect(Collectors.toList());
                    monsters.stream().forEach(monster -> monster.setRing(Ring.FOREST));
                    return true;
                }
            }
            case WAR_STORM: {
                // do 1 point of damage to all monsters in swordsman, knight, and archer rings of 1 color
                if (selectedMonster == null) {
                    monsterTargetCriteria.reset().targetMiddleRings();
                    return false;
                }
                else {
                    List<Monster> monsters =
                            model.getGame().getMonstersOnBoard().stream()
                                    .filter(monster ->
                                            monster.getArc().getColor() == selectedMonster.getArc().getColor() &&
                                                    (monster.getRing() == Ring.SWORDSMAN || monster.getRing() == Ring.KNIGHT || monster.getRing() == Ring.ARCHER))
                                    .collect(Collectors.toList());
                    monsters.stream().forEach(monster -> {
                        selectedMonster = monster;
                        hitSelectedMonster();
                    });
                    return true;
                }
            }
            case WIZARD_QUAKE: {
                // TODO destory 1 tower and slay all monsters in same arc as tower (inc forest).  Does not affect walls.
                return false;
            }
        }
        return true;
    }

    private void applyOverrides(Card card){
        if (!card.isHitCard())
            return;
        if (overrideRing != null)
            applyOverrideRing(card);
        if (overrideColor != null)
            applyOverrideColor(card);
    }

    private void applyOverrideRing(Card card){
        switch (card.getAbility()){
            case ARCHER_ANY_COLOR:
            case KNIGHT_ANY_COLOR:
            case SWORDSMAN_ANY_COLOR:{
                switch (overrideRing) {
                    case SWORDSMAN -> {
                        card.setAbilityOverride(CardAbility.SWORDSMAN_ANY_COLOR);
                    }
                    case KNIGHT -> {
                        card.setAbilityOverride(CardAbility.KNIGHT_ANY_COLOR);
                    }
                    case ARCHER -> {
                        card.setAbilityOverride(CardAbility.ARCHER_ANY_COLOR);
                    }
                }
                break;
            }
            case ARCHER_BLUE:
            case KNIGHT_BLUE:
            case SWORDSMAN_BLUE:{
                switch (overrideRing) {
                    case SWORDSMAN -> {
                        card.setAbilityOverride(CardAbility.SWORDSMAN_BLUE);
                    }
                    case KNIGHT -> {
                        card.setAbilityOverride(CardAbility.KNIGHT_BLUE);
                    }
                    case ARCHER -> {
                        card.setAbilityOverride(CardAbility.ARCHER_BLUE);
                    }
                }
                break;
            }
            case ARCHER_GREEN:
            case KNIGHT_GREEN:
            case SWORDSMAN_GREEN:{
                switch (overrideRing) {
                    case SWORDSMAN -> {
                        card.setAbilityOverride(CardAbility.SWORDSMAN_GREEN);
                    }
                    case KNIGHT -> {
                        card.setAbilityOverride(CardAbility.KNIGHT_GREEN);
                    }
                    case ARCHER -> {
                        card.setAbilityOverride(CardAbility.ARCHER_GREEN);
                    }
                }
                break;
            }
            case ARCHER_RED:
            case KNIGHT_RED:
            case SWORDSMAN_RED:{
                switch (overrideRing) {
                    case SWORDSMAN -> {
                        card.setAbilityOverride(CardAbility.SWORDSMAN_RED);
                    }
                    case KNIGHT -> {
                        card.setAbilityOverride(CardAbility.KNIGHT_RED);
                    }
                    case ARCHER -> {
                        card.setAbilityOverride(CardAbility.ARCHER_RED);
                    }
                }
                break;
            }
        }
    }

    private void applyOverrideColor(Card card){
        switch (card.getAbility()){
            case HERO_BLUE:
            case HERO_GREEN:
            case HERO_RED:{
                if (overrideColor == Color.BLUE) {
                    card.setAbilityOverride(CardAbility.HERO_BLUE);
                }
                else if (overrideColor == Color.GREEN) {
                    card.setAbilityOverride(CardAbility.HERO_GREEN);
                }
                else if (overrideColor == Color.RED){
                    card.setAbilityOverride(CardAbility.HERO_RED);
                }
                break;
            }
            case ARCHER_BLUE:
            case ARCHER_GREEN:
            case ARCHER_RED:{
                if (overrideColor == Color.BLUE) {
                    card.setAbilityOverride(CardAbility.ARCHER_BLUE);
                }
                else if (overrideColor == Color.GREEN) {
                    card.setAbilityOverride(CardAbility.ARCHER_GREEN);
                }
                else if (overrideColor == Color.RED){
                    card.setAbilityOverride(CardAbility.ARCHER_RED);
                }
                break;
            }
            case KNIGHT_BLUE:
            case KNIGHT_GREEN:
            case KNIGHT_RED:{
                if (overrideColor == Color.BLUE) {
                    card.setAbilityOverride(CardAbility.KNIGHT_BLUE);
                }
                else if (overrideColor == Color.GREEN) {
                    card.setAbilityOverride(CardAbility.KNIGHT_GREEN);
                }
                else if (overrideColor == Color.RED){
                    card.setAbilityOverride(CardAbility.KNIGHT_RED);
                }
                break;
            }
            case SWORDSMAN_BLUE:
            case SWORDSMAN_GREEN:
            case SWORDSMAN_RED:{
                if (overrideColor == Color.BLUE) {
                    card.setAbilityOverride(CardAbility.SWORDSMAN_BLUE);
                }
                else if (overrideColor == Color.GREEN) {
                    card.setAbilityOverride(CardAbility.SWORDSMAN_GREEN);
                }
                else if (overrideColor == Color.RED){
                    card.setAbilityOverride(CardAbility.SWORDSMAN_RED);
                }
                break;
            }
        }
    }

    private boolean hitSelectedMonster(){
        if (selectedMonster != null){
            if (selectedMonster.getFatalRing() == selectedMonster.getRing()){
                monsterTargetCriteria.slay = true;
            }
            else if (selectedMonster.getImmunityRing() == selectedMonster.getRing()){
                ViewUtil.popupNotify(selectedMonster + " cannot be harmed in this ring");
                selectedMonster = null;
                return false;
            }

            if (monsterTargetCriteria.slay || niceShotActivated) {
                selectedMonster.setHitpoints(0);
                logger.info("Slay - " + selectedMonster.getName());
                niceShotActivated = false;
            }
            else {
                int hits = 1;
                if (enchantedActivated) {
                    hits += 2;
                    enchantedActivated = false;
                }
                selectedMonster.adjHitpoints(-hits);
                logger.info("Hit (" + hits + "x) - " + selectedMonster.getName());

                if (selectedMonster.getHitpoints() > 0){
                    if (knockBackActivated) {
                        if (selectedMonster.getRing() != Ring.FOREST)
                            selectedMonster.setRing(Ring.values()[selectedMonster.getRing().ordinal() + 1]);
                        knockBackActivated = false;
                    }
                    if (flamingActivated){
                        selectedMonster.adjFireTokens(1);
                        flamingActivated = false;
                    }
                }
            }

            removeDestroyedMonsters();

            if (berserkActivated){
                model.getGame().getHand().add(model.getGame().getCastlePanicDeck().draw());
            }
            selectedMonster = null;
        }
        return true;
    }

    private void moveMonsters(){
        if (monstersFrozen)
            return;
        for (Monster monster: model.getGame().getMonstersOnBoard()){
            moveMonster(monster);
        }
    }

    private void moveMonsters(Color color){
        if (monstersFrozen)
            return;
        for (Monster monster: model.getGame().getMonstersOnBoard()){
            if (monster.getArc().getColor() == color)
                moveMonster(monster);
        }
    }

    private void moveMonster(Monster monster){
        if (monstersFrozen)
            return;
        if (monster.hasAbility(MonsterAbility.DRAGON)) {
            // In play, roll 1d6 and consult table
            int die = Util.roll();
            switch (die){
                case 1:
                    // Move 1 space CW
                    moveMonsterRotate(monster, true);
                    break;
                case 2:
                case 5:
                    // Move 1 forward
                    moveMonsterNormal(monster);
                    break;
                case 3:
                    // No movement
                    break;
                case 4:
                    // Move 1 space backward
                    Ring nextRing = monster.getRing().closerToForest();
                    monster.setRing(nextRing);
                    break;
                case 6:
                    // Move 1 space CCW
                    moveMonsterRotate(monster, false);
                    break;
            }
            if (monster.getRing() != Ring.CASTLE)
                breathFire(monster);
        }
        else if (monster.hasAbility(MonsterAbility.CHIMERA)){
            // In play, moves 1 space CCW, then 1 space toward castle, if still alive, it breathes fire (if outside castle ring)
            if (monster.isTar()) {
                monster.setTar(false);
            }
            else {
                moveMonsterRotate(monster, false);
                moveMonsterNormal(monster);
                if (monster.getHitpoints() > 0 && monster.getRing() != Ring.CASTLE){
                    breathFire(monster);
                }
            }
        }
        else if (monster.hasAbility(MonsterAbility.WARLOCK)){
            if (!monster.isTar() && monster.getRing() != Ring.CASTLE) {
                Arc newArc = Arc.values()[Util.randInt(6)];
                monster.setArc(newArc);
            }
            moveMonsterNormal(monster);
        }
        else
            moveMonsterNormal(monster);
    }

    private void moveMonsterNormal(Monster monster){
        if (monster.hasAbility(MonsterAbility.DOPPELGANGER))
            return;
        if (monster.isTar()) {
            monster.setTar(false);
            return;
        }

        int numRings = monster.hasAbility(MonsterAbility.MOVE_2_SPACES)? 2: 1;
        for (int i = 0; i < numRings; ++i) {
            switch (monster.getRing()) {
                case CASTLE: {
                    moveMonsterRotate(monster, true);
                    break;
                }
                case SWORDSMAN: {
                    CastleWall wall = model.getGame().getWall(monster.getArc());
                    if (monster.hasAbility(MonsterAbility.CLIMB_WALLS)){
                        // Bypass wall, but if wall is burning, add fire token to monster
                        if (wall.getFireTokens() > 0){
                            monster.adjFireTokens(1);
                        }
                        CastleTower tower = model.getGame().getTower(monster.getArc());
                        if (!tower.isDestroyed()) {
                            tower.setDestroyed(true);
                            monster.adjHitpoints(-1);
                            if (monster.hasAbility(MonsterAbility.HYDRA)){
                                handleHydraDamaged(monster, 1);
                            }
                            else if (monster.hasAbility(MonsterAbility.NECROMANCER)){
                                monster.setHitpoints(0);
                                // Move monster from discard pile to draw pile
                                model.getGame().getMonsterBag().shuffleDefeated();
                                Monster monster1 = model.getGame().getMonsterBag().drawDefeatedMonster();
                                if (monster1 != null && monster1.getType() != MonsterType.EFFECT && monster1.getType() != MonsterType.MEGABOSS)
                                    model.getGame().getMonsterBag().add(monster1);
                            }
                        } else
                            monster.setRing(Ring.CASTLE);
                    }
                    else {
                        // Check if there's a wall
                        if (!wall.isDestroyed()) {
                            if (wall.isFortified()) {
                                wall.setFortified(false);
                            } else
                                wall.setDestroyed(true);
                            monster.adjHitpoints(-1);
                            if (monster.hasAbility(MonsterAbility.HYDRA)){
                                handleHydraDamaged(monster, 1);
                            }
                            else if (monster.hasAbility(MonsterAbility.NECROMANCER)){
                                monster.setHitpoints(0);
                                // Move monster from discard pile to draw pile
                                model.getGame().getMonsterBag().shuffleDefeated();
                                Monster monster1 = model.getGame().getMonsterBag().drawDefeatedMonster();
                                if (monster1 != null && monster1.getType() != MonsterType.EFFECT && monster1.getType() != MonsterType.MEGABOSS)
                                    model.getGame().getMonsterBag().add(monster1);
                            }
                        } else
                            monster.setRing(Ring.CASTLE);
                    }
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

    private void moveMonsterRotate(Monster monster, boolean cw){
        if (monster.isTar()) {
            monster.setTar(false);
            return;
        }
        Arc nextArc = cw? monster.getArc().rotateClockwise(): monster.getArc().rotateCounterClockwise();
        if (monster.getRing() == Ring.CASTLE) {
            // Check if arc contains castle tower
            CastleTower tower = model.getGame().getTower(nextArc);
            if (!tower.isDestroyed()) {
                tower.setDestroyed(true);
                monster.adjHitpoints(-1);
                if (monster.hasAbility(MonsterAbility.HYDRA)){
                    handleHydraDamaged(monster, 1);
                }
                else if (monster.hasAbility(MonsterAbility.NECROMANCER)){
                    monster.setHitpoints(0);
                    // Move monster from discard pile to draw pile
                    model.getGame().getMonsterBag().shuffleDefeated();
                    Monster monster1 = model.getGame().getMonsterBag().drawDefeatedMonster();
                    if (monster1 != null && monster1.getType() != MonsterType.EFFECT && monster1.getType() != MonsterType.MEGABOSS)
                        model.getGame().getMonsterBag().add(monster1);
                }
                return;
            }
        }
        monster.setArc(nextArc);
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
        if (monster == null)
            return;
        ViewUtil.popupNotify("New Monster - " + monster);
        if (monster.getType() == MonsterType.EFFECT){
            handleMonsterEffectOnDraw(monster);
        }
        else {
            placeMonsterOnBoard(monster);
            if (!monster.getAbilities().isEmpty())
                handleMonsterEffectOnDraw(monster);
        }
    }

    private void handleMonsterEffectOnDraw(Monster monster){
        for (MonsterAbility ability: monster.getAbilities()) {
            switch (ability){
                case CHIMERA:
                case DRAGON:{
                    // breathes fire when placed
                    breathFire(monster);
                    break;
                }
                case WARLOCK:{
                    // TODO Player must discard 1 Wizard card
                    break;
                }
                case HEALER:{
                    // Heal all monster by 1
                    model.getGame().getMonstersOnBoard().stream().forEach(monster1 -> {
                        if (monster1.getHitpoints() > 0){
                            monster1.adjHitpoints(1);
                        }
                    });
                    break;
                }
                case BASILISK:{
                    // TODO Player discards down to 2 cards
                    // TODO While in play, skip discard-and-draw step
                    break;
                }
                case CONJURER:{
                    placeMonsterOnBoard(monster);
                    // Roll 1d6, place that many Imps in forest ring, one per arc, starting at Arc 1
                    int numImps = Util.roll();
                    for (int i = 0; i < numImps; ++i){
                        Imp imp = new Imp();
                        imp.setArc(Arc.values()[i]);
                        imp.setRing(Ring.FOREST);
                        model.getGame().getMonstersOnBoard().add(imp);
                    }
                    break;
                }
                case TROLL_MAGE:{
                    // Move all monsters
                    moveMonsters();
                    break;
                }
                case NECROMANCER:{
                    // Put two monsters (not effects) from discard pile to draw pile
                    model.getGame().getMonsterBag().shuffleDefeated();
                    int count = 0;
                    while (count < 2){
                        Monster monster1 = model.getGame().getMonsterBag().drawDefeatedMonster();
                        if (monster1 != null && monster1.getType() == MonsterType.EFFECT || monster1.getType() == MonsterType.MEGABOSS)
                            continue;
                        model.getGame().getMonsterBag().add(monster1);
                        count += 1;
                    }
                    break;
                }
                case GOBLIN_KING:{
                    // place 3 more monster tokens
                    drawNewMonsters(3);
                    break;
                }
                case ORC_WARLORD:{
                    // Move all monsters in same color 1x
                    moveMonsters(monster.getArc().getColor());
                    break;
                }
                case DOPPELGANGER:{
                    // Place on board, but do not move.  Gets replaced by next destroyed monster.
                    placeMonsterOnBoard(monster);
                    break;
                }
                case PHOENIX:{
                    // When destroyed, all monsters in same space get 1 fire token (but no damage)
                    model.getGame().getMonstersOnBoard().stream().forEach(monster1 -> {
                        if (monster1.getArc() == monster.getArc() && monster1.getRing() == monster.getRing() && monster1.getHitpoints() > 0){
                            monster1.adjFireTokens(1);
                        }
                    });
                    break;
                }
                case GIANT_BOULDER:
                case TREBUCHET:{
                    // Boulder comes from forest and destroys all monsters in path until it hits a wall, tower, or goes off board
                    placeMonsterOnBoard(monster);
                    rollGiantBoulderOrTrebuchet(monster);
                    model.getGame().getMonstersOnBoard().remove(monster);
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

    private void rollGiantBoulderOrTrebuchet(Monster monster){
        // Boulder comes from forest and destroys all monsters in path until it hits a wall, tower, or goes off board
        boolean setNextStructureOnFire = false;

        for (int i = Ring.FOREST.ordinal(); i >= Ring.CASTLE.ordinal(); --i){
            Ring ring = Ring.values()[i];
            monster.setRing(ring);
            removeDestroyedMonsters();
            view.getGamePanel().getBoardPanel().refresh();
            model.getGame().getMonstersOnBoard().stream().forEach(monster1 -> {
                if (monster != monster1 &&
                        !monster1.hasAbility(MonsterAbility.DOPPELGANGER) &&
                        monster1.getArc() == monster.getArc() &&
                        monster1.getRing() == ring &&
                        ((monster.hasAbility(MonsterAbility.GIANT_BOULDER) && !monster1.hasAbility(MonsterAbility.FLYING) ||
                                (monster.hasAbility(MonsterAbility.TREBUCHET) && monster1.hasAbility(MonsterAbility.FLYING))))){
                    monster1.setHitpoints(0);
                }
            });

            // Check if next ring contains wall or tower
            if (ring == Ring.SWORDSMAN) {
                CastleWall wall = model.getGame().getWall(monster.getArc());
                if (!wall.isDestroyed()){
                    if (wall.isFortified()) {
                        wall.setFortified(false);
                        if (monster.hasAbility(MonsterAbility.FIRE_STARTER))
                            wall.adjFireTokens(1);
                    }
                    else {
                        wall.setDestroyed(true);
                        if (monster.hasAbility(MonsterAbility.FIRE_STARTER))
                            setNextStructureOnFire = true;
                    }
                    if (!setNextStructureOnFire)
                        return;
                }
                CastleTower tower = model.getGame().getTower(monster.getArc());
                if (!tower.isDestroyed()){
                    if (setNextStructureOnFire) {
                        tower.adjFireTokens(1);
                        return;
                    }
                    else {
                        tower.setDestroyed(true);
                        if (monster.hasAbility(MonsterAbility.FIRE_STARTER))
                            setNextStructureOnFire = true;
                        else
                            return;
                    }
                }
            }
        }

        // Keep going on other side of board
        monster.setArc(monster.getArc().getOpposite());
        CastleTower tower = model.getGame().getTower(monster.getArc());
        if (tower != null && !tower.isDestroyed()){
            if (setNextStructureOnFire) {
                tower.adjFireTokens(1);
                return;
            }
            else {
                tower.setDestroyed(true);
                if (monster.hasAbility(MonsterAbility.FIRE_STARTER))
                    setNextStructureOnFire = true;
                else
                    return;
            }
        }

        for (int i = Ring.CASTLE.ordinal(); i <= Ring.FOREST.ordinal(); ++i){
            Ring ring = Ring.values()[i];
            monster.setRing(ring);
            removeDestroyedMonsters();
            view.getGamePanel().getBoardPanel().refresh();
            model.getGame().getMonstersOnBoard().stream().forEach(monster1 -> {
                if (monster != monster1 &&
                        !monster1.hasAbility(MonsterAbility.DOPPELGANGER) &&
                        monster1.getArc() == monster.getArc() &&
                        monster1.getRing() == ring &&
                        ((monster.hasAbility(MonsterAbility.GIANT_BOULDER) && !monster1.hasAbility(MonsterAbility.FLYING) ||
                                (monster.hasAbility(MonsterAbility.TREBUCHET) && monster1.hasAbility(MonsterAbility.FLYING))))){
                        monster1.setHitpoints(0);
                }
            });

            // Check if next ring contains wall or tower
            if (ring == Ring.CASTLE) {
                CastleWall wall = model.getGame().getWall(monster.getArc());
                if (!wall.isDestroyed()){
                    if (wall.isFortified()) {
                        wall.setFortified(false);
                        if (monster.hasAbility(MonsterAbility.FIRE_STARTER)) {
                            wall.adjFireTokens(1);
                        }
                    }
                    else {
                        if (monster.hasAbility(MonsterAbility.FIRE_STARTER) && setNextStructureOnFire) {
                            wall.adjFireTokens(1);
                        } else {
                            wall.setDestroyed(true);
                        }
                    }
                    return;
                }
            }
        }
    }

    private void breathFire(Monster monster){
        // Fireball travels like Giant Boulder, but does not damage monsters.  Add fire token to first structure encountered.
        CastleWall wall = model.getGame().getWall(monster.getArc());
        if (!wall.isDestroyed()){
            if (wall.isFortified()) {
                wall.setFortified(false);
            }
            else {
                wall.adjFireTokens(1);
            }
            return;
        }

        CastleTower tower = model.getGame().getTower(monster.getArc());
        if (!tower.isDestroyed()){
            tower.adjFireTokens(1);
        }

        // Keep going on other side of board
        monster.setArc(monster.getArc().getOpposite());

        tower = model.getGame().getTower(monster.getArc());
        if (!tower.isDestroyed()){
            tower.adjFireTokens(1);
            return;
        }

        wall = model.getGame().getWall(monster.getArc());
        if (!wall.isDestroyed()){
            if (wall.isFortified()) {
                wall.setFortified(false);
            }
            else {
                wall.adjFireTokens(1);
            }
        }
    }

    /**
     * Add 2 imps for each point of damage to Hydra
     * @param hydra
     * @param numDamage
     */
    private void handleHydraDamaged(Monster hydra, int numDamage){
        for (int i = 0; i < numDamage; ++i){
            Imp imp = new Imp();
            imp.setRing(Ring.FOREST);
            imp.setArc(hydra.getArc());
            model.getGame().getMonstersOnBoard().add(imp);
            imp = new Imp();
            imp.setRing(Ring.FOREST);
            imp.setArc(hydra.getArc());
            model.getGame().getMonstersOnBoard().add(imp);
        }
    }

    private static class MonsterTargetCriteria{
        public Color color;
        public Arc arc;
        public Set<Ring> validRings = new HashSet<>();
        public boolean slay;

        public MonsterTargetCriteria reset(){
            this.color = null;
            this.arc = null;
            this.validRings.clear();
            this.slay = false;
            return this;
        }

        public MonsterTargetCriteria target(Arc arc){
            this.arc = arc;
            return this;
        }

        public MonsterTargetCriteria target(Color color){
            this.color = color;
            return this;
        }

        public MonsterTargetCriteria target(Ring ... rings){
            for (Ring ring: rings)
                this.validRings.add(ring);
            return this;
        }

        public MonsterTargetCriteria targetAllRings(){
            return target(Ring.CASTLE).target(Ring.SWORDSMAN).target(Ring.KNIGHT).target(Ring.ARCHER).target(Ring.FOREST);
        }

        public MonsterTargetCriteria targetAllRingsExceptForest(){
            return target(Ring.CASTLE).target(Ring.SWORDSMAN).target(Ring.KNIGHT).target(Ring.ARCHER);
        }

        public MonsterTargetCriteria targetAllRingsExceptCastle(){
            return target(Ring.FOREST).target(Ring.SWORDSMAN).target(Ring.KNIGHT).target(Ring.ARCHER);
        }

        public MonsterTargetCriteria targetMiddleRings(){
            return target(Ring.SWORDSMAN).target(Ring.KNIGHT).target(Ring.ARCHER);
        }

        public MonsterTargetCriteria slay(){
            this.slay = true;
            return this;
        }

        public boolean matches(Monster monster){
            if (this.color != null && this.color != monster.getArc().getColor())
                return false;
            if (this.arc != null && this.arc != monster.getArc())
                return false;
            if (this.validRings != null && (!this.validRings.contains(monster.getRing()) || (this.validRings.contains(Ring.ARCHER) && monster.getRing() != Ring.ARCHER && !monster.hasAbility(MonsterAbility.FLYING))))
                return false;
            return true;
        }

        public void explain(Monster monster){
            if (this.color != null && this.color != monster.getArc().getColor()) {
                logger.info("required color is " + this.color + ", but monster color is " + monster.getArc().getColor());
            }
            if (this.arc != null && this.arc != monster.getArc()) {
                logger.info("required arc is " + this.arc + ", but monster arc is " + monster.getArc());
            }
            if (this.validRings != null && (!this.validRings.contains(monster.getRing()) || (this.validRings.contains(Ring.ARCHER) && monster.getRing() != Ring.ARCHER && !monster.hasAbility(MonsterAbility.FLYING)))){
                logger.info("required ring is " + this.validRings + ", but monster ring is " + monster.getRing());
            }
        }
    }
}
