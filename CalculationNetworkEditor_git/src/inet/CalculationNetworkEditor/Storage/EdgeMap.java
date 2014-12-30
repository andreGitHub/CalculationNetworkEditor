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
 *
 * @author andre
 */
public class EdgeMap<V, E> {
    private HashMap<E, Pair<V,V>> edges = null;
    private HashMap<E, Double> resources = null;
    
    protected StackingVertex<V> vertices = null;
    
    public EdgeMap(StackingVertex<V> s) {
        edges = new HashMap<E, Pair<V,V>>();
        resources = new HashMap<E, Double>();
        
        vertices = s;
    }
    
    IStorage.Type getType(E e) {
        if(!edges.containsKey(e)) {
            return null;
        }
        
        Pair<V,V> p = edges.get(e);
        
        return vertices.getType(p.getKey());
    }
    
    boolean contains(E e) {
        return edges.containsKey(e);
    }
    
    boolean containsResources(E e) {
        return resources.containsKey(e);
    }
    
    boolean addAble(E e, V source, V dest) {
        if(     e == null || source == null || dest == null ||
                vertices.getType(source) != vertices.getType(dest) ||
                edges.containsKey(e)) {
            
            return false;
        }
        return true;
    }
    
    boolean addEdge(E e, V source, V dest) {
        if(addAble(e, source, dest)){
            edges.put(e, new Pair<V,V>(source, dest));
            return true;
        }
        return false;
    }
    
    boolean deleteEdge(E e) {
        Pair<V,V> endpoints = edges.get(e);
        edges.remove(e);
        return true;
    }
    
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
    
    V getSource(E e) {
        return edges.get(e).getKey();
    }
    
    V getDest(E e) {
        return edges.get(e).getValue();
    }
    
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
    
    double getRessourcesOfEdge(E e) {
        if(!edges.containsKey(e)) {
            return 0.0d;
        }
        
        if(!resources.containsKey(e)) {
            return 0.0d;
        }
        
        return resources.get(e);
    }
    
    void deleteRessourcesOfEdge(E e) {
        resources.remove(e);
    }
}
