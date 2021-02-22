package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private Model model;
    private View view;

    public MainMenuPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;
    }
}
