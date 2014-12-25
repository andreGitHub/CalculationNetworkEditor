/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Logic;

import edu.uci.ics.jung.graph.Graph;
import inet.CalculationNetworkEditor.Storage.IStorage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author andre
 */
public interface ILogic<V,E> {
    
    public void init(
                    List<Graph<V, E>> phys, List<Graph<V, E>> virts,
                    HashMap<V,V> stackVertex, HashMap<E,List<E>> stackEdge, 
                    HashMap<V, Double> vertexRessources, HashMap<E, Double> edgeRessources);
    
    public IStorage.Type getTypeOfVertex(V v);
    public IStorage.Type getTypeOfEdge(E e);
    
    public boolean contains(V v);
    
    public Graph<V,E> getDisplay(IStorage.Type t);
    
    public boolean addVertex(V v, IStorage.Type t);
    public boolean addEdge(E e, V v1, V v2);
    
    public boolean deleteVertex(V v);
    public boolean deleteEdge(E e);
    
    public Collection<V> getAllVertOfType(IStorage.Type t);
    public Collection<E> getAllEdgesOfType(IStorage.Type t);
    
    public boolean stackVertex(V v1, V v2);
    public boolean unstackVertex(V v);
    public V getStackedVertexTo(V virt);
    public Collection<V> getVertexStack(V v);
    
    public boolean stackEdge(E virt, List<E> path);
//    public boolean incrementalStackEdge(E virt, E phy);
    public boolean testEdgePathValidity(E virt, List<E> phy);
    public boolean unstackEdge(E virt);
    public List<E> getStackedEdgePath(E virt);
    public Collection<E> getEdgeStack(E e);
    
    public double getRessourcesOfVertex(V v);
    public double getRessourcesOfEdge(E e);
    
    public boolean addRessourceToVertex(V v, Double res);
    public boolean addRessourceToEdge(E e, Double res);
}
