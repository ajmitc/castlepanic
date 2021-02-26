package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    private BoardPanel boardPanel;
    private HandPanel handPanel;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        boardPanel = new BoardPanel(model, view);
        handPanel  = new HandPanel(model, view);

        add(boardPanel, BorderLayout.CENTER);
        add(handPanel, BorderLayout.SOUTH);
    }

    public void refresh(){
        boardPanel.refresh();
        handPanel.refresh();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public HandPanel getHandPanel() {
        return handPanel;
    }
}
