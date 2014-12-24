/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author andre
 */
public interface IStorage<V,E> {
    public enum Type {PHYSICAL, VIRTUAL};
    
    public IStorage.Type getTypeOfVertex(V v);
    public IStorage.Type getTypeOfEdge(E e);
    
    public boolean containsVertex(V v);
    
    public Collection<V> getAllVertOfType(IStorage.Type t);
    public Collection<Pair<E,Pair<V,V>>> getAllEdgesOfType(IStorage.Type t);
    
    public boolean addAbleVertex(V v, IStorage.Type t);
    public boolean addAbleEdge(E e, V source, V dest);
    
    public boolean addVertex(V v, IStorage.Type t);
    public boolean addEdge(E e, V source, V dest);
    
    public V getEdgeSource(E e);
    public V getEdgeDest(E e);
    
    public boolean deleteVertex(V v);
    public boolean deleteEdge(E e);
    
    public boolean stackVertex(V v1, V v2);
    public boolean unstackVertex(V v);
    public V getStackedVertexTo(V virt);
    public Collection<V> getAllStackedVertexes(V phy);
    public Collection<V> getVertexStack(V v);
    
    public boolean stackEdge(E virt, List<E> path);
    public boolean unstackEdge(E virt);
    public List<E> getStackedEdgePath(E virt);
    public boolean containsStackedEdge(E virt);
    public Collection<E> getEdgeStack(E e);
    
    
    public boolean addRessourceToVertex(V v, Double res);
    public double getRessourcesOfVertex(V v);
    public double getRessourcesOfVertexLeft(V phy);
    public void deleteRessourcesOfVertex(V v);
    
    public boolean addRessourceToEdge(E e, Double res);
    public double getRessourcesOfEdge(E e);
    public double getRessourcesOfEdgeLeft(E phy);
    public void deleteRessourcesOfEdge(E e);
}
