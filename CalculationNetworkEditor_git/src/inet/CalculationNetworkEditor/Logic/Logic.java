/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Logic;

import edu.uci.ics.jung.graph.Graph;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.Storage.Storage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author andre
 */
public class Logic<V,E> implements ILogic<V, E>{
    private IStorage<V,E> storage = null;
    private ComposeDecomposeGraph<V,E> cdGraph = null;
    
    
    public Logic() {
        storage = new Storage();
        cdGraph = new ComposeDecomposeGraph<V,E>(storage);
    }
    
    @Override
    public void init(
                    List<Graph<V, E>> phys, List<Graph<V, E>> virts,
                    HashMap<V,V> stackVertex, HashMap<E,List<E>> stackEdge, 
                    HashMap<V, Double> vertexRessources, HashMap<E, Double> edgeRessources) {
        if(phys != null) {
            for(int i = 0; i < phys.size(); i++) {
                if(!cdGraph.decomposeGraph(phys.get(i), IStorage.Type.PHYSICAL)) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unable to add physical Graph. Maybe the  graph contain a Vertex/Edge, which was used in a previously added Graph.");
                }
            }
        }
        if(virts != null) {
            for(int i = 0; i < virts.size(); i++) {
                if(!cdGraph.decomposeGraph(virts.get(i), IStorage.Type.VIRTUAL)) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unable to add virtual Graph. Maybe the  graph contain a Vertex/Edge, which was used in a previously added Graph.");
                }
            }
        }
        if(stackVertex != null) {
            if(!addStackedVertexMap(stackVertex)) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "can't apply all Stacking Vertices");
            }
        }
        if(stackEdge != null) {
            if(!addStackedEdgeMap(stackEdge)) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "can't apply all Stacking Edges");
            }
        }
        if(vertexRessources != null) {
            if(!addVertexRessources(vertexRessources)) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "can't apply all Veretx Ressources");
            }
        }
        if(edgeRessources != null) {
            if(!addEdgeRessources(edgeRessources)) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "can't apply all Edge Ressources");
            }
        }
    }
    
    private boolean addStackedVertexMap(HashMap<V,V> pStacked) {
        for(V virt : pStacked.keySet()) {
            V phy = pStacked.get(virt);
            if(!storage.stackVertex(virt, phy)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addStackedEdgeMap(HashMap<E,List<E>> pStacked) {
        for(E virt : pStacked.keySet()) {
            List<E> path = pStacked.get(virt);
            if(!storage.stackEdge(virt, path)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addVertexRessources(HashMap<V,Double> pRessources){
        for(V v : pRessources.keySet()) {
            double res = pRessources.get(v);
            if(!storage.addRessourceToVertex(v, res)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addEdgeRessources(HashMap<E,Double> pRessources){
        for(E e : pRessources.keySet()) {
            double res = pRessources.get(e);
            if(!storage.addRessourceToEdge(e, res)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(V v) {
        return storage.containsVertex(v);
    }
    
    @Override
    public IStorage.Type getTypeOfVertex(V v) {
        return storage.getTypeOfVertex(v);
    }
    
    @Override
    public IStorage.Type getTypeOfEdge(E e) {
        return storage.getTypeOfEdge(e);
    }
    
    @Override
    public Graph<V, E> getDisplay(IStorage.Type t) {
        Graph<V,E> ret = cdGraph.composeGraph(t);
        // System.out.println("logic - Vert in " + t + ": " + ret.getVertexCount());
        return ret;
    }
    
    @Override
    public boolean addVertex(V v, IStorage.Type t) {
        return storage.addVertex(v, t);
    }
    
    @Override
    public boolean addEdge(E e, V v1, V v2) {
        if(v1.equals(v2)) {
            return false;
        } else {
            return storage.addEdge(e, v1, v2);
        }
    }

    @Override
    public boolean stackVertex(V v1, V v2) {
        return storage.stackVertex(v1, v2);
    }

    @Override
    public boolean unstackVertex(V v) {
        return storage.unstackVertex(v);
    }
    
    @Override
    public V getStackedVertexTo(V virt) {
        return storage.getStackedVertexTo(virt);
    }
    
    @Override
    public Collection<V> getVertexStack(V v) {
        return storage.getVertexStack(v);
    }

    @Override
    public Collection<V> getAllVertOfType(IStorage.Type t) {
        return storage.getAllVertOfType(t);
    }
    
    @Override
    public Collection<E> getAllEdgesOfType(IStorage.Type t) {
        Collection<E> ret = new ArrayList<E>();
        for(Pair<E,Pair<V,V>> p : storage.getAllEdgesOfType(t)) {
            ret.add(p.getKey());
        }
        return ret;
    }

    @Override
    public boolean deleteVertex(V v) {
        return storage.deleteVertex(v);
    }

    @Override
    public boolean deleteEdge(E e) {
        return storage.deleteEdge(e);
    }

    @Override
    public double getRessourcesOfVertex(V v) {
        return storage.getRessourcesOfVertex(v);
    }
    
    @Override
    public double getRessourcesOfEdge(E e) {
        return storage.getRessourcesOfEdge(e);
    }

    @Override
    public boolean addRessourceToVertex(V v, Double res) {
        return storage.addRessourceToVertex(v, res);
    }

    @Override
    public boolean addRessourceToEdge(E e, Double res) {
        return storage.addRessourceToEdge(e, res);
    }
    
    @Override
    public boolean stackEdge(E virt, List<E> path) {
        return storage.stackEdge(virt, path);
    }

    @Override
    public boolean unstackEdge(E virt) {
        return storage.unstackEdge(virt);
    }
    
    @Override
    public List<E> getStackedEdgePath(E virt) {
        return storage.getStackedEdgePath(virt);
    }
    
    @Override
    public Collection<E> getEdgeStack(E e) {
        return storage.getEdgeStack(e);
    }
    
    /*
    @Override
    public boolean incrementalStackEdge(E e1, E e2) {
        if(e1 == null || e2 == null) {
            return false;
        }
        
        E virt = null;
        E phy = null;
        
        if(storage.getTypeOfEdge(e1) == IStorage.Type.VIRTUAL && storage.getTypeOfEdge(e2) == IStorage.Type.PHYSICAL) {
            virt = e1;
            phy = e2;
        } else if(storage.getTypeOfEdge(e1) == IStorage.Type.PHYSICAL && storage.getTypeOfEdge(e2) == IStorage.Type.VIRTUAL) {
            virt = e2;
            phy = e1;
        } else {
            return false;
        }
        
        if(!storage.containsStackedEdge(virt)) {
            // add newly
            List<E> path = new ArrayList<E>();
            path.add(phy);
            testPathValidity(path);
            
            // test for resources
            if(storage.getRessourcesOfEdgeLeft(phy) >= storage.getRessourcesOfEdge(virt)) {
                storage.stackEdge(virt, path);
                return true;
            } else {
                return false;
            }
        } else {
            // increment
            List<E> path = storage.getStackedEdgePath(virt);
            E start = path.get(0);
            E end = path.get(path.size()-1);
            if( storage.getEdgeDest(phy).equals(storage.getEdgeSource(start)) ) {
                // phy can stack to the beginning of the path
                List<E> newPath = new ArrayList<E>();
                newPath.add(phy);
                newPath.addAll(path);
                if(!testPathValidity(path)) {
                    return false;
                }
                storage.stackEdge(virt, newPath);
            } else if(storage.getEdgeSource(phy).equals(storage.getEdgeDest(end))) {
                // phy can stack to the end of the path
                path.add(phy);
                if(!testPathValidity(path)) {
                    path.remove(phy);
                }
            }
        }
        return true;
    }
    
    private boolean testPathValidity(List<E> path) {
        if(path.size() == 1) {
            if(storage.getEdgeSource(path.get(0)).equals(storage.getEdgeDest(path.get(0)))) {
                // one edge - loop
                return false;
            }
        } else if(path.size()>1) {
            E end = path.get(path.size()-1);
            // test connections
            for(E e : path) {
                if(e != end) {
                    if(!storage.getEdgeDest(e).equals(storage.getEdgeSource(path.get(path.indexOf(e)+1)))) {
                        return false;
                    }
                }
            }
        }
        
        // test loop
        for(E e1 : path) {
            for(E e2 : path) {
                if(!e1.equals(e2)) {
                    if( storage.getEdgeSource(e1).equals(storage.getEdgeSource(e2)) ||
                        storage.getEdgeDest(e1).equals(storage.getEdgeDest(e2))) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    */
    
    public boolean testEdgePathValidity(E virt, List<E> phys) {
        // test null
        if(virt == null || phys == null) {
            return false;
        }
        
        // test path len
        if(phys.size() == 0) {
            return false;
        }
        
        // test correct type
        if(getTypeOfEdge(virt) != IStorage.Type.VIRTUAL) {
            return false;
        }
        
        for(E phy : phys) {
            if(getTypeOfEdge(phy) != IStorage.Type.PHYSICAL) {
                return false;
            }
        }
        
        // test begin / end
        if(!(   getStackedVertexTo(storage.getEdgeSource(virt)) == storage.getEdgeSource(phys.get(0)) &&
                getStackedVertexTo(storage.getEdgeDest(virt)) == storage.getEdgeDest(phys.get(phys.size()-1)))) {
            return false;
        }
        
        // test connections
        for(int i = 0; i<phys.size()-1; i++) {
            if(storage.getEdgeDest(phys.get(i)) != storage.getEdgeSource(phys.get(i + 1))) {
                return false;
            }
        }
        
        // test loop
        for(int i = 0; i<phys.size(); i++) {
            for(int j = i+1; j<phys.size(); j++) {
                if(storage.getEdgeSource(phys.get(i)) == storage.getEdgeSource(phys.get(j))) {
                    return false;
                }
                if(storage.getEdgeSource(phys.get(i)) == storage.getEdgeDest(phys.get(phys.size()-1))) {
                    return false;
                }
            }
        }
        return true;
    }
}
