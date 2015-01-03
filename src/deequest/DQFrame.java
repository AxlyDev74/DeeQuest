/*
 * DQFrame.java
 * Desmond Packwood dpac011 1611356
 * Date: 02/07/2013
 */
package deequest;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DQFrame extends JFrame {
	// ATTRIBUTES //
    // Input Listeners that are attached to this Frame
    private DQKeyInput keyboard = new DQKeyInput();
    private DQMouseInput mouse = new DQMouseInput();
    private DQGame game; // the game panel
    
    // CONSTRUCTOR //
    public DQFrame() {
        this.game = new DQGame(keyboard, mouse);
        setTitle("DeeQuest");
        setLocationByPlatform(true); // window location default to OS
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(keyboard);
        addMouseListener(mouse);       
        setResizable(true);
        setVisible(true);
    }// end constructor
    
    // Start the game
    public void start(){
        add(game); // add the game to the frame
        pack(); // fits the panel to the minimum size of the panel 
        // the game will be started now, which hands control to the DFGame panel
        game.start(); // this will run as long as the game is 'on'
        dispose(); // closes the game
    }// end start
}
