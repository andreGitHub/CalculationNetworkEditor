/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.control.listener;

import edu.uci.ics.jung.algorithms.layout.Layout;
import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerBoth;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import javax.swing.JButton;

/**
 *
 * @author andre
 */
public class BothGraphActions<V,E> implements ActionListener{
    private final double mapDistance = 10.0d;
    private ILogic<V,E> logic = null;
    private VisualizationViewerBoth<V,E> visViewBoth = null;
    
    public BothGraphActions(ILogic<V,E> pLogic, VisualizationViewerBoth<V,E> pVisViewBoth) {
        logic = pLogic;
        visViewBoth = pVisViewBoth;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object objSource = e.getSource();
        if(objSource instanceof JButton) {
            JButton jbtnSource = (JButton)objSource;
            if("relocate".equals(jbtnSource.getText())) {
                System.out.println("jbtn Relocate");
                relocateVertexes();
            } else if("visualize".equals(jbtnSource.getText())) {
                System.out.println("jbtn Visualize");
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void relocateVertexes() {
        Layout<V,E> layout = visViewBoth.getGraphLayout();
        Collection<V> physical = logic.getAllVertOfType(IStorage.Type.PHYSICAL);
        for(V phy : physical) {
            Collection<V> mapping = logic.getVertexStack(phy);
            Point2D pAct = layout.transform(phy);
            for(V mapped : mapping) {
                if(!mapped.equals(phy)) {
                    // all vertices of mapping but the phisical vertex
                    pAct.setLocation(pAct.getX() + mapDistance, pAct.getY() + mapDistance);
                    layout.setLocation(mapped, pAct);
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
}
