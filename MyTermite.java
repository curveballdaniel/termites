
import java.awt.Color;
import java.util.Random;

/*
 * implemented using code from AutomatonAnimator, AutomatonImage, and implementing DisplayableAutomaton
 * 
 */
public class MyTermite implements DisplayableAutomaton{
	
	private int gridsize;
	private AutomatonImage image;
	
	private int[][] imagearray; // image portrayed on an array, 0 means white, 1 means black. rest can be defined in the color array down bottom
	private Color[] colorarray = getColorArray();
	
	
	private int[] antposition = new int[3]; // keeps track of the ants position, (x,y). the last index is for keeping the face - 
	// 0 means facing down, 1 means facing right, 2 means facing up, 3 means facing left, we are starting in the middle facing right
	
	private int state = 0; // always start at state 0. this can change, and will, especially in the ruleset and spiral methods.
	public Rule[] ruleset = null; // start with a null ruleset, but create as it goes
	
	
	/*
	 * Behavior Observed from Langton's Ant:
	 * 
	 * This was an extremely interesting project to implement because its relatively simple implementation did not reflect the
	 * actions of the ant. At first, the ant seems to move completely chaotically, a class III structure which travels in somewhat
	 * circular, untraceable motions around the midpoint. This can be found until just about the 10,000 mark, where the ant suddenly seems 
	 * to reach an equilibrium and moves in a very predictable 'road-like' pattern moving across the screen. It moves this way until it collides
	 * again with the jumble it created before, and resumes its crazy like path. I thought the most interesting part of this was the ability to 
	 * collide again with the mess it first created, slowly regain its weird pattern to restart the 'road', and then do it again. 
	 * 
	 * After about 150,000 steps, we can see that almost the entire screen is filled with black and white marks and the ant seems to travel
	 * randomly around the grid again. This almost resembles static, and I was not sure whether any of the behavior seen here is repeated, although
	 * not much coherence can be found in the chaotic movements the ant seems to regain again. I also find it fascinating how the ant travels when it has 
	 * passed this certain threshold of the '10,000' mark, as it seems that many of the patterns it creates become nearly symmetrical and understandable.
	 * Basically, unchaotic. 
	 * 
	 */
	public MyTermite(int gridsize){
		this.gridsize = gridsize;
		this.image = new AutomatonImage(gridsize, 5, Color.WHITE); // background color of white, 5 pixels, try to use gridsize 200
		this.imagearray = new int[gridsize][gridsize];
		antposition[0] = gridsize/2; // start in the middle
		antposition[1] = gridsize/2;
		antposition[2] = 1; // 0 means facing down, 1 means facing right, 2 means facing up, 3 means facing left, we are starting in the middle facing right
	}

	@Override
	public AutomatonImage getImage() {
		return image;
	}
	
	
	// this method should work as follows - if state = 0, two options: 
	// first: if white, keep going (do not change face!) and change color to black. state = 1. 
	// second: if black, turn left, but keep color black. state = 1.
	// if state = 1, two options:
	// first: if white, turn right and change color to black. state = 0.
	// second: if black, keep going (do not change face! and change color to white. state = 0.
	
	// this creates a spiral that eminates from the middle moving out, leaning left.
	private int[] spiralAdvance(int[] antposition, int[][] imagearray){
		if (state == 0){
			if (imagearray[antposition[0]][antposition[1]] == 0){ // current cell is white, state = 0
				// do not turn at all! (no turn)
				imagearray[antposition[0]][antposition[1]] = 1;
				image.colorCell(antposition[0], antposition[1], Color.BLACK); // set color
				state = 1;
				
			} else { //if (imagearray[antposition[0]][antposition[1]] == 1){ // current cell is black, state = 0
				antposition[2]--; // turn left
				imagearray[antposition[0]][antposition[1]] = 1;
				image.colorCell(antposition[0], antposition[1], Color.BLACK);
				state = 0;
			}
			
		} else { // if state == 1
			if (imagearray[antposition[0]][antposition[1]] == 0){ // current cell is white, state = 1
				antposition[2]++; // turn right
				imagearray[antposition[0]][antposition[1]] = 1;
				image.colorCell(antposition[0], antposition[1], Color.BLACK); // set color
				state = 1;
				
			} else { //if (imagearray[antposition[0]][antposition[1]] == 1){ // current cell is black, state = 1
				//do not turn at all (no turn)
				imagearray[antposition[0]][antposition[1]] = 0;
				image.colorCell(antposition[0], antposition[1], Color.WHITE);
				state = 0;
			}
		}
		
		return movement(antposition, imagearray);
	}
	
	// the normal advance method! this creates the langton's ant we all know and love. Maybe not love.
	// the general description of how the ant moves is given inside the method
	private int[] advance(int[] antposition, int[][] imagearray){
		// If State=0 & the current cell is white, turn right, set the cell to black, State:=0, and move forward.
		// If State=0 & the current cell is black, turn left, set the cell to white, State:=0,  and move forward.
		// first move should be facing down, turning right, and creating a sort of box in 4 steps, then stepping out
		
		if (imagearray[antposition[0]][antposition[1]] == 0 && state == 0){ // current cell is white, state = 0
			antposition[2]++; // turn right
			imagearray[antposition[0]][antposition[1]] = 1;
			image.colorCell(antposition[0], antposition[1], Color.BLACK); // set color
			//System.out.println(antposition[2]);
			
		} else if (imagearray[antposition[0]][antposition[1]] == 1 && state == 0){ // current cell is black, state = 0
			antposition[2]--; // turn left
			imagearray[antposition[0]][antposition[1]] = 0;
			image.colorCell(antposition[0], antposition[1], Color.WHITE);
		}
		
		return movement(antposition, imagearray);
	}
	
	// this method moves the ant in the correct way. this is extremely important to call in every single advance
	// method because this causes the actual movement of the ant in the correct way. Without it, the entire screen would just be a flashing light
	// or an extremely captivating black dot. 
	private int[] movement(int[] antposition, int[][] imagearray){

		if (antposition[2] == 4) // correct movement right
		antposition[2] = 0;
		if (antposition[2] == -1)  // correct movement left
		antposition[2] = 3;
		
		// moving the ant's position
		if (antposition[2] == 0){ // facing down, move down
			antposition[1]++;
		} else if (antposition[2] == 1){ // facing right, move right
			antposition[0]++;
		} else if (antposition[2] == 2){ // facing up, move up
			antposition[1]--;
		} else { // antposition[2] == 3 // facing left, move left
			antposition[0]--;
		}
		
		// if it goes off the map, wrap correctly
		if (antposition[0] == gridsize){
			antposition[0] = 0; 
		}
		if (antposition[0] == -1){
			antposition[0] = gridsize-1; 
		}		
		if (antposition[1] == gridsize){
			antposition[1] = 0; 
		}	
		if (antposition[1] == -1){
			antposition[1] = gridsize-1; 
		}
		
		return antposition;
	}
	
	// step method which runs the meat of the program, called upon many times, each step is one movement from the termite.
	@Override
	public void step(int numSteps) {
		// TODO Auto-generated method stub
		for (int i = 0; i < numSteps; i++){
			// advance termite
			advance(antposition, imagearray);
			
			// advance our spiral termite
			//spiralAdvance(antposition, imagearray);
			
			// advance our wacko random termite
			//ruleAdvance(imagearray, antposition, ruleset, state, colorarray);
		}
		image.markComplete();
	}
	
	@Override
	public int getGridSize() {
		// TODO Auto-generated method stub
		return gridsize;
	}
	
	// saves the image with an input String, .jpg is still needed to add to the string
	public void saveImage(String fileName){
		image.saveImage(fileName);
	}
	
	// for the second part
	// creates an array of rules, with length of the color array. 
	// this is so that each color gets its own # of states, determined by the int numberofstates in the method.
	// implements the class Rule created in order to store each color's rules, states, and next moves
	public Rule[] createRuleSets(){
		
		Color[] colorarray = getColorArray(); // 6 colors
		int numberofstates = 8; // EDIT THE STATES HERE!
		
		Rule[] rules = new Rule[colorarray.length];
		
		for (int i = 0; i < rules.length; i++){
			rules[i] = new Rule(numberofstates, colorarray);
		}
		
		this.ruleset = rules;
		
		Rule.printRuleSet(rules, colorarray, numberofstates);
		
		return rules;
	}
	
	// this is where the color array of the method is created and named - if any color would like to be changed it can do so here and will change
	// for every single ruleset and for the color array of the method itself. The important part to keep is for WHITE to be kept in first and
	// BLACK to be kept in second - this is so as to keep consistency with the previous methods spiral and normal advance because the connection
	// between their twoD array and which to draw is given by WHITE == 0 and BLACK == 1
	private Color[] getColorArray(){
		Color[] colorarray = {Color.WHITE, Color.BLACK, Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE};
		return colorarray;
	}
	
	// an extremely abstract method which moves the ant, paints, and moves to next state given a rule array, the image array (which it needs to 
	// keep track of where it is on the image board), the color array (to paint the color), and the state to obtain the correct movements, and then
	// return the next state. instead of returning this next state, it is rewritten to the class variable state in order for the step() method to
	// run more fluidly. the antposition is the point value x,y, and the movement method is called to move the ant in the correct way 
	// after the correct 'side' is given to the ant.
	public int[] ruleAdvance(int[][] imagearray, int[] antposition, Rule[] rules, int state, Color[] colorarray){
		for (int i = 0; i < rules.length; i++){
			if (imagearray[antposition[0]][antposition[1]] == i){
				antposition[2] += rules[i].getMovement(state);
				imagearray[antposition[0]][antposition[1]] = rules[i].getColorToDraw(state); // next color to draw
				image.colorCell(antposition[0], antposition[1], colorarray[rules[i].getColorToDraw(state)]); // set color
				this.state = rules[i].getNextState(state);
				return movement(antposition, imagearray);
			}
		}
		
		return movement(antposition, imagearray);
	}

	public static void main(String[] args){
		
		
		MyTermite termite = new MyTermite(100);
		termite.createRuleSets();
		
		// if this is commented out, nothing will be saved. if this is not commented out, the terminal will start at step 100,000.
		/*
		termite.step(100);
		termite.saveImage("crazyhundred.png");
		termite.step(900);
		termite.saveImage("crazythousand.png");
		termite.step(9000);
		termite.saveImage("crazytenthousand.png");
		termite.step(90000);
		termite.saveImage("crazyhundredthousand.png");
		*/
		
		AutomatonAnimator screen = new AutomatonAnimator(termite);
	}

}
