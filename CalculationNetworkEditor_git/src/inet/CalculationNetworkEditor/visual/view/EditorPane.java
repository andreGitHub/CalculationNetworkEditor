/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.view;

import inet.CalculationNetworkEditor_Stacking.visual.control.listener.ComponentResizedListener;
import edu.uci.ics.jung.graph.Graph;
import inet.CalculationNetworkEditor.Logic.ILogic;
import inet.CalculationNetworkEditor.Logic.Logic;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.Transformers.EdgePaintTransformer;
import inet.CalculationNetworkEditor.Transformers.VertexPaintTransformer;
import inet.CalculationNetworkEditor.visual.contol.controller.BackendController;
import inet.CalculationNetworkEditor_Stacking.visual.control.listener.TabSwitchedListener;
import inet.CalculationNetworkEditor_Stacking.visual.control.listener.MouseAbstraction;
import inet.CalculationNetworkEditor.visual.contol.controller.ViewController;
import inet.CalculationNetworkEditor_Stacking.visual.control.listener.EditingPanelsListener;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerBoth;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerPhysical;
import inet.CalculationNetworkEditor.visual.view.tabbedPane.VisualizationViewer.VisualizationViewerVirtual;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author andre
 */
public class EditorPane<V,E> extends JPanel implements PanelResetable<V,E> {
/*
    public enum View {PHYSICAL, VIRTUAL, BOTH};
    private View view = View.PHYSICAL;
    
    private CircleLayout<V, E> layoutPhysical = null;
    private CircleLayout<V, E> layoutVirtual = null;
    private CircleLayout<V, E> layoutBoth = null;
*/
    private JPanel editorPanel = null;
    private JPanel rightPanel = null;
    
    private VisualizationViewerPhysical<V, E> visViewPhysical = null;
    private VisualizationViewerVirtual<V, E> visViewVirtual = null;
    private VisualizationViewerBoth<V, E> visViewBoth = null;

    private VertexPaintTransformer<V,E> vertexPaintTransformer = null;
    private EdgePaintTransformer<V,E> edgePaintTransformer = null;
    
    private MouseAbstraction<V,E> mouseAbstraction = null;
    private EditingPanelsListener<V,E> editingPanelsListener = null;
    
    private ViewController<V,E> vc = null;
    private BackendController<V,E> bc = null;
    
    private JTabbedPane tabbedPane = null;
    private TabSwitchedListener tabSwitchListener = null;
    
    private ILogic<V,E> logic = null;
    
    private Factory<V> vertexFactory = null;
    private Factory<E> edgeFactory = null;
    
    private Dimension actDimension = null;
    
    public EditorPane(
                    List<Graph<V,E>> phys, List<Graph<V,E>> virts,
                    HashMap<V,V> stackVertex, HashMap<E,List<E>> stackEdge, 
                    HashMap<V, Double> vertexRessources, HashMap<E, Double> edgeRessources,
                    Factory<V> pVertexFactory, Factory<E> pEdgeFactory,
                    Dimension pActDimension) {
        super();
        actDimension = pActDimension;
        
        Dimension tabbedDimensions = new Dimension((int)pActDimension.getWidth()-200,(int)pActDimension.getHeight());
        
        setSize(pActDimension);
        setPreferredSize(pActDimension);
        setLayout(new BorderLayout());
        
        vertexFactory = pVertexFactory;
        edgeFactory = pEdgeFactory;
        
        logic = new Logic<V,E>();
        logic.init(phys, virts, stackVertex, stackEdge, vertexRessources, edgeRessources);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setSize(tabbedDimensions);
        tabbedPane.setPreferredSize(tabbedDimensions);
        add(tabbedPane, BorderLayout.CENTER);
        rightPanel = new JPanel();
        rightPanel.setSize(200,500);
        rightPanel.setPreferredSize(new Dimension(200,500));
        add(rightPanel,BorderLayout.LINE_END);
        
        vertexPaintTransformer = new VertexPaintTransformer<V,E>(logic, tabbedPane);
        edgePaintTransformer = new EdgePaintTransformer<V,E>(logic, tabbedPane);
        
        // init VisualisationViewer
        visViewPhysical = new VisualizationViewerPhysical<V,E>(
                        logic.getDisplay(IStorage.Type.PHYSICAL),
                        vertexPaintTransformer, edgePaintTransformer,
                        this);
        
        visViewVirtual = new VisualizationViewerVirtual<V,E>(
                        logic.getDisplay(IStorage.Type.VIRTUAL),
                        vertexPaintTransformer, edgePaintTransformer,
                        this);
        
        visViewBoth = new VisualizationViewerBoth<V,E>(
                        logic.getDisplay(null),
                        vertexPaintTransformer, edgePaintTransformer,
                        this);
        
        vc = new ViewController<V,E>(this, visViewPhysical, visViewVirtual, visViewBoth);
        bc = new BackendController<V,E>(logic, pVertexFactory, pEdgeFactory);
        
        
        mouseAbstraction = new MouseAbstraction<V,E>(vc, bc, this);
        editingPanelsListener = new EditingPanelsListener<V,E>(this,bc);
        
        
        visViewPhysical.setMouseAbstraction(mouseAbstraction);
        visViewVirtual.setMouseAbstraction(mouseAbstraction);
        visViewBoth.setMouseAbstraction(mouseAbstraction);
        
        /*
        visViewPhysical.setGraphMouse(mgm);
        visViewVirtual.setGraphMouse(mgm);
        visViewBoth.setGraphMouse(mgm);
        */
        
        vertexPaintTransformer.setVisualizationViewers(visViewPhysical, visViewVirtual, visViewBoth);
        edgePaintTransformer.setVisualizationViewers(visViewPhysical, visViewVirtual, visViewBoth);
        
        /*
        deprecatedMML = new Controller(
                        layoutPhysical, layoutVirtual, layoutBoth,
                        visViewPhysical, visViewVirtual, visViewBoth,
                        tabbedPane, logic, physGraph, virtGraph,
                        vertexFactory, edgeFactory); //,
        //                mousePhysical, mouseVirtual, mouseBoth);
        */
        
        //visViewStateChangedListener = new VisualViewerStateChangedListener(visViewPhysical, visViewVirtual, visViewBoth);
        tabSwitchListener = new TabSwitchedListener(this,tabbedPane);//,deprecatedMML);
        
        reinitializeTabPane();
        
        addComponentListener(new ComponentResizedListener(tabbedPane, this));
        add(tabbedPane, BorderLayout.CENTER);
        //add(vv);
    }
    
    /*
    public void setEditingPanel(JPanel panel){
        remove(rightPanel);
        rightPanel = panel;
        add(rightPanel, BorderLayout.LINE_END);
        Insets insets = getInsets();
        rightPanel.setBounds(1010 + insets.left, 10+insets.top, 200, 300);
        rightPanel.setSize(200, 300);
        rightPanel.setPreferredSize(new Dimension(200,300));
        
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 400, 10));
    }
    */
    
    
    // everything to create a Stacking Edge Panel
    private JComboBox<E> allPhysicalEdgeJCB = null;
    private DefaultListModel<E> defaultListModel = null;
    private JList<E> pathList = null;
    private E phyE = null;
    private E virtE = null;
    
    @Override
    public E getStackingEdgeVirt() {
        return virtE;
    }
    
    @Override
    public List<E> getStackingEdgePhy() {
        if(pathList == null) {
            return null;
        }
        
        List<E> ret = new ArrayList<E>();
        for(int i = 0; i<pathList.getModel().getSize(); i++) {
            ret.add(pathList.getModel().getElementAt(i));
        }
        return ret;
    }
    
    public void addStackingEdgePhyToPath() {
        if(allPhysicalEdgeJCB == null || pathList == null || defaultListModel == null) {
            return;
        }
        
        Object selected = allPhysicalEdgeJCB.getSelectedItem();
        E selectedEdge = (E)selected;
        allPhysicalEdgeJCB.removeItem(selected);
        
        defaultListModel.addElement(selectedEdge);
    }
    
    public void removeStackingEdgePath() {
        if(pathList == null || defaultListModel == null) {
            return;
        }
        
        if(pathList.getSelectedIndex()>=0) {
            E selected = pathList.getSelectedValue();
            defaultListModel.removeElementAt(pathList.getSelectedIndex());
            allPhysicalEdgeJCB.addItem(selected);
        }
    }
    
    @Override
    public void setStackingEdgePanel(E e, Collection<E> allPhysicalCol) {
        virtE = e;
        
        remove(rightPanel);
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        add(rightPanel, BorderLayout.LINE_END);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        rightPanel.setSize(200,(int)actDimension.getHeight()-100);
        rightPanel.setPreferredSize(new Dimension(200,500));
        
        // add Content Stacking-Label
        addContentToStackingEdgePanel(rightPanel, e, allPhysicalCol);
    }
    
    private void addContentToStackingEdgePanel(JPanel rightPanel, E e, Collection<E> allPhysicalCol) {
        //caption
        JLabel caption = new JLabel("Map: " + e);
        caption.setSize(new Dimension(180,20));
        caption.setPreferredSize(new Dimension(180,20));
        rightPanel.add(caption,BorderLayout.NORTH);
        
        //to
        JPanel toPanel = new JPanel();
        toPanel.setLayout(new BorderLayout());
        rightPanel.add(toPanel, BorderLayout.CENTER);
        JLabel to = new JLabel("To:");
        to.setSize(new Dimension(180,20));
        to.setPreferredSize(new Dimension(180,20));
        toPanel.add(to, BorderLayout.NORTH);
        
        // buttons edge button panel
        JPanel buttonsEdgePanel = new JPanel();
        buttonsEdgePanel.setLayout(new BorderLayout());
        toPanel.add(buttonsEdgePanel, BorderLayout.CENTER);
        

        // path listPanel
        //JPanel pathListPanel = new JPanel();
        //pathListPanel.setLayout(new BorderLayout);
        
        defaultListModel = new DefaultListModel<E>();
        pathList = new JList<E>(defaultListModel);
        pathList.setSize(180,100);
        pathList.setPreferredSize(new Dimension(180,100));
        buttonsEdgePanel.add(pathList, BorderLayout.CENTER);
        
        
        
        // add remove edge button panel
        JPanel addRemoveEdgePanel = new JPanel();
        addRemoveEdgePanel.setLayout(new BorderLayout());
        buttonsEdgePanel.add(addRemoveEdgePanel, BorderLayout.SOUTH);
        
        // add Button
        JButton addButton = new JButton("add");
        addRemoveEdgePanel.add(addButton, BorderLayout.LINE_START);
        addButton.addActionListener(editingPanelsListener);
        addButton.setSize(new Dimension(70,20));
        addButton.setPreferredSize(new Dimension(70,20));
        
        // remove Button
        JButton removeButton = new JButton("remove");
        addRemoveEdgePanel.add(removeButton, BorderLayout.LINE_END);
        removeButton.addActionListener(editingPanelsListener);
        removeButton.setSize(new Dimension(90,20));
        removeButton.setPreferredSize(new Dimension(90,20));
                
        // add drop done box with all physical edges
        allPhysicalEdgeJCB = new JComboBox<E>();
        for(E edge : allPhysicalCol) {
            if(!defaultListModel.contains(edge)) {
                allPhysicalEdgeJCB.addItem(edge);
            }
        }
        toPanel.add(allPhysicalEdgeJCB, BorderLayout.SOUTH);
        allPhysicalEdgeJCB.setSize(new Dimension(180,30));
        allPhysicalEdgeJCB.setPreferredSize(new Dimension(180,30));
        allPhysicalEdgeJCB.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // JButton stack
        JButton stack = new JButton("map");
        stack.addActionListener(editingPanelsListener);
        rightPanel.add(stack,BorderLayout.SOUTH);
    }
    
    @Override
    public void setStackingEdgeActualStackToEdge(E e) {
        if(e == null) {
            return;
        }
        
        if(allPhysicalEdgeJCB == null) {
            return;
        }
        
        phyE = e;
        
        for(int i = 0; i < allPhysicalEdgeJCB.getItemCount(); i++) {
            if(allPhysicalEdgeJCB.getModel().getElementAt(i).equals(e)) {
                allPhysicalEdgeJCB.setSelectedIndex(i);
                break;
            }
        }
    }
    // end creating a stacking edge panel
    
    
    // everything to create a Stacking Vertex Panel
    private JComboBox<V> allPhysicalVertexJCB = null;
    private V phyV = null;
    private V virtV = null;
    
    @Override
    public V getStackingVertVirt() {
        return virtV;
    }
    
    @Override
    public V getStackingVertPhy() {
        if(phyV != null) {
            return phyV;
        }
        if(allPhysicalVertexJCB != null) {
            return (V)allPhysicalVertexJCB.getModel().getSelectedItem();
        } else {
            return null;
        }
    }
    
    @Override
    public void setStackingVertexPanel(V v, Collection<V> allPhysical) {
        // empty Panel added
        virtV = v;
        
        remove(rightPanel);
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        add(rightPanel, BorderLayout.LINE_END);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        rightPanel.setSize(200,500);
        rightPanel.setPreferredSize(new Dimension(200,500));
        
        // add Content Stacking-Label
        addContentToStackingVertexPanel(rightPanel, v, allPhysical);
    }
    
    private void addContentToStackingVertexPanel(JPanel rightPanel, V v, Collection<V> allPhysicalCol) {
        //caption
        JLabel caption = new JLabel("Map: " + v);
        caption.setSize(new Dimension(180,20));
        caption.setPreferredSize(new Dimension(180,20));
        rightPanel.add(caption,BorderLayout.NORTH);
        
        //to
        JPanel toPanel = new JPanel();
        toPanel.setLayout(new BorderLayout());
        rightPanel.add(toPanel, BorderLayout.CENTER);
        JLabel to = new JLabel("To:");
        to.setSize(new Dimension(180,20));
        to.setPreferredSize(new Dimension(180,20));
        toPanel.add(to, BorderLayout.NORTH);
        
        //all physical JComboBox
        allPhysicalVertexJCB = new JComboBox<V>();
        for(V vert : allPhysicalCol) {
            allPhysicalVertexJCB.addItem(vert);
        }
        JPanel allPhysicalPanel = new JPanel();
        toPanel.add(allPhysicalPanel, BorderLayout.CENTER);
        allPhysicalPanel.setLayout(new BorderLayout());
        allPhysicalPanel.add(allPhysicalVertexJCB, BorderLayout.NORTH);
        allPhysicalVertexJCB.setSize(new Dimension(180,20));
        allPhysicalVertexJCB.setPreferredSize(new Dimension(180,20));
        
        // JButton stack
        JButton stack = new JButton("map");
        stack.addActionListener(editingPanelsListener);
        rightPanel.add(stack,BorderLayout.SOUTH);
    }
    
    @Override
    public void setStackingVertexActualStackToVertex(V v) {
        if(v == null) {
            return;
        }
        
        if(allPhysicalVertexJCB == null) {
            return;
        }
        
        phyV = v;
        
        for(int i = 0; i < allPhysicalVertexJCB.getItemCount(); i++) {
            if(allPhysicalVertexJCB.getModel().getElementAt(i).equals(v)) {
                allPhysicalVertexJCB.setSelectedIndex(i);
                break;
            }
        }
    }
    
    // end creating a stacking vertex panel
    
    
    
    // everything to create a Resource editing Panel
    private JTextField resInput = null;
    private V vert = null;
    private E edge = null;
    
    @Override
    public double getEditingResources() {
        return Double.parseDouble(resInput.getText());
    }
    
    @Override
    public V getEditingResourceVertex() {
        return vert;
    }

    @Override
    public E getEditingResourceEdge() {
        return edge;
    }
    
    @Override
    public void setEditingResourcePanel(V v, E e, String editRes, double actRes) {
        vert = v;
        edge = e;
        
        remove(rightPanel);
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        add(rightPanel, BorderLayout.LINE_END);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 500, 10));
        rightPanel.setSize(200,500);
        rightPanel.setPreferredSize(new Dimension(200,500));
        
        // add Content Stacking-Label
        addContentToEditingResourcePanel(rightPanel, v, e, editRes, actRes);
    }
    
    private void addContentToEditingResourcePanel(JPanel rightPanel, V v, E e, String editRes, double actRes) {
        //caption
        JLabel caption = null;
        if("bandwidth".equals(editRes)) {
            caption = new JLabel("<html>Edit: " + e + "<br>" + editRes + ":</html>");
        } else {
            caption = new JLabel("<html>Edit: " + v + "<br>" + editRes + ":</html>");
        }
        
        caption.setSize(new Dimension(180,38));
        caption.setPreferredSize(new Dimension(180,38));
        rightPanel.add(caption,BorderLayout.NORTH);
        
        JPanel inputPanel= new JPanel();
        inputPanel.setLayout(new BorderLayout());
        rightPanel.add(inputPanel, BorderLayout.CENTER);
        resInput = new JTextField(Double.toString(actRes));
        inputPanel.add(resInput, BorderLayout.NORTH);
        
        // JButton stack
        JButton ok = new JButton("OK");
        ok.addActionListener(editingPanelsListener);
        rightPanel.add(ok,BorderLayout.SOUTH);
    }
    // end creating a Resource editing Panel
    
    
    
    public void clearPanel() {
        remove(rightPanel);
        rightPanel = new JPanel();
        add(rightPanel, BorderLayout.LINE_END);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 500, 10));
        rightPanel.setSize(200,500);
        rightPanel.setPreferredSize(new Dimension(200,500));
        
        // reset Variables
        // stack edge:
        allPhysicalEdgeJCB = null;
        defaultListModel = null;
        pathList = null;
        phyE = null;
        virtE = null;
        // stack vertex:
        allPhysicalVertexJCB = null;
        phyV = null;
        virtV = null;
        // edit resources
        resInput = null;
        vert = null;
        edge = null;
    }
    
    
    public Dimension getDimensionsVisualizationViewer() {
        return new Dimension((int)getSize().getWidth()-200, (int)getSize().getHeight()-25);
    }
    
    public Graph<V,E> reinitializePhysical() {
        Graph<V,E> phys = logic.getDisplay(IStorage.Type.PHYSICAL);
        visViewPhysical.reinitialize(phys);
        reinitializeTabPane();
        
        return phys;
    }
    
    public Graph<V,E> reinitializeVirtual() {
        Graph<V,E> virt = logic.getDisplay(IStorage.Type.VIRTUAL);
        visViewVirtual.reinitialize(virt);
        reinitializeTabPane();
        
        return virt;
    }
    
    public void reinitializeBoth() {
        visViewBoth.reinitialize(logic.getDisplay(null));
        reinitializeTabPane();
    }
    
    private void reinitializeTabPane() {
        int selected = tabbedPane.getSelectedIndex();
        
        tabbedPane.setSize(new Dimension((int)getSize().getWidth(), (int)getSize().getHeight()));
        tabbedPane.setPreferredSize(new Dimension((int)getSize().getWidth(), (int)getSize().getHeight()));
        
        tabbedPane.removeChangeListener(tabSwitchListener);
        tabbedPane.removeAll();
        
        tabbedPane.addTab("Physical", visViewPhysical);
        tabbedPane.addTab("Virtual", visViewVirtual);
        tabbedPane.addTab("Both", visViewBoth);
        if(selected >= 0 && selected <= 2) {
            tabbedPane.setSelectedIndex(selected);
        }
        
        tabbedPane.addChangeListener(tabSwitchListener);
    }
    
    public int getActualTab() {
        return tabbedPane.getSelectedIndex();
    }
}
