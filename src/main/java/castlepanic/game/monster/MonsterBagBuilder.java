package castlepanic.game.monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonsterBagBuilder {
    public static MonsterBag buildMonsterBag(){
        MonsterBag bag = new MonsterBag();

        // Normal Monsters
        bag.add(new Centaur());
        for (int i = 0; i < 2; ++i)
            bag.add(new ClimbingTroll());
        bag.add(new Conjurer());
        bag.add(new Cyclops());
        bag.add(new Doppelganger());
        for (int i = 0; i < 2; ++i)
            bag.add(new Gargoyle());
        for (int i = 0; i < 2; ++i)
            bag.add(new GoblinCavalry());
        bag.add(new Golem());
        bag.add(new Imp());
        for (int i = 0; i < 3; ++i)
            bag.add(new Ogre());
        for (int i = 0; i < 5; ++i)
            bag.add(new Orc());
        for (int i = 0; i < 4; ++i)
            bag.add(new Troll());
        for (int i = 0; i < 2; ++i)
            bag.add(new Phoenix());

        // Effects
        bag.add(new OneImpPerTower());
        for (int i = 0; i < 2; ++i)
            bag.add(new AllPlayersDiscardOneCard());
        bag.add(new Draw3MonsterTokens());
        bag.add(new Draw4MonsterTokens());
        bag.add(new FlamingBoulder());
        for (int i = 0; i < 2; ++i)
            bag.add(new GiantBoulder());
        bag.add(new MonstersMoveClockwise());
        bag.add(new MonstersMoveCounterClockwise());
        bag.add(new MonstersMove1Blue());
        bag.add(new MonstersMove1Green());
        bag.add(new MonstersMove1Red());
        bag.add(new PlagueArchers());
        bag.add(new PlagueKnights());
        bag.add(new PlagueSwordsmen());
        bag.add(new Trebuchet());

        // Bosses
        bag.add(new GoblinKing());
        bag.add(new Healer());
        bag.add(new OrcWarlord());
        bag.add(new TrollMage());

        bag.shuffle();
        return bag;
    }

    public static void addMegaBosses(MonsterBag bag){
        List<Monster> megabosses = new ArrayList<>();
        megabosses.add(new Basilisk());
        megabosses.add(new Chimera());
        megabosses.add(new Dragon());
        megabosses.add(new Hydra());
        megabosses.add(new Necromancer());
        megabosses.add(new Warlock());

        // Add 3 randomly to bag
        Collections.shuffle(megabosses);
        for (int i = 0; i < 3; ++i){
            bag.add(megabosses.get(i));
        }
    }

    private MonsterBagBuilder(){}
}
