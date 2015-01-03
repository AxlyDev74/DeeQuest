/*
 * DFGame.java
 * Desmond Packwood dpac011 1611356
 * Date: 31/05/2013
 * 
 * Description: This is the game class which instantiates all the game
 * entity threads. The main loop in this class (running) maintains timing
 * for all the threads.
 */
package deequest;

import javax.swing.*; // for message dialog

import java.awt.*; // for graphics
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService; // both for Threading

@SuppressWarnings("serial")
public class DQGame extends JPanel {
    
    // ATTRIBUTES //
    private final static long MAIN_LOOP_TICK = 15; // milliseconds
    public DQKeyInput keyboard;
    public DQMouseInput mouse; // listeners
    // new for testing
    boolean button_clicked;
    public PlayerGridData gridData; // the game data
    //public DFAudio audio; // audio manager
    private ExecutorService threadExecutor; // thread pool manager
    private long timestamp = 0; // used for timing main loop
    // Game Threaded classes (runnable objects)
    //private DFMenu menu;
    //private DFUIBar uibar;
    //private DFPlayer player;
    //public DFPlatform platform;
    //public DFGhost ghost;
    //private DFBackground background; // all runnable threads
    private boolean[] threads = {false,false,false,false,false,false};
    private final static int NUM_THREADS = 6;
    public final static int UIBAR_THREAD = 0; // no longer threaded
    public final static int PLAYER_THREAD = 1;
    public final static int PLATFORM_THREAD = 2;
    public final static int GHOST_THREAD = 3;
    public final static int BACKGROUND_THREAD = 4;
    public final static int MENU_THREAD = 5; // thread index
    // Game state attributes, for game modes etc
    private boolean game_on = false;
    private boolean end_on = false;
    private boolean game_over = false;
    private boolean menu_on = false;
    private boolean entities_on = false;
    private boolean pause_on = false;
    private int level_step = 1;
    private final static int LEVEL_STEPSIZE = 50;
        // step size for level increase with player scores
    
    // CONSTRUCTOR //
    public DQGame(DQKeyInput new_keyboard, DQMouseInput new_mouse){
        this.keyboard = new_keyboard;
        this.mouse = new_mouse;
        this.button_clicked = false;
        this.threadExecutor = Executors.newCachedThreadPool(); // creates the thread pool
        this.gridData = new PlayerGridData(40,320,240,64);
        //this.audio = new DFAudio();
        //this.menu = new DFMenu(this);
        //this.uibar = new DFUIBar(this);
        //this.player = new DFPlayer(this);
        //this.platform = new DFPlatform(this);
        //this.ghost = new DFGhost(this);
        //this.background = new DFBackground(this);
        setPreferredSize(new Dimension(640,480)); // constant game display size
        setMinimumSize(new Dimension(640,480)); // minimum size for the game panel   
    }// end constructor
    
    // This method sets all the thread flags to true, enabling thread execution
    private synchronized void resetThreads(){
        for(int i = 0; i < NUM_THREADS; i++){
            threads[i] = true;
        }
    }// end resetThreads
    // This method sets all the thread flags to false, disabling thread execution
    private synchronized void disableThreads(){
        for(int i = 0; i < NUM_THREADS; i++){
            threads[i] = false;
        }
    }// end disableThreads
    // This method returns the indexed thread flag, then sets the flag to false
    // allowing only a one time execution of the thread
    public synchronized boolean getThread(int i){
        if((i >= 0) && (i < NUM_THREADS)){
            if(this.threads[i]){
                this.threads[i] = false;
                return true; // return true for desired thread enable signal
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }// end getThread
    // This method sets the game state on/off
    public synchronized void setGame(boolean state){
        if(state){ // sets menu
            this.game_on = true;
            this.end_on = false;
            this.game_over = false;
            this.menu_on = true;
            this.entities_on = false;
            this.pause_on = true;
        }
        else { // sets entities
            this.game_on = false;
            this.end_on = false;
            this.game_over = true;
            this.menu_on = false;
            this.entities_on = false;
            this.pause_on = false; // all off
        }
    }// end setGame
    public synchronized boolean getGame(){
        return this.game_on;
    }// end get game
    // This method sets the end game state on/off
    public synchronized void setEndOn(boolean state){
        if(state){ // sets menu
            this.end_on = true;
            this.menu_on = false;
            this.entities_on = true;
            this.pause_on = true;
        }
        else { // sets entities
            this.end_on = false;
            this.menu_on = false;
            this.entities_on = true;
            this.pause_on = true;
        }
    }// end setEndOn
    public synchronized boolean getEndOn(){
        return this.end_on;
    }// end getEndOn
    public synchronized void setGameOver(boolean state){
        this.game_over = state;
    }
    public synchronized boolean getGameOver(){
        return this.game_over;
    }
    // This method sets the menu game state on/off
    public synchronized void setMenuOn(boolean state){
        if(state){ // sets menu
            this.end_on = false;
            this.menu_on = true;
            this.entities_on = true;
            this.pause_on = true;
        }
        else { // sets entities
            this.end_on = false;
            this.menu_on = false;
            this.entities_on = true;
            this.pause_on = false;
        }
    }// end setMenuOn
    public synchronized boolean getMenuOn(){
        return this.menu_on;
    }// end getMenuOn
    // This method sets the pause game state on/off depending on menu state
    public synchronized void setPauseOn(boolean state){
        if(this.menu_on || this.end_on){
            // cannot override pause when in menu state
        }
        else {
            if(state){ // sets pause
                //this.menu_on = false;
                //this.entities_on = true;
                this.pause_on = true;
            }
            else { // sets entities
                //this.menu_on = false;
                //this.entities_on = true;
                this.pause_on = false;
            }
        }
    }// end setPausedOn
    public synchronized boolean getPauseOn(){
        return this.pause_on;
    }// end getPausedOn
    // This menu enables or disables entities (graphical display)
    public synchronized void setEntitiesOn(boolean state){
        if(state){ // sets entities
            this.menu_on = false;
            this.entities_on = true;
            this.pause_on = false;
        }
        else { // sets pause
            this.menu_on = false;
            this.entities_on = false;
            this.pause_on = true;
        }
    }// end setEntitiesOn
    
    public synchronized boolean getEntitiesOn(){
        return this.entities_on;
    }// end getEntitiesOn

    // This method is used to start the game and initiate the main loop
    public void start(){
        setGame(true); // Turn on the game
        running(); // run the 'main loop'
    }// end start
    
    // Running is the main loop of the game
    private synchronized void running(){
        try {
            // start all the threads.
            //this.threadExecutor.execute(menu);
            //this.threadExecutor.execute(player);
            //this.threadExecutor.execute(platform);
            //this.threadExecutor.execute(ghost);
            //this.threadExecutor.execute(background);
            
            while(this.game_on){
                // 1 - Timestamp #1. Take a time for start of main loop.
                this.timestamp = System.currentTimeMillis();
                // 2 - Update the panel graphics.
                this.repaint();
                //checkEnd();
                // 3 - checkPause keyboard polling.
                keyboard.poll();
                // new
                button_clicked = mouse.getClicked();
                checkPause();
                //updateLevel();
                // 4 - Enable all threads to run loop iteration.
                resetThreads(); // the threads automatically 'disable' after one iteration
                // 5 - Timestamp #2. Count until 15ms from timestamp #1
                long temp = timestamp + MAIN_LOOP_TICK - System.currentTimeMillis();
                if(temp > 0){
                    wait(temp);
                }
            }// end 'main loop'
            threadExecutor.shutdown();
        }
        catch (Exception e){
            threadExecutor.shutdown();
        }
    }// end Running
    
    // Restart the game
    public synchronized void restartGame(){
        disableThreads(); // turn off threads for tick
        setMenuOn(false); // turn off menu
        setGameOver(false);
        // reset all entities
        //this.background.resetBackground();
        //this.ghost.resetGhost();
        //this.platform.resetPlatforms();
        //this.player.resetPlayers();
    }// end restartGame
    // Update the pause state of the game to reflect user input
    private synchronized void checkPause(){
        if(this.pause_on){
            // check if pause is to be disabled (second key press)         
            // 0 - nothing, 1 - typed (once),
            // 2 - pressed (held), 3 - released.
            if(keyboard.getEscape() == 3){
                setPauseOn(false);
            }
        } else {
            if(keyboard.getEscape() == 3 ){
                setPauseOn(true);
            }
        }
    }// end checkPause
    
/*    // Check the end condition of the game
//    private synchronized void checkEnd(){
//        if(!this.getGameOver()){
//            if(this.data.isOnePlayer()){ // one player game
//                if(this.data.isPlayerAlive(true)){
//                    // one player and player is alive, no end condition
//                }
//                else {
//                    // one player and player 1 is dead
//                    this.setEndOn(true);
//                    this.setGameOver(true);
//                }
//            }
//            else {
//                // two player game
//                if((this.data.isPlayerAlive(true)) || (this.data.isPlayerAlive(false))){
//                    // BOTH players are not dead, game still running
//                }
//                else {
//                    // BOTH players are dead
//                    this.setEndOn(true);
//                    this.setGameOver(true);
//                }
//            }
//        }
//    }// end checkEnd
    
    // Update the game level over time (with score)
//    private synchronized void updateLevel(){
//        if(!this.data.isTraining()){
//            int score1 = this.data.getScore(true);
//            int score2 = this.data.getScore(false); // get scores
//            if( (score1 > 0) || (score2 > 0)){
//                // one score is above zero
//                if(score1 >= score2){
//                    // player one is highest score
//                    if(score1 > (this.level_step * LEVEL_STEPSIZE)){
//                        // time to increase game level
//                        this.data.setLevel(this.data.getLevel() + 1, false);
//                        this.level_step++;
//                    }
//                }
//                else {
//                    // player two has highest score
//                    if(score2 > (this.level_step * LEVEL_STEPSIZE)){
//                        // time to increase game level
//                        this.data.setLevel(this.data.getLevel() + 1, false);
//                        this.level_step++;
//                    }
//                }
//            }
//        }
//        // else no level increase
//    }// end updateLevel
*/    

    // The graphical update to the panel
    @Override
    public void paintComponent(Graphics g){
        // this is called in the running 'main loop' to update the
        // rendered graphics on the display (in the game panel)
        super.paintComponent(g); // calls the superclass
        this.setBackground(Color.DARK_GRAY);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        
        if (this.entities_on){
            // 7 - Background
            //this.background.doPaint(g);
            // 6 - Platforms
            //this.platform.doPaint(g);
            // 5 - Ghost
            //this.ghost.doPaint(g);
            // 4 - Players
            //this.player.doPaint(g);
            // 3 - UIBar
            //this.uibar.doPaint(g);
        }
//        if (this.pause_on) {
//            // 2 - Game is paused, show text information
//            // Paint pause message
//            g.setColor(Color.DARK_GRAY);
//            g.drawString("GAME PAUSED", 281, 401);
//            g.setColor(Color.WHITE);
//            g.drawString("GAME PAUSED", 280, 400);
//        }
        if (this.menu_on){
            // 1 - Game is in menu mode, draw the menu
            //this.menu.doPaint(g);
        }
        if(this.end_on && this.game_over){
            // 0 - display end of game message
            g.setColor(Color.BLACK);
            g.fillRect(244,280,145,45);
            g.setColor(Color.DARK_GRAY);
            g.drawString("GAME OVER!", 284, 301);
            g.setColor(Color.RED);
            g.drawString("GAME OVER!", 283, 300);
            g.setColor(Color.DARK_GRAY);
            g.drawString("Press ENTER for menu", 254, 312);
            g.setColor(Color.RED);
            g.drawString("Press ENTER for menu", 253, 311);
        }
        
        // New for the algorithm testing
        if(gridData != null){
        	g.setColor(Color.BLUE);
    		g.drawString("Players Info:-", 12, 20);
    		g.drawString("Num Players: " + gridData.getNumPlayers(), 12, 32);
    		g.drawString("Last Position: " + gridData.getLastPos().toString(), 12, 44);
    		g.drawString("Last Coordinates: "
    				+ gridData.getPlayerCoordinates(gridData.getNumPlayers()).toString(),
    				12, 58);
        	Point temp = new Point();
        	for(int n = 1; n <= gridData.getNumPlayers(); n++){
        		temp = gridData.getPlayerCoordinates(n);
        		g.setColor(Color.YELLOW);
        		g.fillRect(temp.x,temp.y,4,4);
        		g.setColor(Color.BLUE);
        		g.drawString("" + n, temp.x, temp.y);
        	}
        }
        // ** added mouse click location to add players
        if(button_clicked){
        	g.setColor(Color.RED);
        	gridData.addPlayer();
        	button_clicked = false;
        } else {
        	g.setColor(Color.GREEN);
        }
		g.fillRect(20,100,40,20);
		g.setColor(Color.WHITE);
		g.drawString("-: ADD PLAYER", 60, 115);
		g.drawString("Mouse Clicked: " + button_clicked, 10, 146);
		g.drawString("Mouse X: " + mouse.getMouseX(), 10, 158);
		g.drawString("Mouse Y: " + mouse.getMouseY(), 10, 170);
		
        // ***** Testing Section **************
//        g.setColor(Color.RED);
//        g.drawString("Game State:", 10, 120);
//        g.drawString("Game     :" + this.game_on, 10, 132);
//        g.drawString("Menu     :" + this.menu_on, 10, 144);
//        g.drawString("Entities :" + this.entities_on, 10, 156);
//        g.drawString("Pause    :" + this.pause_on, 10, 168);
//        g.drawString("End      :" + this.end_on, 10, 190);
//        g.drawString("MOUSE Location:" + this.mouse.getMouseX(), 10, 202);
//        g.drawString("AUDIO  :" + audio.isAudioOn(), 10, 214);
//        g.drawString("RIGHT:" + keyboard.getRightArrow(), 10, 226);
        // ***** end Testing Section **********
    }// end paintComponent
}

