package castlepanic.view;

import castlepanic.Model;
import castlepanic.game.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HandPanel extends JPanel {
    private Model model;
    private View view;

    public HandPanel(Model model, View view, int height){
        super();
        this.model = model;
        this.view = view;
        setPreferredSize(new Dimension(Card.CARD_WIDTH * 3, height));
    }

    public void paintComponent(Graphics graphics){
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        if (model.getGame() != null && model.getGame().getHand() != null){
            int x = 0;
            int y = 0;
            int maxColumns = 2;
            if (model.getGame().getHand().size() > 8)
                maxColumns += 1;
            for (int i = 0; i < model.getGame().getHand().size(); ++i){
                Card card = model.getGame().getHand().get(i);
                BufferedImage bi = card.getCardImage();
                x = (i % maxColumns) * bi.getWidth();
                y = (i / maxColumns) * bi.getHeight();
                card.setLocation(x, y);
                graphics.drawImage(bi, x, y, null);
            }

            for (int i = 0; i < model.getGame().getHand().size(); ++i){
                Card card = model.getGame().getHand().get(i);
                if (card.isSelected()){
                    BufferedImage bi = card.getCardImage();
                    graphics.setColor(Color.GREEN);
                    graphics.drawRect(card.getPx(), card.getPy(), bi.getWidth(), bi.getHeight());
                }
            }
        }
    }

    public Card getSelected(int mx, int my){
        for (Card card: model.getGame().getHand()){
            if (card.contains(mx, my))
                return card;
        }
        return null;
    }

    public void refresh(){
        repaint();
    }
}
