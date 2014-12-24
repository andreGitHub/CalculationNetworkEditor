/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import inet.CalculationNetworkEditor.Transformers.EdgePaintTransformer;
import inet.CalculationNetworkEditor.Transformers.VertexPaintTransformer;
import inet.CalculationNetworkEditor.visual.contol.listener.MouseAbstraction;
import inet.CalculationNetworkEditor.visual.view.EditorPane;

/**
 *
 * @author andre
 */
public class VisualizationViewerVirtual<V,E> extends VisualizationViewer<V, E>{
    private Graph<V,E> graph = null;
    private Layout<V,E> layout = null;
    private VertexPaintTransformer<V,E> vertexPaintTransformer= null;
    private EdgePaintTransformer<V,E> edgePaintTransformer = null;
    private MouseAbstraction<V,E> mouse = null;
    
    private EditorPane<V,E> edit = null;
    
    //private double zoomFactorVirtual                   = 1.0;
    //private Point2D centerPointVirtual                 = null;
    
    public VisualizationViewerVirtual(
                Graph<V,E> pGraph, 
                VertexPaintTransformer<V,E> pVertexPaintTransformer, 
                EdgePaintTransformer<V,E> pEdgePaintTransformer, 
                EditorPane<V,E> pEdit) {
        super(new CircleLayout<V,E>(pGraph));
        
        getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<V,E>());
        
        graph = pGraph;
        vertexPaintTransformer = pVertexPaintTransformer;
        edgePaintTransformer = pEdgePaintTransformer;
        
        edit = pEdit;
        
        layout = getGraphLayout();
        layout.setSize(edit.getDimensionsVisualizationViewer());
        
        // mousePhysical = new EditingModalGraphMouse(visViewPhysical.getRenderContext(), vertexFactory, edgeFactory);
        // mousePhysical.setMode(ModalGraphMouse.Mode.PICKING);
        setSize(edit.getDimensionsVisualizationViewer());
        setPreferredSize(edit.getDimensionsVisualizationViewer());
        getRenderContext().setVertexFillPaintTransformer(pVertexPaintTransformer);
        getRenderContext().setEdgeDrawPaintTransformer(edgePaintTransformer);
        // visViewPhysical.setGraphMouse(mousePhysical);
        //// addChangeListener(interactions);
    }
    /*
    public double getZoomFactorVirtual() {
        return zoomFactorVirtual;
    }
    
    public void setZoomFactorVirtual(double pZoomFactor) {
        zoomFactorVirtual = pZoomFactor;
    }
    */
    
    public double getScaleFactor() {
        MutableTransformer viewTransformer = getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
        return viewTransformer.getScale();
    }
    
    public void setMouseAbstraction(MouseAbstraction<V,E> pMouse) {
        mouse = pMouse;
        
        addMouseMotionListener(pMouse);
        addMouseListener(pMouse);
        addMouseWheelListener(pMouse);
    }
    
    public void reinitialize(Graph<V,E> pGraph) {
        // System.out.println("editor2 - #verts in storage-Physical: " + logic.getDisplay(IStorage.Type.PHYSICAL).getVertexCount());
        
        graph = pGraph;
        layout = new CircleLayout(graph);
        layout.setSize(edit.getDimensionsVisualizationViewer());
        setGraphLayout(layout);
        setSize(edit.getDimensionsVisualizationViewer());
        setPreferredSize(edit.getDimensionsVisualizationViewer());
        getRenderContext().setVertexFillPaintTransformer(vertexPaintTransformer);
        //mousePhysical = new EditingModalGraphMouse(visViewPhysical.getRenderContext(), vertexFactory, edgeFactory);
        //mousePhysical.setMode(ModalGraphMouse.Mode.PICKING);
        //visViewPhysical.setGraphMouse(mousePhysical);
        // visViewPhysical.addChangeListener(interactions);
        
        //addMouseMotionListener(mouse);
        //addMouseListener(mouse);
        //addMouseWheelListener(mouse);
    }
}
