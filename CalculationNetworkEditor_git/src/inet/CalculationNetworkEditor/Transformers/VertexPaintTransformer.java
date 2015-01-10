    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Transformers;

import edu.uci.ics.jung.visualization.picking.PickedState;
import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.Storage.Storage;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerBoth;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerPhysical;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerVirtual;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author andre
 */
public class VertexPaintTransformer<V,E> implements Transformer<V, Paint>{

    protected ILogic<V,E> logic = null;
    private Paint colorPhysical = new Color(129, 0, 0); // red wine
    private Paint colorVirtual = new Color(0, 129, 9); // lime green
    
    private Paint colorPickedPhysical = new Color(255, 0, 0);
    private Paint colorPickedVirtual = new Color(0, 255, 0);
    private Paint colorPickedStack = new Color(0, 255, 255);
    
    private VisualizationViewerPhysical<V,E> vvPhysical = null;
    private VisualizationViewerVirtual<V,E> vvVirtual = null;
    private VisualizationViewerBoth<V,E> vvBoth = null;
    
    protected JTabbedPane jtp = null;
    
    public VertexPaintTransformer(
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
    
    /**
     * This method return the color of the vertex, which is schown on the screen.
     * It distinguish between selected views.
     * @param v vertex of interest
     * @return color of v
     */
    @Override
    public Paint transform(V v) {
        if(!logic.containsVertex(v)) {
            System.out.println("logic does not contain: " + v);
            return null;
        }
        
        switch (jtp.getSelectedIndex()) {
            case 0: { // physical
                PickedState<V> psV = vvPhysical.getPickedVertexState();
                Object[] selectedV = psV.getSelectedObjects();
                for(int i = 0; i<selectedV.length; i++) {
                    if(selectedV[i].equals(v)) {
                        return colorPickedPhysical;
                    }
                }
                return colorPhysical;
            }
            case 1: { // virtual
                PickedState psV = vvVirtual.getPickedVertexState();
                Object[] selectedV = psV.getSelectedObjects();
                for(int i = 0; i<selectedV.length; i++) {
                    if(selectedV[i].equals(v)) {
                        return colorPickedVirtual;
                    }
                }
                return colorVirtual;
            }
            case 2: { // both
                if(logic.getTypeOfVertex(v) == IStorage.Type.PHYSICAL) {
                    PickedState psV = vvBoth.getPickedVertexState();
                    Object[] selectedV = psV.getSelectedObjects();
                    for(int i = 0; i<selectedV.length; i++) {
                        if(selectedV[i].equals(v)) {
                            //return colorPickedPhysical;
                            return colorPickedStack;
                        }
                    }
                    Set<V> pickedVerts = vvBoth.getPickedVertexState().getPicked();
                    for(V picked : pickedVerts) {
                        if(logic.getStackedVertexTo(picked) == v) {
                            return colorPickedStack;
                        }
                    }
                    return colorPhysical;
                    
                    
                    
                    
                } else if(logic.getTypeOfVertex(v) == IStorage.Type.VIRTUAL) {
                    PickedState psV = vvBoth.getPickedVertexState();
                    Object[] selectedV = psV.getSelectedObjects();
                    for(int i = 0; i<selectedV.length; i++) {
                        if(selectedV[i].equals(v)) {
                            //return colorPickedVirtual;
                            return colorPickedStack;
                        }
                    }
                    
                    Collection<V> stack = logic.getVertexStack(v);
                    boolean picked = false;
                    for(V vert : stack) {
                        if(vvBoth.getPickedVertexState().getPicked().contains(vert)) {
                            picked = true;
                        }
                    }
                    if(picked) {
                        return colorPickedStack;
                    } else {
                        return colorVirtual;
                    }
                    
                    /*
                    V phy = logic.getStackedTo(v);
                    System.out.println("stackedTo: " + phy);
                    if(vvBoth.getPickedVertexState().getPicked().contains(phy)) {
                        //System.out.println("pickedStack");
                        return colorPickedStack;
                    } else {
                        return colorVirtual;
                    }
                    */
                } else {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown vertex type");
                }
            } break;
            default: {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown view");
            }
        }
        return null;
    }
    
    private Paint getUnselectedColor(V v) {
        if(logic.getTypeOfVertex(v) == IStorage.Type.PHYSICAL) {
            return colorPhysical;
        } else if(logic.getTypeOfVertex(v) == IStorage.Type.VIRTUAL) {
            return colorVirtual;
        } else {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, "unknown vertex type");
        }
        return null;
    }
}
