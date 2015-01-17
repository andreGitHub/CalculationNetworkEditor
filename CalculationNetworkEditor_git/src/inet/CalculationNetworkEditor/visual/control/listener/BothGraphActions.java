/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.control.listener;

import edu.uci.ics.jung.algorithms.layout.Layout;
import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerBoth;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author andre
 */
public class BothGraphActions<V,E> implements ActionListener{
    private final double mapDistance = 10.0d;
    private ILogic<V,E> logic = null;
    private VisualizationViewerBoth<V,E> visViewBoth = null;
    private Factory<V> vertexFactory = null;
    private Factory<E> edgeFactory = null;
    
    public BothGraphActions(ILogic<V,E> pLogic, VisualizationViewerBoth<V,E> pVisViewBoth,
                            Factory<V> pVertexFactory, Factory<E> pEdgeFactory) {
        logic = pLogic;
        visViewBoth = pVisViewBoth;
        vertexFactory = pVertexFactory;
        edgeFactory = pEdgeFactory;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object objSource = e.getSource();
        if(objSource instanceof JButton) {
            JButton jbtnSource = (JButton)objSource;
            if("relocate".equals(jbtnSource.getText())) {
                System.out.println("jbtn Relocate");
                relocateVertexes();
            } else if("visualize".equals(jbtnSource.getText())) {
                System.out.println("jbtn Visualize");
                visualizeStacking();
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void relocateVertexes() {
        Layout<V,E> layout = visViewBoth.getGraphLayout();
        Collection<V> physical = logic.getAllVertOfType(IStorage.Type.PHYSICAL);
        for(V phy : physical) {
            Collection<V> mapping = logic.getVertexStack(phy);
            
            for(V visual : visualPhysicalV.keySet()) {
                if(visualPhysicalV.get(visual) == phy) {
                    mapping.add(visual);
                }
            }
            
            Point2D pAct = layout.transform(phy);
            for(V mapped : mapping) {
                if(!mapped.equals(phy)) {
                    // all vertices of mapping but the phisical vertex
                    pAct.setLocation(pAct.getX() + mapDistance, pAct.getY() + mapDistance);
                    layout.setLocation(mapped, pAct);
                }
            }
        }
        
        //printLocationOfAllVertices();
    }
    
    private HashMap<V,V> visualPhysicalV = new HashMap<V,V>();
    private HashMap<E,E> visualPhysicalE = new HashMap<E,E>();
    
    /**
     * This method insert further graph elements in the visualization component.
     * These graph elements are painted in greenbrown and act as visualization components.
     * The method copy path of stacked edges and add them to the graph.
     */
    public void visualizeStacking() {
        Layout<V,E> layout = visViewBoth.getGraphLayout();
        
        // delete all previousely added visual components
        for(E e : visualPhysicalE.keySet()) {
            layout.getGraph().removeEdge(e);
        }
        visualPhysicalE = new HashMap<E,E>();
        for(V v : visualPhysicalV.keySet()) {
            layout.getGraph().removeVertex(v);
        }
        visualPhysicalV = new HashMap<V,V>();
        
        // add visualizing components
        Collection<E> virtualE = logic.getAllEdgesOfType(IStorage.Type.VIRTUAL);
        for(E virt : virtualE) {
            List<E> pathOrig = logic.getStackedEdgePath(virt);
            List<E> path = new ArrayList<E>();
            if(pathOrig == null) {
                path = null;
            } else {
                for(E pe : pathOrig) {
                    path.add(pe);
                }
            }
            
            if(path != null && path.size()>1) {
                // copy path to graph and hashmaps
                V firstVirtual = logic.getEdgeSource(virt);
                V firstPhysical = logic.getStackedVertexTo(firstVirtual);
                V secondVirtual = logic.getEdgeDest(virt);
                V secondPhysical = logic.getStackedVertexTo(secondVirtual);
                
                // copy path
                V actVertPhy = firstPhysical;
                V actVertVirt = firstVirtual;
                ArrayList<E> pathCopy = new ArrayList<E>();
                
                int pathSize = path.size();
                while(pathCopy.size()<pathSize){
                    for(E pathElem : path) {
                        V pathElemSource = logic.getEdgeSource(pathElem);
                        V pathElemDest = logic.getEdgeDest(pathElem);
                        if(pathElemSource == actVertPhy) {
                            if(pathElemDest == secondPhysical) {
                                E tmpEdgeVisu = edgeFactory.create();
                                pathCopy.add(tmpEdgeVisu);
                                layout.getGraph().addEdge(tmpEdgeVisu, actVertVirt, secondVirtual);
                                visualPhysicalE.put(tmpEdgeVisu, pathElem);
                                
                                actVertPhy = logic.getEdgeDest(pathElem);
                                actVertVirt = secondVirtual;
                                
                                path.remove(pathElem);
                                
                                break;
                            } else {
                                actVertVirt = addVertexAndEdgeForeward(layout,pathCopy,pathElem,actVertVirt);
                                actVertPhy = logic.getEdgeDest(pathElem);
                                
                                path.remove(pathElem);
                                
                                break;
                            }
                        } else if(pathElemDest == actVertPhy) {
                            if(pathElemSource == secondPhysical) {
                                E tmpEdgeVisu = edgeFactory.create();
                                pathCopy.add(tmpEdgeVisu);
                                layout.getGraph().addEdge(tmpEdgeVisu, actVertVirt, secondVirtual);
                                visualPhysicalE.put(tmpEdgeVisu, pathElem);
                                
                                actVertPhy = logic.getEdgeSource(pathElem);
                                actVertVirt = secondVirtual;
                                
                                path.remove(pathElem);
                                
                                break;
                            } else {
                                
                                actVertVirt = addVertexAndEdgeBackward(layout,pathCopy,pathElem,actVertVirt);
                                actVertPhy = logic.getEdgeSource(pathElem);
                                
                                path.remove(pathElem);
                                
                                break;
                            }
                        }
                    }
                }
                visViewBoth.getGraphLayout().getGraph().removeEdge(virt);
            }
        }
        visViewBoth.repaint();
    }
    
    private V addVertexAndEdgeForeward(Layout<V,E> layout, ArrayList<E> pathCopy, E pathElem, V actVertVirt) {
        V tmpVertVisu = vertexFactory.create();
        layout.getGraph().addVertex(tmpVertVisu);
        visualPhysicalV.put(tmpVertVisu, logic.getEdgeDest(pathElem));
        E tmpEdgeVisu = edgeFactory.create();
        pathCopy.add(tmpEdgeVisu);
        layout.getGraph().addEdge(tmpEdgeVisu, actVertVirt, tmpVertVisu);
        visualPhysicalE.put(tmpEdgeVisu, pathElem);
        
        return tmpVertVisu;
    }
    
    private V addVertexAndEdgeBackward(Layout<V,E> layout, ArrayList<E> pathCopy, E pathElem, V actVertVirt) {
        V tmpVertVisu = vertexFactory.create();
        layout.getGraph().addVertex(tmpVertVisu);
        visualPhysicalV.put(tmpVertVisu, logic.getEdgeSource(pathElem));
        E tmpEdgeVisu = edgeFactory.create();
        pathCopy.add(tmpEdgeVisu);
        layout.getGraph().addEdge(tmpEdgeVisu, actVertVirt, tmpVertVisu);
        visualPhysicalE.put(tmpEdgeVisu, pathElem);
        
        return tmpVertVisu;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    /*
    private void printLocationOfAllVertices() {
        Layout<V,E> layout = visViewBoth.getGraphLayout();
        Collection<V> verts = layout.getGraph().getVertices();
        for(V vert : verts) {
            System.out.println("loc " + vert + ": " + layout.transform(vert));
        }
    }
    */
}
