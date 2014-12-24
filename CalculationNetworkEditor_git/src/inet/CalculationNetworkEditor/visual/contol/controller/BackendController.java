/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.contol.controller;

import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author andre
 */
public class BackendController<V,E> { /* implements MouseMotionListener, MouseListener { */
    
    private Factory<V> vertexFactory = null;
    private Factory<E> edgeFactory = null;
    
    private ILogic<V,E> logic = null;
    
    public BackendController(ILogic<V,E> pLogic, Factory<V> pVertexFactory, Factory<E> pEdgeFactory) {

        logic = pLogic;
        
        vertexFactory = pVertexFactory;
        edgeFactory = pEdgeFactory;
    }
    
    public V addVertex(IStorage.Type t) {
        V v = vertexFactory.create();
        logic.addVertex(v, t);
        return v;
    }
    
    public E addEdge(V from, V to) {
        E e = edgeFactory.create();
        if(logic.addEdge(e, from, to)) {
            return e;
        } else {
            return null;
        }
    }
    
    public boolean addRessourcesToVertex(V v, double res) {
        return logic.addRessourceToVertex(v, res);
    }
    
    public boolean addRessourcesToEdge(E e, double res) {
        return logic.addRessourceToEdge(e, res);
    }
    
    public double getRessourcesOfVertex(V v) {
        return logic.getRessourcesOfVertex(v);
    }
    
    public double getRessourcesOfEdge(E e) {
        return logic.getRessourcesOfEdge(e);
    }
    
    public boolean deleteVertex(V v) {
        return logic.deleteVertex(v);
    }
    
    public boolean deleteEdge(E e) {
        return logic.deleteEdge(e);
    }
    
    public IStorage.Type getTypeOfVertex(V v) {
        return logic.getTypeOfVertex(v);
    }
    
    public IStorage.Type getTypeOfEdge(E e) {
        return logic.getTypeOfEdge(e);
    }
    
    public Collection<V> getAllVertexPhysical() {
        return logic.getAllVertOfType(IStorage.Type.PHYSICAL);
    }
    
    public Collection<E> getAllEdgePhysical() {
        return logic.getAllEdgesOfType(IStorage.Type.PHYSICAL);
    }
    
    public boolean stackVertexVirtToPhy(V virt, V phy) {
        return logic.stackVertex(virt, phy);
    }
    
    public boolean unstackVertex(V virt) {
        return logic.unstackVertex(virt);
    }
    
    public boolean isStackedToVertex(V v) {
        if(logic.getStackedVertexTo(v) == null) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean isStackedToEdge(E e) {
        if(logic.getStackedEdgePath(e) == null) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean stackEdgeVirtToPhy(E virt, List<E> phys) {
        return logic.stackEdge(virt, phys);
    }
    
    public boolean testEdgePathValidity(E virt, List<E> phys) {
        return logic.testEdgePathValidity(virt, phys);
    }
}
