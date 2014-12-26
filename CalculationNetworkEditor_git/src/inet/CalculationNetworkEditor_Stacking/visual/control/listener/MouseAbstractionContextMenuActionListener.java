/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor_Stacking.visual.control.listener;

import inet.CalculationNetworkEditor.visual.contol.controller.ViewController;
import inet.CalculationNetworkEditor.visual.contol.controller.BackendController;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.visual.view.PanelResetable;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author andre
 */
public class MouseAbstractionContextMenuActionListener<V,E> implements ActionListener {
    private BackendController<V,E> bc = null;
    private ViewController<V,E> vc = null;
    private PanelResetable<V,E> panelResetable = null;
    
    private V actVertex = null;
    private Collection<V> allPhysicalVertex = null;
    
    
    private E actEdge = null;
    private Collection<E> allPhysicalEdge = null;
     
    public MouseAbstractionContextMenuActionListener(BackendController<V,E> pBc, ViewController<V,E> pVc, PanelResetable<V,E> pResetable) {
        bc = pBc;
        vc = pVc;
        
        panelResetable = pResetable;
    }
    
    public void setActVeretxForStacking(V pV, Collection<V> pAllPhysical) {
        actVertex = pV;
        actEdge = null;
        allPhysicalVertex = pAllPhysical;
    }
    
    public void setActEdge(E pE) {
        actVertex = null;
        actEdge = pE;
        allPhysicalEdge = null;
    }
    
    public void setActEdgeForStacking(E pE, Collection<E> pAllPhysical) {
        actVertex = null;
        actEdge = pE;
        allPhysicalEdge = pAllPhysical;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JMenuItem) {
            System.out.println("triggered");
            JMenuItem jmi = (JMenuItem)e.getSource();
            if("delete".equals(jmi.getText())) {
                if(actVertex != null) {
                    if(bc.deleteVertex(actVertex)) {
                        vc.deleteVertex(actVertex);
                        actVertex = null;
                    }
                } else if(actEdge != null) {
                    if(bc.deleteEdge(actEdge)) {
                        vc.deleteEdge(actEdge);
                        actEdge = null;
                    }
                } else {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "no graph element selected for delete");
                }
            } else if("edit".equals(jmi.getText())) {
                if(actVertex != null) {
                    double oldRes = bc.getRessourcesOfVertex(actVertex);
                    
                    panelResetable.setEditingResourcePanel(actVertex, null, "computation units", oldRes);
                } else if(actEdge != null) {
                    double oldRes = bc.getRessourcesOfEdge(actEdge);
                    
                    panelResetable.setEditingResourcePanel(null, actEdge, "bandwidth", oldRes);
                }
            } else if("map".equals(jmi.getText())) {
                //sf = new StackingPanel<V,E>();
                //panelResetable.setEditingPanel(sf);
                if(actVertex != null) {
                    panelResetable.setStackingVertexPanel(actVertex, allPhysicalVertex);
                } else if(actEdge != null) {
                    panelResetable.setStackingEdgePanel(actEdge, allPhysicalEdge);
                }
            } else if("unmap".equals(jmi.getText())) {
                if(actVertex != null) {
                    bc.unstackVertex(actVertex);
                } else if(actEdge != null) {
                    bc.unstackEdge(actEdge);
                }
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown button");
            }
        }
    }
    
    public void setStackToVertex(V vert) {
        if(vert == null) {
            return;
        }
        
        if(bc.getTypeOfVertex(vert) != IStorage.Type.PHYSICAL) {
            return;
        }
        
        if (panelResetable != null) {
            //sf.setStackToVertex(vert);
            panelResetable.setStackingVertexActualStackToVertex(vert);
        }
    }
    
    public void setStackToEdge(E edge) {
        if(edge == null) {
            return;
        }
        
        if(bc.getTypeOfEdge(edge) != IStorage.Type.PHYSICAL) {
            return;
        }
        
        if (panelResetable != null) {
            //sf.setStackToVertex(vert);
            panelResetable.setStackingEdgeActualStackToEdge(edge);
        }
    }
    
    private double getRessourcesByInputBox(Component parent, String message, String title, double oldRes) {
        double res = oldRes;
        Object objRes = JOptionPane.showInputDialog(parent,             //parent
                                                    message,          //Object message,
                                                    title,       //String title,
                                                    JOptionPane.QUESTION_MESSAGE, //int messageType,
                                                    null,               //Icon icon,
                                                    null,               //Object[] selectionValues,
                                                    oldRes);             //Object initialSelectionValue);
        if(objRes != null) {
            String strRes = (String)objRes;
        
            try {
                res = Double.parseDouble(strRes);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "can't parse number: " + strRes, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return res;
    }
}
