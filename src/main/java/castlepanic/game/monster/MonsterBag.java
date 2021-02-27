package castlepanic.game.monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonsterBag {
    private List<Monster> monsters = new ArrayList<>();
    private List<Monster> defeatedMonsters = new ArrayList<>();

    public MonsterBag(){

    }

    public Monster draw() {
        if (monsters.isEmpty()){
            return null;
        }
        return monsters.remove(0);
    }

    public Monster drawDefeatedMonster(){
        if (defeatedMonsters.isEmpty()){
            return null;
        }
        return defeatedMonsters.remove(0);
    }

    public void defeat(Monster monster){
        defeatedMonsters.add(monster);
    }

    public void shuffle(){
        Collections.shuffle(monsters);
    }

    public void shuffleDefeated(){
        Collections.shuffle(defeatedMonsters);
    }

    public void add(Monster monster){
        monsters.add(monster);
    }

    public List<Monster> getDefeatedMonsters() {
        return defeatedMonsters;
    }

    public boolean isEmpty(){
        return monsters.isEmpty();
    }
}
