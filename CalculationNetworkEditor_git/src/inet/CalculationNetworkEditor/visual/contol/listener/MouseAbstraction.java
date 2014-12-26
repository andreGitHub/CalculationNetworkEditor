/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.CalculationNetworkEditor.visual.contol.listener;

import inet.CalculationNetworkEditor.visual.contol.controller.ViewController;
import inet.CalculationNetworkEditor.visual.contol.controller.BackendController;
import inet.CalculationNetworkEditor.Storage.IStorage;
import inet.CalculationNetworkEditor.visual.view.PanelResetable;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author andre
 */
public class MouseAbstraction<V,E> implements MouseMotionListener, MouseListener, MouseWheelListener  {
    
    private final double scrollFactor = 0.9;
    private final int stackDistance = 50;
    
// vars for scrolling
    private int stateBUTTON = -1;
    private Point2D lastMousePos = null;
    
    
    private Point posFrom = null;
    
// vars for dragging (left MouseButton)
    private V dragged = null;
    private boolean drag = false;
    
// vars for add edge
    private boolean addEdge = false;
    
    private BackendController<V,E> bc = null;
    private ViewController<V,E> vc = null;
    
    private PanelResetable<V,E> panelResetable = null;
    
    MouseAbstractionContextMenuActionListener macmal = null;
    
    public MouseAbstraction(ViewController<V,E> pVC, BackendController<V,E> pBC, PanelResetable<V,E> pPanelResetable) {
        vc = pVC;
        bc = pBC;
        
        panelResetable = pPanelResetable;
        
        macmal = new MouseAbstractionContextMenuActionListener(bc, vc, panelResetable);
    }
    
    /*
    use cases to implement:
        physical + virtual graph:
            done    add:            Vertex                      - editing: left_pressed
            done    add:            Edge                        - editing: drag
            done    select:         Veretx                      - picking: left_pressed
            done    select:         Edge                        - picking: left_pressed
            done    move:           Veretx                      - picking: left_pressed -> drag -> release
            done    delete:         Vertex                      - editing/picking/transforming: right_pressed -> context_menu -> click_delete_vertex
            done    delete:         Edge                        - editing/picking/transforming: right_pressed -> context_menu -> click_delete_vertex
            done    edit recources: Vertex                      - (not supported out of the box) right_pressed -> context_menu -> click_edit -> input_box -> ok
            done    edit recources: edge                        - (not supported out of the box) right_pressed -> context_menu -> click_edit -> input_box -> ok

        both:
            done    select:         stacked Vertex
            done    select:         unstacked Vertex    
            done    move:           stacked Vertex
            done    move:           unstacked Vertex
            done    stack:          unstacked Vertex
                    stack:          unstacked Edge
            done    unstack:        stacked Veretx
                    unstack:        stacked Edge
    */
    
    public void showBothStackingVertexMenu(Point p, Component c) {
        V actVertex = vc.getVertexAtPoint(p);
        
        if(bc.getTypeOfVertex(actVertex).equals(IStorage.Type.VIRTUAL)) {
            MouseAbstractionContextMenuActionListener macmal = new MouseAbstractionContextMenuActionListener(bc, vc, panelResetable);
            macmal.setActVeretxForStacking(actVertex, bc.getAllVertexPhysical());
        
            String elementName = (String)actVertex;
            
            JPopupMenu pop = new JPopupMenu();
            JMenuItem name = new JMenuItem(elementName);
            pop.add(name);
            pop.addSeparator();
            
            if(bc.isStackedToVertex(actVertex)) {
                // show unstack menu
                JMenuItem unstack = new JMenuItem("unmap");
                unstack.addActionListener(macmal);
                pop.add(unstack);
            } else {
                // show stacking menu
                JMenuItem stack = new JMenuItem("map");
                stack.addActionListener(macmal);
                pop.add(stack);
            }
            
            pop.show(c, (int)p.getX(), (int)p.getY());
        } else if(bc.getTypeOfVertex(actVertex).equals(IStorage.Type.PHYSICAL)) {
            String elementName = (String)actVertex;
            
            JPopupMenu pop = new JPopupMenu();
            JMenuItem name = new JMenuItem(elementName);
            pop.add(name);
            
            pop.show(c, (int)p.getX(), (int)p.getY());
        }
    }
    
    public void showBothStackingEdgeMenu(Point p, Component c) {
        E actEdge = vc.getEdgeAtPoint(p);
        
        String elementName = (String)actEdge;
            
        JPopupMenu pop = new JPopupMenu();
        JMenuItem name = new JMenuItem(elementName);
        pop.add(name);
            
        if(bc.getTypeOfEdge(actEdge).equals(IStorage.Type.VIRTUAL)) {
            MouseAbstractionContextMenuActionListener macmal = new MouseAbstractionContextMenuActionListener(bc, vc, panelResetable);
            macmal.setActEdgeForStacking(actEdge, bc.getAllEdgePhysical());
        
            pop.addSeparator();
            
            if(bc.isStackedToEdge(actEdge)) {
                // show unstack menu
                JMenuItem unstack = new JMenuItem("unmap");
                unstack.addActionListener(macmal);
                pop.add(unstack);
            } else {
                // show stacking menu
                JMenuItem stack = new JMenuItem("map");
                stack.addActionListener(macmal);
                pop.add(stack);
            }
        } else if(bc.getTypeOfEdge(actEdge).equals(IStorage.Type.PHYSICAL)) {
            // nothing else to do for physical edges
        }
        pop.show(c, (int)p.getX(), (int)p.getY());
    }
    
    public void showPhyVirtEditingContextMenu(Point p, Component c) {
        if(vc.elementAtPositionIsVertex(p)) {
            macmal.setActVeretxForStacking(vc.getVertexAtPoint(p), bc.getAllVertexPhysical());
        } else if(vc.elementAtPositionIsEdge(p)) {
            macmal.setActEdge(vc.getEdgeAtPoint(p));
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "unknown graph element");
        }
                
        String elementName = null;
        JPopupMenu pop = new JPopupMenu();
        
        
        if(vc.elementAtPositionIsEdge(p)) {
            E edge = vc.getEdgeAtPoint(p);
            elementName = (String)edge;
            JMenuItem name = new JMenuItem(elementName);
            
            JMenuItem edit = new JMenuItem("edit");
            edit.addActionListener(macmal);
            JMenuItem del = new JMenuItem("delete");
            del.addActionListener(macmal);
            pop.add(name);
            pop.addSeparator();
            pop.add(edit);
            pop.add(del);
            pop.show(c, (int)p.getX(), (int)p.getY());
        } 
        if(vc.elementAtPositionIsVertex(p)) {
            V vert = vc.getVertexAtPoint(p);
            elementName = (String)vert;
            JMenuItem name = new JMenuItem(elementName);
            
            //    JMenuItem name = new JMenuItem("<html><u>name: " + elementName + "</u></html>");
            JMenuItem edit = new JMenuItem("edit");
            edit.addActionListener(macmal);
            JMenuItem del = new JMenuItem("delete");
            del.addActionListener(macmal);
            pop.add(name);
            pop.addSeparator();
            pop.add(edit);
            pop.add(del);
        }
        pop.show(c, (int)p.getX(), (int)p.getY());
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(stateBUTTON == MouseEvent.BUTTON1) {
            drag = true;
            
            //if(vc.selectedElementsCount() > 0) {
            if(vc.isSelected(posFrom) || dragged != null) {
                // drag vertex
                
                if(dragged == null) {
                    if(vc.selectedElementsCount() > 0 && vc.selectedElementIsVertex() && vc.isSelected(e.getPoint())) {
                        dragged = vc.getVertexAtPoint(e.getPoint());
                    }
                } else {
                    // drag vertex
                    vc.relocateSelectedVertex(e.getPoint());
                }
            } else { // !vc.isSelected(posFrom) || dragged == null
                // add edge
                addEdge = true;
            }
        }
        if(stateBUTTON == MouseEvent.BUTTON2) {
            vc.move(e.getPoint().getX()-lastMousePos.getX(), e.getPoint().getY()-lastMousePos.getY());
            lastMousePos = e.getPoint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            // add vertex or select vertex/edge
            if(vc.isElementAt(e.getPoint())) {
                // select
                vc.selectElementAt(e.getPoint());
                if(vc.elementAtPositionIsVertex(e.getPoint())) {
                    macmal.setStackToVertex(vc.getVertexAtPoint(e.getPoint()));
                } else if(vc.elementAtPositionIsEdge(e.getPoint())) {
                    macmal.setStackToEdge(vc.getEdgeAtPoint(e.getPoint()));
                }
            } else {
                // add Vertex
                vc.unselectAll();
                // System.out.println("mouse clicked: BUTTON1 - addVertex()");
                V v = bc.addVertex(vc.getSelectedTab());
                vc.addVertex(e.getPoint(), v);
            }
            
            stateBUTTON = -1;
            posFrom = null;
        } else if(e.getButton() == MouseEvent.BUTTON2) {
            // System.out.println("mouse clicked: BUTTON2");
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("mouse clicked: BUTTON3");
            
            if(vc.isElementAt(e.getPoint())) {
                if(vc.getSelectedTab() == IStorage.Type.PHYSICAL || vc.getSelectedTab() == IStorage.Type.VIRTUAL) {
                    showPhyVirtEditingContextMenu(e.getPoint(), e.getComponent());
                } else {
                    if(vc.elementAtPositionIsVertex(e.getPoint())){
                        showBothStackingVertexMenu(e.getPoint(), e.getComponent());
                    } else if(vc.elementAtPositionIsEdge(e.getPoint())) {
                        showBothStackingEdgeMenu(e.getPoint(), e.getComponent());
                    }
                }
            }
        } else if(e.getButton() == MouseEvent.NOBUTTON) {
            System.out.println("mouse clicked: NoBUTTON");
        } else {
            System.out.println("mouse clicked: mouse button not specified");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            // System.out.println("mouse pressed: BUTTON1 " + MouseEvent.BUTTON1);
            stateBUTTON = e.getButton();
            posFrom = e.getPoint();
            
            if(vc.isElementAt(e.getPoint()) && vc.elementAtPositionIsVertex(e.getPoint())) {
                if(!vc.isSelected(e.getPoint())) {
                    // act pos-element not selected + element vertex + drag -> maybe add edge
                    posFrom = e.getPoint();
                } else {
                    // element at mousePos is selected + element vertex + drag -> relocate vertex
                    dragged = vc.getVertexAtPoint(e.getPoint());
                }
            } else {
                // no element at position - maybe add vertex
            }
        } else if(e.getButton() == MouseEvent.BUTTON2) {
//            System.out.println("mouse pressed: BUTTON2 " + MouseEvent.BUTTON2);
            lastMousePos = e.getPoint();
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("mouse pressed: BUTTON3 " + MouseEvent.BUTTON3);
        } else if(e.getButton() == MouseEvent.NOBUTTON) {
            System.out.println("mouse pressed: NoBUTTON " + MouseEvent.NOBUTTON);
        } else {
            System.out.println("mouse pressed: mouse button not specified");
        }
        stateBUTTON = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            //System.out.println("mouse released: BUTTON1");
            
            if(     posFrom != null && !vc.isSelected(posFrom) &&
                    vc.isElementAt(posFrom) && vc.isElementAt(e.getPoint()) &&
                    vc.elementAtPositionIsVertex(posFrom) && vc.elementAtPositionIsVertex(e.getPoint())) {
                // dragged from position exists, dragged from position not selected
                // there is an element at from and to position
                // elements at from and to positions are vertices
                
                V from = vc.getVertexAtPoint(posFrom);
                V to = vc.getVertexAtPoint(e.getPoint());
                
                E edge = bc.addEdge(from, to);
                
                if(edge != null) {
                    vc.addEdge(edge, from, to);
                }
            }
            
            stateBUTTON = -1;
            posFrom = null;
            dragged = null;
            drag = false;
        } else if(e.getButton() == MouseEvent.BUTTON2) {
//            System.out.println("mouse released: BUTTON2");
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("mouse released: BUTTON3");
        } else if(e.getButton() == MouseEvent.NOBUTTON) {
            System.out.println("mouse released: NoBUTTON");
        } else {
            System.out.println("mouse released: mouse button not specified");
        }
        
        lastMousePos = null;
        stateBUTTON = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // System.out.println("mouse entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // System.out.println("mouse exited");
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("mouse wheel moved - scroll: " + e.getPreciseWheelRotation());// e.getWheelRotation());
        
        double zooming = 1.0;
        if(e.getPreciseWheelRotation() < 0) {
            zooming = 1.0 / scrollFactor;
        } else  {
            zooming = 1.0 * scrollFactor;
        }
        
        vc.zoom(zooming);
    }
}
