/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author andre
 */
public class StackingVertex<V> extends Vertexes<V>{
    // the key is a vertex of type Virtual, the value is a vertex of the type Physical
    // 1: Virtual 2: Physical
    private HashMap<V, V> stacked = new HashMap<V,V>();
    //private VertexTypes<V> vt = null;
    
    public StackingVertex() {
        
    }
    
    boolean unstackVertex(V v) {
        if(v == null) {
            return false;
        }
        
        if(getType(v) == IStorage.Type.VIRTUAL) {
            if(stacked.remove(v) != null) {
                return true;
            }
        }
        return false;
    }
    
    boolean stackVertex(V v1, V v2) {
        if(v1 == null || v2 == null) {
            return false;
        }
        
        if(!contains(v1) || !contains(v2)) {
            // vertexes do not exist
            return false;
        }
        
        IStorage.Type t1 = getType(v1);
        IStorage.Type t2 = getType(v2);
        
        if(         t1 == IStorage.Type.PHYSICAL &&
                    t2 == IStorage.Type.VIRTUAL &&
                    !stacked.containsKey(v2)) {
            // test Ressources
            if(getRessourcesOfVertexLeft(v1) >= getRessourcesOfVertex(v2)) {
                // stack
                stacked.put(v2, v1);
                return true;
            } else {
                return false;
            }
        } else if(  t1 == IStorage.Type.VIRTUAL &&
                    t2 == IStorage.Type.PHYSICAL &&
                    !stacked.containsKey(v1)) {
                        // test Ressources
            if(getRessourcesOfVertexLeft(v2) >= getRessourcesOfVertex(v1)) {
                // stack
                stacked.put(v1, v2);
                return true;
            } else {
                return false;
            }
        } else {
            // not stackable
            return false;
        }
    }
    
    double getRessourcesOfVertexLeft(V phy) {
        if(phy == null) {
            return -1.0d;
        }
        if(getType(phy) != IStorage.Type.PHYSICAL) {
            return -1.0d;
        }
        if(!contains(phy) || !containsRessources(phy)) {
            return -1.0d;
        }
        
        Collection<V> stack = getAllStackedVertexesOfVertex(phy);
        double res = getRessourcesOfVertex(phy);
        for(V v : stack) {
            res = res - getRessourcesOfVertex(v);
        }
        return res;
    }
    
    Collection<V> getAllStackedVertexesOfVertex(V phy) {
        if(phy == null) {
            return null;
        }
        if(getType(phy) != IStorage.Type.PHYSICAL) {
            return null;
        }
        
        Collection<V> ret = new LinkedList<V>();
        for(V v : stacked.keySet()) {
            if(stacked.get(v).equals(phy)) {
                ret.add(v);
            }
        }
        return ret;
    }
    
    public V getStackedTo(V virt) {
        return stacked.get(virt);
    }
    
    public Collection<V> getVertexStack(V v) {
        if(v == null) {
            return null;
        }
        
        Collection<V> ret = new ArrayList<V>();
        V phy = null;
        if(getType(v) == IStorage.Type.VIRTUAL) {
            phy = getStackedTo(v);
            if(phy == null) {
                ret.add(v);
                return ret;
            }
        } else if(getType(v) == IStorage.Type.PHYSICAL) {
            phy = v;
        }
        ret.add(phy);
        Set<V> keys = stacked.keySet();
        for(V key : keys) {
            if(getStackedTo(key) == phy) {
                ret.add(key);
            }
        }
        return ret;
    }
    
    boolean vertexStacked(V v) {
        if(v == null) {
            return false;
        }
        
        if(     contains(v) &&
                getType(v) == IStorage.Type.VIRTUAL &&
                stacked.containsKey(v)) {
            
            return true;
        } else {
            return false;
        }
    }
}
