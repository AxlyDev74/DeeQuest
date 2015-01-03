// package specific to where used it, change for your location and src etc
package deequest;

import java.awt.*; // for graphics

public class PlayerGridData {
	// static properties of a grid system
	private final static int GRID_POINT_SCALE = 8;
	
	// the number of players currently in the system
	private int num_players;
	// maximum players
	private int max_players;
	// use Point as it has (x,y) for coordinate system
	private Point[] player_pos;
	// for grid calculations
	private int x_offset, y_offset, grid_size;
	private int nx, ny, dx, dy, dt;
	
	// * CONSTRUCTOR: requires..
	// grid_size = distance between points (only used for generating coordinates)
	// x_offset, y_offset = origin of the grid (only for generating coordinates)
	// set_max_players = maximum points around the grid
	public PlayerGridData(int set_grid_size, int set_x_offset, int set_y_offset, int set_max_players){
		// Constructor
		this.max_players = set_max_players;
		this.player_pos = new Point[max_players];
		this.num_players = 0;
		//this.num_players = 50;
		this.grid_size = set_grid_size;
		this.x_offset = set_x_offset;
		this.y_offset = set_y_offset;
		this.dx = this.nx = this.ny = 0;
		this.dy = -1;
		this.dt = 0;
		setGrid();
	}
	
	// returns the current number of players
	public int getNumPlayers(){
		return num_players;
	}
	
	// add a player
	public int addPlayer(){
		if(num_players < max_players){
			num_players++;
			return num_players;
		} else {
			return -1;
		}
	}
	
	// return the grid reference point for a particular player
	public Point getPlayerGridPos(int player_number){
		if(player_number <= num_players){
			return player_pos[player_number-1];
		} else {
			return new Point();
		}
	}
	
	// return the x and y coordinate point for a particular player
	public Point getPlayerCoordinates(int player_number){
		if((player_number > 0) && (player_number <= max_players)){
			return new Point((x_offset + (player_pos[player_number-1].x * this.grid_size)),
					(y_offset + (player_pos[player_number-1].y * this.grid_size)));
		} else {
			return new Point();
		}
	}
	
	// Adds a player and returns a Point, the point is Null when error
	public void setGrid(){
		// initial variables
		int grid_matrix = (max_players/GRID_POINT_SCALE)+ 1;
		//int max_iterations = grid_matrix * grid_matrix;
		// iterate through the "spiral" until all players are 'drawn'
		for(int n = 1; n <= max_players; n++){
			// Simply put.. this checks if you are at a 'corner' and then
			// changes the values of dx, dy (direction x, and direction y).
			// ... else it will just increment 
			if((-grid_matrix/2 <= this.nx) && (this.nx <= grid_matrix/2)
				&& (-grid_matrix/2 <= this.ny) && (this.ny <= grid_matrix/2)){
				// ensure we are in the matrix "grid_matrix by grid_matrix"
				// Set the current players position Point
				player_pos[n-1] = new Point(this.nx, this.ny);
			}
			if( (this.nx == this.ny) || ((this.nx < 0) && (this.nx == -this.ny))
				|| ((this.nx > 0) && (this.nx == 1-this.ny))){
				// Diagonal changes
				this.dt = this.dx;
				this.dx = -this.dy;
				this.dy = this.dt;
		    }
			this.nx += this.dx;	// increment in direction x
			this.ny += this.dy;	// increment in direction y
			// lets see...
		}
	}
	
	// return the last player ID added
	public Point getLastPos(){
		// iterate through the players in the list
		if(num_players > 1){
			return player_pos[num_players-1];
		} else {
			return new Point(); // null Point(er :P)
		}
	}
}
