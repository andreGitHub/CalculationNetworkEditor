/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Transformers;

import edu.uci.ics.jung.visualization.picking.PickedState;
import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerBoth;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerPhysical;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerVirtual;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author andre
 */
public class EdgePaintTransformer<V,E> implements Transformer<E,Paint>{

    private ILogic<V,E> logic = null;
    private Paint colorPhysical = new Color(129, 0, 0); // red wine
    private Paint colorVirtual = new Color(0, 129, 9); // lime green
    private Paint colorVisual = new Color(0,0,255);
    
    private Paint colorPickedPhysical = new Color(255, 0, 0);
    private Paint colorPickedVirtual = new Color(0, 255, 0);
    private Paint colorPickedStack = new Color(0, 255, 255);
    
    private VisualizationViewerPhysical<V,E> vvPhysical = null;
    private VisualizationViewerVirtual<V,E> vvVirtual = null;
    private VisualizationViewerBoth<V,E> vvBoth = null;
    
    private JTabbedPane jtp = null;
    
    public EdgePaintTransformer(
                ILogic<V,E> pLogic,
                JTabbedPane pJtp) {
        
        logic = pLogic;
        jtp = pJtp;
    }

    public void setVisualizationViewers(
                    VisualizationViewerPhysical<V,E> pVvPhysical,
                    VisualizationViewerVirtual<V,E> pVvVirtual,
                    VisualizationViewerBoth<V,E> pVvBoth) {
        vvPhysical = pVvPhysical;
        vvVirtual = pVvVirtual;
        vvBoth = pVvBoth;
    }
    
    @Override
    public Paint transform(E e) {
        switch(jtp.getSelectedIndex()) {
            case 0: { // physical
                PickedState<E> psE = vvPhysical.getPickedEdgeState();
                Object[] selectedE = psE.getSelectedObjects();
                for(int i = 0; i<selectedE.length; i++) {
                    if(selectedE[i].equals(e)) {
                        return colorPickedPhysical;
                    }
                }
                return colorPhysical;
            }
            case 1: { // virtual
                PickedState<E> psE = vvVirtual.getPickedEdgeState();
                Object[] selectedE = psE.getSelectedObjects();
                for(int i = 0; i<selectedE.length; i++) {
                    if(selectedE[i].equals(e)) {
                        return colorPickedVirtual;
                    }
                }
                return colorVirtual;
                
            }
            case 2: { // both
                if(logic.containsEdge(e)) {
                    if(logic.getTypeOfEdge(e) == IStorage.Type.PHYSICAL) {
                        Collection<E> stack = logic.getEdgeStack(e);
                        PickedState psE = vvBoth.getPickedEdgeState();
                        Object[] selectedE = psE.getSelectedObjects();
                        for(int i = 0; i<selectedE.length; i++) {
                            for(E stackElem : stack) {
                                if(selectedE[i].equals(stackElem)) {
                                    return colorPickedStack;
                                }
                            }
                            if(selectedE[i].equals(e)) {
                                return colorPickedStack;
                            }
                        }
                        return colorPhysical;
                    } else if(logic.getTypeOfEdge(e) == IStorage.Type.VIRTUAL) {
                        List<E> stackedEdgesTo = logic.getStackedEdgePath(e);
                        PickedState psE = vvBoth.getPickedEdgeState();
                        Object[] selectedE = psE.getSelectedObjects();
                        for(int i = 0; i<selectedE.length; i++) {
                            if(stackedEdgesTo != null) {
                                for(E stackedEdgeTo : stackedEdgesTo) {
                                    if(selectedE[i].equals(stackedEdgeTo)) {
                                        return colorPickedStack;
                                    }
                                }
                            }
                            if(selectedE[i].equals(e)) {
                                return colorPickedStack;
                            }
                        }
                        return colorVirtual;
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown edge type");
                    }
                } else {
                    // edge does not exist in logic/storage subsystem
                    // it is only used for visualization purpose
                    return colorVisual;
                }
            } break;
            default: {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown view");
            }
        }
        if(logic.getTypeOfEdge(e) == IStorage.Type.PHYSICAL) {
            return colorPhysical;
        } else if(logic.getTypeOfEdge(e) == IStorage.Type.VIRTUAL) {
            return colorVirtual;
        }
        
        return null;
    }
    
}
