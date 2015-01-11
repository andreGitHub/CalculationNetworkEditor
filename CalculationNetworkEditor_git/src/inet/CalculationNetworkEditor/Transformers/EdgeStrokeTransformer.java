/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Transformers;

import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author andre
 */
public class EdgeStrokeTransformer<V,E> implements Transformer<E, Stroke> {
    private float dash[] = { 10.0f };
    private ILogic logic = null;
    private JTabbedPane jtp = null;
    
    public EdgeStrokeTransformer(ILogic<V,E> pLogic, JTabbedPane pTabbedPane) {
        logic = pLogic;
        jtp = pTabbedPane;
    }
    
    @Override
    public Stroke transform(E i) {
        switch(jtp.getSelectedIndex()) {
            case 0: //Physical
            case 1: { //Virtual
                //return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                return new BasicStroke(3.0f);
            }
            case 2: { //Both
                if(logic.containsEdge(i)) {
                    if(logic.getTypeOfEdge(i) == IStorage.Type.PHYSICAL) {
                        return new BasicStroke(3.0f);
                    } else if(logic.getTypeOfEdge(i) == IStorage.Type.VIRTUAL) {
                        if(logic.getStackedEdgePath(i) == null) {
                            // edge is not stacked
                            BasicStroke dashed = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                            return dashed;
                        } else {
                            return new BasicStroke(3.0f);
                        }
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown edge type");
                    }
                } else {
                    // it is an edge which only exists in frontend for visualization purpose
                    return new BasicStroke(3.0f);
                }
            } break;
            default: {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown view");
            }
        }
        
        return new BasicStroke(3.0f);
    }
    
}
