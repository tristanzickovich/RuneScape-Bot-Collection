package scripts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class UI extends JFrame implements ItemListener {
    private ArrayList<String> chosenMethods = new ArrayList<String>();
    public UI(){
        //set up UI layout
        setLayout(new GridBagLayout());
        setTitle("Test UI");
        setSize(300,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        JButton startButton= new JButton("Start");
        startButton.addActionListener((ActionEvent) -> {
            System.out.println("start clicked");
        });
        gc.gridx = 0;
        gc.gridy = methodCheckBoxes.length;
        add(startButton, gc);
        /*
        JButton quit = new JButton("Quit");
        quit.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        */
    }

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            UI ex = new UI();
            ex.setVisible(true);
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
        System.out.println(chosenMethods);
    }
}
