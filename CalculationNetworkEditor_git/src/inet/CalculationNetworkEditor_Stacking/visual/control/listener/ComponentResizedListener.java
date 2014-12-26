/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor_Stacking.visual.control.listener;

import inet.CalculationNetworkEditor.visual.view.EditorPane;
import inet.CalculationNetworkEditor_Stacking.visual.control.listener.TabSwitchedListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;

/**
 *
 * @author andre
 */
public class ComponentResizedListener implements ComponentListener {
    private JTabbedPane jtp = null;
    private EditorPane edit = null;
    
    public ComponentResizedListener(JTabbedPane pJtp, EditorPane e) {
        jtp = pJtp;
        edit = e;
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        switch(jtp.getSelectedIndex()) {
            case 0: { // physical
                edit.reinitializePhysical();
            } break;
            case 1: { // virtual
                edit.reinitializeVirtual();
            } break;
            case 2: { // both
                edit.reinitializeBoth();
            } break;
            default: {
                Logger.getLogger(TabSwitchedListener.class.getName()).log(Level.SEVERE, "unknown view type");
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        
    }
    
}
