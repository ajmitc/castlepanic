package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    private BufferedImage boardImage;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.get("Board 3.png", 1000);

        add(new JScrollPane(new JLabel(new ImageIcon(boardImage))), BorderLayout.CENTER);
    }

    public void refresh(){

    }
}
