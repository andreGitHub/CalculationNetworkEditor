/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Logic;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.Storage.Storage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 * This class is used to store a graph to the internal storage structure and retrive graphs from this structure.
 * 
 * @author andre
 */
public class ComposeDecomposeGraph<V,E> {
    private IStorage<V,E> storage = null;
    
    /** 
     * Initializes the class.
     * 
     * @param pStorage storage subsystem.
     */
    public ComposeDecomposeGraph(IStorage<V,E> pStorage) {
        storage = pStorage;
    }
    
    /**
     * Decompose the given Graph and add the graph - elements to the internal storage structure.
     * 
     * @param g graph, which should be decomposed
     * @param t type of the graph. specify if the graph is added to Physical or Virtual.
     * @return true if all graph components can be added (if the graph elements are not yet added), otherwise false.
     */
    public boolean decomposeGraph(Graph<V, E> g, IStorage.Type t) {
        // to add the graph all vertexes and all edges have to by addAble
        // check if vertexes are addAble
        for(V v : g.getVertices()) {
            if(!storage.addAbleVertex(v, t)) {
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, "Vertex not addable");
                return false;
            }
        }
        // check if edges are addAble
        for(E e : g.getEdges()) {
            if(!storage.addAbleEdge(e, g.getEndpoints(e).getFirst(), g.getEndpoints(e).getSecond())) {
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, "Edge not addable");
                return false;
            }
        }
        
        // add all vertexes and edges
        for(V v : g.getVertices()) {
            storage.addVertex(v, t);
        }
        for(E e : g.getEdges()) {
            storage.addEdge(e, g.getEndpoints(e).getFirst(), g.getEndpoints(e).getSecond());
        }
        return true;
    }
    
    /**
     * Retrive a graph of the given type from the internal storage structure.
     * 
     * @param t Type of the graph.
     * @return The composed graph.
     */
    public Graph<V,E> composeGraph(IStorage.Type t) {
        Graph<V,E> g = new UndirectedSparseGraph<V,E>();
        if(t != null) {
            for(V v : storage.getAllVertOfType(t)) {
                g.addVertex(v);
            }
            for(Pair<E,Pair<V,V>> p : storage.getAllEdgesOfType(t)) {
                g.addEdge(p.getKey(), p.getValue().getKey(), p.getValue().getValue());
            }
            return g;
        } else {
            for(V v : storage.getAllVertOfType(IStorage.Type.PHYSICAL)) {
                g.addVertex(v);
            }
            for(V v : storage.getAllVertOfType(IStorage.Type.VIRTUAL)) {
                g.addVertex(v);
            }
            for(Pair<E,Pair<V,V>> p : storage.getAllEdgesOfType(IStorage.Type.PHYSICAL)) {
                g.addEdge(p.getKey(), p.getValue().getKey(), p.getValue().getValue());
            }
            for(Pair<E,Pair<V,V>> p : storage.getAllEdgesOfType(IStorage.Type.VIRTUAL)) {
                g.addEdge(p.getKey(), p.getValue().getKey(), p.getValue().getValue());
            }
            return g;
        }
    }
}
