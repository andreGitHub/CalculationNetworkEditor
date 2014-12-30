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
 * The logic subsystem rest on top of the storage subsystem. It enhance the storage subsystem with some checking capabilities.
 * 
 * @author andre
 */
public class Logic<V,E> implements ILogic<V, E>{
    private IStorage<V,E> storage = null;
    private ComposeDecomposeGraph<V,E> cdGraph = null;
    
    /**
     * Create a new class.
     */
    public Logic() {
        storage = new Storage();
        cdGraph = new ComposeDecomposeGraph<V,E>(storage);
    }
    
    /**
     * Initialises the class. Add graphs to the storage subsystem, set stacking
     * vertices and edges and add ressources of vertices and edges.
     * @param phys list of graphs, which are added to physical.
     * @param virts list of graphs, which are added to virtual.
     * @param stackVertex list of Vertex pairs. it specifies, which vertex is tacked to which vertex.
     * @param stackEdge like vertex pairs, but with edges and paths of edges.
     * @param vertexRessources list of vertex - double pairs, which specify the ressources of each vertex.
     * @param edgeRessources like vertex ressources, but with edges
     */    
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
    
    /**
     * Add list of stacked Vertices to internal storage structure.
     * @param pStacked List of Vertices, which should be stacked.
     * @return true if all list entries could be added, otherwise false.
     */
    private boolean addStackedVertexMap(HashMap<V,V> pStacked) {
        boolean ret = true;
        for(V virt : pStacked.keySet()) {
            V phy = pStacked.get(virt);
            if(!storage.stackVertex(virt, phy)) {
                ret = false;
            }
        }
        return ret;
    }
    
    /**
     * Add list of stacked Edges to internal storage structure.
     * @param pStacked List of Edges, which should be stacked.
     * @return true if all list entries could be added, otherwise false.
     */
    public boolean addStackedEdgeMap(HashMap<E,List<E>> pStacked) {
        boolean ret = true;
        for(E virt : pStacked.keySet()) {
            List<E> path = pStacked.get(virt);
            if(!storage.stackEdge(virt, path)) {
                ret = false;
            }
        }
        return ret;
    }
    
    /**
     * Add list of Vertex-Ressource - Pairs to internal data structure.
     * @param pRessources List of vertex-ressource pairs.
     * @return return true, if all entries can be added.
     */
    public boolean addVertexRessources(HashMap<V,Double> pRessources){
        boolean ret = true;
        for(V v : pRessources.keySet()) {
            double res = pRessources.get(v);
            if(!storage.addRessourceToVertex(v, res)) {
                ret = false;
            }
        }
        return ret;
    }
    
    /**
     * Add list of Edge-Ressource - Pairs to internal data structure.
     * @param pRessources List of edge-ressource pairs.
     * @return return true, if all entries can be added.
     */
    public boolean addEdgeRessources(HashMap<E,Double> pRessources){
        boolean ret = true;
        for(E e : pRessources.keySet()) {
            double res = pRessources.get(e);
            if(!storage.addRessourceToEdge(e, res)) {
                ret = false;
            }
        }
        return ret;
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
    
    /**
     * This method test if a given physical path is valid an can be added to the internal storage structure.
     * @param virt virtual edge, which is mapped to an physical path.
     * @param phys physical path
     * @return true, if the path is valid.
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
        boolean foundBegin = false;
        boolean foundEnd = false;
        for(E e : phys) {
            V virtSourceStackedTo = getStackedVertexTo(storage.getEdgeSource(virt));
            V virtDestStackedTo = getStackedVertexTo(storage.getEdgeDest(virt));
            V eSource = storage.getEdgeSource(e);
            if(virtSourceStackedTo == eSource || virtDestStackedTo == eSource) {
                foundBegin = true;
            }
            
            V eDest = storage.getEdgeDest(e);
            if(virtSourceStackedTo == eDest || virtDestStackedTo == eDest) {
                foundEnd = true;
            }
        }
        
        if(!(foundBegin && foundEnd)) {
            return false;
        }
        // there is an edge in phy, whose source vertex is attached to the source vertex of virt and
        // there is an edge in phy, whose dest   vertex is attached to the dest   vertex of virt
        
        
        
        // test connections between edges in path
        // copy edges
        List<E> copiedEdges = new ArrayList<E>();
        for(E e : phys) {
            copiedEdges.add(e);
        }
        
        V act = null;
        V actNew = getStackedVertexTo(storage.getEdgeSource(virt));
        E actEdge = null;
        while(act != actNew && copiedEdges.size()>0) {
            act = actNew;
            for(E e : copiedEdges) {
                if(storage.step(act, e) != null) {
                    actEdge = e;
                    actNew = storage.step(act, e);
                    copiedEdges.remove(e);
                    break;
                }
            }
        }
        
        //V actSource = storage.getEdgeSource(actEdge);
        //V actDest = storage.getEdgeDest(actEdge);
        V virtDestStackedTo = getStackedVertexTo(storage.getEdgeDest(virt));
        
        if(!(copiedEdges.size() == 0 && actNew == virtDestStackedTo)) {
            return false;
        }
        
        // test loop
        for(int i = 0; i<phys.size(); i++) {
            for(int j = i+1; j<phys.size(); j++) {
                if(storage.getEdgeSource(phys.get(i)) == storage.getEdgeSource(phys.get(j))) {
                    return false;
                }
                if(storage.getEdgeDest(phys.get(i)) == storage.getEdgeDest(phys.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
