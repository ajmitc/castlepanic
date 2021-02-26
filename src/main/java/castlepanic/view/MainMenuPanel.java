package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {
    private Model model;
    private View view;

    private BufferedImage coverImage;
    private JButton btnNewGame;
    private JButton btnExit;

    public MainMenuPanel(Model model, View view){
        super(new GridLayout(1, 2));
        this.model = model;
        this.view = view;

        this.coverImage = ImageUtil.get("castle-panic.jpg");
        add(new JLabel(new ImageIcon(this.coverImage)));

        btnNewGame = new JButton("New Game");
        btnExit = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(btnNewGame);
        buttonPanel.add(btnExit);
        add(buttonPanel);
    }

    public JButton getBtnNewGame() {
        return btnNewGame;
    }

    public JButton getBtnExit() {
        return btnExit;
    }
}
