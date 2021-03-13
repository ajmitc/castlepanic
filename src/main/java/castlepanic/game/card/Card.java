package castlepanic.game.card;

import castlepanic.view.ImageUtil;

import java.awt.image.BufferedImage;

public abstract class Card {
    public static final int CARD_WIDTH = 150;
    protected CardType type;
    protected String name;
    protected BufferedImage cardImage;
    protected CardAbility ability;
    protected CardAbility abilityOverride; // Used for Change Color and Change Range effects
    protected boolean hitCard;
    protected int order;

    // Current location of card on screen
    protected int px, py;
    protected boolean selected;

    public Card(CardType type, String name, CardAbility ability, String imageName){
        this.type = type;
        this.name = name;
        this.ability = ability;
        this.cardImage = ImageUtil.get(imageName, CARD_WIDTH);
        this.abilityOverride = null;
        this.hitCard = false;
        this.selected = false;
        this.order = 10;
    }

    public CardType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public CardAbility getAbility() {
        if (abilityOverride != null)
            return abilityOverride;
        return ability;
    }

    public BufferedImage getCardImage() {
        return cardImage;
    }

    public void setAbilityOverride(CardAbility abilityOverride) {
        this.abilityOverride = abilityOverride;
    }

    public void setLocation(int px, int py){
        this.px = px;
        this.py = py;
    }

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }

    public boolean contains(int px, int py) {
        return px >= this.px && py >= this.py && px < this.px + cardImage.getWidth() && py < this.py + cardImage.getHeight();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHitCard() {
        return hitCard;
    }

    public int getOrder() {
        return order;
    }

    public String toString(){
        return name + " [" + type + "]";
    }
}
