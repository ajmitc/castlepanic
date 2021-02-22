package castlepanic.game.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<>();
    private List<Card> discardPile = new ArrayList<>();

    public Deck(){

    }

    public Card draw() {
        if (cards.isEmpty()){
            cards.addAll(discardPile);
            discardPile.clear();
            shuffle();
        }
        return cards.remove(0);
    }

    public void discard(Card card){
        discardPile.add(card);
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public void add(Card card){
        cards.add(card);
    }
}
