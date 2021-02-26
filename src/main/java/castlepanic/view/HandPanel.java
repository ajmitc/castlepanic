package castlepanic.view;

import castlepanic.Model;
import castlepanic.game.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HandPanel extends JPanel {
    private Model model;
    private View view;

    public HandPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;
    }

    public void paintComponent(Graphics graphics){
        if (model.getGame() != null && model.getGame().getHand() != null){
            for (int i = 0; i < model.getGame().getHand().size(); ++i){
                Card card = model.getGame().getHand().get(i);
                BufferedImage bi = card.getCardImage();
                graphics.drawImage(bi, i * bi.getWidth(), 0, null);
            }
        }
    }

    public void refresh(){
        repaint();
    }
}
