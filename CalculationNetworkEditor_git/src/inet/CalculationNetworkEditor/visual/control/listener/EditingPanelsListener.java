/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.control.listener;

import inet.CalculationNetworkEditor.visual.contol.controller.BackendController;
import inet.CalculationNetworkEditor.visual.view.PanelResetable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author andre
 */
public class EditingPanelsListener<V,E> implements ActionListener{
    private PanelResetable<V,E> panelResetable = null;
    private BackendController<V,E> backendController = null;
    
    public EditingPanelsListener(PanelResetable<V,E> pPanelResetable, BackendController<V,E> pBC) {
        panelResetable = pPanelResetable;
        backendController = pBC;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {
            JButton source = (JButton)e.getSource();
            if(source.getText().equals("map")) {
                V virtV = panelResetable.getStackingVertVirt();
                V phyV = panelResetable.getStackingVertPhy();
                
                E virtE = panelResetable.getStackingEdgeVirt();
                List<E> phyE = panelResetable.getStackingEdgePhy();
                
                if(virtV != null) {
                    // stack vertices
                    System.out.println("virt " + virtV + " phy " + phyV);
                
                    backendController.stackVertexVirtToPhy(virtV, phyV);
                
                    panelResetable.clearPanel();
                } else if(virtE != null) {
                    // stack edges
                    boolean validPath = backendController.testEdgePathValidity(virtE, phyE);
                    if(validPath) {
                        if(backendController.stackEdgeVirtToPhy(virtE, phyE)) {
                            panelResetable.clearPanel();
                        }
                    }
                }
                
            } else if(source.getText().equals("add")) {
                panelResetable.addStackingEdgePhyToPath();
            } else if(source.getText().equals("remove")) {
                panelResetable.removeStackingEdgePath();
            } else if(source.getText().equals("OK")) {
                double newRes = panelResetable.getEditingResources();
                V vert = panelResetable.getEditingResourceVertex();
                if(vert != null) {
                    System.out.println("vert: " + vert + " res: " + newRes);
                    backendController.addRessourcesToVertex(vert, newRes);
                } else {
                    E edge = panelResetable.getEditingResourceEdge();
                    System.out.println("edge: " + edge + " res: " + newRes);
                    backendController.addRessourcesToEdge(edge, newRes);
                }
                panelResetable.clearPanel();
            }
        }
    }
}
