package castlepanic;

import castlepanic.game.*;
import castlepanic.game.card.Card;
import castlepanic.game.card.CardAbility;
import castlepanic.game.monster.*;
import castlepanic.view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Model model;
    private View view;

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
                view.showGame();
                run();
            }
        });
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
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DISCARD_AND_DRAW_1);
                            break;
                        }
                        case PLAY_DISCARD_AND_DRAW_1:{
                            // TODO Ask player if they want to discard and draw
                            // TODO Solo rules allow 2x
                            model.getGame().setPhaseStep(PhaseStep.PLAY_DISCARD_AND_DRAW_2);
                            break;
                        }
                        case PLAY_DISCARD_AND_DRAW_2:{
                            model.getGame().setPhaseStep(PhaseStep.PLAY_CARDS);
                            break;
                        }
                        case PLAY_CARDS:{
                            // TODO Ask player to choose a card to play
                            // TODO When Hydra is damaged (but not slain), add 2 Imps to FOREST ring of same Arc as Hydra
                            // TODO Warlock unaffected by wizard cards
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MOVE_MONSTERS);
                            return;
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
                            // Remove destroyed monsters
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
                            }
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
        }
    }

    private void applyCastlePanicCardAbility(CardAbility ability){
        switch (ability) {
            case ARCHER_ANY_COLOR: {
            }
            case ARCHER_BLUE: {
            }
            case ARCHER_GREEN: {
            }
            case ARCHER_RED: {
            }
            case BARBARIAN: {
            }
            case BERSERK: {
            }
            case BRICK: {
            }
            case CHANGE_COLOR: {
                // TODO Select card in hand to change color (set card's ability override)
                break;
            }
            case CHANGE_RANGE: {
                // TODO Select card in hand to change range (set card's ability override)
                break;
            }
            case DOUBLE_STRIKE: {
            }
            case DRAW_2_CARDS: {
            }
            case DRIVE_HIM_BACK: {
            }
            case ENCHANTED: {
            }
            case FLAMING: {
            }
            case FORTIFY_WALL: {
            }
            case HERO_BLUE: {
            }
            case HERO_GREEN: {
            }
            case HERO_RED: {
            }
            case KNIGHT_ANY_COLOR: {
            }
            case KNIGHT_BLUE: {
            }
            case KNIGHT_GREEN: {
            }
            case KNIGHT_RED: {
            }
            case KNOCK_BACK: {
            }
            case MISSING: {
            }
            case MORTAR: {
            }
            case NEVER_LOSE_HOPE: {
            }
            case NICE_SHOT: {
            }
            case REINFORCE: {
            }
            case SCAVENGE: {
            }
            case STAND_TOGETHER: {
            }
            case SWORDSMAN_ANY_COLOR: {
            }
            case SWORDSMAN_BLUE: {
            }
            case SWORDSMAN_GREEN: {
            }
            case SWORDSMAN_RED: {
            }
            case TAR: {
            }
        }
    }

    private void applyWizardCardAbility(CardAbility ability){
        switch (ability){
            case ARCANE_ASSEMBLY:{
                // TODO All player may immediately build walls for 1 BrickCard OR 1 Mortar per wall
                break;
            }
            case AZRIELS_FIST: {
            }
            case BURNING_BLAST: {
            }
            case CHAIN_LIGHTNING: {
            }
            case EXTINGUISHING_WIND: {
            }
            case EYE_OF_THE_ORACLE: {
            }
            case FIREBALL_BLUE: {
            }
            case FIREBALL_GREEN: {
            }
            case FIREBALL_RED: {
            }
            case HAMMER_OF_LIGHT: {
            }
            case HYPNOTIZE: {
            }
            case LIGHTNING_BOLT: {
            }
            case MYSTICAL_MANUFACTURING: {
            }
            case RAIN_OF_IRON: {
            }
            case RAIN_OF_ICE: {
            }
            case RING_OF_FIRE: {
            }
            case TELEPORT: {
            }
            case THALGARS_BLESSING: {
            }
            case VALADORS_WAVE: {
            }
            case WALL_OF_FORCE: {
            }
            case WAR_STORM: {
            }
            case WIZARD_QUAKE: {
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
}
