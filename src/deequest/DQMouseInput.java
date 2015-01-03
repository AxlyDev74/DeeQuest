/*
 * DQMouseInput.java
 * Desmond Packwood dpac011 1611356
 * Date: 02/07/2013
 * 
 * Description: Captures mouse events and stores the x position
 * (currently only x axis used)
 */
package deequest;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener; // Will capture movement of the mouse
	// need to implement full mouse input, with buttons etc too

public class DQMouseInput implements MouseListener {
    
    // ATTRIBUTES //
    // Mouse movements recorded
    private int mouse_x;
    private int mouse_y;
    boolean mouse_clicked;
    private static final int XLEFT = 27;
    private static final int XRIGHT = 68;
    private static final int YBOTTOM = 130;
    private static final int YTOP = 151;
    
    @Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
    	if(e.getID() == MouseEvent.MOUSE_CLICKED){
          this.mouse_x = e.getX(); // update the mouse location
          this.mouse_y = e.getY();
          if((mouse_x > XLEFT)&&(mouse_x < XRIGHT)&&(mouse_y > YBOTTOM)&&(mouse_y < YTOP)){
        	  mouse_clicked = true;
          }
      }
	}
    
    // register of mouse click
    public boolean getClicked(){
    	if(mouse_clicked){
    		mouse_clicked = false;
    		return true;
    	} else {
    		return false;
    	}
    }

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//    public void mouseMoved(MouseEvent e){
//        if(e.getID() == MouseEvent.MOUSE_MOVED){
//            this.mouse_x = e.getX(); // update the mouse location
//            this.mouse_y = e.getY();
//        }
//    }
    
//    @Override
//    public void mouseDragged(MouseEvent e){
//        // Not implemented
//    }
    
    // retrieve the mouse x coordinates
    public synchronized int getMouseX(){
        return this.mouse_x;
    }
    // retrieve the mouse y coordinates
    public synchronized int getMouseY(){
        return this.mouse_y;
    }
}

