/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.view;

import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author andre
 */
public interface IPanelResetable<V,E> {
    public Graph<V,E> reinitializePhysical();
    public Graph<V,E> reinitializeVirtual();
    public void reinitializeBoth();
    
    public void setBothActionPanel();
    public void clearBothActionPanel();
    public void repaintBoth();
    
    public void setStackingEdgePanel(E e, Collection<E> allPhysical);
    public void setStackingEdgeActualStackToEdge(E e);
    public void addStackingEdgePhyToPath();
    public void removeStackingEdgePath();
    public E getStackingEdgeVirt();
    public List<E> getStackingEdgePhy();
    
    public void setStackingVertexPanel(V v, Collection<V> allPhysical);
    public void setStackingVertexActualStackToVertex(V v);
    public V getStackingVertPhy();
    public V getStackingVertVirt();
    
    public void setEditingResourcePanel(V v, E e, String editRes, double actRes);
    public V getEditingResourceVertex();
    public E getEditingResourceEdge();
    public double getEditingResources();
    
    public void clearPanel();
}
