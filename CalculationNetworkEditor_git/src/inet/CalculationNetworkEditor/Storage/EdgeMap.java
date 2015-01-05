/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javafx.util.Pair;

/**
 * This class is used to store the edges and resources of edges.
 * @author andre
 */
public class EdgeMap<V, E> {
    private HashMap<E, Pair<V,V>> edges = null;
    private HashMap<E, Double> resources = null;
    
    protected StackingVertex<V> vertices = null;
    
    /**
     * Initiates the class.
     * @param s An object, which contains all veretx stuff.
     */
    public EdgeMap(StackingVertex<V> s) {
        edges = new HashMap<E, Pair<V,V>>();
        resources = new HashMap<E, Double>();
        
        vertices = s;
    }
    
    /**
     * There are two types. Virtual and Physical. This method return the type of
     * e.
     * @param e edge, which type should be retrieved.
     * @return type of e.
     */
    IStorage.Type getType(E e) {
        if(!edges.containsKey(e)) {
            return null;
        }
        
        Pair<V,V> p = edges.get(e);
        
        return vertices.getType(p.getKey());
    }
    
    /**
     * Determine, if edge e is already stored.
     * @param e edge e.
     * @return true, if the edge is already stored.
     */
    boolean contains(E e) {
        return edges.containsKey(e);
    }
    
    /**
     * Determine, if edge e's resources are already stored.
     * @param e edge e.
     * @return true, if the resources of the edge are already stored.
     */
    boolean containsResources(E e) {
        return resources.containsKey(e);
    }
    
    /**
     * Test for all things, which prohibit storing of e. These things are for
     * example, that the endpoints have the same type and the edge is not jet stored.
     * @param e edge, which should be stored.
     * @param source first endpoint of edge. 
     * @param dest second endpoint of edge.
     * @return true, if e can be added.
     */
    boolean addAble(E e, V source, V dest) {
        if(     e == null || source == null || dest == null ||
                vertices.getType(source) != vertices.getType(dest) ||
                edges.containsKey(e)) {
            
            return false;
        }
        return true;
    }
    
    /**
     * Add an edge to the storage subsystem.
     * @param e edge, which sould be added.
     * @param source first endpoint of edge
     * @param dest second endpoint of edge.
     * @return true, if the edge can be added.
     */
    boolean addEdge(E e, V source, V dest) {
        if(addAble(e, source, dest)){
            edges.put(e, new Pair<V,V>(source, dest));
            return true;
        }
        return false;
    }
    
    /**
     * Delege edge e from storage subsystem.
     * @param e edge, which should be deleted.
     * @return true, if edge was deleted successfully
     */
    boolean deleteEdge(E e) {
        Pair<V,V> endpoints = edges.get(e);
        edges.remove(e);
        return true;
    }
    
    /**
     * Find all edges, which are connected to v.
     * @param v vertex of interest.
     * @return Collection of all edges, which are connected to v.
     */
    Collection<E> getAllEdgesConnectedToVertex(V v) {
        Map<E,Pair<V,V>> ret = new HashMap<E,Pair<V,V>>();
        
        for(Map.Entry<E,Pair<V,V>> entry : edges.entrySet()) {
            if(entry.getValue().getKey().equals(v)) {
                ret.put(entry.getKey(),entry.getValue());
            } else if (entry.getValue().getValue().equals(v)) {
                ret.put(entry.getKey(),entry.getValue());
            }
        }
        
        return ret.keySet();
    }
    
    /**
     * Get first endpoint of e.
     * @param e edge of interest.
     * @return first endpoint.
     */
    V getSource(E e) {
        return edges.get(e).getKey();
    }
    
    /**
     * Get second endpoint of e.
     * @param e edge of interest.
     * @return second endpoint.
     */
    V getDest(E e) {
        return edges.get(e).getValue();
    }
    
    /**
     * Start at v (first endpoint), go along e, and return second endpoint.
     * @param v first endpoint
     * @param e way to go
     * @return second endpoint
     */
    V step(V v, E e) {
        Pair<V,V> p = edges.get(e);
        if(p.getKey() == v) {
            return p.getValue();
        } else if(p.getValue() == v) {
            return p.getKey();
        } else {
            return null;
        }
    }
    
    /**
     * Return a collection of all edges of the specified type t.
     * @param t type of edges.
     * @return A collection of edgees of type t.
     */
    Collection<Pair<E, Pair<V,V>>> getAllEdgesOfType(IStorage.Type t) {
        Collection<Pair<E, Pair<V,V>>> ret = new LinkedList<Pair<E, Pair<V,V>>>();
        for(E e : edges.keySet()) {
            if(     vertices.getType(getSource(e)) == t &&
                    vertices.getType(getDest(e)) == t ) {
                ret.add(new Pair<E, Pair<V,V>>(e,new Pair<V,V>(edges.get(e).getKey(), edges.get(e).getValue())));
            }
        }
        return ret;
    }
    
    /**
     * Set the resources of edge e.
     * @param e edge of interest.
     * @param res resources of e.
     * @return true, if resources are successfully set.
     */
    boolean addRessourceToEdge(E e, Double res) {
        if(e == null) {
            return false;
        }
        
        if(!edges.containsKey(e)) {
            return false;
        }
        
        resources.put(e, res);
        return true;
    }
    
    /**
     * Return the resources of edge e.
     * @param e edge of interest.
     * @return resource of e.
     */
    double getRessourcesOfEdge(E e) {
        if(!edges.containsKey(e)) {
            return 0.0d;
        }
        
        if(!resources.containsKey(e)) {
            return 0.0d;
        }
        
        return resources.get(e);
    }
    
    /**
     * Remove resources of e from storage subsystem.
     * @param e edge of interest.
     */
    void deleteRessourcesOfEdge(E e) {
        resources.remove(e);
    }
}
