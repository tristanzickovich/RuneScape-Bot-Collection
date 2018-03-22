package scripts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class UI extends JFrame implements ItemListener {
    public ArrayList<String> chosenMethods = new ArrayList<String>();
    public UI(){
        //set up UI layout
        setLayout(new GridBagLayout());
        setTitle("Test UI");
        setSize(300,200);
        setLocationRelativeTo(null);
        GridBagConstraints gc = new GridBagConstraints();
        //create UI checkboxes
        final String[] fishingMethods = {"Harpoon", "Cage", "Net"};
        JCheckBox[] methodCheckBoxes = new JCheckBox[fishingMethods.length];
        gc.anchor = GridBagConstraints.WEST;
        for(int i = 0; i < methodCheckBoxes.length; ++i){
            methodCheckBoxes[i] = new JCheckBox(fishingMethods[i]);
            methodCheckBoxes[i].addItemListener(this);
            gc.gridx = 0;
            gc.gridy = i;
            add(methodCheckBoxes[i], gc);
        }
    }

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            new UI().setVisible(true);
        });
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox check = (JCheckBox)e.getSource();
        int sel = e.getStateChange();
        if(sel == ItemEvent.SELECTED){
           chosenMethods.add(check.getText());
        }
        else{
            chosenMethods.remove(check.getText());
        }
    }
}
