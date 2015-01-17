/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Transformers;

import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import java.awt.Color;
import java.awt.Paint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;

/**
 *
 * @author andre
 */
public class VertexPaintTransformerStacking<V, E> extends VertexPaintTransformer<V, E>{

    private Paint transparent = null;
    private Paint greenbrown = null;
    
    public VertexPaintTransformerStacking(ILogic<V, E> pLogic, JTabbedPane pJtp) {
        super(pLogic, pJtp);
        transparent = new Color(0,0,0,0);
        greenbrown = new Color(102,85,17);
    }
    
    @Override
    public Paint transform(V v) {
        Paint sup = super.transform(v);
        
        switch(jtp.getSelectedIndex()) {
            case 0: // Physical
            case 1: // Virtual
            { 
                return sup;
            }
            case 2: { // both
                if(logic.containsVertex(v)) {
                    if(logic.getTypeOfVertex(v) == IStorage.Type.PHYSICAL) {
                        return sup;
                    } else if(logic.getTypeOfVertex(v) == IStorage.Type.VIRTUAL) {
                        
                        if(logic.getStackedVertexTo(v) == null) {
                            // vertex is not stacked to another vertex
                            return transparent;
                        } else {
                            return sup;
                        }
                    
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown vertex type");
                    }
                } else {
                    // logic/storage subsystem does not contain vertex
                    // vertex is only used for visualization purpose
                    return greenbrown;
                }
            } break;
            default: {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown view");
            }
        }
        return sup;
    }
}
