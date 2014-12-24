/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author andre
 */
public class Vertexes<V> {
    
    private HashMap<V, IStorage.Type> vertexes = new HashMap();
    private HashMap<V, Double> ressources = new HashMap<V,Double>();
    
    boolean contains(V v) {
        return vertexes.containsKey(v);
    }
    
    boolean containsRessources(V v) {
        return ressources.containsKey(v);
    }
    
    boolean addAble(V v, IStorage.Type t) {
        if(vertexes.containsKey(v)) {
            return false;
        }
        return true;
    }
    
    boolean addVertex(V v, IStorage.Type t) {
        if(addAble(v, t)) {
            vertexes.put(v, t);
            return true;
        }
        return false;
    }
    
    boolean deleteVertex(V v) {
        if(vertexes.containsKey(v)) {
            vertexes.remove(v);
            ressources.remove(v);
            return true;
        } else {
            return false;
        }
    }
    
    IStorage.Type getType(V v) {
        if(vertexes.containsKey(v)) {
            return vertexes.get(v);
        } else {
            return null;
        }
    }
    
    Collection<V> getAllVertOfType(IStorage.Type t) {
        Collection<V> ret = new LinkedList<V>();
        for(V v : vertexes.keySet()) {
            if(vertexes.get(v) == t) {
                ret.add(v);
            }
        }
        return ret;
    }
    
    boolean addRessourceToVertex(V v, Double res) {
        if(v == null) {
            return false;
        }
        
        if(!vertexes.containsKey(v)) {
            return false;
        }
        
        ressources.put(v, res);
        return true;
    }
    
    double getRessourcesOfVertex(V v) {
        if(v == null) {
            return -1.0d;
        }
        if(!vertexes.containsKey(v)) {
            return -1.0d;
        }
        
        if(!ressources.containsKey(v)) {
            return -1.0d;
        }
        
        return ressources.get(v);
    }
    
    void deleteRessourcesOfVertex(V v) {
        ressources.remove(v);
    }
}
