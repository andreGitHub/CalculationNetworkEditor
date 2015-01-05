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
 * This class save information about the mapping of vertices. It store, which virtual
 * vertex is mapped to which physical vertex. Furthermore there are methods to edit
 * the mapping.
 * @author andre
 */
public class StackingVertex<V> extends Vertexes<V>{
    // the key is a vertex of type Virtual, the value is a vertex of the type Physical
    // 1: Virtual 2: Physical
    private HashMap<V, V> stacked = new HashMap<V,V>();
    //private VertexTypes<V> vt = null;
    
    /**
     * The constructor initializes the class, but doesn't do something special.
     */
    public StackingVertex() {
        
    }
    
    /**
     * The precondition of this method is, that the virtual vertex v is mapped to
     * a physical vertex. If this is the case, the method remove the mapping.
     * @param v vertex of interest
     * @return true, if the vertex was successfully unmapped.
     */
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
    
    /**
     * Precondition of this method is that v1 and v2 are both vertexes. One of them
     * have to be a virtual vertex, the other one have to be a physical vertex.
     * If this is the case and the reccources of these vertexes allow a mapping,
     * the mapping is stored.
     * @param v1 first vertex of interest.
     * @param v2 second vertex of interest.
     * @return true, if one vertex can be stacked to the other one.
     */
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
            double resV2 = getRessourcesOfVertexLeft(v2);
            double resV1 = getRessourcesOfVertex(v1);
            if(resV2 >= resV1) {
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
    
    /**
     * This method calculate the resources of phy. It subtracts the reccouces of
     * all vertices stacked to phy.
     * @param phy vertex of interest.
     * @return amount of ressources left.
     */
    double getRessourcesOfVertexLeft(V phy) {
        if(phy == null) {
            return 0.0d;
        }
        if(getType(phy) != IStorage.Type.PHYSICAL) {
            return 0.0d;
        }
        if(!contains(phy) || !containsRessources(phy)) {
            return 0.0d;
        }
        
        Collection<V> stack = getAllStackedVertexesOfVertex(phy);
        double res = getRessourcesOfVertex(phy);
        for(V v : stack) {
            res = res - getRessourcesOfVertex(v);
        }
        return res;
    }
    
    /**
     * This method figure out, which vertexes are mapped to the physical vertex 
     * phy. This can be many vertexes.
     * @param phy vertex of interest.
     * @return a collection of vertexes stacked to phy.
     */
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
    
    /**
     * This method return the vertex, the virtual vertex virt is mapped to. This
     * is a single vertex.
     * @param virt the vertex of interest.
     * @return the physical vertex, virt is mapped to
     */
    public V getStackedTo(V virt) {
        return stacked.get(virt);
    }
    
    /**
     * This method return the stack of v.
     * To the stack belong only v, if v is a virtual vertex and not mapped to any
     * other vertex.
     * To the stack belong v and all vertexes mapped to v, if v is a physical vertex.
     * To the stack belong the vertex v is mapped to (vs) and all vertexes, which
     * are mapped to vs, if v is a virtual vertex.
     * @param v vertex of interest
     * @return a collection, which represent the stack of v.
     */
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
    
    /**
     * Check if v allready belong to a stack.
     * @param v vertex of interest.
     * @return true, if v belong to a stack.
     */
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
