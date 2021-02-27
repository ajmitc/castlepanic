package castlepanic.view;

import castlepanic.Model;

import javax.swing.*;
import java.awt.*;

public class DoneButtonPanel extends JPanel {
    private Model model;
    private View view;

    private JButton btnDone;

    public DoneButtonPanel(Model model, View view){
        super(new FlowLayout(FlowLayout.CENTER));
        this.model = model;
        this.view = view;

        btnDone = new JButton("Play");
        add(btnDone);
    }

    public JButton getBtnDone() {
        return btnDone;
    }
}
