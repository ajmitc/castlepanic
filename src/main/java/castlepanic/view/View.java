package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;
import java.awt.*;

public class View {
    private static final String MAINMENU = "mainmenu";
    private static final String GAME = "game";

    private Model model;
    private JFrame frame;

    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;

    public View(Model model){
        this.model = model;
        this.frame = new JFrame();
        this.frame.setTitle("Castle Panic");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setLocation(0, 0);
        this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        mainMenuPanel = new MainMenuPanel(model, this);
        gamePanel = new GamePanel(model, this);

        this.frame.getContentPane().setLayout(new CardLayout());
        this.frame.getContentPane().add(mainMenuPanel, MAINMENU);
        this.frame.getContentPane().add(gamePanel, GAME);
    }

    public void refresh(){
        gamePanel.refresh();
    }

    public void showGame(){
        CardLayout cardLayout = (CardLayout) this.frame.getContentPane().getLayout();
        cardLayout.show(this.frame.getContentPane(), GAME);
    }

    public JFrame getFrame(){
        return frame;
    }
}
