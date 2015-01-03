/*
 * DQKeyInput.java
 * Desmond Packwood dpac011 1611356
 * Date: 02/06/2013
 * 
 * Description:
 * This class handles all the keyboard input and updates conditions of the
 * keys when poll() is called.
 */
package deequest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DQKeyInput implements KeyListener {
    
    // ATTRIBUTES //
    // These are the keys currently being implemented
    private int[] keys = { 0,0,0,0,0,0,0 }; // key conditions
    private static final int NOTHING = 0; // off
    private static final int TYPED = 1; // pressed once
    private static final int PRESSED = 2; // held on
    private static final int RELEASED = 3; // just released: condition values
    // keys used, the array index
    private boolean[] keystate =
        { false, false, false, false, false, false, false };
    private static final int ESCAPE = 0;	// Pause
    private static final int RETURN = 1;	// 
    private static final int LEFTARROW = 2;	// Player movement
    private static final int RIGHTARROW = 3;// Player movement
    private static final int UPARROW = 4;	// Player movement
    private static final int DOWNARROW = 5;	// Player movement
    private static final int SPACE = 6;		//
    private static final int NUM_KEYS = 7;
    
    public synchronized int getEscape(){
        return keys[ESCAPE];
    }// end getEscape
    public synchronized int getReturn(){
        return keys[RETURN];
    }// end getmenu
    public synchronized int getLeftArrow(){
        return keys[LEFTARROW];
    }// end getLeftArrow
    public synchronized int getRightArrow(){
         return keys[RIGHTARROW];
    }// end getRightArrow
    public synchronized int getUpArrow(){
        return keys[UPARROW];
    }// end getUpArrow
    public synchronized int getDownArrow(){
        return keys[DOWNARROW];
    }// end getDownArrow
    public synchronized int getSpace(){
        return keys[SPACE];
    }// end getSpace
    
    @Override
    public synchronized void keyTyped(KeyEvent e){
        // Not implemented
    }// end keyTyped
    
    @Override
    public synchronized void keyPressed(KeyEvent e){
        // Uses 'ESC' (pause game), 'ENTER' (menu)
        // Left and Right cursor (non numeric) for player2
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keystate[ESCAPE] = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            keystate[RETURN] = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            keystate[LEFTARROW] = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keystate[RIGHTARROW] = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP) {
            keystate[UPARROW] = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            keystate[DOWNARROW] = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            keystate[SPACE] = true;
        }
    }// end keyPressed
    
    @Override
    public synchronized void keyReleased(KeyEvent e){
        // Uses 'ESC' (pause game), 'ENTER' (menu)
        // Left and Right cursor (non numeric) for player2
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            keystate[ESCAPE] = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            keystate[RETURN] = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            keystate[LEFTARROW] = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keystate[RIGHTARROW] = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP) {
            keystate[UPARROW] = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            keystate[DOWNARROW] = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            keystate[SPACE] = false;
        }
    }// end keyReleased
    
    // Update the keys (called by DQGame every 'tick')
    public synchronized void poll(){
        // updates the state of the keys
        for(int i = 0; i < NUM_KEYS; i++){
            // set the key condition
            if(keystate[i]){
                // the key is active
                if(keys[i] == NOTHING){
                    // the key is now typed 'once'
                    keys[i] = TYPED;
                }
                else if(keys[i] == TYPED){
                    // the key is now held 'more than once'
                    keys[i] = PRESSED;
                }
            }
            else {
                // key is not pressed 'released'
                if(keys[i] == PRESSED){
                    keys[i] = RELEASED;
                }
                else {
                    keys[i] = NOTHING;
                }
            }
        }
    }// end poll
}

