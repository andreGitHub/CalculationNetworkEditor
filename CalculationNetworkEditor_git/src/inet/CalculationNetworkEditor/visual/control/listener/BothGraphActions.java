/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.control.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author andre
 */
public class BothGraphActions implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        Object objSource = e.getSource();
        if(objSource instanceof JButton) {
            JButton jbtnSource = (JButton)objSource;
            if("relocate".equals(jbtnSource.getText())) {
                System.out.println("jbtn Relocate");
            } else if("visualize".equals(jbtnSource.getText())) {
                System.out.println("jbtn Visualize");
            }
        }
    }
    
}
