package castlepanic.game.card;

import castlepanic.view.ImageUtil;

import java.awt.image.BufferedImage;

public abstract class Card {
    protected CardType type;
    protected String name;
    protected BufferedImage cardImage;
    protected CardAbility ability;
    protected CardAbility abilityOverride; // Used for Change Color and Change Range effects

    public Card(CardType type, String name, CardAbility ability, String imageName){
        this.type = type;
        this.name = name;
        this.ability = ability;
        this.cardImage = ImageUtil.get(imageName);
        this.abilityOverride = null;
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
}
