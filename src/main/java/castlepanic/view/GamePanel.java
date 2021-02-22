package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    public GamePanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;
    }

    public void refresh(){

    }
}
