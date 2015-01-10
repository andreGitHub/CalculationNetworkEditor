/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Transformers;

import java.awt.BasicStroke;
import java.awt.Stroke;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author andre
 */
public class VertexStrokeTransformer<V> implements Transformer<V, Stroke>{
    private float dash[] = { 10.0f };
    
    @Override
    public Stroke transform(V i) {
        //return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        return new BasicStroke(3.0f);
    }
}
