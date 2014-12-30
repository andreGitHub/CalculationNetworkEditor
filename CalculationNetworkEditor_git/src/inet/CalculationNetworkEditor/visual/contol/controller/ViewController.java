/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.contol.controller;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.visual.view.EditorPane;
import inet.CalculationNetworkEditor.visual.control.listener.TabSwitchedListener;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerBoth;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerPhysical;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerVirtual;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class ViewController<V,E> {
    private VisualizationViewerPhysical<V,E> visViewerPhysical = null;
    //private Layout<V,E> layoutPhysical = null;
    //private Graph<V,E> graphPhysical = null;
    
    private VisualizationViewerVirtual<V,E> visViewerVirtual  = null;
    //private Layout<V,E> layoutVirtual = null;
    //private Graph<V,E> graphVirtual = null;
    
    private VisualizationViewerBoth<V,E> visViewerBoth     = null;
    
    private EditorPane edit = null;
    
    public ViewController(
        EditorPane pEdit,
            VisualizationViewerPhysical<V,E> pVisViewerPhysical,
            VisualizationViewerVirtual<V,E> pVisViewerVirtual,
            VisualizationViewerBoth<V,E> pVisViewerBoth) {
        
        edit = pEdit;
        
        visViewerPhysical = pVisViewerPhysical;
        //layoutPhysical = pVisViewerPhysical.getGraphLayout();
        //graphPhysical = pVisViewerPhysical.getGraphLayout().getGraph();
        
        visViewerVirtual  = pVisViewerVirtual;
        //layoutVirtual = pVisViewerVirtual.getGraphLayout();
        //graphVirtual = pVisViewerVirtual.getGraphLayout().getGraph();
        
        visViewerBoth     = pVisViewerBoth;
    }
    
    private VisualizationViewer<V,E> getActualVisualizationViewer() {
        VisualizationViewer<V,E> ret = null;
        
        switch(edit.getActualTab()) {
            case 0: { // physical
                ret = visViewerPhysical;
            } break;
            case 1: { // virtual
                ret = visViewerVirtual;
            } break;
            case 2: { // both
                ret = visViewerBoth;
            } break;
            default: { // unknown
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown view type");
            }
        }
        
        return ret;
    }
    
    public void zoom(double zooming) {
        System.out.println("#vertex: " + visViewerPhysical.getGraphLayout().getGraph().getVertexCount());
        
        switch(edit.getActualTab()) {
            case 0: { // physical
                //visViewerPhysical.setZoomFactorPhysical(visViewerPhysical.getZoomFactorPhysical() * zooming);
                LayoutScalingControl lsc = new LayoutScalingControl();
                lsc.scale(visViewerPhysical, (float)zooming, visViewerPhysical.getCenter());
                visViewerPhysical.scaleToLayout(lsc);
            } break;
            case 1: { // virtual
                //visViewerVirtual.setZoomFactorVirtual(visViewerVirtual.getZoomFactorVirtual() * zooming);
                LayoutScalingControl lsc = new LayoutScalingControl();
                lsc.scale(visViewerVirtual, (float)zooming, visViewerVirtual.getCenter());
                visViewerVirtual.scaleToLayout(lsc);
            } break;
            case 2: { // both
                //visViewerBoth.setZoomFactorBoth(visViewerBoth.getZoomFactorBoth() * zooming);
                LayoutScalingControl lsc = new LayoutScalingControl();
                lsc.scale(visViewerBoth, (float)zooming, visViewerBoth.getCenter());
                visViewerBoth.scaleToLayout(lsc);
            } break;
            default: { // unknown
                Logger.getLogger(TabSwitchedListener.class.getName()).log(Level.SEVERE, "unknown view type");
            }
        }
    }
    
    public void move(double x, double y) {
        switch(edit.getActualTab()) {
            case 0: { // physical
                MutableTransformer layout = visViewerPhysical.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
                layout.translate(x/visViewerPhysical.getScaleFactor(), y/visViewerPhysical.getScaleFactor());
                // found in: EditingGraphMousePlugin
                // layout.setLocation(newVertex, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
                System.out.println(visViewerPhysical.getRenderContext().getMultiLayerTransformer().transform(new Point(100,100)));
            } break;
            case 1: {
                MutableTransformer layout = visViewerVirtual.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
                layout.translate(x/visViewerVirtual.getScaleFactor(), y/visViewerVirtual.getScaleFactor());
            } break;
            case 2: {
                MutableTransformer layout = visViewerBoth.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
                layout.translate(x/visViewerBoth.getScaleFactor(), y/visViewerBoth.getScaleFactor());
            } break;
            default: {
                Logger.getLogger(TabSwitchedListener.class.getName()).log(Level.SEVERE, "unknown view type");
            }
        }
    }
    
    public void addVertex(Point2D p, V v) {
         switch(edit.getActualTab()) {
            case 0: { // physical
                // System.out.println("add vertex to physical");
                
                visViewerPhysical.getGraphLayout().getGraph().addVertex(v);

                MutableTransformer modelTransformer = visViewerPhysical.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
                visViewerPhysical.getGraphLayout().setLocation(v, new Point(
                        (int)((p.getX()-modelTransformer.getTranslateX())/visViewerPhysical.getScaleFactor()),
                        (int)((p.getY()-modelTransformer.getTranslateY())/visViewerPhysical.getScaleFactor()) ));
            } break;
            case 1: { // virtual
                System.out.println("add vertex to virtual");
                visViewerVirtual.getGraphLayout().getGraph().addVertex(v);

                MutableTransformer modelTransformer = visViewerVirtual.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
                visViewerVirtual.getGraphLayout().setLocation(v, new Point(
                        (int)((p.getX()-modelTransformer.getTranslateX())/visViewerVirtual.getScaleFactor()),
                        (int)((p.getY()-modelTransformer.getTranslateY())/visViewerVirtual.getScaleFactor()) ));
            } break;
            case 2: { // both
                System.out.println("add vertex to both - do nothing");
                // do nothing
            } break;
            default: { // unknown view
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown view type");
            }
        }
    }
    
    public boolean deleteVertex(V v) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        if(visView.getGraphLayout().getGraph().removeVertex(v)) {
            visView.repaint();
            return true;
        } else {
            return false;
        }
    }
    
    public void addEdge(E e, V from, V to) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        visView.getGraphLayout().getGraph().addEdge(e, from, to);
        visView.repaint();
    }
    
    public boolean deleteEdge(E e) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        if(visView.getGraphLayout().getGraph().removeEdge(e)) {
            visView.repaint();
            return true;
        } else {
            return false;
        }
    }
    
    /*
    public boolean isElementAtVertex(Point p) {
        
    }
    
    public boolean isElementAtEdge(Point p) {
        
    }
    */
    public boolean isElementAt(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        V vertex = pickSupport.getVertex(visView.getModel().getGraphLayout(), p.getX(), p.getY());
        E edge = pickSupport.getEdge(visView.getModel().getGraphLayout(), p.getX(), p.getY());
        
        if(vertex != null || edge != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean selectElementAt(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        V vertex = pickSupport.getVertex(visView.getGraphLayout(), p.getX(), p.getY());
        
        PickedState<V> pickedVertexState = visView.getPickedVertexState();
        PickedState<E> pickedEdgeState = visView.getPickedEdgeState();
        
        if(vertex != null) {
            if(!pickedVertexState.isPicked(vertex)) {
                pickedVertexState.clear();
                pickedEdgeState.clear();
                pickedVertexState.pick(vertex, true);
            }
            return true;
        }
        
        
        E edge = pickSupport.getEdge(visView.getGraphLayout(), p.getX(), p.getY());
        
        if(edge != null) {
            if(!pickedEdgeState.isPicked(edge)) {
                pickedVertexState.clear();
                pickedEdgeState.clear();
                pickedEdgeState.pick(edge, true);
            }
            return true;
        }
        
        
        return false;
    }
    
    public boolean isSelected(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        V vertex = pickSupport.getVertex(visView.getGraphLayout(), p.getX(), p.getY());
        
        if(vertex != null) {
            PickedState<V> pickedVertexState = visView.getPickedVertexState();
            if(pickedVertexState.getPicked().contains(vertex)) {
                return true;
            }
        }
        
        E edge = pickSupport.getEdge(visView.getGraphLayout(), p.getX(), p.getY());
        
        if(edge != null) {
            PickedState<E> pickedEdgeState = visView.getPickedEdgeState();
            if(pickedEdgeState.getPicked().contains(edge)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean elementAtPositionIsVertex(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        V vertex = pickSupport.getVertex(visView.getGraphLayout(), p.getX(), p.getY());
        
        if(vertex != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean elementAtPositionIsEdge(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        E edge = pickSupport.getEdge(visView.getGraphLayout(), p.getX(), p.getY());
        
        if(edge != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public V getVertexAtPoint(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        V vertex = pickSupport.getVertex(visView.getGraphLayout(), p.getX(), p.getY());
        
        return vertex;
    }
    
    public E getEdgeAtPoint(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        GraphElementAccessor<V,E> pickSupport = visView.getPickSupport();
        E edge = pickSupport.getEdge(visView.getGraphLayout(), p.getX(), p.getY());
        
        return edge;
    }
    
    public boolean selectedElementIsVertex() {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        PickedState<V> pickedVertexState = visView.getPickedVertexState();
        
        int count = pickedVertexState.getPicked().size();
        
        if(count > 0) {
            return true;
        }
        return false;
    }
    
    public boolean selectedElementIsEdge() {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        PickedState<E> pickedEdgeState = visView.getPickedEdgeState();
        
        int count = pickedEdgeState.getPicked().size();
        
        if(count > 0) {
            return true;
        }
        return false;
    }
    
    public int selectedElementsCount() {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        PickedState<V> pickedVertexState = visView.getPickedVertexState();
        PickedState<E> pickedEdgeState = visView.getPickedEdgeState();
        
        int count = pickedVertexState.getPicked().size();
        count = count + pickedEdgeState.getPicked().size();
        
        return count;
    }
    
    public void relocateSelectedVertex(Point p) {
        if(selectedElementIsVertex()) {
            VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
            PickedState<V> pickedVertexState = visView.getPickedVertexState();
            
            Set<V> selected = pickedVertexState.getPicked();
            V vertex = (V)selected.toArray()[0];
            
            
            System.out.println(visViewerPhysical.getRenderContext().getMultiLayerTransformer().transform(new Point(0,0)));
            double x = visViewerPhysical.getRenderContext().getMultiLayerTransformer().transform(new Point(0,0)).getX();
            double y = visViewerPhysical.getRenderContext().getMultiLayerTransformer().transform(new Point(0,0)).getY();
            
            MutableTransformer viewTransformer = visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
            
            Point p2 = new Point((int)((p.getX()-x)/viewTransformer.getScale()), (int)((p.getY()-y)/viewTransformer.getScale()));
            
            visView.getGraphLayout().setLocation(vertex, p2);
            //visView.getGraphLayout().setLocation(vertex, p);
        }
    }
    
    public void unselectAll() {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        PickedState<V> pickedVertexState = visView.getPickedVertexState();
        PickedState<E> pickedEdgeState = visView.getPickedEdgeState();
        
        pickedVertexState.clear();
        pickedEdgeState.clear();
    }
    
    public IStorage.Type getSelectedTab() {
        switch(edit.getActualTab()) {
            case 0: { // physical
                return IStorage.Type.PHYSICAL;
            }
            case 1: { // virtual
                return IStorage.Type.VIRTUAL;
            }
            case 2: { // both
                return null;
            }
            default: {
                Logger.getLogger(TabSwitchedListener.class.getName()).log(Level.SEVERE, "unknown view type");
                return null;
            }
        }
    }
    /*
    public V getPhysicalNearBy(Point p) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        
        Layout<V,E> layout = visView.getGraphLayout();
        Collection<V> verts = visViewerPhysical.getGraphLayout().getGraph().getVertices();
        
        double minDist = Double.MAX_VALUE;
        V minVert = null;
        for(V vert : verts) {
            Point2D p2 = layout.transform(vert);
            double dist = p.distance(p2);
            if(dist < minDist) {
                minDist = dist;
                minVert = vert;
            } 
        }
        return minVert;
    }
    
    public Point getPositionOfVertex(V v) {
        VisualizationViewer<V,E> visView = getActualVisualizationViewer();
        Layout<V,E> layout = visView.getGraphLayout();
        
        return (Point)layout.transform(v);
    }
    */
}
