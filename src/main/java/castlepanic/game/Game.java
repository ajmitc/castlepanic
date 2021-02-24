package castlepanic.game;

import castlepanic.Util;
import castlepanic.game.card.Card;
import castlepanic.game.card.Deck;
import castlepanic.game.card.DeckBuilder;
import castlepanic.game.monster.Monster;
import castlepanic.game.monster.MonsterBag;
import castlepanic.game.monster.MonsterBagBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    public static final int HAND_SIZE = 6;

    private Phase phase;
    private PhaseStep phaseStep;

    private Deck castlePanicDeck;
    private Deck wizardDeck;

    private MonsterBag monsterBag;
    private Set<Monster> monstersOnBoard = new HashSet<>();

    private List<Card> hand = new ArrayList<>();

    private List<CastleTower> towers = new ArrayList<>(6);
    private List<CastleWall> walls = new ArrayList<>(6);

    public Game() {

    }

    public void setup(){
        phase = Phase.SETUP;
        phaseStep = PhaseStep.START_PHASE;

        castlePanicDeck = DeckBuilder.buildCastlePanicDeck();
        wizardDeck      = DeckBuilder.buildWizardDeck();

        monsterBag      = MonsterBagBuilder.buildMonsterBag();

        towers.clear();
        towers.add(new CastleTower(Arc.ARC_1));
        towers.add(new CastleTower(Arc.ARC_2));
        towers.add(new CastleTower(Arc.ARC_3));
        towers.add(new CastleTower(Arc.ARC_4));
        towers.add(new CastleTower(Arc.ARC_5));
        towers.add(new CastleTower(Arc.ARC_6));

        Arc wizardTowerArc = Arc.values()[Util.randInt(Arc.values().length)];
        towers.stream().forEach(tower -> {
            if (tower.getArc() == wizardTowerArc){
                tower.setWizardTower();
            }
        });

        walls.clear();
        walls.add(new CastleWall(Arc.ARC_1));
        walls.add(new CastleWall(Arc.ARC_2));
        walls.add(new CastleWall(Arc.ARC_3));
        walls.add(new CastleWall(Arc.ARC_4));
        walls.add(new CastleWall(Arc.ARC_5));
        walls.add(new CastleWall(Arc.ARC_6));
    }

    public void addInitialMonstersToBoard(){
        // Randomly choose 6 monsters and add to Archer ring of each Arc
        for (Arc arc: Arc.values()){
            Monster monster = monsterBag.draw();
            monster.setArc(arc);
            monster.setRing(Ring.ARCHER);
            monstersOnBoard.add(monster);
        }
    }

    public void addMegaBossesToBag(){
        MonsterBagBuilder.addMegaBosses(monsterBag);
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

    public Deck getWizardDeck() {
        return wizardDeck;
    }

    public MonsterBag getMonsterBag() {
        return monsterBag;
    }

    public Set<Monster> getMonstersOnBoard() {
        return monstersOnBoard;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<CastleTower> getTowers() {
        return towers;
    }

    public CastleTower getTower(Arc arc){
        return towers.stream().filter(tower -> tower.getArc() == arc).findFirst().get();
    }

    public List<CastleWall> getWalls() {
        return walls;
    }

    public CastleWall getWall(Arc arc){
        return walls.stream().filter(wall -> wall.getArc() == arc).findFirst().get();
    }
}
