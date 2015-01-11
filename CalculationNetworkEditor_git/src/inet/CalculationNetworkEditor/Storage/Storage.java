/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Storage;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author andre
 */
public class Storage<V, E> implements IStorage<V,E> {
    private StackingVertex<V> vertexes = null;
    private StackingEdge<V, E> edges = null;
    
    public Storage() {
        vertexes = new StackingVertex<V>();
        edges = new StackingEdge<V, E>(vertexes);
    }
    
    @Override
    public boolean containsVertex(V v) {
        return vertexes.contains(v);
    }
    
    @Override
    public boolean containsEdge(E e) {
        return edges.contains(e);
    }
    
    @Override
    public Type getTypeOfVertex(V v) {
        return vertexes.getType(v);
    }
    
    @Override
    public Type getTypeOfEdge(E e) {
        return edges.getType(e);
    }

    @Override
    public Collection<V> getAllVertOfType(IStorage.Type t) {
        return vertexes.getAllVertOfType(t);
    }

    @Override
    public Collection<Pair<E, Pair<V, V>>> getAllEdgesOfType(IStorage.Type t) {
        return edges.getAllEdgesOfType(t);
    }

    @Override
    public boolean addAbleVertex(V v, IStorage.Type t) {
        return vertexes.addAble(v, t);
    }

    @Override
    public boolean addAbleEdge(E e, V source, V dest) {
        return edges.addAble(e, source, dest);
    }

    @Override
    public boolean addVertex(V v, IStorage.Type t) {
        return vertexes.addVertex(v, t);
    }

    @Override
    public boolean addEdge(E e, V source, V dest) {
        return edges.addEdge(e, source, dest);
    }

    @Override
    public boolean deleteVertex(V v) {
        Collection<E> connectedEdges = edges.getAllEdgesConnectedToVertex(v);
        boolean success = true;
        for(E e : connectedEdges) {
            success = edges.deleteEdge(e);
            if(!success) {
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, "failure during delete of edge. rollback not implemented yet.");
                return false;
            }
        }
        return vertexes.deleteVertex(v);
    }

    @Override
    public boolean deleteEdge(E e) {
        return edges.deleteEdge(e);
    }

    @Override
    public boolean stackVertex(V v1, V v2) {
        return vertexes.stackVertex(v1, v2);
    }

    @Override
    public boolean unstackVertex(V v) {
        return vertexes.unstackVertex(v);
    }
    
    @Override
    public V getStackedVertexTo(V virt) {
        return vertexes.getStackedTo(virt);
    }
    
    @Override
    public Collection<V> getVertexStack(V v) {
        return vertexes.getVertexStack(v);
    }

    @Override
    public boolean addRessourceToVertex(V v, Double res) {
        return vertexes.addRessourceToVertex(v, res);
    }

    @Override
    public double getRessourcesOfVertex(V v) {
        return vertexes.getRessourcesOfVertex(v);
    }
    
    @Override
    public double getRessourcesOfVertexLeft(V phy) {
        return vertexes.getRessourcesOfVertexLeft(phy);
    }
    
    public Collection<V> getAllStackedVertexes(V phy) {
        return vertexes.getAllStackedVertexesOfVertex(phy);
    }

    @Override
    public void deleteRessourcesOfVertex(V v) {
        vertexes.deleteRessourcesOfVertex(v);
    }

    @Override
    public boolean addRessourceToEdge(E e, Double res) {
        return edges.addRessourceToEdge(e, res);
    }

    @Override
    public double getRessourcesOfEdge(E e) {
        return edges.getRessourcesOfEdge(e);
    
    }
    @Override
    public double getRessourcesOfEdgeLeft(E phy) {
        return edges.getRessourcesOfEdgeLeft(phy);
    }

    @Override
    public void deleteRessourcesOfEdge(E e) {
        edges.deleteRessourcesOfEdge(e);
    }

    @Override
    public boolean stackEdge(E virt, List<E> path) {
        return edges.stackEdge(virt, path);
    }

    @Override
    public boolean unstackEdge(E virt) {
        return edges.unstackEdge(virt);
    }
    
    @Override
    public List<E> getStackedEdgePath(E virt) {
        return edges.getStackedEdgePath(virt);
    }
            
    @Override
    public boolean containsStackedEdge(E virt) {
        return edges.containsStackedEdge(virt);
    }
    
    @Override
    public Collection<E> getEdgeStack(E e) {
        return edges.getEdgeStack(e);
    }
    
    @Override
    public V getEdgeSource(E e) {
        return edges.getSource(e);
    }

    @Override
    public V getEdgeDest(E e) {
        return edges.getDest(e);
    }
    
    @Override
    public V step(V v, E e){
        return edges.step(v,e);
    }
}
