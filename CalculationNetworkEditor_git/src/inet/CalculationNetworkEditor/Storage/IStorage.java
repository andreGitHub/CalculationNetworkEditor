/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author andre
 */
public interface IStorage<V,E> {
    public enum Type {PHYSICAL, VIRTUAL};
    
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
    public boolean addEdge(E e, V source, V dest);
    
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
    public Collection<Pair<E,Pair<V,V>>> getAllEdgesOfType(IStorage.Type t);
    
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
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Figure out if a vertex can be added to storage subsystem or not.
     * @param v vertex of interest.
     * @param t type of vertex.
     * @return true, if vertex can be added.
     */
    public boolean addAbleVertex(V v, IStorage.Type t);
    
    /**
     * Figure out if an edge can be added to storage subsystem or not.
     * @param e edge of interest.
     * @param source first endpoint of e.
     * @param dest second endpoint of e.
     * @return true, if edge can be added.
     */
    public boolean addAbleEdge(E e, V source, V dest);
    
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
     * Start at v (first endpoint), go along e, and return second endpoint.
     * @param v first endpoint
     * @param e way to go
     * @return second endpoint
     */
    public V step(V v, E e);
    
    /**
     * Get all vertexes of the mapping. A mapping contain all vertices, which are
     * mapped to the physical vertex phy.
     * @param phy vertex of interest.
     * @return a collection of vertices, which are mapped to phy.
     */
    public Collection<V> getAllStackedVertexes(V phy);
    
    /**
     * Figure out if storage subsystem has virt saved as stacked.
     * @param virt edge of interest
     * @return true, if virt is already mapped to another vertex.
     */
    public boolean containsStackedEdge(E virt);
    
    /**
     * Figure out how much resources has phy left after subtracting the resources
     * of all stacked vertices.
     * @param phy vertex of interest.
     * @return return the amount of ressources left.
     */
    public double getRessourcesOfVertexLeft(V phy);
    
    /**
     * Remove resources of v from storage subsystem.
     * @param v vertex of interest.
     */
    public void deleteRessourcesOfVertex(V v);
    
    /**
     * Figure out how much resources has phy left after subtracting the resources
     * of all stacked edges.
     * @param phy edge of interest.
     * @return return the amount of ressources left.
     */
    public double getRessourcesOfEdgeLeft(E phy);
    
    /**
     * Remove resources of e from storage subsystem.
     * @param v edge of interest.
     */
    public void deleteRessourcesOfEdge(E e);
}
