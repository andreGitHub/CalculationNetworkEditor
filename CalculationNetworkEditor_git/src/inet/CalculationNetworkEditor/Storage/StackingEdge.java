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
import java.util.List;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author andre
 */
public class StackingEdge<V,E> extends EdgeMap<V, E>{
    // 1. Virt, 2. Phys
    private HashMap<E,List<E>> stacked = null;
    
    StackingEdge(StackingVertex<V> s) {
        super(s);
        
        stacked = new HashMap<E,List<E>>();
    }
    
    boolean stackEdge(E virt, List<E> phys) {
        if(virt == null || phys == null || phys.size() == 0) {
            return false;
        }
        
        if(getType(virt) != IStorage.Type.VIRTUAL) {
            return false;
        }
        
        for(E e : phys) {
            if(getType(e) != IStorage.Type.PHYSICAL) {
                return false;
            }
        }
        
        List<E> oldPath = null;
        if(stacked.containsKey(virt)) {
            oldPath = stacked.remove(virt);
        }
        
        
        boolean valid = true;
        for(E phy : phys) {
            if(getRessourcesOfEdgeLeft(phy) < getRessourcesOfEdge(virt)) {
                valid = false;
                break;
            }
        }
        
        if(!valid && oldPath != null) {
            stacked.put(virt, oldPath);
            return false;
        }
        
        if(!valid) {
            return false;
        }
        
        // valid
        List<E> cPhys = new LinkedList();
        for(E phy : phys) {
            cPhys.add(phy);
        }
        
        V tmp = vertices.getStackedTo(getSource(virt));
        List<E> orderedPhys = new LinkedList<E>();
        int orderedPhysSize = -1;
        while (orderedPhys.size()>orderedPhysSize) {
            orderedPhysSize = orderedPhys.size();
            for(E phy : cPhys) {
                if(step(tmp, phy) != null) {
                    orderedPhys.add(phy);
                    cPhys.remove(phy);
                    tmp = step(tmp, phy);
                    break;
                }
            }
        }
        
        if(tmp == vertices.getStackedTo(getDest(virt))) {
            // virt is compleatly projected on phys
            stacked.put(virt, phys);
            return true;
        } else {
            return false;
        }
    }
    
    double getRessourcesOfEdgeLeft(E phy) {
        if(phy == null) {
            return -1.0d;
        }
        if(getType(phy) != IStorage.Type.PHYSICAL) {
            return -1.0d;
        }
        if(!contains(phy) || !containsResources(phy)) {
            return -1.0d;
        }
        
        Collection<E> stack = getAllStackedEdgesOfEdge(phy);
        double res = getRessourcesOfEdge(phy);
        for(E virt : stack) {
            res = res - getRessourcesOfEdge(virt);
        }
        return res;
    }
    
    Collection<E> getAllStackedEdgesOfEdge(E phy) {
        if(phy == null) {
            return null;
        }
        if(getType(phy) != IStorage.Type.PHYSICAL) {
            return null;
        }
        if(!contains(phy)) {
            return null;
        }
        
        Collection<E> ret = new LinkedList<E>();
        for(E virt : stacked.keySet()) {
            if(stacked.get(virt).contains(phy)){
                ret.add(virt);
            }
        }
        return ret;
    }
    
    boolean containsStackedEdge(E virt) {
        return stacked.containsKey(virt);
    }
    
    Collection<E> getEdgeStack(E e) {
        if(e == null) {
            return null;
        }
        
        Collection<E> ret = new ArrayList<E>();
        ret.add(e);
        if(getType(e) == IStorage.Type.VIRTUAL) {
            for(E act : stacked.get(e)) {
                ret.add(act);
            }
        } else if(getType(e) == IStorage.Type.PHYSICAL) {
            Set<E> keys = stacked.keySet();
            for(E key : keys) {
                if(stacked.get(key).contains(e)) {
                    ret.add(key);
                }
            }
        }
        return ret;
    }
    
    List<E> getStackedEdgePath(E virt) {
        if(virt == null) {
            return null;
        }
        
        if(getType(virt) == IStorage.Type.PHYSICAL) {
            return null;
        }
        
        return stacked.get(virt);
    }
    
    boolean stackedEdge(E e) {
        if(e == null) {
            return false;
        }
        
        if(     contains(e) &&
                getType(e) == IStorage.Type.VIRTUAL &&
                stacked.containsKey(e)) {
            
            return true;
        } else {
            return false;
        }
    }
    
    boolean unstackEdge(E virt) {
        if(virt == null) {
            return false;
        }
        
        List<E> ret = stacked.remove(virt);
        if(ret != null) {
            return true;
        } else {
            return false;
        }
    }
}
