/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.control.listener;

import edu.uci.ics.jung.graph.Graph;
import inet.CalculationNetworkEditor.visual.view.EditorPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author andre
 */
public class TabSwitchedListener<V,E> implements ChangeListener {
    private EditorPane editorPane = null;
    private JTabbedPane mainJtp = null;
//    private Controller vvuml = null;
    
    public TabSwitchedListener(EditorPane pEditorPane, JTabbedPane jtp) {//, Controller pVvuml) {
        editorPane = pEditorPane;
        mainJtp = jtp;
//        vvuml = pVvuml;
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        //System.out.println(">>> tab changed");
        //ToolBox.ToolBox.printStackTrace();
        if(e.getSource() != null && e.getSource() instanceof JTabbedPane) {
            JTabbedPane jtp = (JTabbedPane)e.getSource();
            editorPane.clearPanel();
            switch (mainJtp.getSelectedIndex()) {
                case 0: {   // physical
                    Graph<V,E> graphPhys = editorPane.reinitializePhysical();
//                    vvuml.resetPhysGraph(graphPhys);
//                    jtp.setSelectedIndex(0);
                } break;
                
                case 1: { // virtual
                    Graph<V,E> graphVirt = editorPane.reinitializeVirtual();
//                    vvuml.resetVirtGraph(graphVirt);
//                    jtp.setSelectedIndex(1);
                } break;
                    
                case 2: { // both
                    editorPane.reinitializeBoth();
//                    jtp.setSelectedIndex(2);
                } break;
                    
                default: {
                    Logger.getLogger(TabSwitchedListener.class.getName()).log(Level.SEVERE, "unknown view type: " + jtp.getSelectedIndex());
                }
            }
            
        }
    }
}
