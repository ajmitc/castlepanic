package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    private BoardPanel boardPanel;
    private HandPanel handPanel;
    private DoneButtonPanel doneButtonPanel;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        boardPanel = new BoardPanel(model, view);
        handPanel  = new HandPanel(model, view, boardPanel.getPreferredSize().height);
        doneButtonPanel = new DoneButtonPanel(model, view);

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(handPanel, BorderLayout.CENTER);
        eastPanel.add(doneButtonPanel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
    }

    public void init(){
        boardPanel.init();
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

    public DoneButtonPanel getDoneButtonPanel() {
        return doneButtonPanel;
    }
}
