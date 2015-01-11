/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Logic;

import edu.uci.ics.jung.graph.Graph;
import inet.CalculationNetworkEditor.Storage.IStorage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author andre
 */
public interface ILogic<V,E> {
    
    /**
     * Init the logic and storage subsystem. Fill graphs and other data structures.
     * @param phys List of graphs, which will be added to physical.
     * @param virts List of graphs, which will be added to virtual.
     * @param stackVertex List of virtual-physical pairs. Specify which vertex is maped to which vertex.
     * @param stackEdge List of virtual-physical pairs. Specify which edge is mapped to which path.
     * @param vertexRessources List of vertex double pairs. Specify which vertex has which resource.
     * @param edgeRessources  List of edge double pairs. Specify which edge has which ressource.
     */
    public void init(
                    List<Graph<V, E>> phys, List<Graph<V, E>> virts,
                    HashMap<V,V> stackVertex, HashMap<E,List<E>> stackEdge, 
                    HashMap<V, Double> vertexRessources, HashMap<E, Double> edgeRessources);
    
    /**
     * Every vertex has a type - Phisycal or Virtual. This method return the type of the vertex.
     * @param v vertex, you want to know the type of
     * @return Type of vertex.
     */
    public IStorage.Type getTypeOfVertex(V v);
    
    /**
     * Every edge has a type - Phisycal or Virtual. This method return the type of the edge.
     * @param e edge, you want to know the type of
     * @return Type of edge.
     */
    public IStorage.Type getTypeOfEdge(E e);
    
    /**
     * Return if a vertex is already stored or not.
     * @param v Vertex, which is checked.
     * @return True, if v is already stored.
     */
    public boolean containsVertex(V v);
    
    /**
     * Return if an edge is already stored or not.
     * @param e Edge, which is checked.
     * @return True, if e is already stored.
     */
    public boolean containsEdge(E e);
    
    /**
     * Return the graph, containing all graph - elements of the specified type. 
     * These graphs are the graphs, which are displayed at Physical, Virtual and Both - View.
     * @param t type of the graph, which should be returned.
     * @return graph of a specified type.
     */
    public Graph<V,E> getDisplay(IStorage.Type t);
    
    /**
     * Add a Vertex to the data structures.
     * @param v Vertex, which should be added. 
     * @param t Type of the veretx, which should be added.
     * @return true if the vertex is added successfully.
     */
    public boolean addVertex(V v, IStorage.Type t);
    
    /**
     * Add an edge to the data structures.
     * @param e edge, which should be added.
     * @param v1 first vertex, the edge is connected to.
     * @param v2 second vertex, the edge is connected to.
     * @return true, if the edge is added sucessfully.
     */
    public boolean addEdge(E e, V v1, V v2);
    
    /**
     * Delete the vertex v from data structures.
     * @param v vertex, which should be deleted.
     * @return true, if the vertex is deleted sucessfully.
     */
    public boolean deleteVertex(V v);
    
    /**
     * Delete an edge from the data strutures.
     * @param e edge, which should be deleted.
     * @return true, if the edge is deleted sucessfully.
     */
    public boolean deleteEdge(E e);
    
    /**
     * Return all vertices of the type t.
     * @param t type of the vertexes, which are returned.
     * @return a collection of all vertices of the given type.
     */
    public Collection<V> getAllVertOfType(IStorage.Type t);
    
    /**
     * Return all edges of type t.
     * @param t type of edges, which are returned.
     * @return a collection of all edges of type t.
     */
    public Collection<E> getAllEdgesOfType(IStorage.Type t);
    
    /**
     * Map a virtual vertex to a physical vertex. It does not matter if v1 or
     * v2 is the virtual one.
     * @param v1 physical or virtual vertex
     * @param v2 the other type of vertex
     * @return true, if the vertices can be mapped.
     */
    public boolean stackVertex(V v1, V v2);
    
    /**
     * UnMap a vertex from another one.
     * @param v Vertex, the mapping should deleted from.
     * @return true, if the mapping is sucessfully deleted.
     */
    public boolean unstackVertex(V v);
    
    /**
     * Return the physical vertex, virt is mapped to.
     * @param virt Vertex, which is mapped.
     * @return Physical vertex, mapped to virt.
     */
    public V getStackedVertexTo(V virt);
    
    /**
     * Return all vertexes, which belong to a stack of a vertex. To a stack belong
     * all vertices, which are stacked to the physical vertex, v is stacked to.
     * @param v one vertex out of the stack.
     * @return A collection of vertexes, which represent the stack.
     */
    public Collection<V> getVertexStack(V v);
    
    /**
     * Map a virtual edge to a path of physical edges.
     * @param virt a virtual edge.
     * @param path a list of edges, which build a path.
     * @return true, if an 
     */
    public boolean stackEdge(E virt, List<E> path);

    /**
     * Unmap an edge. This removes a maped edge from an edge path.
     * @param virt edge, which should be unmaped.
     * @return true, if the edge is sucessfully unstacked.
     */
    public boolean unstackEdge(E virt);
    
    /**
     * Get first endpoint of edge. 
     * @param e edge of interest.
     * @return first endpoint.
     */
    public V getEdgeSource(E e);
    
    /**
     * Get second endpoint of edge. 
     * @param e edge of interest.
     * @return second endpoint.
     */
    public V getEdgeDest(E e);
    
    /**
     * This method test if a given physical path is valid an can be added to the
     * internal storage structure.
     * @param virt virtual edge, which is mapped to an physical path.
     * @param phys physical path
     * @return true, if the path is valid.
     */
    public boolean testEdgePathValidity(E virt, List<E> phy);
    
    /**
     * Retrieve the path of physical edges, which is mapped to a virtual edge.
     * @param virt the edge, which maps to an physical path.
     * @return A List of edges, which represent a 
     */
    public List<E> getStackedEdgePath(E virt);
    
    /**
     * Get all edges of the mapping. The mapping contain the physical edge and all
     * virtual edges, which are mapped to. This are the edges, which are highlighted
     * if one edge is selected.
     * @param e edge, which is used to retrieve the stack.
     * @return A list of edges, which represent the mapping.
     */
    public Collection<E> getEdgeStack(E e);
    
    /**
     * Get the resources of a given vertex.
     * @param v vertex, which ressources should be returned.
     * @return ressources of v
     */
    public double getRessourcesOfVertex(V v);
    
    /**
     * Get the resources of a given edge.
     * @param e edge, which ressources should be returned.
     * @return ressources of e
     */
    public double getRessourcesOfEdge(E e);
    
    /**
     * Set Ressources of Vertex.
     * @param v vertex, which ressources should be added.
     * @param res ressources of vertex, which should be set
     * @return true, if resources are successfully set to vertex
     */
    public boolean addRessourceToVertex(V v, Double res);
    
    /**
     * Set resources of edge.
     * @param e edge, which resources should be set.
     * @param res resources of edge, which should be set.
     * @return true, if resources are successfully set to edge.
     */
    public boolean addRessourceToEdge(E e, Double res);
}
